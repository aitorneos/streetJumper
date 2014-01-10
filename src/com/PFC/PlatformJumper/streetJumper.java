package com.PFC.PlatformJumper;

// ---------- JAVA IMPORTS -------------------------------------------------------------- //
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;





//---------- ANDENGINE KERNEL IMPORTS -------------------------------------------------------------- //
import org.andengine.engine.Engine;
import org.andengine.engine.LimitedFPSEngine;
import org.andengine.engine.options.ConfigChooserOptions;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.camera.BoundCamera;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.WakeLockOptions;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.augmentedreality.BaseAugmentedRealityGameActivity;
import org.andengine.input.sensor.acceleration.AccelerationData;
import org.andengine.input.sensor.acceleration.IAccelerationListener;
import org.andengine.util.debug.Debug;

//---------- ANDENGINE MULTIPLAYER EXTENSION IMPORTS -------------------------------------------------------------- //
import org.andengine.util.exception.BluetoothException;
import org.andengine.extension.multiplayer.adt.message.IMessage;
import org.andengine.extension.multiplayer.adt.message.server.IServerMessage;
import org.andengine.extension.multiplayer.adt.message.server.ServerMessage;
import org.andengine.extension.multiplayer.client.IServerMessageHandler;
import org.andengine.extension.multiplayer.client.connector.BluetoothSocketConnectionServerConnector;
import org.andengine.extension.multiplayer.client.connector.BluetoothSocketConnectionServerConnector.IBluetoothSocketConnectionServerConnectorListener;
import org.andengine.extension.multiplayer.client.connector.ServerConnector;
import org.andengine.extension.multiplayer.server.BluetoothSocketServer;
import org.andengine.extension.multiplayer.server.BluetoothSocketServer.IBluetoothSocketServerListener;
import org.andengine.extension.multiplayer.server.connector.BluetoothSocketConnectionClientConnector;
import org.andengine.extension.multiplayer.server.connector.BluetoothSocketConnectionClientConnector.IBluetoothSocketConnectionClientConnectorListener;
import org.andengine.extension.multiplayer.server.connector.ClientConnector;
import org.andengine.extension.multiplayer.shared.BluetoothSocketConnection;
import org.andengine.extension.multiplayer.util.MessagePool;





//---------- NETWORK IMPORTS  -------------------------------------------------------------- //
import Network.ConnectionCloseServerMessage;
//---------- OTHERS -------------------------------------------------------------- //
import ResourcesManagment.ResourcesManager;
import ResourcesManagment.SceneManager;
import android.view.Display;
import android.view.KeyEvent;
import android.app.AlertDialog;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseArray;
import android.widget.Toast;


/**
 *
 * @author Aitor Arque Arnaiz (1194443)
 */

public class streetJumper extends BaseAugmentedRealityGameActivity implements IAccelerationListener
{
	
	 // ===========================================================
    // Constants
    // ===========================================================

    /** Create your own unique UUID at: http://www.uuidgenerator.com/ */
    private static final String UUID = "9c4a7a70-788f-11e3-981f-0800200c9a66";
    
    private static final short FLAG_MESSAGE_SERVER_ADD_SPRITE = 1;
    private static final short FLAG_MESSAGE_SERVER_MOVE_SPRITE = FLAG_MESSAGE_SERVER_ADD_SPRITE + 1;

    private static final int DIALOG_CHOOSE_SERVER_OR_CLIENT_ID = 0;
    private static final int DIALOG_SHOW_SERVER_IP_ID = DIALOG_CHOOSE_SERVER_OR_CLIENT_ID + 1;

    private static final int REQUESTCODE_BLUETOOTH_ENABLE = 0;
    private static final int REQUESTCODE_BLUETOOTH_CONNECT = REQUESTCODE_BLUETOOTH_ENABLE + 1;
	
	//------------------ Class Variables --------------------------------------
	
	private BoundCamera camera;
	private ResourcesManager resourcesManager;
	private boolean activated = false;
	
