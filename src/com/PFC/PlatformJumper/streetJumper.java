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
import org.andengine.extension.multiplayer.adt.message.IMessage;
import org.andengine.extension.multiplayer.adt.message.server.IServerMessage;

//---------- ANDENGINE MULTIPLAYER EXTENSION IMPORTS -------------------------------------------------------------- //
import org.andengine.extension.multiplayer.adt.message.server.ServerMessage;
import org.andengine.extension.multiplayer.client.IServerMessageHandler;
import org.andengine.extension.multiplayer.client.connector.ServerConnector;
import org.andengine.extension.multiplayer.client.connector.SocketConnectionServerConnector;
import org.andengine.extension.multiplayer.client.connector.SocketConnectionServerConnector.ISocketConnectionServerConnectorListener;
import org.andengine.extension.multiplayer.server.IClientMessageHandler;
import org.andengine.extension.multiplayer.server.SocketServer;
import org.andengine.extension.multiplayer.server.SocketServer.ISocketServerListener;
import org.andengine.extension.multiplayer.server.connector.ClientConnector;
import org.andengine.extension.multiplayer.server.connector.SocketConnectionClientConnector;
import org.andengine.extension.multiplayer.server.connector.SocketConnectionClientConnector.ISocketConnectionClientConnectorListener;
import org.andengine.extension.multiplayer.shared.SocketConnection;
import org.andengine.extension.multiplayer.util.MessagePool;
import org.andengine.input.sensor.acceleration.AccelerationData;
import org.andengine.input.sensor.acceleration.IAccelerationListener;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder.TextureAtlasBuilderException;
import org.andengine.util.WifiUtils;
import org.andengine.util.WifiUtils.WifiUtilsException;
import org.andengine.extension.multiplayer.adt.message.client.ClientMessage;
import org.andengine.extension.multiplayer.adt.message.client.IClientMessage;
import org.andengine.util.debug.Debug;


//---------- NETWORK IMPORTS  -------------------------------------------------------------- //
import Network.ConnectionCloseServerMessage;
import Network.ServerMessageFlags;
import Players.PlayerOnline;
import Network.ClientMessageFlags;

//---------- OTHERS -------------------------------------------------------------- //
import ResourcesManagment.ResourcesManager;
import ResourcesManagment.SceneManager;
import Timers.playTimer;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.ConfigurationInfo;
import android.util.SparseArray;
import android.view.Display;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.Toast;


/**
 *
 * @author Aitor Arque Arnaiz
 * @Date : 12 / 01 / 2014
 * @category : Platformer AndEngine Game 
 * @see : https://github.com/nicolasgramlich/AndEngine/tree/GLES2-AnchorCenter
 */

public class streetJumper extends BaseAugmentedRealityGameActivity implements IAccelerationListener, ServerMessageFlags, ClientMessageFlags
{
	
	 // ===========================================================
    // Constants
    // ===========================================================
	public Dialog dialog;

	 public static final String LOCALHOST_IP = "127.0.0.1";

	 // --------------------- DATA TRANSMISSION FLAGS ------------------------------------------------------------------------------
	 
     private static final int SERVER_PORT = 4444;
     private static final short FLAG_MESSAGE_SERVER_MOVE_PLAYER = 1;
     public static final short FLAG_MESSAGE_SERVER_PLAYER_SELECTED = FLAG_MESSAGE_SERVER_MOVE_PLAYER + 1;
     public static final short FLAG_MESSAGE_CLIENT_PLAYER_SELECTED_CLIENT = FLAG_MESSAGE_SERVER_PLAYER_SELECTED + 1;
     public static final short FLAG_MESSAGE_CLIENT_MOVE_PLAYER_CLIENT = FLAG_MESSAGE_CLIENT_PLAYER_SELECTED_CLIENT + 1;
     public static final short FLAG_MESSAGE_SERVER_PLAYER = FLAG_MESSAGE_CLIENT_MOVE_PLAYER_CLIENT + 1;
     public static final short FLAG_MESSAGE_CLIENT_PLAYER = FLAG_MESSAGE_SERVER_PLAYER + 1;

     // ----------------------------- DIALOG FLAGS ----------------------------------------------------------------------------------
     
