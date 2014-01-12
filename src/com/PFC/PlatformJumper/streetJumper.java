package com.PFC.PlatformJumper;

// ---------- JAVA IMPORTS -------------------------------------------------------------- //
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

//---------- ANDENGINE KERNEL IMPORTS -------------------------------------------------------------- //
import org.andengine.engine.Engine;
import org.andengine.engine.LimitedFPSEngine;
import org.andengine.engine.camera.BoundCamera;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.engine.options.ConfigChooserOptions;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.WakeLockOptions;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.augmentedreality.BaseAugmentedRealityGameActivity;
import org.andengine.input.sensor.acceleration.AccelerationData;
import org.andengine.input.sensor.acceleration.IAccelerationListener;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.util.WifiUtils;
import org.andengine.util.WifiUtils.WifiUtilsException;
import org.andengine.util.debug.Debug;

//---------- ANDENGINE MULTIPLAYER EXTENSION IMPORTS -------------------------------------------------------------- //
import org.andengine.extension.multiplayer.adt.message.server.ServerMessage;
import org.andengine.extension.multiplayer.adt.message.server.IServerMessage;
import org.andengine.extension.multiplayer.adt.message.IMessage;
import org.andengine.extension.multiplayer.client.IServerMessageHandler;
import org.andengine.extension.multiplayer.client.connector.ServerConnector;
import org.andengine.extension.multiplayer.client.connector.SocketConnectionServerConnector;
import org.andengine.extension.multiplayer.client.connector.SocketConnectionServerConnector.ISocketConnectionServerConnectorListener;
import org.andengine.extension.multiplayer.server.SocketServer;
import org.andengine.extension.multiplayer.server.SocketServer.ISocketServerListener;
import org.andengine.extension.multiplayer.server.connector.ClientConnector;
import org.andengine.extension.multiplayer.server.connector.SocketConnectionClientConnector;
import org.andengine.extension.multiplayer.server.connector.SocketConnectionClientConnector.ISocketConnectionClientConnectorListener;
import org.andengine.extension.multiplayer.shared.SocketConnection;
import org.andengine.extension.multiplayer.util.MessagePool;

//---------- NETWORK IMPORTS  -------------------------------------------------------------- //
import Network.ConnectionCloseServerMessage;
import Network.ServerMessageFlags;
import Network.ClientMessageFlags;

//---------- OTHERS -------------------------------------------------------------- //
import ResourcesManagment.ResourcesManager;
import ResourcesManagment.SceneManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.util.SparseArray;
import android.view.Display;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.Toast;


/**
 *
 * @author Aitor Arque Arnaiz (1194443)
 * @Date : 12 / 01 / 2014
 */

public class streetJumper extends BaseAugmentedRealityGameActivity implements IAccelerationListener, ServerMessageFlags, ClientMessageFlags
{
	
	 // ===========================================================
    // Constants
    // ===========================================================

	 private static final String LOCALHOST_IP = "127.0.0.1";

     //private static final int CAMERA_WIDTH = 720;
     //private static final int CAMERA_HEIGHT = 480;

     private static final int SERVER_PORT = 4444;

     private static final short FLAG_MESSAGE_SERVER_ADD_FACE = 1;
     private static final short FLAG_MESSAGE_SERVER_MOVE_FACE = FLAG_MESSAGE_SERVER_ADD_FACE + 1;

     private static final int DIALOG_CHOOSE_SERVER_OR_CLIENT_ID = 0;
     private static final int DIALOG_ENTER_SERVER_IP_ID = DIALOG_CHOOSE_SERVER_OR_CLIENT_ID + 1;
     private static final int DIALOG_SHOW_SERVER_IP_ID = DIALOG_ENTER_SERVER_IP_ID + 1;
     
	//------------------ Class Variables --------------------------------------
	
	private BoundCamera camera;
	private ResourcesManager resourcesManager;
	private boolean activated = false;
	