	 private int mSpriteIDCounter;
     private final SparseArray<Sprite> mSprites = new SparseArray<Sprite>();

     private String mServerMACAddress;
     private BluetoothSocketServer<BluetoothSocketConnectionClientConnector> mBluetoothSocketServer;
     private ServerConnector<BluetoothSocketConnection> mServerConnector;

     private final MessagePool<IMessage> mMessagePool = new MessagePool<IMessage>();

     private BluetoothAdapter mBluetoothAdapter;
   
	//------------------------------------------------------------------------
     
     // ===========================================================
     // Constructors
     // ===========================================================

     public streetJumper() 
     {
             this.initMessagePool();
     }

     private void initMessagePool() 
     {
             this.mMessagePool.registerMessage(FLAG_MESSAGE_SERVER_ADD_SPRITE, AddSpriteServerMessage.class);
             this.mMessagePool.registerMessage(FLAG_MESSAGE_SERVER_MOVE_SPRITE, MoveSpriteServerMessage.class);
     }

	
	 // ----------------------- Methods ---------------------------------------
     
     // ===========================================================
     // Methods for/from SuperClass/Interfaces
     // ===========================================================
     
     @SuppressWarnings("deprecation")
	 @Override
     protected void onCreate(final Bundle pSavedInstanceState)
     {
             super.onCreate(pSavedInstanceState);

             this.mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
             if (this.mBluetoothAdapter == null) 
             {
                 Toast.makeText(this, "Bluetooth is not available!", Toast.LENGTH_LONG).show();
                 this.finish();
                 return;
                     
             } 
             else 
             {
                 this.mServerMACAddress = this.mBluetoothAdapter.getAddress();

                 if (this.mBluetoothAdapter.isEnabled()) 
                 {
                         this.showDialog(DIALOG_CHOOSE_SERVER_OR_CLIENT_ID);
                 } 
                 else 
                 {
                     final Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                     this.startActivityForResult(enableIntent, REQUESTCODE_BLUETOOTH_ENABLE);
                 }
             }
     }
     
    @SuppressWarnings("deprecation")
	@Override
     protected Dialog onCreateDialog(final int pID) 
     {
             switch(pID) 
             {
                     case DIALOG_SHOW_SERVER_IP_ID:
                    	 
                         return new AlertDialog.Builder(this)
                         .setIcon(android.R.drawable.ic_dialog_info)
                         .setTitle("Server-Details")
                         .setCancelable(false)
                         .setMessage("The Name of your Server is:\n" + BluetoothAdapter.getDefaultAdapter().getName() + "\n" + "The MACAddress of your Server is:\n" + this.mServerMACAddress)
                         .setPositiveButton(android.R.string.ok, null)
                         .create();
                             
                     case DIALOG_CHOOSE_SERVER_OR_CLIENT_ID:
                    	 
                             return new AlertDialog.Builder(this)
                             .setIcon(android.R.drawable.ic_dialog_info)
                             .setTitle("Be Server or Client ...")
                             .setCancelable(false)
                             // CLIENT ----> RUNS
                             .setPositiveButton("Client", new OnClickListener() 
                             {
                                 @Override
                                 public void onClick(final DialogInterface pDialog, final int pWhich) 
                                 {
                                	 Intent result = new Intent(streetJumper.this, BluetoothDeviceDetection.class);
                                     streetJumper.this.startActivityForResult(result, REQUESTCODE_BLUETOOTH_CONNECT);
                                 }
                             })
                             // SERVER -----> RUNS
                             .setNeutralButton("Server", new OnClickListener()
                             {
                                 @Override
                                 public void onClick(final DialogInterface pDialog, final int pWhich)
                                 {
                                	 streetJumper.this.toast("You can add and move sprites, which are only shown on the clients.");
                                	 streetJumper.this.initServer();
                                	 streetJumper.this.showDialog(DIALOG_SHOW_SERVER_IP_ID);
                                 }
                             })
                             // CLIENT && SERVER -------> RUNS
                             .setNegativeButton("Both", new OnClickListener() 
                             {
                                 @Override
                                 public void onClick(final DialogInterface pDialog, final int pWhich) 
                                 {
                                	 streetJumper.this.toast("You can add sprites and move them, by dragging them.");
                                	 streetJumper.this.initServerAndClient();
                                	 streetJumper.this.showDialog(DIALOG_SHOW_SERVER_IP_ID);
                                 }
                             })
                             .create();
                     default:
                           return super.onCreateDialog(pID);
             }
     }

	
	