     private static final int DIALOG_CHOOSE_SERVER_OR_CLIENT_ID = 0;
     private static final int DIALOG_ENTER_SERVER_IP_ID = DIALOG_CHOOSE_SERVER_OR_CLIENT_ID + 1;
     private static final int DIALOG_SHOW_SERVER_IP_ID = DIALOG_ENTER_SERVER_IP_ID + 1;
     
	//------------------ Class Variables --------------------------------------
	
	private BoundCamera camera;
	private ResourcesManager resourcesManager;
	private boolean activated = false;

    public int mPlayerIDCounter;
    private final SparseArray<Sprite> mPlayers = new SparseArray<Sprite>();

    public String mServerIP = LOCALHOST_IP;
    public SocketServer<SocketConnectionClientConnector> mSocketServer = null;
    public ServerConnector<SocketConnection> mServerConnector;
    private SocketConnectionClientConnector mClientConnector;

    public final MessagePool<IMessage> mMessagePool = new MessagePool<IMessage>();
   
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
        this.mMessagePool.registerMessage(FLAG_MESSAGE_SERVER_MOVE_PLAYER, movePlayerServerMessage.class);
        this.mMessagePool.registerMessage(FLAG_MESSAGE_SERVER_PLAYER_SELECTED, PlayerSelectedServerMessage.class);
        this.mMessagePool.registerMessage(FLAG_MESSAGE_CLIENT_PLAYER_SELECTED_CLIENT, PlayerSelectedClientServerMessage.class);
        this.mMessagePool.registerMessage(FLAG_MESSAGE_CLIENT_MOVE_PLAYER_CLIENT, MovePlayerClientServerMessage.class);
        this.mMessagePool.registerMessage(FLAG_MESSAGE_SERVER_PLAYER, PlayerServer.class);
        this.mMessagePool.registerMessage(FLAG_MESSAGE_CLIENT_PLAYER, PlayerClient.class);
	}

	
	 // ----------------------- Methods ---------------------------------------
     
     // ===========================================================
     // Methods for/from SuperClass/Interfaces
     // ===========================================================
     
	@SuppressWarnings("deprecation")
	/**
	 Initialize screen options 
	 **/
    @Override
	public EngineOptions onCreateEngineOptions()
    {
		final Display defaultDisplay = getWindow().getWindowManager().getDefaultDisplay();
	    @SuppressWarnings("deprecation")
		int CAMERA_WIDTH = defaultDisplay.getWidth();
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
		//this.showDialog(DIALOG_CHOOSE_SERVER_OR_CLIENT_ID);
		dialog = onCreateDialog(DIALOG_CHOOSE_SERVER_OR_CLIENT_ID);
		dialog.hide();
	    return engineOptions;
    }
	
	@SuppressWarnings("deprecation")
	public void getDialog()
	{
		dialog.show();
	}
	
	
	/**
	 Method that limits the FPS (frames per second) to 60 Herz due to 
	 the diferent frame rate technology
	 */
	@SuppressWarnings("deprecation")
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
        
    }

	@Override
    public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback)
    {
		// Check if the system supports OpenGL ES 2.0.
  	  final ActivityManager activityManager = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
  	  final ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
  	  final boolean supportsEs2 = configurationInfo.reqGlEsVersion >= 0x20000;

  	  if (supportsEs2)
  	  {
  	    // Request an OpenGL ES 2.0 compatible context.
  		// Every 0.15 SECONDS velocity && position are sended to CLIENT (twekeable)
      	playTimer playT = new playTimer(0.15f, new playTimer.ITimerCallback()
  	    {
  	        @SuppressWarnings("deprecation")
  			@Override
  	         public void onTick()
  	         {
  	        	// Move SERVER player in CLIENT machine
  	         	if (SceneManager.getInstance().getGameScene() != null && SceneManager.getInstance().getGameScene().firstTouch == true && mSocketServer != null)
  	         	{
  	         		final movePlayerServerMessage movePlayerServerMessage = (movePlayerServerMessage) streetJumper.this.mMessagePool.obtainMessage(FLAG_MESSAGE_SERVER_MOVE_PLAYER);
  	         		movePlayerServerMessage.set(ResourcesManager.getInstance().activity.mPlayerIDCounter++, SceneManager.getInstance().getGameScene().player.body.getLinearVelocity().x, SceneManager.getInstance().getGameScene().player.body.getLinearVelocity().y, 3.0f);
  	         		streetJumper.this.mSocketServer.sendBroadcastServerMessage(movePlayerServerMessage);
  	         		streetJumper.this.mMessagePool.recycleMessage(movePlayerServerMessage);
  	         	}
  	         	
  	         	// Move Client player in SERVER machine
  	         	if (SceneManager.getInstance().getGameScene() != null && SceneManager.getInstance().getGameScene().firstTouch == true && mSocketServer == null)
  	         	{
  	         		final MovePlayerClientServerMessage movePlayerClientServerMessage = (MovePlayerClientServerMessage) streetJumper.this.mMessagePool.obtainMessage(FLAG_MESSAGE_CLIENT_MOVE_PLAYER_CLIENT);
  	         		movePlayerClientServerMessage.set(ResourcesManager.getInstance().activity.mPlayerIDCounter++, SceneManager.getInstance().getGameScene().player.body.getLinearVelocity().x, SceneManager.getInstance().getGameScene().player.body.getLinearVelocity().y, 3.0f);
  	         		streetJumper.this.mServerConnector.sendClientMessage(movePlayerClientServerMessage);
  	         		streetJumper.this.mMessagePool.recycleMessage(movePlayerClientServerMessage);
  	         	}
  	         }
  	       }
  	     );
  		this.mEngine.registerUpdateHandler(playT);
  		SceneManager.getInstance().createSplashScene(pOnCreateSceneCallback);

  	  }
  	  else
  	  {
  	    // This is where you could create an OpenGL ES 1.x compatible
  	    // renderer if you wanted to support both ES 1 and ES 2.
  		  System.exit(2);

  	  }
		
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
    public boolean onKeyDown(int keyCode, KeyEvent event) 
    {  
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            SceneManager.getInstance().getCurrentScene().onBackKeyPressed();
        }
        
        if (keyCode == KeyEvent.KEYCODE_MENU && event.getAction() == KeyEvent.ACTION_DOWN) 
        {
            if (this.mEngine.isRunning() && SceneManager.getInstance().getGameScene() != null) 
            {
            	// Put Pause Text ...
            	this.mEngine.stop();
            	ResourcesManager.getInstance().getSceneMusic().pause();
            }
            else if (!this.mEngine.isRunning() && SceneManager.getInstance().getGameScene() != null)
            {
            	this.mEngine.start();
            	ResourcesManager.getInstance().getSceneMusic().play();
            }
            else
            {
            	this.showDialog(DIALOG_CHOOSE_SERVER_OR_CLIENT_ID);
            }
        }
        return false; 
    }
	
	@SuppressWarnings("deprecation")
	@Override
    protected Dialog onCreateDialog(final int pID) 
	{
        switch(pID)
        {
            case DIALOG_SHOW_SERVER_IP_ID:
				try 
				{
					return new AlertDialog.Builder(this)
					.setIcon(android.R.drawable.ic_dialog_info)
					.setTitle("Your Server-IP ...")
					.setCancelable(false)
					.setMessage("The IP of your Server is:\n" + WifiUtils.getWifiIPv4Address(this))
					.setPositiveButton(android.R.string.ok, null)
					.create();
				} 
				catch (WifiUtilsException e) 
				{
					return new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Your Server-IP ...")
                    .setCancelable(false)
                    .setMessage("Error retrieving IP of your Server: " + e)
                    .setPositiveButton(android.R.string.ok, new OnClickListener()
                    {
                        @Override
                        public void onClick(final DialogInterface pDialog, final int pWhich)
                        {
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
                .setPositiveButton("Connect", new OnClickListener() 
                {
                    public void onClick(final DialogInterface pDialog, final int pWhich) 
                    {
                            streetJumper.this.mServerIP = ipEditText.getText().toString();
                            streetJumper.this.initClient();
                    }
                })
                .setNegativeButton(android.R.string.cancel, new OnClickListener() 
                {
                    @Override
                    public void onClick(final DialogInterface pDialog, final int pWhich) 
                    {
                    	pDialog.cancel();
                    }
                })
                .create();
                
             case DIALOG_CHOOSE_SERVER_OR_CLIENT_ID:

                return new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_info)
                .setTitle("Be Server or Client ...")
                .setCancelable(false)
                .setPositiveButton("Client", new OnClickListener() 
                {
                    @SuppressWarnings("deprecation")
					@Override
                    public void onClick(final DialogInterface pDialog, final int pWhich) 
                    {
                    	streetJumper.this.showDialog(DIALOG_ENTER_SERVER_IP_ID);
                    }
                })
                .setNeutralButton("Server", new OnClickListener() 
                {
                    @SuppressWarnings("deprecation")
					@Override
                    public void onClick(final DialogInterface pDialog, final int pWhich)
                    {
                    	streetJumper.this.initServer();
                        streetJumper.this.showDialog(DIALOG_SHOW_SERVER_IP_ID);
                    }
                })
                .setNegativeButton("Both", new OnClickListener() 
                {
                    @SuppressWarnings("deprecation")
					@Override
                    public void onClick(final DialogInterface pDialog, final int pWhich)
                    {
                    	streetJumper.this.initServer();
                    	streetJumper.this.showDialog(DIALOG_ENTER_SERVER_IP_ID);
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
    protected void onDestroy() 
	{
        if(this.mSocketServer != null) 
        {
            this.mSocketServer.sendBroadcastServerMessage(new ConnectionCloseServerMessage());
            this.mSocketServer.terminate();
        }

        if(this.mServerConnector != null) 
        {
            this.mServerConnector.terminate();
        }

        super.onDestroy();
    }
  
    
	// ===========================================================
    // Methods
    // ===========================================================
    
    /**
     * 
     * @param pID : Sprite ID (Identificator)
     * @param pX : Sprite x-axis position
     * @param pY : Spite y-axis position
     * 
     * Method that migrates the code to client machine and it is executed into the CLIENT.
     */
    public void playerSelection(int playerID, final float pX, final float pY)
    {
    	// Obtain Client Engine Scene
    	final Scene scene = this.mEngine.getScene();
    	
    	// Create variable that contains player online information
    	PlayerOnline playerOnline = SceneManager.getInstance().getGameScene().playerOnline;
    	playerOnline.setPosition(pX, pY);
    	playerOnline.setUserData(playerID);
        
        // Put ID to mPlayers list
        this.mPlayers.put(playerID, playerOnline);
        
        // configure player options
        scene.registerTouchArea(playerOnline);
        playerOnline.setVisible(true);
        playerOnline.setRunning();
    }
	
    /**
     * 
     * @param pID : Sprite ID (Identificator)
     * @param vX : Sprite x-axis velocity
     * @param vY : Spite y-axis velocity
     * 
     * Method that sends player position to other smartphone conected
     */
	public void movePlayer(final int pID, final float vX, float vY, float jumpImpulse) 
	{
		if (SceneManager.getInstance().getGameScene() != null)
		{
			PlayerOnline playerOnline = SceneManager.getInstance().getGameScene().playerOnline;
			if (SceneManager.getInstance().getGameScene().isJumping == true) 
			{
				vY = vY * jumpImpulse;
				SceneManager.getInstance().getGameScene().isJumping = false;
			}
	        playerOnline.body.setLinearVelocity(vX, vY);
	        
	     // Comprovate Flipped mode
	        if (vX > 0.0f)
	        {
	        	playerOnline.setFlippedHorizontal(false);
	        }
	        else
	        {
	        	playerOnline.setFlippedHorizontal(true);
	        }
		}
    
	}
	
	/**
     * 
     * @param pID : Sprite ID (Player Texture Identificator)
     * 
     * Method load apropiate texture
     */
    public void playerLoad(int playerID, final float pX, final float pY)
    {
    	// Obtain Client Engine Scene
    	final Scene scene = this.mEngine.getScene();
    	
    	BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/game/Players/");
    	if (playerID == 0 && resourcesManager.controlLoaded == true) resourcesManager.playerOnline_region = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(resourcesManager.gameTextureAtlas, this, "player.png", 3, 1);
        if (playerID == 1 && resourcesManager.controlLoaded == true) resourcesManager.playerOnline_region = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(resourcesManager.gameTextureAtlas, this, "player2.png", 3, 1);
        if (playerID == 2 && resourcesManager.controlLoaded == true) resourcesManager.playerOnline_region = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(resourcesManager.gameTextureAtlas, this, "player3.png", 3, 1);
        if (playerID == 3 && resourcesManager.controlLoaded == true) resourcesManager.playerOnline_region = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(resourcesManager.gameTextureAtlas, this, "player4.png", 3, 1);
        
        SceneManager.getInstance().getGameScene().playerOnline = new PlayerOnline(pX, pY, this.getVertexBufferObjectManager(), camera, SceneManager.getInstance().getGameScene().physicsWorld)
        {	
        };
        if (resourcesManager.getLevelComplete() == 1) SceneManager.getInstance().getGameScene().playerOnline.setSize(75, 75);
        if (resourcesManager.getLevelComplete() == 2 || resourcesManager.getLevelComplete() == 3) SceneManager.getInstance().getGameScene().playerOnline.setSize(90, 90);
        
        try 
        {
            resourcesManager.gameTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
            resourcesManager.gameTextureAtlas.load();
            scene.attachChild(SceneManager.getInstance().getGameScene().playerOnline);
        } 
        catch (final TextureAtlasBuilderException e)
        {
            Debug.e(e);
        }        
    }


	private void initServer()
	{
		this.mSocketServer = new SocketServer<SocketConnectionClientConnector>(SERVER_PORT, new ExampleClientConnectorListener(), new ExampleServerStateListener()) 
		{
	        @Override
	        protected SocketConnectionClientConnector newClientConnector(final SocketConnection pSocketConnection) throws IOException
	        {          
                mClientConnector = new SocketConnectionClientConnector(pSocketConnection);
            	
                mClientConnector.registerClientMessage(FLAG_MESSAGE_CLIENT_PLAYER_SELECTED_CLIENT, PlayerSelectedClientServerMessage.class, new IClientMessageHandler<SocketConnection>() 
        		{
	                @Override
	                public void onHandleMessage(final ClientConnector<SocketConnection> pClientConnector, final IClientMessage pClientMessage) throws IOException
	                {
	                	final PlayerSelectedClientServerMessage playerSelectedClientServerMessage = (PlayerSelectedClientServerMessage)pClientMessage;
	                    streetJumper.this.playerSelection(playerSelectedClientServerMessage.mPlayerID, playerSelectedClientServerMessage.mX, playerSelectedClientServerMessage.mY);
	                }
                });
                
                mClientConnector.registerClientMessage(FLAG_MESSAGE_CLIENT_MOVE_PLAYER_CLIENT, MovePlayerClientServerMessage.class, new IClientMessageHandler<SocketConnection>() 
        		{
	                @Override
	                public void onHandleMessage(final ClientConnector<SocketConnection> pClientConnector, final IClientMessage pClientMessage) throws IOException
	                {
	                	final MovePlayerClientServerMessage movePlayerClientServerMessage = (MovePlayerClientServerMessage)pClientMessage;
	                    streetJumper.this.movePlayer(movePlayerClientServerMessage.mID, movePlayerClientServerMessage.mX, movePlayerClientServerMessage.mY, movePlayerClientServerMessage.jumpImpulse);
	                }
                });
                
                mClientConnector.registerClientMessage(FLAG_MESSAGE_CLIENT_PLAYER, PlayerClient.class, new IClientMessageHandler<SocketConnection>() 
        		{
	                @Override
	                public void onHandleMessage(final ClientConnector<SocketConnection> pClientConnector, final IClientMessage pClientMessage) throws IOException
	                {
	                	final PlayerClient playerClient = (PlayerClient)pClientMessage;
	                    streetJumper.this.playerLoad(playerClient.playerRegion, playerClient.pX, playerClient.pY);
	                }
                });
                
                return mClientConnector;
	        }
		};
	
	    this.mSocketServer.start();
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


	private void initClient() 
	{
	    try
	    {
            this.mServerConnector = new SocketConnectionServerConnector(new SocketConnection(new Socket(this.mServerIP, SERVER_PORT)), new ExampleServerConnectorListener());

            this.mServerConnector.registerServerMessage(FLAG_MESSAGE_SERVER_CONNECTION_CLOSE, ConnectionCloseServerMessage.class, new IServerMessageHandler<SocketConnection>() 
            {
                @Override
                public void onHandleMessage(final ServerConnector<SocketConnection> pServerConnector, final IServerMessage pServerMessage) throws IOException 
                {
                	streetJumper.this.finish();
                }
            });
            
            this.mServerConnector.registerServerMessage(FLAG_MESSAGE_SERVER_PLAYER_SELECTED, PlayerSelectedServerMessage.class, new IServerMessageHandler<SocketConnection>() 
            {
                @Override
                public void onHandleMessage(final ServerConnector<SocketConnection> pServerConnector, final IServerMessage pServerMessage) throws IOException 
                {
                	final PlayerSelectedServerMessage playerSelectedServerMessage = (PlayerSelectedServerMessage)pServerMessage;
                    streetJumper.this.playerSelection(playerSelectedServerMessage.mPlayerID, playerSelectedServerMessage.mX, playerSelectedServerMessage.mY);
                }
            });

            this.mServerConnector.registerServerMessage(FLAG_MESSAGE_SERVER_MOVE_PLAYER, movePlayerServerMessage.class, new IServerMessageHandler<SocketConnection>()
            {
                @Override
                public void onHandleMessage(final ServerConnector<SocketConnection> pServerConnector, final IServerMessage pServerMessage) throws IOException
                {
                    final movePlayerServerMessage movePlayerServerMessage = (movePlayerServerMessage)pServerMessage;
                    streetJumper.this.movePlayer(movePlayerServerMessage.mID, movePlayerServerMessage.mX, movePlayerServerMessage.mY, movePlayerServerMessage.Jumpimpulse);
                }
            });
            
            this.mServerConnector.registerServerMessage(FLAG_MESSAGE_SERVER_PLAYER, PlayerServer.class, new IServerMessageHandler<SocketConnection>()
            {
                @Override
                public void onHandleMessage(final ServerConnector<SocketConnection> pServerConnector, final IServerMessage pServerMessage) throws IOException
                {
                    final PlayerServer playerServer = (PlayerServer)pServerMessage;
                    streetJumper.this.playerLoad(playerServer.playerRegion, playerServer.pX, playerServer.pY);
                }
            });
       
            this.mServerConnector.getConnection().start();
	    } 
	    
	    catch (final Throwable t) 
	    {
            Debug.e(t);
		}
	}

	private void log(final String pMessage)
	{
	    Debug.d(pMessage);
	}
	
	public void toast(final String pMessage) 
	{
	    this.log(pMessage);
	    this.runOnUiThread(new Runnable()
	    {
            @Override
            public void run()
            {
                Toast.makeText(streetJumper.this, pMessage, Toast.LENGTH_LONG).show();
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
    	if (SceneManager.getInstance().getGameScene() != null && SceneManager.getInstance().getGameScene().player.getAc() == false && (pAccelerationData.getX() > 0.25 || pAccelerationData.getX() < -0.25 ))
    	{
    		this.enableAccelerationSensor(this);
    		if (pAccelerationData.getX() > 0.0f)
        	{
            	SceneManager.getInstance().getGameScene().player.body.setLinearVelocity((float) (pAccelerationData.getX()*2.5f), SceneManager.getInstance().getGameScene().player.body.getLinearVelocity().y);
            	SceneManager.getInstance().getGameScene().player.setFlippedHorizontal(false);
            	
        	}  
        	
        	if (pAccelerationData.getX() < 0.0f)
        	{ 
        		SceneManager.getInstance().getGameScene().player.body.setLinearVelocity((float) (pAccelerationData.getX()*2.5f), SceneManager.getInstance().getGameScene().player.body.getLinearVelocity().y);
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

    public static class PlayerSelectedServerMessage extends ServerMessage
    {
        private int mPlayerID;
        private float mX;
        private float mY;

        public PlayerSelectedServerMessage()
        {

        }

        public PlayerSelectedServerMessage(final int pID, final float pX, final float pY) 
        {
                this.mPlayerID = pID;
                this.mX = pX;
                this.mY = pY;
        }

        public void set(final int pID, final float pX, final float pY) 
        {
                this.mPlayerID = pID;
                this.mX = pX;
                this.mY = pY;
        }

        @Override
        public short getFlag()
        {
                return FLAG_MESSAGE_SERVER_PLAYER_SELECTED;
        }

        @Override
        protected void onReadTransmissionData(final DataInputStream pDataInputStream) throws IOException
        {
                this.mPlayerID = pDataInputStream.readInt();
                this.mX = pDataInputStream.readFloat();
                this.mY = pDataInputStream.readFloat();
        }

        @Override
        protected void onWriteTransmissionData(final DataOutputStream pDataOutputStream) throws IOException 
        {
                pDataOutputStream.writeInt(this.mPlayerID);
                pDataOutputStream.writeFloat(this.mX);
                pDataOutputStream.writeFloat(this.mY);
        }
    }
    
    public static class PlayerSelectedClientServerMessage extends ClientMessage
    {
        private int mPlayerID;
        private float mX;
        private float mY;

        public PlayerSelectedClientServerMessage()
        {

        }

        public PlayerSelectedClientServerMessage(final int pID, final float pX, final float pY) 
        {
                this.mPlayerID = pID;
                this.mX = pX;
                this.mY = pY;
        }

        public void set(final int pID, final float pX, final float pY) 
        {
                this.mPlayerID = pID;
                this.mX = pX;
                this.mY = pY;
        }

        @Override
        public short getFlag()
        {
                return FLAG_MESSAGE_CLIENT_PLAYER_SELECTED_CLIENT;
        }

        @Override
        protected void onReadTransmissionData(final DataInputStream pDataInputStream) throws IOException
        {
                this.mPlayerID = pDataInputStream.readInt();
                this.mX = pDataInputStream.readFloat();
                this.mY = pDataInputStream.readFloat();
        }

        @Override
        protected void onWriteTransmissionData(final DataOutputStream pDataOutputStream) throws IOException 
        {
                pDataOutputStream.writeInt(this.mPlayerID);
                pDataOutputStream.writeFloat(this.mX);
                pDataOutputStream.writeFloat(this.mY);
        }
    }

	public static class movePlayerServerMessage extends ServerMessage 
	{
        private int mID;
        private float mX;
        private float mY;
        private float Jumpimpulse;

        public movePlayerServerMessage() 
        {

        }

        public movePlayerServerMessage(final int pID, final float pX, final float pY, float jumpImpulse) 
        {
                this.mID = pID;
                this.mX = pX;
                this.mY = pY;
                this.Jumpimpulse = jumpImpulse;
        }

        public void set(final int pID, final float pX, final float pY, float jumpImpulse)
        {
                this.mID = pID;
                this.mX = pX;
                this.mY = pY;
                this.Jumpimpulse = jumpImpulse;
        }

        @Override
        public short getFlag()
        {
                return FLAG_MESSAGE_SERVER_MOVE_PLAYER;
        }

        @Override
        protected void onReadTransmissionData(final DataInputStream pDataInputStream) throws IOException 
        {
                this.mID = pDataInputStream.readInt();
                this.mX = pDataInputStream.readFloat();
                this.mY = pDataInputStream.readFloat();
                this.Jumpimpulse = pDataInputStream.readFloat();
        }

        @Override
        protected void onWriteTransmissionData(final DataOutputStream pDataOutputStream) throws IOException
        {
                pDataOutputStream.writeInt(this.mID);
                pDataOutputStream.writeFloat(this.mX);
                pDataOutputStream.writeFloat(this.mY);
                pDataOutputStream.writeFloat(this.Jumpimpulse);
        }
	}
	
	public static class MovePlayerClientServerMessage extends ClientMessage 
	{
        private int mID;
        private float mX;
        private float mY;
        private float jumpImpulse;

        public MovePlayerClientServerMessage() 
        {

        }

        public MovePlayerClientServerMessage(final int pID, final float pX, final float pY, float jumpImpulse) 
        {
                this.mID = pID;
                this.mX = pX;
                this.mY = pY;
                this.jumpImpulse = jumpImpulse;
        }

        public void set(final int pID, final float pX, final float pY, float jumpImpulse)
        {
                this.mID = pID;
                this.mX = pX;
                this.mY = pY;
                this.jumpImpulse = jumpImpulse;
        }

        @Override
        public short getFlag()
        {
                return FLAG_MESSAGE_CLIENT_MOVE_PLAYER_CLIENT;
        }

        @Override
        protected void onReadTransmissionData(final DataInputStream pDataInputStream) throws IOException 
        {
                this.mID = pDataInputStream.readInt();
                this.mX = pDataInputStream.readFloat();
                this.mY = pDataInputStream.readFloat();
                this.jumpImpulse = pDataInputStream.readFloat();
        }

        @Override
        protected void onWriteTransmissionData(final DataOutputStream pDataOutputStream) throws IOException
        {
                pDataOutputStream.writeInt(this.mID);
                pDataOutputStream.writeFloat(this.mX);
                pDataOutputStream.writeFloat(this.mY);
                pDataOutputStream.writeFloat(this.jumpImpulse);
        }
	}
	
	public static class PlayerServer extends ServerMessage 
	{
        private int playerRegion;
        private float pX;
        private float pY;

        public PlayerServer() 
        {

        }

        public PlayerServer(int pR, float pX, float pY) 
        {
                this.playerRegion = pR;
                this.pX = pX;
                this.pY = pY;
        }

        public void set(int pR, float pX, float pY)
        {
              this.playerRegion = pR;
              this.pX = pX;
              this.pY = pY;
        }

        @Override
        public short getFlag()
        {
                return FLAG_MESSAGE_SERVER_PLAYER;
        }

        @Override
        protected void onReadTransmissionData(final DataInputStream pDataInputStream) throws IOException 
        {
                this.playerRegion = pDataInputStream.readInt();
                this.pX = pDataInputStream.readFloat();
                this.pY = pDataInputStream.readFloat();
        }

        @Override
        protected void onWriteTransmissionData(final DataOutputStream pDataOutputStream) throws IOException
        {
                pDataOutputStream.writeInt(this.playerRegion);
                pDataOutputStream.writeFloat(this.pX);
                pDataOutputStream.writeFloat(this.pY);
        }
	}
	
	public static class PlayerClient extends ClientMessage 
	{
        private int playerRegion;
        private float pX;
        private float pY;

        public PlayerClient() 
        {

        }

        public PlayerClient(int pR, float pX, float pY) 
        {
                this.playerRegion = pR;
                this.pX = pX;
                this.pY = pY;
        }

        public void set(int pR, float pX, float pY)
        {
              this.playerRegion = pR;
              this.pX = pX;
              this.pY = pY;
        }

        @Override
        public short getFlag()
        {
                return FLAG_MESSAGE_CLIENT_PLAYER;
        }

        @Override
        protected void onReadTransmissionData(final DataInputStream pDataInputStream) throws IOException 
        {
                this.playerRegion = pDataInputStream.readInt();
                this.pX = pDataInputStream.readFloat();
                this.pY = pDataInputStream.readFloat();
        }

        @Override
        protected void onWriteTransmissionData(final DataOutputStream pDataOutputStream) throws IOException
        {
                pDataOutputStream.writeInt(this.playerRegion);
                pDataOutputStream.writeFloat(this.pX);
                pDataOutputStream.writeFloat(this.pY);
        }
	}
	
	
	private class ExampleServerConnectorListener implements ISocketConnectionServerConnectorListener 
	{
        @Override
        public void onStarted(final ServerConnector<SocketConnection> pConnector) 
        {
                streetJumper.this.toast("CLIENT: Connected to server.");
        }

        @Override
        public void onTerminated(final ServerConnector<SocketConnection> pConnector) 
        {
        	streetJumper.this.toast("CLIENT: Disconnected from Server...");
        	streetJumper.this.finish();
        }
	}
	
	private class ExampleServerStateListener implements ISocketServerListener<SocketConnectionClientConnector>
	{
        @Override
        public void onStarted(final SocketServer<SocketConnectionClientConnector> pSocketServer) 
        {
        	streetJumper.this.toast("SERVER: Started.");
        }

        @Override
        public void onTerminated(final SocketServer<SocketConnectionClientConnector> pSocketServer) 
        {
        	streetJumper.this.toast("SERVER: Terminated.");
        }

        @Override
        public void onException(final SocketServer<SocketConnectionClientConnector> pSocketServer, final Throwable pThrowable) 
        {
                Debug.e(pThrowable);
                streetJumper.this.toast("SERVER: Exception: " + pThrowable);
        }
	}
	
	private class ExampleClientConnectorListener implements ISocketConnectionClientConnectorListener 
	{
        @Override
        public void onStarted(final ClientConnector<SocketConnection> pConnector)
        {
        	streetJumper.this.toast("SERVER: Client connected: " + pConnector.getConnection().getSocket().getInetAddress().getHostAddress());
        }

        @Override
        public void onTerminated(final ClientConnector<SocketConnection> pConnector) 
        {
        	streetJumper.this.toast("SERVER: Client disconnected: " + pConnector.getConnection().getSocket().getInetAddress().getHostAddress());
        }
	}
}