	 private BitmapTextureAtlas mBitmapTextureAtlas;
     private ITextureRegion mFaceTextureRegion;

    private int mFaceIDCounter;
    private final SparseArray<Sprite> mFaces = new SparseArray<Sprite>();

    private String mServerIP = LOCALHOST_IP;
    private SocketServer<SocketConnectionClientConnector> mSocketServer;
    private ServerConnector<SocketConnection> mServerConnector;

    private final MessagePool<IMessage> mMessagePool = new MessagePool<IMessage>();
   
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
	        this.mMessagePool.registerMessage(FLAG_MESSAGE_SERVER_ADD_FACE, AddFaceServerMessage.class);
	        this.mMessagePool.registerMessage(FLAG_MESSAGE_SERVER_MOVE_FACE, MoveFaceServerMessage.class);
	}

	
	 // ----------------------- Methods ---------------------------------------
     
     // ===========================================================
     // Methods for/from SuperClass/Interfaces
     // ===========================================================
     
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
		this.showDialog(DIALOG_CHOOSE_SERVER_OR_CLIENT_ID);
	    return engineOptions;
    	
        /*final Camera camera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
        return new EngineOptions(true, ScreenOrientation.LANDSCAPE_SENSOR, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), camera);*/
    }
	
	
	/**
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
    	 pOnCreateResourcesCallback.onCreateResourcesFinished();
		
		/*BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");

        this.mBitmapTextureAtlas = new BitmapTextureAtlas(this.getTextureManager(), 32, 32, TextureOptions.BILINEAR);
        this.mFaceTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mBitmapTextureAtlas, this, "face_box.png", 0, 0);

        this.mBitmapTextureAtlas.load();*/
        
    }

	@Override
    public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback)
    {
    	
    	SceneManager.getInstance().createSplashScene(pOnCreateSceneCallback);
		
		/*this.mEngine.registerUpdateHandler(new FPSLogger());

        final Scene scene = new Scene();
        scene.setBackground(new Background(0.09804f, 0.6274f, 0.8784f));

                scene.setOnSceneTouchListener(new IOnSceneTouchListener() {
                        @SuppressWarnings("deprecation")
						@Override
                        public boolean onSceneTouchEvent(final Scene pScene, final TouchEvent pSceneTouchEvent) {
                                if(pSceneTouchEvent.isActionDown()) {
                                        final AddFaceServerMessage addFaceServerMessage = (AddFaceServerMessage) streetJumper.this.mMessagePool.obtainMessage(FLAG_MESSAGE_SERVER_ADD_FACE);
										addFaceServerMessage.set(streetJumper.this.mFaceIDCounter++, pSceneTouchEvent.getX(), pSceneTouchEvent.getY());

										streetJumper.this.mSocketServer.sendBroadcastServerMessage(addFaceServerMessage);

										streetJumper.this.mMessagePool.recycleMessage(addFaceServerMessage);
                                        return true;
                                } 
                                else
                                {
                                	
                                        return true;
                                }
                        }
                });

                scene.setOnAreaTouchListener(new IOnAreaTouchListener() {
                        @SuppressWarnings("deprecation")
						@Override
                        public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final ITouchArea pTouchArea, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
                                final Sprite face = (Sprite)pTouchArea;
								final Integer faceID = (Integer)face.getUserData();

								final MoveFaceServerMessage moveFaceServerMessage = (MoveFaceServerMessage) streetJumper.this.mMessagePool.obtainMessage(FLAG_MESSAGE_SERVER_MOVE_FACE);
								moveFaceServerMessage.set(faceID, pSceneTouchEvent.getX(), pSceneTouchEvent.getY());

								streetJumper.this.mSocketServer.sendBroadcastServerMessage(moveFaceServerMessage);

								streetJumper.this.mMessagePool.recycleMessage(moveFaceServerMessage);
                                return true;
                        }
                });

                scene.setTouchAreaBindingOnActionDownEnabled(true);
                scene.setTouchAreaBindingOnActionMoveEnabled(true);

        return scene;*/
		
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
    protected Dialog onCreateDialog(final int pID) {
            switch(pID) {
                    case DIALOG_SHOW_SERVER_IP_ID:
				try {
					return new AlertDialog.Builder(this)
					.setIcon(android.R.drawable.ic_dialog_info)
					.setTitle("Your Server-IP ...")
					.setCancelable(false)
					.setMessage("The IP of your Server is:\n" + WifiUtils.getWifiIPv4Address(this))
					.setPositiveButton(android.R.string.ok, null)
					.create();
				} catch (WifiUtilsException e) {
					return new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Your Server-IP ...")
                    .setCancelable(false)
                    .setMessage("Error retrieving IP of your Server: " + e)
                    .setPositiveButton(android.R.string.ok, new OnClickListener() {
                            @Override
                            public void onClick(final DialogInterface pDialog, final int pWhich) {
                                    streetJumper.this.finish();
                            }
                    })
                    .create();
				}
                    case DIALOG_ENTER_SERVER_IP_ID:
                            final EditText ipEditText = new EditText(this);
                            return new AlertDialog.Builder(this)
                            .setIcon(android.R.drawable.ic_dialog_info)
                            .setTitle("Enter Server-IP ...")
                            .setCancelable(false)
                            .setView(ipEditText)
                            .setPositiveButton("Connect", new OnClickListener() {
                                    public void onClick(final DialogInterface pDialog, final int pWhich) {
                                            streetJumper.this.mServerIP = ipEditText.getText().toString();
                                            streetJumper.this.initClient();
                                    }
                            })
                            .setNegativeButton(android.R.string.cancel, new OnClickListener() {
                                    @Override
                                    public void onClick(final DialogInterface pDialog, final int pWhich) {
                                    	streetJumper.this.finish();
                                    }
                            })
                            .create();
                    case DIALOG_CHOOSE_SERVER_OR_CLIENT_ID:
                            return new AlertDialog.Builder(this)
                            .setIcon(android.R.drawable.ic_dialog_info)
                            .setTitle("Be Server or Client ...")
                            .setCancelable(false)
                            .setPositiveButton("Client", new OnClickListener() {
                                    @SuppressWarnings("deprecation")
									@Override
                                    public void onClick(final DialogInterface pDialog, final int pWhich) {
                                    	streetJumper.this.showDialog(DIALOG_ENTER_SERVER_IP_ID);
                                    }
                            })
                            .setNeutralButton("Server", new OnClickListener() {
                                    @SuppressWarnings("deprecation")
									@Override
                                    public void onClick(final DialogInterface pDialog, final int pWhich) {
                                    	streetJumper.this.toast("You can add and move sprites, which are only shown on the clients.");
                                    	streetJumper.this.initServer();
                                            streetJumper.this.showDialog(DIALOG_SHOW_SERVER_IP_ID);
                                    }
                            })
                            .setNegativeButton("Both", new OnClickListener() {
                                    @SuppressWarnings("deprecation")
									@Override
                                    public void onClick(final DialogInterface pDialog, final int pWhich) {
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

	@SuppressWarnings("deprecation")
	@Override
    protected void onDestroy() {
            if(this.mSocketServer != null) {
                    this.mSocketServer.sendBroadcastServerMessage(new ConnectionCloseServerMessage());
                    this.mSocketServer.terminate();
            }

            if(this.mServerConnector != null) {
                    this.mServerConnector.terminate();
            }

            super.onDestroy();
    }

    
    /*@Override
    public boolean onKeyUp(final int pKeyCode, final KeyEvent pEvent) {
            switch(pKeyCode) {
                    case KeyEvent.KEYCODE_BACK:
                            this.finish();
                            return true;
            }
            return super.onKeyUp(pKeyCode, pEvent);
    }*/


    
    
 // ===========================================================
    // Methods
    // ===========================================================

    public void addFace(final int pID, final float pX, final float pY) {
        final Scene scene = this.mEngine.getScene();
        /* Create the face and add it to the scene. */
        final Sprite face = new Sprite(0, 0, this.mFaceTextureRegion, this.getVertexBufferObjectManager());
        face.setPosition(pX - face.getWidth() * 0.5f, pY - face.getHeight() * 0.5f);
        face.setUserData(pID);
        this.mFaces.put(pID, face);
        scene.registerTouchArea(face);
        scene.attachChild(face);
	}
	
	public void moveFace(final int pID, final float pX, final float pY) {
	        /* Find and move the face. */
	        final Sprite face = this.mFaces.get(pID);
	        face.setPosition(pX - face.getWidth() * 0.5f, pY - face.getHeight() * 0.5f);
	}


	private void initServer() {
    this.mSocketServer = new SocketServer<SocketConnectionClientConnector>(SERVER_PORT, new ExampleClientConnectorListener(), new ExampleServerStateListener()) {
            @Override
            protected SocketConnectionClientConnector newClientConnector(final SocketConnection pSocketConnection) throws IOException {
                    return new SocketConnectionClientConnector(pSocketConnection);
            }
	    };
	
	    this.mSocketServer.start();
	}
	
	private void initServerAndClient() {
        this.initServer();

        /* Wait some time after the server has been started, so it actually can start up. */
        try {
                Thread.sleep(250);
        } catch (final Throwable t) {
                Debug.e(t);
        }

        this.initClient();
	}


	private void initClient() {
	    try {
	            this.mServerConnector = new SocketConnectionServerConnector(new SocketConnection(new Socket(this.mServerIP, SERVER_PORT)), new ExampleServerConnectorListener());
	
	            this.mServerConnector.registerServerMessage(FLAG_MESSAGE_SERVER_CONNECTION_CLOSE, ConnectionCloseServerMessage.class, new IServerMessageHandler<SocketConnection>() {
	                    @Override
	                    public void onHandleMessage(final ServerConnector<SocketConnection> pServerConnector, final IServerMessage pServerMessage) throws IOException {
	                    	streetJumper.this.finish();
	                    }
	            });
	
	            this.mServerConnector.registerServerMessage(FLAG_MESSAGE_SERVER_ADD_FACE, AddFaceServerMessage.class, new IServerMessageHandler<SocketConnection>() {
	                    @Override
	                    public void onHandleMessage(final ServerConnector<SocketConnection> pServerConnector, final IServerMessage pServerMessage) throws IOException {
	                            final AddFaceServerMessage addFaceServerMessage = (AddFaceServerMessage)pServerMessage;
	                            streetJumper.this.addFace(addFaceServerMessage.mID, addFaceServerMessage.mX, addFaceServerMessage.mY);
	                    }
	            });
	
	            this.mServerConnector.registerServerMessage(FLAG_MESSAGE_SERVER_MOVE_FACE, MoveFaceServerMessage.class, new IServerMessageHandler<SocketConnection>() {
	                    @Override
	                    public void onHandleMessage(final ServerConnector<SocketConnection> pServerConnector, final IServerMessage pServerMessage) throws IOException {
	                            final MoveFaceServerMessage moveFaceServerMessage = (MoveFaceServerMessage)pServerMessage;
	                            streetJumper.this.moveFace(moveFaceServerMessage.mID, moveFaceServerMessage.mX, moveFaceServerMessage.mY);
	                    }
	            });
	
	            this.mServerConnector.getConnection().start();
		    } catch (final Throwable t) {
		            Debug.e(t);
		}
	}

	private void log(final String pMessage) {
	    Debug.d(pMessage);
	}
	
	private void toast(final String pMessage) {
	    this.log(pMessage);
	    this.runOnUiThread(new Runnable() {
	            @Override
	            public void run() {
	                    Toast.makeText(streetJumper.this, pMessage, Toast.LENGTH_SHORT).show();
	            }
	    });
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
    
    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================

    public static class AddFaceServerMessage extends ServerMessage {
        private int mID;
        private float mX;
        private float mY;

        public AddFaceServerMessage() {

        }

        public AddFaceServerMessage(final int pID, final float pX, final float pY) {
                this.mID = pID;
                this.mX = pX;
                this.mY = pY;
        }

        public void set(final int pID, final float pX, final float pY) {
                this.mID = pID;
                this.mX = pX;
                this.mY = pY;
        }

        @Override
        public short getFlag() {
                return FLAG_MESSAGE_SERVER_ADD_FACE;
        }

        @Override
        protected void onReadTransmissionData(final DataInputStream pDataInputStream) throws IOException {
                this.mID = pDataInputStream.readInt();
                this.mX = pDataInputStream.readFloat();
                this.mY = pDataInputStream.readFloat();
        }

        @Override
        protected void onWriteTransmissionData(final DataOutputStream pDataOutputStream) throws IOException {
                pDataOutputStream.writeInt(this.mID);
                pDataOutputStream.writeFloat(this.mX);
                pDataOutputStream.writeFloat(this.mY);
        }
}

public static class MoveFaceServerMessage extends ServerMessage {
        private int mID;
        private float mX;
        private float mY;

        public MoveFaceServerMessage() {

        }

        public MoveFaceServerMessage(final int pID, final float pX, final float pY) {
                this.mID = pID;
                this.mX = pX;
                this.mY = pY;
        }

        public void set(final int pID, final float pX, final float pY) {
                this.mID = pID;
                this.mX = pX;
                this.mY = pY;
        }

        @Override
        public short getFlag() {
                return FLAG_MESSAGE_SERVER_MOVE_FACE;
        }

        @Override
        protected void onReadTransmissionData(final DataInputStream pDataInputStream) throws IOException {
                this.mID = pDataInputStream.readInt();
                this.mX = pDataInputStream.readFloat();
                this.mY = pDataInputStream.readFloat();
        }

        @Override
        protected void onWriteTransmissionData(final DataOutputStream pDataOutputStream) throws IOException {
                pDataOutputStream.writeInt(this.mID);
                pDataOutputStream.writeFloat(this.mX);
                pDataOutputStream.writeFloat(this.mY);
        }
}

private class ExampleServerConnectorListener implements ISocketConnectionServerConnectorListener {
        @Override
        public void onStarted(final ServerConnector<SocketConnection> pConnector) {
                streetJumper.this.toast("CLIENT: Connected to server.");
        }

        @Override
        public void onTerminated(final ServerConnector<SocketConnection> pConnector) {
        	streetJumper.this.toast("CLIENT: Disconnected from Server...");
        	streetJumper.this.finish();
        }
}

private class ExampleServerStateListener implements ISocketServerListener<SocketConnectionClientConnector> {
        @Override
        public void onStarted(final SocketServer<SocketConnectionClientConnector> pSocketServer) {
        	streetJumper.this.toast("SERVER: Started.");
        }

        @Override
        public void onTerminated(final SocketServer<SocketConnectionClientConnector> pSocketServer) {
        	streetJumper.this.toast("SERVER: Terminated.");
        }

        @Override
        public void onException(final SocketServer<SocketConnectionClientConnector> pSocketServer, final Throwable pThrowable) {
                Debug.e(pThrowable);
                streetJumper.this.toast("SERVER: Exception: " + pThrowable);
        }
}

private class ExampleClientConnectorListener implements ISocketConnectionClientConnectorListener {
        @Override
        public void onStarted(final ClientConnector<SocketConnection> pConnector) {
        	streetJumper.this.toast("SERVER: Client connected: " + pConnector.getConnection().getSocket().getInetAddress().getHostAddress());
        }

        @Override
        public void onTerminated(final ClientConnector<SocketConnection> pConnector) {
        	streetJumper.this.toast("SERVER: Client disconnected: " + pConnector.getConnection().getSocket().getInetAddress().getHostAddress());
        }
	}
}