	/**
	 Initialize screen options 
	 **/
    @Override
	public EngineOptions onCreateEngineOptions()
    {
		final Display defaultDisplay = getWindow().getWindowManager().getDefaultDisplay();
	    @SuppressWarnings("deprecation")
		int CAMERA_WIDTH = defaultDisplay.getWidth();
	    @SuppressWarnings("deprecation")
		int CAMERA_HEIGHT = defaultDisplay.getHeight();
	    
		camera = new BoundCamera(0, 0, 800, 480);
	    EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), this.camera);
	    engineOptions.getAudioOptions().setNeedsSound(true);
	    engineOptions.getAudioOptions().setNeedsMusic(true);
	    engineOptions.setWakeLockOptions(WakeLockOptions.SCREEN_ON);
	    final ConfigChooserOptions configChooserOptions = engineOptions.getRenderOptions().getConfigChooserOptions();
		configChooserOptions.setRequestedRedSize(8);
		configChooserOptions.setRequestedGreenSize(8);
		configChooserOptions.setRequestedBlueSize(8);
		configChooserOptions.setRequestedAlphaSize(8);
		configChooserOptions.setRequestedDepthSize(16);
	    return engineOptions;
    }
	
	
	/*
	 Method that limits the FPS (frames per second) to 60 Herz due to 
	 the diferent frame rate technology
	 */
	@Override
	public Engine onCreateEngine(EngineOptions pEngineOptions) 
	{
	    return new LimitedFPSEngine(pEngineOptions, 60);
	}

	@Override
    public void onCreateResources(OnCreateResourcesCallback pOnCreateResourcesCallback) throws IOException
    {
    	
    	 ResourcesManager.prepareManager(mEngine, this, camera, getVertexBufferObjectManager());
    	 resourcesManager = ResourcesManager.getInstance();
    	 //this.enableAccelerationSensor(this);
    	 pOnCreateResourcesCallback.onCreateResourcesFinished();
        
    }

	@Override
    public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback) throws IOException
    {
    	
    	SceneManager.getInstance().createSplashScene(pOnCreateSceneCallback);
    }

	@Override
    public void onPopulateScene(Scene pScene, OnPopulateSceneCallback pOnPopulateSceneCallback) throws IOException
    {
        mEngine.registerUpdateHandler(new TimerHandler(2f, new ITimerCallback() 
        {
            public void onTimePassed(final TimerHandler pTimerHandler) 
            {
                mEngine.unregisterUpdateHandler(pTimerHandler);
                SceneManager.getInstance().createMenuScene();
            }
        }));
        pOnPopulateSceneCallback.onPopulateSceneFinished();
    }
    
    @SuppressWarnings("deprecation")
	@Override
    protected void onDestroy()
    {
    	if(this.mBluetoothSocketServer != null) 
    	{
            this.mBluetoothSocketServer.sendBroadcastServerMessage(new ConnectionCloseServerMessage());
            this.mBluetoothSocketServer.terminate();
	    }
	
	    if(this.mServerConnector != null) 
	    {
	            this.mServerConnector.terminate();
	    }

    	super.onDestroy();
    	this.disableAccelerationSensor();
        System.exit(0);	
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) 
    {  
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            SceneManager.getInstance().getCurrentScene().onBackKeyPressed();
        }
        
        if (keyCode == KeyEvent.KEYCODE_MENU && event.getAction() == KeyEvent.ACTION_DOWN) 
        {
            if (this.mEngine.isRunning()) 
            {
            	// Put Pause Text ...
            	this.mEngine.stop();
            	ResourcesManager.getInstance().getSceneMusic().pause();
            }
            else
            {
            	this.mEngine.start();
            	ResourcesManager.getInstance().getSceneMusic().play();
            }
        }
        return false; 
    }
    
    @SuppressWarnings("deprecation")
	@Override
    protected void onActivityResult(final int pRequestCode, final int pResultCode, final Intent pData) 
    {
        switch(pRequestCode) 
        {
            case REQUESTCODE_BLUETOOTH_ENABLE:
            	
                this.showDialog(DIALOG_CHOOSE_SERVER_OR_CLIENT_ID);
                break;
                    
            case REQUESTCODE_BLUETOOTH_CONNECT:
            	
                this.mServerMACAddress = pData.getExtras().getString(BluetoothDeviceDetection.EXTRA_DEVICE_ADDRESS);
                this.initClient();
                break;
                    
            default:
                super.onActivityResult(pRequestCode, pResultCode, pData);
        }
    }
    
 // ===========================================================
    // Methods
    // ===========================================================

    public void addSprite(final int pID, final float pX, final float pY) 
    {

    }

    public void moveSprite(final int pID, final float pX, final float pY) 
    {
        /* Find and move the sprite. */
        final Sprite sprite = this.mSprites.get(pID);
        sprite.setPosition(pX, pY);
    }

    private void initServerAndClient() 
    {
        this.initServer();

        /* Wait some time after the server has been started, so it actually can start up. */
        try 
        {
            Thread.sleep(500);
        }
        catch (final Throwable t) 
        {
            Debug.e(t);
        }

        this.initClient();
    }

    private void initServer() 
    {
        this.mServerMACAddress = BluetoothAdapter.getDefaultAdapter().getAddress();
        try 
        {
            this.mBluetoothSocketServer = new BluetoothSocketServer<BluetoothSocketConnectionClientConnector>(UUID, new ExampleClientConnectorListener(), new ExampleServerStateListener()) 
            {
                @Override
                protected BluetoothSocketConnectionClientConnector newClientConnector(final BluetoothSocketConnection pBluetoothSocketConnection) throws IOException
                {
                    try 
                    {
                        return new BluetoothSocketConnectionClientConnector(pBluetoothSocketConnection);
                    } 
                    catch (final BluetoothException e) 
                    {
                        Debug.e(e);
                        /* Actually cannot happen. */
                        return null;
                    }
                }
            };
        } 
        catch (final BluetoothException e) 
        {
            Debug.e(e);
        }

        this.mBluetoothSocketServer.start();
    }

    private void initClient() 
    {
        try 
        {
            this.mServerConnector = new BluetoothSocketConnectionServerConnector(new BluetoothSocketConnection(this.mBluetoothAdapter, this.mServerMACAddress, UUID), new ExampleServerConnectorListener());

            /*this.mServerConnector.registerServerMessage(FLAG_MESSAGE_SERVER_CONNECTION_CLOSE, ConnectionCloseServerMessage.class, new IServerMessageHandler<BluetoothSocketConnection>() {
                @Override
                public void onHandleMessage(final ServerConnector<BluetoothSocketConnection> pServerConnector, final IServerMessage pServerMessage) throws IOException {
                        streetJumper.this.finish();
                }
        });*/
            
            this.mServerConnector.registerServerMessage(FLAG_MESSAGE_SERVER_ADD_SPRITE, AddSpriteServerMessage.class, new IServerMessageHandler<BluetoothSocketConnection>() 
            {
                @Override
                public void onHandleMessage(final ServerConnector<BluetoothSocketConnection> pServerConnector, final IServerMessage pServerMessage) throws IOException 
                {
                    final AddSpriteServerMessage addSpriteServerMessage = (AddSpriteServerMessage)pServerMessage;
                    streetJumper.this.addSprite(addSpriteServerMessage.mID, addSpriteServerMessage.mX, addSpriteServerMessage.mY);
                }
            });

            this.mServerConnector.registerServerMessage(FLAG_MESSAGE_SERVER_MOVE_SPRITE, MoveSpriteServerMessage.class, new IServerMessageHandler<BluetoothSocketConnection>()
        	{
                @Override
                public void onHandleMessage(final ServerConnector<BluetoothSocketConnection> pServerConnector, final IServerMessage pServerMessage) throws IOException
                {
                    final MoveSpriteServerMessage moveSpriteServerMessage = (MoveSpriteServerMessage)pServerMessage;
                    streetJumper.this.moveSprite(moveSpriteServerMessage.mID, moveSpriteServerMessage.mX, moveSpriteServerMessage.mY);
                }
            });

            this.mServerConnector.getConnection().start();
        } 
        catch (final Throwable t) 
        {
            Debug.e(t);
        }
    }


    @Override
    protected void onPause()
    {
        super.onPause();
            
    }

    @Override
    public void onAccelerationAccuracyChanged(AccelerationData pAccelerationData)
    {

    }

    @Override
    public void onAccelerationChanged(AccelerationData pAccelerationData)
    {
    	if (SceneManager.getInstance().getGameScene() != null && SceneManager.getInstance().getGameScene().player.getAc() == false && (pAccelerationData.getX() > 0.5 || pAccelerationData.getX() < -0.5 ))
    	{
    		this.enableAccelerationSensor(this);
    		if (SceneManager.getInstance().getGameScene() != null && pAccelerationData.getX() > 0.0f)
        	{
            	SceneManager.getInstance().getGameScene().player.body.setLinearVelocity((float) (pAccelerationData.getX()*1.5), SceneManager.getInstance().getGameScene().player.body.getLinearVelocity().y);
            	SceneManager.getInstance().getGameScene().player.setFlippedHorizontal(false);
            	
        	}  
        	
        	if (SceneManager.getInstance().getGameScene() != null && pAccelerationData.getX() < 0.0f)
        	{ 
        		SceneManager.getInstance().getGameScene().player.body.setLinearVelocity((float) (pAccelerationData.getX()*1.5), SceneManager.getInstance().getGameScene().player.body.getLinearVelocity().y);
            	SceneManager.getInstance().getGameScene().player.setFlippedHorizontal(true);
            	
        	}    	
    	}
    	else if (SceneManager.getInstance().getGameScene() != null && SceneManager.getInstance().getGameScene().player.getAc() == true)
    	{
    		this.disableAccelerationSensor();
    		if (SceneManager.getInstance().getGameScene().player.getX() < 50) SceneManager.getInstance().getGameScene().player.body.setLinearVelocity(3, 0);
    		if (SceneManager.getInstance().getGameScene().player.getX() > 1090) SceneManager.getInstance().getGameScene().player.body.setLinearVelocity(-3, 0);
    		this.enableAccelerationSensor(this);
    	}
    }
    
    // ------------------------------------------------------------------------------------------

    // -------------------------------------- GETTERS AND SETTERS ----------------------------------
    public void setAccelerometerActivated(boolean activated)
    {
    	this.activated = activated;
    	
    	if (this.activated == true)
    	{
    		this.enableAccelerationSensor(this);
    	}
    	
    	else
    	{
    		this.disableAccelerationSensor();
    	}
    }
    
    private void log(final String pMessage) 
    {
        Debug.d(pMessage);
    }
    
    private void toast(final String pMessage)
    {
        this.log(pMessage);
        this.toastOnUiThread(pMessage);
    }
    
    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================

    public static class AddSpriteServerMessage extends ServerMessage 
    {
        private int mID;
        private float mX;
        private float mY;

        public AddSpriteServerMessage() 
        {

        }

        public AddSpriteServerMessage(final int pID, final float pX, final float pY) 
        {
                this.mID = pID;
                this.mX = pX;
                this.mY = pY;
        }

        public void set(final int pID, final float pX, final float pY)
        {
                this.mID = pID;
                this.mX = pX;
                this.mY = pY;
        }

        @Override
        public short getFlag() 
        {
                return FLAG_MESSAGE_SERVER_ADD_SPRITE;
        }

        @Override
        protected void onReadTransmissionData(final DataInputStream pDataInputStream) throws IOException 
        {
                this.mID = pDataInputStream.readInt();
                this.mX = pDataInputStream.readFloat();
                this.mY = pDataInputStream.readFloat();
        }

        @Override
        protected void onWriteTransmissionData(final DataOutputStream pDataOutputStream) throws IOException 
        {
                pDataOutputStream.writeInt(this.mID);
                pDataOutputStream.writeFloat(this.mX);
                pDataOutputStream.writeFloat(this.mY);
        }
    }

    public static class MoveSpriteServerMessage extends ServerMessage 
    {
        private int mID;
        private float mX;
        private float mY;

        public MoveSpriteServerMessage()
        {

        }

        public MoveSpriteServerMessage(final int pID, final float pX, final float pY) 
        {
                this.mID = pID;
                this.mX = pX;
                this.mY = pY;
        }

        public void set(final int pID, final float pX, final float pY) 
        {
                this.mID = pID;
                this.mX = pX;
                this.mY = pY;
        }

        @Override
        public short getFlag() 
        {
                return FLAG_MESSAGE_SERVER_MOVE_SPRITE;
        }

        @Override
        protected void onReadTransmissionData(final DataInputStream pDataInputStream) throws IOException 
        {
                this.mID = pDataInputStream.readInt();
                this.mX = pDataInputStream.readFloat();
                this.mY = pDataInputStream.readFloat();
        }

        @Override
        protected void onWriteTransmissionData(final DataOutputStream pDataOutputStream) throws IOException
        {
                pDataOutputStream.writeInt(this.mID);
                pDataOutputStream.writeFloat(this.mX);
                pDataOutputStream.writeFloat(this.mY);
        }
    }

    private class ExampleServerConnectorListener implements IBluetoothSocketConnectionServerConnectorListener 
    {
        @Override
        public void onStarted(final ServerConnector<BluetoothSocketConnection> pConnector) 
        {
                streetJumper.this.toast("CLIENT: Connected to server.");
        }

        @Override
        public void onTerminated(final ServerConnector<BluetoothSocketConnection> pConnector) 
        {
        	streetJumper.this.toast("CLIENT: Disconnected from Server...");
        	streetJumper.this.finish();
        }
    }

    private class ExampleServerStateListener implements IBluetoothSocketServerListener<BluetoothSocketConnectionClientConnector> 
    {
        @Override
        public void onStarted(final BluetoothSocketServer<BluetoothSocketConnectionClientConnector> pBluetoothSocketServer) 
        {
        	streetJumper.this.toast("SERVER: Started.");
        }

        @Override
        public void onTerminated(final BluetoothSocketServer<BluetoothSocketConnectionClientConnector> pBluetoothSocketServer) 
        {
        	streetJumper.this.toast("SERVER: Terminated.");
        }

        @Override
        public void onException(final BluetoothSocketServer<BluetoothSocketConnectionClientConnector> pBluetoothSocketServer, final Throwable pThrowable) 
        {
                Debug.e(pThrowable);
                streetJumper.this.toast("SERVER: Exception: " + pThrowable);
        }
    }

    private class ExampleClientConnectorListener implements IBluetoothSocketConnectionClientConnectorListener 
    {
        @Override
        public void onStarted(final ClientConnector<BluetoothSocketConnection> pConnector) 
        {
        	streetJumper.this.toast("SERVER: Client connected: " + pConnector.getConnection().getBluetoothSocket().getRemoteDevice().getAddress());
        }

        @Override
        public void onTerminated(final ClientConnector<BluetoothSocketConnection> pConnector) 
        {
        	streetJumper.this.toast("SERVER: Client disconnected: " + pConnector.getConnection().getBluetoothSocket().getRemoteDevice().getAddress());
        }
    }

}
