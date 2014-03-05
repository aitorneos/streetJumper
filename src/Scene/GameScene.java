package Scene;

//---------------------------------- JAVA && TEXTURES IMPORTS ------------------------------------------------------
import java.io.IOException;
import java.util.Iterator;
import javax.microedition.khronos.opengles.GL10;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.particle.SpriteParticleSystem;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.extension.physics.box2d.FixedStepPhysicsWorld;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.util.GLState;
import org.andengine.util.SAXUtils;
import org.andengine.util.adt.align.HorizontalAlign;
import org.andengine.util.adt.color.Color;
import org.andengine.util.level.EntityLoader;
import org.andengine.util.level.constants.LevelConstants;
import org.andengine.util.level.simple.SimpleLevelEntityLoaderData;
import org.andengine.util.level.simple.SimpleLevelLoader;
import org.xml.sax.Attributes;

//---------------------------------- PARTICLE SYSTEM && NETWORK ------------------------------------------------------
import particleSystem.FireParticleSystem;
import particleSystem.expulsorParticleSystem;
import particleSystem.waterExplosion;
import particleSystem.waterParticleSystem;
import Animated_Features.Bomb;
import Animated_Features.SpringBoarder;
import Animated_Features.Switcher;
import Enemies.Enemy;
import Enemies.NomoEnemy;
import Enemies.RinoEnemy;
import Enemies.ZombiEnemy;
import Network.ClientMessageFlags;
import Network.ServerMessageFlags;
import Players.Player;
import Players.PlayerOnline;
import Players.PlayerSpecial;

//---------------------------------- RESOURCES MANAGEMENT ------------------------------------------------------
import ResourcesManagment.ResourcesManager;
import ResourcesManagment.SceneManager;
import ResourcesManagment.SceneManager.SceneType;

//---------------------------------- SHADERS && TIMERS ------------------------------------------------------
import Scene.LevelCompleteWindow.StarsCount;
import Shader.WaterMaskEffectShader;
import Timers.playTimer;

//---------------------------------- INNER && ANONIMOUS CLASES ------------------------------------------------------
import com.PFC.PlatformJumper.streetJumper;
import com.PFC.PlatformJumper.streetJumper.PlayerClient;
import com.PFC.PlatformJumper.streetJumper.PlayerSelectedServerMessage;
import com.PFC.PlatformJumper.streetJumper.PlayerSelectedClientServerMessage;
import com.PFC.PlatformJumper.streetJumper.PlayerServer;

//---------------------------------- PHYSICS BOX2D EXTENSION ------------------------------------------------------
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.Manifold;

public class GameScene extends BaseScene implements IOnSceneTouchListener, ServerMessageFlags, ClientMessageFlags
{
	
	// ---------------- GENERAL (NON - SPECIFIC) VARIABLES ----------------------
	
	// -------------------- VARIABLES ------------------------
	private HUD gameHUD;
	private Text scoreText;
	private Text lifeText;
	private Text timeText;
	private Text warningText;
	private int score = 0;
	public boolean firstTouch = false;
	public PhysicsWorld physicsWorld;
	private boolean gameOverDisplayed = false;
	private playTimer playT;
	private playTimer flicker;
	public int controlAc = 0;

	
	//Player variables
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLAYER = "player";
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLAYER_ONLINE = "playerOnline";
	public Player player;
	public PlayerOnline playerOnline;
	public PlayerSpecial playerSpecial;
	public RinoEnemy rEnemy;
	private Enemy enemy;
	private ZombiEnemy zombie;
	private NomoEnemy nomo;
	private Switcher switcher;
	private Switcher switcher2;
	private SpringBoarder springboarder;
	private SpringBoarder springboarder2;
	private RinoEnemy slimeEnemy;
	
	
	//Sprites
	Sprite hurt1;
	Sprite hurt2;
	Sprite hurt3;
	Sprite water;
	Sprite waterDis;
	Sprite kY;
	Sprite kG;
	Sprite bomb;
	private boolean playSound = false;
	private boolean playSoundGreen = false;
	private LevelCompleteWindow levelCompleteWindow;
	public boolean isJumping = false;
	
	// Particle systems
	waterParticleSystem ps;
	waterParticleSystem ps2;
	waterParticleSystem ps3;
	waterParticleSystem ps4;
	waterParticleSystem ps5;
	expulsorParticleSystem eps1;
	expulsorParticleSystem eps2;
	SpriteParticleSystem sps;
	SpriteParticleSystem sps2;
	SpriteParticleSystem sps3;
	SpriteParticleSystem sps4;
	SpriteParticleSystem sps5;
	SpriteParticleSystem ex1;
	SpriteParticleSystem ex2;
	FireParticleSystem explosion;
	FireParticleSystem explosion2;
	waterExplosion wp;
	SpriteParticleSystem waterEx;
	waterExplosion drop;
	Text nBombs;
	//private float playerVelocity = 0f;
	
	private static final String TAG_ENTITY = "entity";
	private static final String TAG_ENTITY_ATTRIBUTE_X = "x";
	private static final String TAG_ENTITY_ATTRIBUTE_Y = "y";
	private static final String TAG_ENTITY_ATTRIBUTE_WIDTH = "width";
	private static final String TAG_ENTITY_ATTRIBUTE_HEIGHT = "height";
	private static final String TAG_ENTITY_ATTRIBUTE_TYPE = "type";
	
	//-------------------- LEVEL_1 VARIABLES -------------------------------------------------------------------
	
	// Platform types and Coins definitions
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLATFORM1 = "platform1";
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLATFORM2 = "platform2";
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLATFORM3 = "platform3";
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_LEVEL_COMPLETE = "levelComplete";
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_COIN = "coin";
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_COIN_GOLD = "coin_gold";
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_COIN_BRONZE = "coin_bronze";
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_CASTLE_CLIFF_LEFT = "castleClifftLeft";
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_CASTLE_CLIFF_RIGHT = "castleClifftRight";
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_CASTLE_HALF = "castleHalf";
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_DIRT_CLIFF_LEFT = "dirtCliffLeft";
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_DIRT_CLIFF_RIGHT = "dirtCliffRight";
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_DIRT_HALF = "dirtHalf";
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_SPIKES = "spikes";
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_KEY_YELLOW = "keyYellow";
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_KEY_GREEN = "keyGreen";
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_DOOR_CLOSED_MID = "doorClosedMid";
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_DOOR_CLOSED_GREEN = "doorClosedGreen";
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_MINE = "mine";
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_STALACTITA_LEFT = "stalactitaLeft";
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_STALACTITA_RIGHT = "stalactitaRight";
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_SNOW_HILL = "snowHill";
	
	// Enemies Types Definitions
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_FLY_ENEMY = "flyEnemy_region";
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_SLIME_ENEMY = "slimeEnemy_region";
	 
	 // ---------------------------- LEVEL_2 VARIABLES ---------------------------------------------------------------
	 
	 private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_ARROW_LEFT = "arrowLeft";
	 private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_ROCK = "rock";
	 private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_SAND = "sand";
	 private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_ROCK_HALF_BIG_PLATFORM = "rockHalfBigPlatform";
	 private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_BOX_EXPLOSIVE_ALT = "boxExposiveAlt";
	 private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_BOX_ITEM_ALT = "boxItemAlt";
	 private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_BOX_WARNING = "boxWarning";
	 private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_CACTUS = "cactus";
	 private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_SWITCHER = "switcher";
	 private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_SWITCHER2 = "switcher2";
	 private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_SPRINGBOARDER = "springboarder";
	 private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_SPRINGBOARDER2 = "springboarder2";
	 private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_WATER_SYNTHETIC = "waterSynthetic";
	 private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_GREEN_PLATFORM = "greenPlatform";
	 private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_BLOCK = "block";
	 private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_TREE = "tree";
	 private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLAYER_SPECIAL = "playerSpecial";
	 private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_SUN = "sun";
	 private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_BUTTON_YELLOW = "buttonYellow";
	 
	 
	// ---------------------------- LEVEL 3 VARIABLES ---------------------------------------------------------------
	 private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_CRADLE = "cradle";
	 private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_LITTLE_MOUNTAIN = "littleMountain";
	 private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_MOUNTAIN_PLATFORM = "mountainPlatform";
	 private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLATFORM = "platform";
	 private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLATFORM_SMALL = "platformSmall";
	 private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_BIG_TREE = "bigTree";
	 private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_TRONC_PLATFORM = "troncPlatform";
	 private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_TRONC_SMALL = "troncSmall";	
	 private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_BUSH = "bush";
	 private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_KEY_BLUE = "keyBlue";
	 private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_KEY_RED = "keyRed";
	 private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_SWITCH_GREEN_OFF = "switchGreenOff";	
	 private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_GRASS = "grass";
	 private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_TILE_GRASS = "tileGrass";
	 private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_GRASS_MAIN = "grassMain";
	 private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_RINO = "RinoEnemy";
	 
	 
	// ---------------------------- LEVEL 4 VARIABLES ---------------------------------------------------------------
	 private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_CRADLE1 = "cradle1";
	 private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_CRADLE2 = "cradle2";
	 private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_CRADLE3 = "cradle3";
	 private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_CRADLE4 = "cradle4";
	 private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_CRADLE5 = "cradle5";
	 private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_EXPULSOR = "expulsor";
	 private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_MINERAL = "mineral";
	 private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_MUSHROOM_TREE = "mushroomTree";
	 private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_BOMB = "bomb";
	 private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_ENEMY1 = "enemy1";
	 private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_ENEMY2 = "enemy2";

	// ---------------------- METHODS ----------------------------------------------------------------------------------------------------------
	
	 /**
	  * METHOD TO CREATE GAME/CAMERA HUD 
	  **/
	private void createHUD()
	{
	    gameHUD = new HUD();
	    
	    if (ResourcesManager.getInstance().getLevelComplete() == 1)
	    {
	    	// CREATE SCORE TEXT
		    scoreText = new Text(140, 450, resourcesManager.font, "Score: 0", new TextOptions(HorizontalAlign.LEFT), vbom);
		    lifeText = new Text(75, 390, resourcesManager.font, "Life:", new TextOptions(HorizontalAlign.LEFT), vbom);
		    timeText = new Text(700, 40, resourcesManager.font, "TIME:80", new TextOptions(HorizontalAlign.LEFT), vbom);
		    scoreText.setSkewCenter(0, 0);    
		    gameHUD.attachChild(scoreText);
		    gameHUD.attachChild(lifeText);
		    gameHUD.attachChild(timeText);
		    
		    // Put Initial Life (hurts)
		    hurt1 = new Sprite(160, 390, resourcesManager.hurtHUD, vbom);
			hurt2 = new Sprite(205, 390, resourcesManager.hurtHUD, vbom);
			hurt3 = new Sprite(245, 390, resourcesManager.hurtHUD, vbom);
		    gameHUD.attachChild(hurt1);
		    gameHUD.attachChild(hurt2);
		    gameHUD.attachChild(hurt3);
	    }
	    
	    else if (ResourcesManager.getInstance().getLevelComplete() == 2)
	    {
	    	// CREATE SCORE TEXT
		    scoreText = new Text(155, 450, resourcesManager.font, "Score: 0", new TextOptions(HorizontalAlign.LEFT), vbom);
		    lifeText = new Text(70, 390, resourcesManager.font, "Life:", new TextOptions(HorizontalAlign.LEFT), vbom);
		    timeText = new Text(660, 40, resourcesManager.font, "TIME:80", new TextOptions(HorizontalAlign.LEFT), vbom);
		    scoreText.setSkewCenter(0, 0);    
		    gameHUD.attachChild(scoreText);
		    gameHUD.attachChild(lifeText);
		    gameHUD.attachChild(timeText);
		    
		    // Put Initial Life (hurts)
		    hurt1 = new Sprite(165, 380, resourcesManager.starHUD, vbom);
			hurt2 = new Sprite(205, 380, resourcesManager.starHUD, vbom);
			hurt3 = new Sprite(245, 380, resourcesManager.starHUD, vbom);
		    gameHUD.attachChild(hurt1);
		    gameHUD.attachChild(hurt2);
		    gameHUD.attachChild(hurt3);
	    }

	    else if (ResourcesManager.getInstance().getLevelComplete() == 3)
	    {
	    	// CREATE SCORE TEXT
		    scoreText = new Text(155, 450, resourcesManager.font, "Score: 0", new TextOptions(HorizontalAlign.LEFT), vbom);
		    lifeText = new Text(70, 390, resourcesManager.font, "Life:", new TextOptions(HorizontalAlign.LEFT), vbom);
		    timeText = new Text(660, 40, resourcesManager.font, "TIME:80", new TextOptions(HorizontalAlign.LEFT), vbom);
		    scoreText.setSkewCenter(0, 0);    
		    gameHUD.attachChild(scoreText);
		    gameHUD.attachChild(lifeText);
		    gameHUD.attachChild(timeText);
		    
		    // Put Initial Life (hurts)
		    hurt1 = new Sprite(165, 390, resourcesManager.starHUD, vbom);
			hurt2 = new Sprite(205, 390, resourcesManager.starHUD, vbom);
			hurt3 = new Sprite(245, 390, resourcesManager.starHUD, vbom);
		    gameHUD.attachChild(hurt1);
		    gameHUD.attachChild(hurt2);
		    gameHUD.attachChild(hurt3);
	    }
	    
	    else if (ResourcesManager.getInstance().getLevelComplete() == 4)
	    {
	    	// CREATE SCORE TEXT
		    scoreText = new Text(155, 450, resourcesManager.font, "Score: 0", new TextOptions(HorizontalAlign.LEFT), vbom);
		    lifeText = new Text(70, 390, resourcesManager.font, "Life:", new TextOptions(HorizontalAlign.LEFT), vbom);
		    timeText = new Text(660, 40, resourcesManager.font, "TIME:80", new TextOptions(HorizontalAlign.LEFT), vbom);
		    scoreText.setSkewCenter(0, 0);    
		    gameHUD.attachChild(scoreText);
		    gameHUD.attachChild(lifeText);
		    gameHUD.attachChild(timeText);
		    
		    // Put Initial Life (hurts)
		    hurt1 = new Sprite(165, 390, resourcesManager.hurtHUD, vbom);
			hurt2 = new Sprite(205, 390, resourcesManager.hurtHUD, vbom);
			hurt3 = new Sprite(245, 390, resourcesManager.hurtHUD, vbom);
		    gameHUD.attachChild(hurt1);
		    gameHUD.attachChild(hurt2);
		    gameHUD.attachChild(hurt3);
	    }
	    
	    camera.setHUD(gameHUD);
	}
	
	/**
	 * METHOD RESPONSABLE OF CREATE ALL GENERAL SCENE PHYSICS
	 **/
	private void createPhysics()
	{
		physicsWorld = new FixedStepPhysicsWorld(60, new Vector2(0, -18.0f), false); 
	    physicsWorld.setContactListener(contactListener());
	    registerUpdateHandler(physicsWorld);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	/**
	 * METHOD TO CREATE MAIN GAME SCENARIOS
	 * @see Scene.BaseScene#createScene()
	 **/
    public void createScene()
    {
		 createBackground();
		 createHUD();
		 createPhysics();
		 levelCompleteWindow = new LevelCompleteWindow(vbom);
		 loadLevel(ResourcesManager.getInstance().getLevelComplete());
		 setOnSceneTouchListener(this);
		 
		 controlAc = 0;
		 ResourcesManager.getInstance().controlLoaded = true;
		
		 // -------------------- LEVEL 1 -------------------------------------------------------------------------------------------
		 if (ResourcesManager.getInstance().getLevelComplete() == 1)
		 {
			 player.setRunning();
			 enemy.setRunning();
	         enemy.body.setLinearVelocity(0.5f, 0.0f);
	         ResourcesManager.getInstance().getSceneMusic().play();
 
			// Put "font" snow raining
			 ps = new waterParticleSystem();
			 attachChild (sps = ps.build(engine, 400, 500, ResourcesManager.getInstance().waterDrop));

			 ps2 = new waterParticleSystem();
			 attachChild (sps2 = ps2.build(engine, 150, 500, ResourcesManager.getInstance().waterDrop));
			 
			 ps3 = new waterParticleSystem();
			 attachChild (sps3 = ps3.build(engine, 800, 500, ResourcesManager.getInstance().waterDrop));
			 
			 ps4 = new waterParticleSystem();
			 attachChild (sps4 = ps4.build(engine, 1240, 500, ResourcesManager.getInstance().waterDrop));
			 
			 // create WATER SHADER !
			 water = new Sprite(0, 120, 2048, -240, ResourcesManager.getInstance().waterShader, vbom);
			 water.setWidth(3000);
			 WaterMaskEffectShader.setup(engine, ResourcesManager.getInstance().waterDisplacement, 0.0f, 4.0f);
			 engine.getShaderProgramManager().loadShaderProgram(WaterMaskEffectShader.getShaderProgram());
	         water.setShaderProgram(WaterMaskEffectShader.getShaderProgram());
	         water.setVisible(true);
	         water.setFlippedVertical(true);
	         attachChild(water);
	         ResourcesManager.getInstance().activity.setAccelerometerActivated(true);
	         // Create and initialize timer options an update
			 playT = new playTimer(1.0f, new playTimer.ITimerCallback()
		     {
		         @Override
		         public void onTick()
		         {
		            if (player.time > 0) player.time = player.time - 1;
		            timeText.setText("Time: " + player.time);
		         }
		       }
		     );
			 engine.registerUpdateHandler(playT);	
		 }
		 
		// -------------------- LEVEL 2 -------------------------------------------------------------------------------------------
		 else if (ResourcesManager.getInstance().getLevelComplete() == 2)
		 {
			 player.setRunning();
			 playerSpecial.setRunning();
			 playerSpecial.body.setLinearVelocity(2.0f, 0.0f);
			 ResourcesManager.getInstance().getSceneMusic().play();
			 wp = new waterExplosion();
			 waterEx = wp.build(engine, 1800 , 150);
			 ResourcesManager.getInstance().activity.setAccelerometerActivated(true);
			 warningText = new Text(400, 240, resourcesManager.font, "Switcher Activated!\nSearch It", new TextOptions(HorizontalAlign.CENTER), vbom);
			 warningText.setColor(android.graphics.Color.LTGRAY);
			 
			// Create and initialize timer options an update
			 playT = new playTimer(1.0f, new playTimer.ITimerCallback()
		     {
		         @Override
		         public void onTick()
		         {
		            if (player.time > 0) player.time = player.time - 1;
		            timeText.setText("Time: " + player.time);
		         }
		       }
		     );
			 engine.registerUpdateHandler(playT);	
		 }
		 
		// -------------------- LEVEL 3 -------------------------------------------------------------------------------------------
		 else if (ResourcesManager.getInstance().getLevelComplete() == 3)
		 {
			 player.setRunning();
			 rEnemy.setRunning();
			 rEnemy.body.setLinearVelocity(2.0f, 0.0f);
			 ResourcesManager.getInstance().activity.setAccelerometerActivated(true);
			 ResourcesManager.getInstance().getSceneMusic().play();
			 
			// Put "font" fire raining
			 ps = new waterParticleSystem();
			 attachChild (sps = ps.build(engine, 300, 500, ResourcesManager.getInstance().rainFire));

			 ps2 = new waterParticleSystem();
			 attachChild (sps2 = ps2.build(engine, 600, 500, ResourcesManager.getInstance().rainFire));
			 
			 ps3 = new waterParticleSystem();
			 attachChild (sps3 = ps3.build(engine, 1000, 500, ResourcesManager.getInstance().rainFire));
			 
			 ps4 = new waterParticleSystem();
			 attachChild (sps4 = ps4.build(engine, 1400, 500, ResourcesManager.getInstance().rainFire));
			 
			 ps5 = new waterParticleSystem();
			 attachChild (sps5 = ps5.build(engine, 1700, 500, ResourcesManager.getInstance().rainFire));
			 
			 // create WATER SHADER !
			 /**water = new Sprite(0, 0, ResourcesManager.getInstance().waterShader, vbom);
			 waterDis = new Sprite(0, 0, ResourcesManager.getInstance().waterDis, vbom);
			 water.setSize(1000, 500);
			 attachChild(new WaterSurfaceEntity(1350, 200, water, waterDis, engine));**/
			 			 
			 // Create and initialize timer options an update
			 playT = new playTimer(1.0f, new playTimer.ITimerCallback()
		     {
		         @Override
		         public void onTick()
		         {
		            if (player.time > 0) player.time = player.time - 1;
		            timeText.setText("Time: " + player.time);
		         }
		       }
		     );
			 engine.registerUpdateHandler(playT);	
		 }
		 
		// -------------------- LEVEL 4 -------------------------------------------------------------------------------------------
		 else if (ResourcesManager.getInstance().getLevelComplete() == 4)
		 {
			 ResourcesManager.getInstance().activity.setAccelerometerActivated(true);
			 ResourcesManager.getInstance().getSceneMusic().play();
			 player.setRunning();
			 nomo.body.setLinearVelocity(1.5f, 0.0f);
			 
			 createBombSpriteControl();
			 warningText = new Text(400, 240, resourcesManager.font, "Switcher Activated!\nSearch It", new TextOptions(HorizontalAlign.CENTER), vbom);
			 warningText.setColor(android.graphics.Color.WHITE);
		   	 nBombs = new Text(40, 300, resourcesManager.font, "+" + player.hasBombs, new TextOptions(HorizontalAlign.LEFT), vbom);
			 
			// Put expulsor particles system
			 eps1 = new expulsorParticleSystem();
			 attachChild (ex1 = eps1.build(engine, 1010, 190));
			 
			 eps2 = new expulsorParticleSystem();
			 attachChild (ex2 = eps2.build(engine, 1850, 305));
			 
			// Create and initialize timer options an update
			 playT = new playTimer(1.0f, new playTimer.ITimerCallback()
		     {
		         @Override
		         public void onTick()
		         {
		            if (player.time > 0) player.time = player.time - 1;
		            timeText.setText("Time: " + player.time);
		         }
		       }
		     );
			 engine.registerUpdateHandler(playT);	
		 }
    }

	/**
     * On exit SCENE METHOD
     **/
    @Override
    public void onBackKeyPressed()
    {
    	if (ResourcesManager.getInstance().loading == false)
    	{
    		if (ResourcesManager.getInstance().getLevelComplete() == 1) 
        	{
        		disposeScene(1);
        	}
        	else if (ResourcesManager.getInstance().getLevelComplete() == 2)
        	{
        		disposeScene(2);
        	}
        	else if (ResourcesManager.getInstance().getLevelComplete() == 3)
        	{
        		disposeScene(3);
        	}
    		
        	else if (ResourcesManager.getInstance().getLevelComplete() == 4)
        	{
        		disposeScene(4);
        	}
        	SceneManager.getInstance().loadMenuScene(engine);
    	}
    }

    /**
     * METHOD FOR GET SCENE DATA CONSTANT TYPE
     **/
    @Override
    public SceneType getSceneType()
    {
        return SceneType.SCENE_GAME;
    }

    /**
     * METHOD RESPONSABLE OF DISPOSING GAME SCENE
     **/
    @Override
    public void disposeScene(int levelID)
    {
    	 camera.setHUD(null);
    	 camera.setCenter(400, 240);
    	 camera.setChaseEntity(null);
    	 
    	 ResourcesManager.getInstance().controlLoaded = false;
    	 
    	 if (levelID == 1)
    	 {
    		 ResourcesManager.getInstance().unloadGameTextures(levelID);
        	 ResourcesManager.getInstance().unloadGameSounds();

        	 // code responsible for disposing scene
        	 // removing all game scene objects.
        	 
        	// --------------------------------------- SCENE SPRITES ---------------------------------------------------------------
        	this.clearChildScene();
        	this.clearTouchAreas();
        	this.clearScene();
        	this.detachSelf();
        	this.dispose();

        	 // ------------------------------------------ HUD -----------------------------------------------------------------------
        	 gameHUD.clearChildScene();
        	 gameHUD.clearEntityModifiers();
        	 gameHUD.clearTouchAreas();
        	 gameHUD.clearUpdateHandlers();
        	 gameHUD.detachChild(hurt1);
        	 gameHUD.detachChild(hurt2);
        	 gameHUD.detachChild(hurt3);
        	 gameHUD.detachChild(scoreText);
        	 gameHUD.detachChild(lifeText);
        	 gameHUD.detachChild(timeText);
        	 gameHUD.detachChildren();
        	 gameHUD.detachSelf();
        	 gameHUD.dispose();    	 
        	 
        	 // ------------------------------------------ TEXT ---------------------------------------------------------------------
        	 scoreText.clearEntityModifiers();
        	 scoreText.clearUpdateHandlers();
        	 lifeText.clearEntityModifiers();
        	 lifeText.clearUpdateHandlers();
        	 timeText.clearEntityModifiers();
        	 timeText.clearUpdateHandlers();
        	 
        	 // --------------------------------------------- TIMERS -------------------------------------------------------------------
        	 engine.unregisterUpdateHandler(playT);
        	 engine.unregisterUpdateHandler(flicker);
        	 ResourcesManager.getInstance().activity.setAccelerometerActivated(false);
        	 ResourcesManager.getInstance().font.unload();
        	 ResourcesManager.getInstance().loadingFont.unload();
        	 
        	 // ---------------------------------------------- SPRITES -------------------------------------------------------------------
        	 player.clearEntityModifiers();
        	 player.clearUpdateHandlers();
        	 playerOnline.clearEntityModifiers();
        	 playerOnline.clearUpdateHandlers();
        	 enemy.clearEntityModifiers();
        	 enemy.clearUpdateHandlers();
        	 
        	 hurt1.clearEntityModifiers();
        	 hurt1.detachSelf();
        	 hurt1.dispose();
        	 hurt2.clearEntityModifiers();
        	 hurt2.detachSelf();
        	 hurt2.dispose();
        	 hurt3.clearEntityModifiers();
        	 hurt3.detachSelf();
        	 hurt3.dispose();
        	 
        	 // ------------------------------------------------ SHADERS --------------------------------------------------------------------
        	 water.clearEntityModifiers();
        	 water.clearUpdateHandlers();
        	 
        	// ------------------------------------------------ PARTICLE SYSTEM/S --------------------------------------------------------------------
        	 sps.clearUpdateHandlers();
        	 sps.getParticleEmitter().reset();
        	 sps.reset();
        	 
        	 sps2.clearUpdateHandlers();
        	 sps2.getParticleEmitter().reset();
        	 sps2.reset();
        	 
        	 sps3.clearUpdateHandlers();
        	 sps3.getParticleEmitter().reset();
        	 sps3.reset();
        	 
        	 sps4.clearUpdateHandlers();
        	 sps4.getParticleEmitter().reset();
        	 sps4.reset();
        	 
        	 System.gc();
    	 }
    	 
    	 else if (levelID == 2)
    	 {
    		 ResourcesManager.getInstance().activity.setAccelerometerActivated(false);
    		 ResourcesManager.getInstance().unloadGameTextures(levelID);
        	 ResourcesManager.getInstance().unloadGameSounds();

        	 // code responsible for disposing scene
        	 // removing all game scene objects.
        	 
        	// --------------------------------------- SCENE SPRITES ---------------------------------------------------------------

         	this.clearChildScene();
         	this.clearTouchAreas();
         	this.clearScene();
         	this.detachSelf();
         	this.dispose();
    		 
    		// ------------------------------------------ HUD -----------------------------------------------------------------------
        	 gameHUD.clearChildScene();
        	 gameHUD.clearEntityModifiers();
        	 gameHUD.clearTouchAreas();
        	 gameHUD.clearUpdateHandlers();
        	 gameHUD.detachChild(hurt1);
        	 gameHUD.detachChild(hurt2);
        	 gameHUD.detachChild(hurt3);
        	 gameHUD.detachChild(scoreText);
        	 gameHUD.detachChild(lifeText);
        	 gameHUD.detachChild(timeText);
        	 gameHUD.detachChildren();
        	 gameHUD.detachSelf();
        	 gameHUD.dispose(); 
        	 
        	// --------------------------------------------- TIMERS -------------------------------------------------------------------
        	 engine.unregisterUpdateHandler(playT);
        	 engine.unregisterUpdateHandler(flicker);
        	 ResourcesManager.getInstance().font.unload();
        	 
        	 hurt1.clearEntityModifiers();
        	 hurt1.detachSelf();
        	 hurt1.dispose();
        	 hurt2.clearEntityModifiers();
        	 hurt2.detachSelf();
        	 hurt2.dispose();
        	 hurt3.clearEntityModifiers();
        	 hurt3.detachSelf();
        	 hurt3.dispose();
        	 
        	// ------------------------------------------ PLAYERS -----------------------------------------------------------------------
        	 player.clearEntityModifiers();
        	 player.clearUpdateHandlers();
        	 playerSpecial.clearEntityModifiers();
        	 playerSpecial.clearUpdateHandlers();
        	 playerOnline.clearEntityModifiers();
        	 playerOnline.clearUpdateHandlers();
        	 
        	// ------------------------------------------ TEXT ---------------------------------------------------------------------
        	 scoreText.clearEntityModifiers();
        	 scoreText.clearUpdateHandlers();
        	 lifeText.clearEntityModifiers();
        	 lifeText.clearUpdateHandlers();
        	 timeText.clearEntityModifiers();
        	 timeText.clearUpdateHandlers();
        	 
        	 waterEx.clearUpdateHandlers();
        	 waterEx.getParticleEmitter().reset();
        	 waterEx.reset();
        	 waterEx.detachSelf();
        	 waterEx.dispose();
        	 playerSpecial.destroyBulletSystem();
        	 
        	 System.gc();
    	 }
    	 
    	 else if (levelID == 3)
    	 {
    		 ResourcesManager.getInstance().activity.setAccelerometerActivated(false);
    		 ResourcesManager.getInstance().unloadGameTextures(levelID);
        	 ResourcesManager.getInstance().unloadGameSounds();

        	 // code responsible for disposing scene
        	 // removing all game scene objects.
        	 
        	// --------------------------------------- SCENE SPRITES ---------------------------------------------------------------

         	this.clearChildScene();
         	this.clearTouchAreas();
         	this.clearScene();
         	this.detachSelf();
         	this.dispose();
    		 
    		// ------------------------------------------ HUD -----------------------------------------------------------------------
        	 gameHUD.clearChildScene();
        	 gameHUD.clearEntityModifiers();
        	 gameHUD.clearTouchAreas();
        	 gameHUD.clearUpdateHandlers();
        	 gameHUD.detachChild(hurt1);
        	 gameHUD.detachChild(hurt2);
        	 gameHUD.detachChild(hurt3);
        	 gameHUD.detachChild(scoreText);
        	 gameHUD.detachChild(lifeText);
        	 gameHUD.detachChild(timeText);
        	 gameHUD.detachChildren();
        	 gameHUD.detachSelf();
        	 gameHUD.dispose(); 
        	 
        	 hurt1.clearEntityModifiers();
        	 hurt1.detachSelf();
        	 hurt1.dispose();
        	 hurt2.clearEntityModifiers();
        	 hurt2.detachSelf();
        	 hurt2.dispose();
        	 hurt3.clearEntityModifiers();
        	 hurt3.detachSelf();
        	 hurt3.dispose();
        	 
        	// ------------------------------------------ PLAYERS -----------------------------------------------------------------------
        	 player.clearEntityModifiers();
        	 player.clearUpdateHandlers();
        	 playerOnline.clearEntityModifiers();
        	 playerOnline.clearUpdateHandlers();
        	 rEnemy.clearEntityModifiers();
        	 rEnemy.clearUpdateHandlers();
        	 
        	 scoreText.clearEntityModifiers();
        	 scoreText.clearUpdateHandlers();
        	 lifeText.clearEntityModifiers();
        	 lifeText.clearUpdateHandlers();
        	 timeText.clearEntityModifiers();
        	 timeText.clearUpdateHandlers();
        	 
        	 sps.clearUpdateHandlers();
        	 sps.getParticleEmitter().reset();
        	 sps.reset();
        	 
        	 sps2.clearUpdateHandlers();
        	 sps2.getParticleEmitter().reset();
        	 sps2.reset();
        	 
        	 sps3.clearUpdateHandlers();
        	 sps3.getParticleEmitter().reset();
        	 sps3.reset();
        	 
        	 sps4.clearUpdateHandlers();
        	 sps4.getParticleEmitter().reset();
        	 sps4.reset();
        	 
        	 sps5.clearUpdateHandlers();
        	 sps5.getParticleEmitter().reset();
        	 sps5.reset();
        	 
    		 System.gc();
    	 }
    	 
    	 else if (levelID == 4)
    	 {
    		 ResourcesManager.getInstance().activity.setAccelerometerActivated(false);
    		 ResourcesManager.getInstance().unloadGameTextures(levelID);
        	 ResourcesManager.getInstance().unloadGameSounds();

        	 // code responsible for disposing scene
        	 // removing all game scene objects.
        	 
        	// --------------------------------------- SCENE SPRITES ---------------------------------------------------------------

         	this.clearChildScene();
         	this.clearTouchAreas();
         	this.clearScene();
         	this.detachSelf();
         	this.dispose();
    		 
    		// ------------------------------------------ HUD -----------------------------------------------------------------------
        	 gameHUD.clearChildScene();
        	 gameHUD.clearEntityModifiers();
        	 gameHUD.clearTouchAreas();
        	 gameHUD.clearUpdateHandlers();
        	 gameHUD.detachChild(hurt1);
        	 gameHUD.detachChild(hurt2);
        	 gameHUD.detachChild(hurt3);
        	 gameHUD.detachChild(scoreText);
        	 gameHUD.detachChild(lifeText);
        	 gameHUD.detachChild(timeText);
        	 gameHUD.detachChildren();
        	 gameHUD.detachSelf();
        	 gameHUD.dispose(); 
        	 
        	 hurt1.clearEntityModifiers();
        	 hurt1.detachSelf();
        	 hurt1.dispose();
        	 hurt2.clearEntityModifiers();
        	 hurt2.detachSelf();
        	 hurt2.dispose();
        	 hurt3.clearEntityModifiers();
        	 hurt3.detachSelf();
        	 hurt3.dispose();
        	 
        	 player.clearEntityModifiers();
        	 player.clearUpdateHandlers();
        	 zombie.clearEntityModifiers();
        	 zombie.clearUpdateHandlers();
        	 nomo.clearEntityModifiers();
        	 nomo.clearUpdateHandlers();
        	 
        	 // ----------------------------------------------------- TEXT ----------------------------------------------------------------------------------
        	 
        	 scoreText.clearEntityModifiers();
        	 scoreText.clearUpdateHandlers();
        	 lifeText.clearEntityModifiers();
        	 lifeText.clearUpdateHandlers();
        	 timeText.clearEntityModifiers();
        	 timeText.clearUpdateHandlers();
        	 
        	 nBombs.clearEntityModifiers();
        	 nBombs.clearUpdateHandlers();
        	 nBombs.detachSelf();
        	 nBombs.dispose();
        	 
        	 // ---------------------------------------- PARTICLES --------------------------------------------------------------------------
        	 
        	 eps1.getParticleSystem().clearUpdateHandlers();
        	 eps1.getParticleSystem().getParticleEmitter().reset();
        	 eps1.getParticleSystem().reset();
        	 
        	 eps2.getParticleSystem().clearUpdateHandlers();
        	 eps2.getParticleSystem().getParticleEmitter().reset();
        	 eps2.getParticleSystem().reset();
        	 
        	 System.gc();
    	 }
    }
    
    /**
     * Function to load background level in scenary memory
     **/
    private void createBackground()
    {

    	// Caclculate X axis position , depending on level background size
    	int xPos;
    	if (ResourcesManager.getInstance().getLevelComplete() == 1)
    	{
    		xPos = 650;
    	}
    	else if (ResourcesManager.getInstance().getLevelComplete() == 2)
    	{
    		xPos = 1500;
    	}
    	else
    	{
    		xPos = 1000;
    	}
    	
    	final Sprite sceneSprite = new Sprite(xPos, 240, resourcesManager.scene_background_region, vbom)
	     {
	         @Override
	         protected void preDraw(GLState pGLState, Camera pCamera) 
	         {
	             super.preDraw(pGLState, pCamera);
	             pGLState.enableDither();
	         }
	     };

	     if (ResourcesManager.getInstance().getAR() == false)
	     {
	    	 if (ResourcesManager.getInstance().getLevelComplete() == 2 || ResourcesManager.getInstance().getLevelComplete() == 3)this.getBackground().setColor(Color.BLACK);
	    	 if (ResourcesManager.getInstance().getLevelComplete() == 1) this.getBackground().setColor(Color.TRANSPARENT);
			// create RADIAL BLUR SHADER !
			 /*engine.getShaderProgramManager().loadShaderProgram(RadialBlur.getShaderProgram());
			 sceneSprite.setShaderProgram(RadialBlur.getShaderProgram());*/
			 
	    	 this.attachChild(sceneSprite);
	     }
	     else
	     {
	    	 this.getBackground().setColor(Color.TRANSPARENT);
	    	 this.detachChild(sceneSprite);
	     }
    }
    
    /**
     * Function to add score to player and print result in game HUD
     **/
    private void addToScore(int i)
    {
        score += i;
        scoreText.setText("Score: " + score);
    }
    
    /**
     * Function to Load Level ... 1, 2, etc...
     **/
    private void loadLevel(int levelID)
    {

        // LOAD LEVEL 1 .......... (SNOW AND MOUNTAINS)
        if (levelID == 1)
        {
        	final SimpleLevelLoader levelLoader = new SimpleLevelLoader(vbom);
            final FixtureDef FIXTURE_DEF = PhysicsFactory.createFixtureDef(0, 0.01f, 0.5f);
            levelLoader.registerEntityLoader(new EntityLoader<SimpleLevelEntityLoaderData>(LevelConstants.TAG_LEVEL)
    	    {
    	        public IEntity onLoadEntity(final String pEntityName, final IEntity pParent, final Attributes pAttributes, final SimpleLevelEntityLoaderData pSimpleLevelEntityLoaderData) throws IOException 
    	        {
    	            final int width = SAXUtils.getIntAttributeOrThrow(pAttributes, LevelConstants.TAG_LEVEL_ATTRIBUTE_WIDTH);
    	            final int height = SAXUtils.getIntAttributeOrThrow(pAttributes, LevelConstants.TAG_LEVEL_ATTRIBUTE_HEIGHT);
    		        camera.setBounds(0, 0, width, height); // here we set camera bounds
    		        camera.setBoundsEnabled(true);
    		
    		        return GameScene.this;
    	        }
            });
            
        	levelLoader.registerEntityLoader(new EntityLoader<SimpleLevelEntityLoaderData>(TAG_ENTITY)
		    {
		        public IEntity onLoadEntity(final String pEntityName, final IEntity pParent, final Attributes pAttributes, final SimpleLevelEntityLoaderData pSimpleLevelEntityLoaderData) throws IOException
		        {
		            final int x = SAXUtils.getIntAttributeOrThrow(pAttributes, TAG_ENTITY_ATTRIBUTE_X);
		            final int y = SAXUtils.getIntAttributeOrThrow(pAttributes, TAG_ENTITY_ATTRIBUTE_Y);
		            final String type = SAXUtils.getAttributeOrThrow(pAttributes, TAG_ENTITY_ATTRIBUTE_TYPE);
		            
		            final Sprite levelObject;

	                // --------------- PLATFORMS AND PLAYER ------------------------------------------------------------------------------------
	                if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLATFORM1))
	                {
	                    levelObject = new Sprite(x, y, resourcesManager.platform1_region, vbom);
	                    PhysicsFactory.createBoxBody(physicsWorld, levelObject, BodyType.StaticBody, FIXTURE_DEF).setUserData("platform1");
	                } 
	                
	                else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLAYER))
	                {
	                    player = new Player(x, y, vbom, camera,  physicsWorld)
	                    {
	                        @Override
	                        public void onDie()
	                        {
	                        	 if (!gameOverDisplayed)
	                    	    {
	                    	        displayGameOverText(1);
	                    	    }
	                        }
	                    };
	                    levelObject = player;
	                    levelObject.setSize(75, 75);
	                }
	                
	                else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLAYER_ONLINE))
	                {
	                    playerOnline = new PlayerOnline(x, y, vbom, camera,  physicsWorld)
	                    {
	                        @Override
	                        public void onDie()
	                        {
	                        	// Nothing to do here, GAME OVER is only displyed in the server not inthe client .
	                        }
	                    };
	                    levelObject = playerOnline;
	                    levelObject.setVisible(false);
	                    levelObject.setSize(75, 75);
	                }
	                
	                else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_SNOW_HILL))
	                {
	                    levelObject = new Sprite(x, y, resourcesManager.snowHill, vbom);
	                    final Body body = PhysicsFactory.createBoxBody(physicsWorld, levelObject, BodyType.StaticBody, FIXTURE_DEF);
	                    body.setUserData("snowHill");
	                    physicsWorld.registerPhysicsConnector(new PhysicsConnector(levelObject, body, true, false));
	                }
	                
	                else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLATFORM2))
	                {
	                    levelObject = new Sprite(x, y, resourcesManager.platform2_region, vbom);
	                    final Body body = PhysicsFactory.createBoxBody(physicsWorld, levelObject, BodyType.StaticBody, FIXTURE_DEF);
	                    body.setUserData("platform2");
	                    physicsWorld.registerPhysicsConnector(new PhysicsConnector(levelObject, body, true, false));
	                }
	                else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLATFORM3))
	                {
	                    levelObject = new Sprite(x, y, resourcesManager.platform3_region, vbom);
	                    final Body body = PhysicsFactory.createBoxBody(physicsWorld, levelObject, BodyType.StaticBody, FIXTURE_DEF);
	                    body.setUserData("platform3");
	                    physicsWorld.registerPhysicsConnector(new PhysicsConnector(levelObject, body, true, false));
	                }
	                
	                else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_CASTLE_CLIFF_LEFT))
	                {
	                    levelObject = new Sprite(x, y, resourcesManager.platform_castleLeft_region, vbom);
	                    final Body body = PhysicsFactory.createBoxBody(physicsWorld, levelObject, BodyType.StaticBody, FIXTURE_DEF);
	                    body.setUserData("castleLeft");
	                    physicsWorld.registerPhysicsConnector(new PhysicsConnector(levelObject, body, true, false));
	                }
	                
	                else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_CASTLE_CLIFF_RIGHT))
	                {
	                    levelObject = new Sprite(x, y, resourcesManager.platform_castleRight_region, vbom);
	                    final Body body = PhysicsFactory.createBoxBody(physicsWorld, levelObject, BodyType.StaticBody, FIXTURE_DEF);
	                    body.setUserData("castleRight");
	                    physicsWorld.registerPhysicsConnector(new PhysicsConnector(levelObject, body, true, false));
	                }
	                
	                else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_CASTLE_HALF))
	                {
	                    levelObject = new Sprite(x, y, resourcesManager.platform_castleHalf_region, vbom);
	                    final Body body = PhysicsFactory.createBoxBody(physicsWorld, levelObject, BodyType.StaticBody, FIXTURE_DEF);
	                    body.setUserData("castleHalf");
	                    physicsWorld.registerPhysicsConnector(new PhysicsConnector(levelObject, body, true, false));
	                }
	                
	                else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_DIRT_CLIFF_LEFT))
	                {
	                    levelObject = new Sprite(x, y, resourcesManager.platform_dirtLeft_region, vbom);
	                    final Body body = PhysicsFactory.createBoxBody(physicsWorld, levelObject, BodyType.StaticBody, FIXTURE_DEF);
	                    body.setUserData("dirtLeft");
	                    physicsWorld.registerPhysicsConnector(new PhysicsConnector(levelObject, body, true, false));
	                }
	                
	                else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_DIRT_CLIFF_RIGHT))
	                {
	                    levelObject = new Sprite(x, y, resourcesManager.platform_dirtRight_region, vbom);
	                    final Body body = PhysicsFactory.createBoxBody(physicsWorld, levelObject, BodyType.StaticBody, FIXTURE_DEF);
	                    body.setUserData("dirtRight");
	                    physicsWorld.registerPhysicsConnector(new PhysicsConnector(levelObject, body, true, false));
	                }
	                
	                else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_DIRT_HALF))
	                {
	                    levelObject = new Sprite(x, y, resourcesManager.platform_dirtHalf_region, vbom);
	                    final Body body = PhysicsFactory.createBoxBody(physicsWorld, levelObject, BodyType.StaticBody, FIXTURE_DEF);
	                    body.setUserData("dirtHalf");
	                    physicsWorld.registerPhysicsConnector(new PhysicsConnector(levelObject, body, true, false));
	                }
	                
	                else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_SPIKES))
	                {
	                    levelObject = new Sprite(x, y, resourcesManager.spike, vbom);
	                    final Body body = PhysicsFactory.createBoxBody(physicsWorld, levelObject, BodyType.StaticBody, FIXTURE_DEF);
	                    body.setUserData("spikes");
	                    physicsWorld.registerPhysicsConnector(new PhysicsConnector(levelObject, body, true, false));
	                }
	                
	                else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_DOOR_CLOSED_MID))
	                {
	                    levelObject = new Sprite(x, y, resourcesManager.doorClosed, vbom);
	                    levelObject.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
	                    levelObject.setAlpha(0.5f);
	                    kY = levelObject;
	                    final Body body = PhysicsFactory.createBoxBody(physicsWorld, levelObject, BodyType.StaticBody, FIXTURE_DEF);
	                    body.setUserData("doorClosedMid");
	                    physicsWorld.registerPhysicsConnector(new PhysicsConnector(levelObject, body, true, false));
	                }
	                
	                else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_DOOR_CLOSED_GREEN))
	                {
	                    levelObject = new Sprite(x, y, resourcesManager.doorClosedGreen, vbom);
	                    levelObject.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
	                    levelObject.setAlpha(0.5f);
	                    kG = levelObject;
	                    final Body body = PhysicsFactory.createBoxBody(physicsWorld, levelObject, BodyType.StaticBody, FIXTURE_DEF);
	                    body.setUserData("doorClosedGreen");
	                    physicsWorld.registerPhysicsConnector(new PhysicsConnector(levelObject, body, true, false));
	                }
	                
	                else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_MINE))
	                {
	                    levelObject = new Sprite(x, y, resourcesManager.mine, vbom);
	                    final Body body = PhysicsFactory.createBoxBody(physicsWorld, levelObject, BodyType.StaticBody, FIXTURE_DEF);
	                    body.setUserData("mine");
	                    physicsWorld.registerPhysicsConnector(new PhysicsConnector(levelObject, body, true, false));
	                }
	                
	                // --------------------------------------------- ENEMIES ----------------------------------------------------------------
	                else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_FLY_ENEMY))
	                {
	                    enemy = new Enemy(x, y, vbom, camera,  physicsWorld)
	                    {
	                    	
	                    };
	                    levelObject = enemy;
	                }
	                
	                else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_SLIME_ENEMY))
	                {
	                	slimeEnemy = new RinoEnemy(x, y, vbom, camera,  physicsWorld)
	                    {
	                    	
	                    };
	                    levelObject = slimeEnemy;
	                }
	                
	                else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_LEVEL_COMPLETE))
	                {
	                    levelObject = new Sprite(x, y, resourcesManager.doorF, vbom)
	                    {
	                        @Override
	                        protected void onManagedUpdate(float pSecondsElapsed) 
	                        {
	                            super.onManagedUpdate(pSecondsElapsed);
	                            if (player.hasKey && player.hasGreenKey && playSound && playSoundGreen) 
	                            {
	                            	this.setVisible(true);
	                            }
	                            else
	                            {
	                            	this.setVisible(false);
	                            }
	                            
	                            if (player.collidesWith(this) && player.hasKey && player.hasGreenKey && playSound && playSoundGreen)
	                            {
	                            	
	                            	if (score <= 35 && score >= 30)
	                            	{
	                            		ResourcesManager.getInstance().activity.setAccelerometerActivated(false);
	                            		player.body.setLinearVelocity(0, 0);
	                            		levelCompleteWindow.display(StarsCount.ONE, GameScene.this, camera);
	   	                                this.setIgnoreUpdate(true);
	   	                                ResourcesManager.getInstance().setLevelComplete(2);
	   	                                
	   	                                // Clear Scenary 1 Graphics ...
	   	                                engine.registerUpdateHandler(new TimerHandler(3.0f, new ITimerCallback()
	   	        		                {                                    
	   	        		                    @SuppressWarnings("deprecation")
											public void onTimePassed(final TimerHandler pTimerHandler)
	   	        		                    {
	   	        		                        pTimerHandler.reset();
	   	        		                        
	   	        		                        detachChild(levelCompleteWindow);
	   	    	                            	disposeScene(1);
		   	    	                 			
	   	    	                            	SceneManager.getInstance().loadGameScene(engine, 2);
	   	        		                        engine.unregisterUpdateHandler(pTimerHandler);
	   	        		                    }
	   	        		                }));
	                            	}
	                            	
	                            	else if (score > 35 && score < 45)
	                            	{
	                            		ResourcesManager.getInstance().activity.setAccelerometerActivated(false);
	                            		player.body.setLinearVelocity(0, 0);
	                            		levelCompleteWindow.display(StarsCount.TWO, GameScene.this, camera);
	   	                                this.setIgnoreUpdate(true);
	   	                                ResourcesManager.getInstance().setLevelComplete(2);
	   	                                
	   	                                // Clear Scenary 1 Graphics ...
	   	                                engine.registerUpdateHandler(new TimerHandler(3.0f, new ITimerCallback()
	   	        		                {                                    
	   	        		                    @SuppressWarnings("deprecation")
											public void onTimePassed(final TimerHandler pTimerHandler)
	   	        		                    {
	   	        		                        pTimerHandler.reset();
	   	        		                        
	   	        		                        detachChild(levelCompleteWindow);
	   	    	                            	disposeScene(1);
		   	    	                 			
	   	    	                            	SceneManager.getInstance().loadGameScene(engine, 2);
	   	        		                        engine.unregisterUpdateHandler(pTimerHandler);
	   	        		                    }
	   	        		                }));
	                            	}
	                            	
	                            	else
	                            	{
	                            		ResourcesManager.getInstance().activity.setAccelerometerActivated(false);
	                            		player.body.setLinearVelocity(0, 0);
	                            		levelCompleteWindow.display(StarsCount.THREE, GameScene.this, camera);
	   	                                this.setIgnoreUpdate(true);
	   	                                ResourcesManager.getInstance().setLevelComplete(2);
	   	                                
	   	                                // Clear Scenary 1 Graphics ...
	   	                                engine.registerUpdateHandler(new TimerHandler(3.0f, new ITimerCallback()
	   	        		                {                                    
	   	        		                    @SuppressWarnings("deprecation")
											public void onTimePassed(final TimerHandler pTimerHandler)
	   	        		                    {
	   	        		                        pTimerHandler.reset();
	   	        		                        
	   	        		                        detachChild(levelCompleteWindow);
	   	    	                            	disposeScene(1);
		   	    	                 			
	   	    	                            	SceneManager.getInstance().loadGameScene(engine, 2);
	   	        		                        engine.unregisterUpdateHandler(pTimerHandler);
	   	        		                    }
	   	        		                }));
	                            	}
	                            	
	                            	firstTouch = false;
	                            }
	                        }
	                    };
	                }
	                
	                // ------------------------------- COINS AND KEYS -------------------------------------------------------------------------------
	                else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_COIN))
	                {
	                    levelObject = new Sprite(x, y, resourcesManager.coin_silver, vbom)
	                    {
	                        @Override
	                        protected void onManagedUpdate(float pSecondsElapsed) 
	                        {
	                            super.onManagedUpdate(pSecondsElapsed);
	                            if (player.collidesWith(this))
	                            {
	                                addToScore(3);
	                                ResourcesManager.getInstance().getCoinSound().play();
	                                this.setVisible(false);
	                                this.setIgnoreUpdate(true);
	                            }
	                           
	                        }
	                    };
	                    levelObject.registerEntityModifier(new LoopEntityModifier(new ScaleModifier(1, 1, 1.3f)));
	                } 
	                
	                else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_COIN_GOLD))
	                {
	                    levelObject = new Sprite(x, y, resourcesManager.coin_gold, vbom)
	                    {
	                        @Override
	                        protected void onManagedUpdate(float pSecondsElapsed) 
	                        {
	                            super.onManagedUpdate(pSecondsElapsed);
	                            if (player.collidesWith(this))
	                            {
	                                addToScore(3);
	                                ResourcesManager.getInstance().getCoinSound().play();
	                                this.setVisible(false);
	                                this.setIgnoreUpdate(true);
	                            }
	                           
	                        }
	                    };
	                    levelObject.registerEntityModifier(new LoopEntityModifier(new ScaleModifier(1, 1, 1.3f)));
	                } 
	                
	                else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_COIN_BRONZE))
	                {
	                    levelObject = new Sprite(x, y, resourcesManager.coin_bronze, vbom)
	                    {
	                        @Override
	                        protected void onManagedUpdate(float pSecondsElapsed) 
	                        {
	                            super.onManagedUpdate(pSecondsElapsed);
	                            if (player.collidesWith(this))
	                            {
	                                addToScore(3);
	                                ResourcesManager.getInstance().getCoinSound().play();
	                                this.setVisible(false);
	                                this.setIgnoreUpdate(true);
	                            }
	                           
	                        }
	                    };
	                    levelObject.registerEntityModifier(new LoopEntityModifier(new ScaleModifier(1, 1, 1.3f)));
	                } 
	                
	                else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_KEY_YELLOW))
	                {
	                    levelObject = new Sprite(x, y, resourcesManager.keyY, vbom)
	                    {
	                        @Override
	                        protected void onManagedUpdate(float pSecondsElapsed) 
	                        {
	                            super.onManagedUpdate(pSecondsElapsed);
	                            if (player.collidesWith(this))
	                            {
	                            	player.hasKey = true;
	                            	final Sprite key = new Sprite(30, 340, resourcesManager.keyHUD, vbom);
	                     	    	gameHUD.attachChild(key);
	                                this.setVisible(false);
	                                this.setIgnoreUpdate(true);
	                            }
	                           
	                        }
	                    };
	                    levelObject.registerEntityModifier(new LoopEntityModifier(new ScaleModifier(1, 1, 1.3f)));
	                } 
	                
	                else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_KEY_GREEN))
	                {
	                    levelObject = new Sprite(x, y, resourcesManager.keyG, vbom)
	                    {
	                        @Override
	                        protected void onManagedUpdate(float pSecondsElapsed) 
	                        {
	                            super.onManagedUpdate(pSecondsElapsed);
	                            if (player.collidesWith(this))
	                            {
	                            	player.hasGreenKey = true;
	                           	 	final Sprite keyG = new Sprite(65, 340, resourcesManager.keyGreenHUD, vbom);
	                     	    	gameHUD.attachChild(keyG);
	                                this.setVisible(false);
	                                this.setIgnoreUpdate(true);
	                            }
	                           
	                        }
	                    };
	                    levelObject.registerEntityModifier(new LoopEntityModifier(new ScaleModifier(1, 1, 1.3f)));
	                } 
	                
	                // ------------------------------------------------------- WATER OBJECTS ----------------------------------------------------------------------------------------
	                
	                else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_STALACTITA_LEFT))
	                {
	                	levelObject = new Sprite(x, y, resourcesManager.stalactitaLeft, vbom);
	                    final Body body = PhysicsFactory.createBoxBody(physicsWorld, levelObject, BodyType.StaticBody, FIXTURE_DEF);
	                    body.setUserData("stalactitaLeft");
	                    physicsWorld.registerPhysicsConnector(new PhysicsConnector(levelObject, body, true, false));
	                } 
	                
	                else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_STALACTITA_RIGHT))
	                {
	                	levelObject = new Sprite(x, y, resourcesManager.stalactitaRight, vbom);
	                    final Body body = PhysicsFactory.createBoxBody(physicsWorld, levelObject, BodyType.StaticBody, FIXTURE_DEF);
	                    body.setUserData("stalactitaRight");
	                    physicsWorld.registerPhysicsConnector(new PhysicsConnector(levelObject, body, true, false));
	                } 
	                
	                else
	                {
	                    throw new IllegalArgumentException();
	                }

	                levelObject.setCullingEnabled(true);

	                return levelObject;
	            }
	        });
        	
        	levelLoader.loadLevelFromAsset(activity.getAssets(), "level/" + levelID + ".lvl");
        }
        
        // LOAD LEVEL 2 ......... (DESERT AND CACTUS)
        else if (levelID == 2)
        {
   
        	final SimpleLevelLoader levelLoader = new SimpleLevelLoader(vbom);
            final FixtureDef FIXTURE_DEF = PhysicsFactory.createFixtureDef(0, 0.01f, 0.5f);
            levelLoader.registerEntityLoader(new EntityLoader<SimpleLevelEntityLoaderData>(LevelConstants.TAG_LEVEL)
    	    {
    	        public IEntity onLoadEntity(final String pEntityName, final IEntity pParent, final Attributes pAttributes, final SimpleLevelEntityLoaderData pSimpleLevelEntityLoaderData) throws IOException 
    	        {
    	            final int width = SAXUtils.getIntAttributeOrThrow(pAttributes, LevelConstants.TAG_LEVEL_ATTRIBUTE_WIDTH);
    	            final int height = SAXUtils.getIntAttributeOrThrow(pAttributes, LevelConstants.TAG_LEVEL_ATTRIBUTE_HEIGHT);
    		        camera.setBounds(0, 0, width, height); // here we set camera bounds
    		        camera.setBoundsEnabled(true);
    		
    		        return GameScene.this;
    	        }
            });
            
        	levelLoader.registerEntityLoader(new EntityLoader<SimpleLevelEntityLoaderData>(TAG_ENTITY)
		    {
		        public IEntity onLoadEntity(final String pEntityName, final IEntity pParent, final Attributes pAttributes, final SimpleLevelEntityLoaderData pSimpleLevelEntityLoaderData) throws IOException
		        {
		            final int x = SAXUtils.getIntAttributeOrThrow(pAttributes, TAG_ENTITY_ATTRIBUTE_X);
		            final int y = SAXUtils.getIntAttributeOrThrow(pAttributes, TAG_ENTITY_ATTRIBUTE_Y);
		            final int width = SAXUtils.getIntAttributeOrThrow(pAttributes, TAG_ENTITY_ATTRIBUTE_WIDTH);
		            final int height = SAXUtils.getIntAttributeOrThrow(pAttributes, TAG_ENTITY_ATTRIBUTE_HEIGHT);
		            final String type = SAXUtils.getAttributeOrThrow(pAttributes, TAG_ENTITY_ATTRIBUTE_TYPE);
		            
		            final Sprite levelObject;


	                // --------------- PLATFORMS AND PLAYER ------------------------------------------------------------------------------------

	                if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_ARROW_LEFT))
	                {
	                    levelObject = new Sprite(x, y, resourcesManager.arrowLeft, vbom);
	                    final Body body = PhysicsFactory.createBoxBody(physicsWorld, levelObject, BodyType.StaticBody, FIXTURE_DEF);
	                    body.setUserData("arrowLeft");
	                    physicsWorld.registerPhysicsConnector(new PhysicsConnector(levelObject, body, true, false));
	                    //levelObject.setSize(width, height);
	                }
	                
	                else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_COIN))
	                {
	                    levelObject = new Sprite(x, y, resourcesManager.coin_silver, vbom)
	                    {
	                        @Override
	                        protected void onManagedUpdate(float pSecondsElapsed) 
	                        {
	                            super.onManagedUpdate(pSecondsElapsed);
	                            if (player.collidesWith(this))
	                            {
	                                addToScore(3);
	                                ResourcesManager.getInstance().getCoinSound().play();
	                                this.setVisible(false);
	                                this.setIgnoreUpdate(true);
	                            }
	                           
	                        }
	                    };
	                    levelObject.registerEntityModifier(new LoopEntityModifier(new ScaleModifier(1, 1, 1.3f)));
	                    //levelObject.setSize(width, height);
	                } 
	                
	                else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_COIN_GOLD))
	                {
	                    levelObject = new Sprite(x, y, resourcesManager.coin_gold, vbom)
	                    {
	                        @Override
	                        protected void onManagedUpdate(float pSecondsElapsed) 
	                        {
	                            super.onManagedUpdate(pSecondsElapsed);
	                            if (player.collidesWith(this))
	                            {
	                                addToScore(5);
	                                ResourcesManager.getInstance().getCoinSound().play();
	                                this.setVisible(false);
	                                this.setIgnoreUpdate(true);
	                            }
	                           
	                        }
	                    };
	                    levelObject.registerEntityModifier(new LoopEntityModifier(new ScaleModifier(1, 1, 1.3f)));
	                    //levelObject.setSize(width, height);
	                } 
	                
	                else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_COIN_BRONZE))
	                {
	                    levelObject = new Sprite(x, y, resourcesManager.coin_bronze, vbom)
	                    {
	                        @Override
	                        protected void onManagedUpdate(float pSecondsElapsed) 
	                        {
	                            super.onManagedUpdate(pSecondsElapsed);
	                            if (player.collidesWith(this))
	                            {
	                                addToScore(1);
	                                ResourcesManager.getInstance().getCoinSound().play();
	                                this.setVisible(false);
	                                this.setIgnoreUpdate(true);
	                            }
	                           
	                        }
	                    };
	                    levelObject.registerEntityModifier(new LoopEntityModifier(new ScaleModifier(1, 1, 1.3f)));
	                    //levelObject.setSize(width, height);
	                } 
	                
	                else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLAYER))
	                {
	                    player = new Player(x, y, vbom, camera,  physicsWorld)
	                    {
	                        @Override
	                        public void onDie()
	                        {
	                            if (!gameOverDisplayed)
	                    	    {
	                    	        displayGameOverText(2);
	                    	    }
	                        }
	                    };
	                    levelObject = player;
	                    levelObject.setSize(width, height);
	                }
	                
	                else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLAYER_ONLINE))
	                {
	                    playerOnline = new PlayerOnline(x, y, vbom, camera,  physicsWorld)
	                    {
	                        @Override
	                        public void onDie()
	                        {
	                        	// Nothing to do here, GAME OVER is only displyed in the server not inthe client .
	                        }
	                    };
	                    levelObject = playerOnline;
	                    levelObject.setVisible(false);
	                    levelObject.setSize(width, height);
	                }
	                
	                else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLAYER_SPECIAL))
	                {
	                    playerSpecial = new PlayerSpecial(x, y, vbom, camera,  physicsWorld)
	                    {
	                        @Override
	                        public void onDie()
	                        {
	                            if (!gameOverDisplayed)
	                    	    {
	                    	        displayGameOverText(2);
	                    	    }
	                        }
	                    };
	                    levelObject = playerSpecial;
	                    levelObject.setSize(width, height);
	                }
	                
	                
	                else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_LEVEL_COMPLETE))
	                {
	                    levelObject = new Sprite(x, y, resourcesManager.doorF, vbom)
	                    {
	                        @Override
	                        protected void onManagedUpdate(float pSecondsElapsed) 
	                        {
	                            super.onManagedUpdate(pSecondsElapsed);
	                            if (player.switch1Touched == true && player.switch2Touched == true) 
	                            {
	                            	this.setVisible(true);
	                            }
	                            else
	                            {
	                            	this.setVisible(false);
	                            }
	                            
	                            if (player.collidesWith(this) && player.switch1Touched && player.switch2Touched)
	                            {
	                            	controlAc = 1;
	                            	if (score <= 35 && score >= 30)
	                            	{
	                            		player.body.setLinearVelocity(0, 0);
	                            		levelCompleteWindow.display(StarsCount.ONE, GameScene.this, camera);
	   	                                this.setIgnoreUpdate(true);
	   	                                ResourcesManager.getInstance().setLevelComplete(3);
	   	                                
	   	                                // Clear Scenary 1 Graphics ...
	   	                                engine.registerUpdateHandler(new TimerHandler(3.0f, new ITimerCallback()
	   	        		                {                                    
	   	        		                    public void onTimePassed(final TimerHandler pTimerHandler)
	   	        		                    {
	   	        		                        pTimerHandler.reset();
	   	        		                        
	   	        		                        detachChild(levelCompleteWindow);
	   	    	                            	disposeScene(2);
	   	    	                            	
	   	    	                            	// Load Level
	   	    	                            	SceneManager.getInstance().loadGameScene(engine, 3);
	   	        		                        engine.unregisterUpdateHandler(pTimerHandler);
	   	        		                    }
	   	        		                }));
	                            	}
	                            	
	                            	else if (score > 50 && score < 65)
	                            	{
	                            		player.body.setLinearVelocity(0, 0);
	                            		levelCompleteWindow.display(StarsCount.TWO, GameScene.this, camera);
	   	                                this.setIgnoreUpdate(true);
	   	                                ResourcesManager.getInstance().setLevelComplete(3);
	   	                                
	   	                                // Clear Scenary 1 Graphics ...
	   	                                engine.registerUpdateHandler(new TimerHandler(3.0f, new ITimerCallback()
	   	        		                {                                    
	   	        		                    public void onTimePassed(final TimerHandler pTimerHandler)
	   	        		                    {
	   	        		                        pTimerHandler.reset();
	   	        		                        
	   	        		                        detachChild(levelCompleteWindow);
	   	    	                            	disposeScene(2);
	   	    	                            	
	   	    	                            	// Load Level
	   	    	                            	SceneManager.getInstance().loadGameScene(engine, 3);
	   	        		                        engine.unregisterUpdateHandler(pTimerHandler);
	   	        		                    }
	   	        		                }));
	                            	}
	                            	
	                            	else
	                            	{
	                            		player.body.setLinearVelocity(0, 0);
	                            		levelCompleteWindow.display(StarsCount.THREE, GameScene.this, camera);
	   	                                this.setIgnoreUpdate(true);
	   	                                ResourcesManager.getInstance().setLevelComplete(3);
	   	                                
	   	                                // Clear Scenary 1 Graphics ...
	   	                                engine.registerUpdateHandler(new TimerHandler(3.0f, new ITimerCallback()
	   	        		                {                                    
	   	        		                    public void onTimePassed(final TimerHandler pTimerHandler)
	   	        		                    {
	   	        		                        pTimerHandler.reset();
	   	        		                        
	   	        		                        detachChild(levelCompleteWindow);
	   	    	                            	disposeScene(2);
	   	    	                            	
	   	    	                            	// Load Level
	   	    	                            	SceneManager.getInstance().loadGameScene(engine, 3);
	   	        		                        engine.unregisterUpdateHandler(pTimerHandler);
	   	        		                    }
	   	        		                }));
	                            	}
	                            	
	                            	firstTouch = false;
	                            }
	                        }
	                    };
	                }
	                
	                
	                else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_WATER_SYNTHETIC))
	                {
	                	levelObject = new Sprite(x, y, 160, 130, resourcesManager.waterSynthetic, vbom);
	                    final Body body = PhysicsFactory.createBoxBody(physicsWorld, levelObject, BodyType.StaticBody, FIXTURE_DEF);
	                    body.setUserData("waterSynthetic");
	                    physicsWorld.registerPhysicsConnector(new PhysicsConnector(levelObject, body, true, false));
	                    levelObject.setSize(width, height);
	                    levelObject.setAlpha(0.6f);
	                }
	                
	                else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_TREE))
	                {
	                	levelObject = new Sprite(x, y, 160, 130, resourcesManager.tree, vbom);
	                    final Body body = PhysicsFactory.createBoxBody(physicsWorld, levelObject, BodyType.StaticBody, FIXTURE_DEF);
	                    body.setUserData("tree");
	                    physicsWorld.registerPhysicsConnector(new PhysicsConnector(levelObject, body, true, false));
	                }
	                
	                else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_BUTTON_YELLOW))
	                {
	                    levelObject = new Sprite(x, y, resourcesManager.buttonYellow, vbom);
	                    final Body body = PhysicsFactory.createBoxBody(physicsWorld, levelObject, BodyType.StaticBody, FIXTURE_DEF);
	                    body.setUserData("buttonYellow");
	                    physicsWorld.registerPhysicsConnector(new PhysicsConnector(levelObject, body, true, false));
	                }
	                
	                else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_SAND))
	                {
	                	levelObject = new Sprite(x, y, resourcesManager.sand, vbom);
	                    final Body body = PhysicsFactory.createBoxBody(physicsWorld, levelObject, BodyType.StaticBody, FIXTURE_DEF);
	                    body.setUserData("sand");
	                    physicsWorld.registerPhysicsConnector(new PhysicsConnector(levelObject, body, true, false));
	                }
	                
	                else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_ROCK_HALF_BIG_PLATFORM))
	                {
	                	levelObject = new Sprite(x, y, resourcesManager.rockHalfBigPlatform, vbom);
	                    final Body body = PhysicsFactory.createBoxBody(physicsWorld, levelObject, BodyType.StaticBody, FIXTURE_DEF);
	                    body.setUserData("rockHalfBigPlatform");
	                    physicsWorld.registerPhysicsConnector(new PhysicsConnector(levelObject, body, true, false));
	                }
	                
	                else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_SWITCHER))
	                {
	                	switcher = new Switcher(x, y, vbom, camera,  physicsWorld)
		       			 {
		       				 @Override
		                        protected void onManagedUpdate(float pSecondsElapsed) 
		                        {
		                            super.onManagedUpdate(pSecondsElapsed);
		                            if (player.collidesWith(this))
		                            {
		                                player.switch1Touched = true;
		                            }  
		                        }
		       			 };
		       			 levelObject = switcher;
	                     levelObject.setVisible(false);
	                }
	                
	                else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_SWITCHER2))
	                {
	                	switcher2 = new Switcher(x, y, vbom, camera,  physicsWorld)
		       			 {
		       				 @Override
		                        protected void onManagedUpdate(float pSecondsElapsed) 
		                        {
		                            super.onManagedUpdate(pSecondsElapsed);
		                            if (player.collidesWith(this))
		                            {
		                                player.switch2Touched = true;
		                            }
		                           
		                        }
		       			 };
		       			 levelObject = switcher2;
	                     levelObject.setVisible(false);
	                }
	                
	                else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_SPRINGBOARDER))
	                {
	                    springboarder = new SpringBoarder(x, y, vbom, camera,  physicsWorld);
	                    levelObject = springboarder;
	                    //levelObject.setSize(width, height);
	                }
	                
	                else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_SPRINGBOARDER2))
	                {
	                    springboarder2 = new SpringBoarder(x, y, vbom, camera,  physicsWorld);
	                    levelObject = springboarder2;
	                    //levelObject.setSize(width, height);
	                }
	                       
	                
	                else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_ROCK))
	                {
	                    levelObject = new Sprite(x, y, resourcesManager.rock, vbom);
	                    final Body body = PhysicsFactory.createBoxBody(physicsWorld, levelObject, BodyType.StaticBody, FIXTURE_DEF);
	                    body.setUserData("rock");
	                    physicsWorld.registerPhysicsConnector(new PhysicsConnector(levelObject, body, true, false));
	                    //levelObject.setSize(width, height);
	                }
	                
	                else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_SUN))
	                {
	                    levelObject = new Sprite(x, y, resourcesManager.sun, vbom);
	                    final Body body = PhysicsFactory.createBoxBody(physicsWorld, levelObject, BodyType.StaticBody, FIXTURE_DEF);
	                    body.setUserData("sun");
	                    physicsWorld.registerPhysicsConnector(new PhysicsConnector(levelObject, body, true, false));
	                    levelObject.setSize(100, 100);
	                }
	                
	                else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_GREEN_PLATFORM))
	                {
	                    levelObject = new Sprite(x, y, resourcesManager.greenPlatform, vbom);
	                    final Body body = PhysicsFactory.createBoxBody(physicsWorld, levelObject, BodyType.StaticBody, FIXTURE_DEF);
	                    body.setUserData("greenPlatform");
	                    physicsWorld.registerPhysicsConnector(new PhysicsConnector(levelObject, body, true, false));
	                    //levelObject.setSize(width, height);
	                }
	                
	                else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_BLOCK))
	                {
	                    levelObject = new Sprite(x, y, resourcesManager.block, vbom);
	                    final Body body = PhysicsFactory.createBoxBody(physicsWorld, levelObject, BodyType.StaticBody, FIXTURE_DEF);
	                    body.setUserData("block");
	                    physicsWorld.registerPhysicsConnector(new PhysicsConnector(levelObject, body, true, false));
	                    //levelObject.setSize(width, height);
	                }
	                
	                else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_BOX_EXPLOSIVE_ALT))
	                {
	                    levelObject = new Sprite(x, y, resourcesManager.boxExplosiveAlt, vbom);
	                    final Body body = PhysicsFactory.createBoxBody(physicsWorld, levelObject, BodyType.StaticBody, FIXTURE_DEF);
	                    body.setUserData("boxExplosiveAlt");
	                    physicsWorld.registerPhysicsConnector(new PhysicsConnector(levelObject, body, true, false));
	                    //levelObject.setSize(width, height);
	                }
	                
	                else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_BOX_ITEM_ALT))
	                {
	                    levelObject = new Sprite(x, y, resourcesManager.boxItemAlt, vbom);
	                    final Body body = PhysicsFactory.createBoxBody(physicsWorld, levelObject, BodyType.StaticBody, FIXTURE_DEF);
	                    body.setUserData("boxItemAlt");
	                    physicsWorld.registerPhysicsConnector(new PhysicsConnector(levelObject, body, true, false));
	                    //levelObject.setSize(width, height);
	                }
	                
	                else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_BOX_WARNING))
	                {
	                    levelObject = new Sprite(x, y, resourcesManager.boxWarning, vbom);
	                    final Body body = PhysicsFactory.createBoxBody(physicsWorld, levelObject, BodyType.StaticBody, FIXTURE_DEF);
	                    body.setUserData("boxWarning");
	                    physicsWorld.registerPhysicsConnector(new PhysicsConnector(levelObject, body, true, false));
	                    //levelObject.setSize(width, height);
	                }
	                
	                else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_CACTUS))
	                {
	                    levelObject = new Sprite(x, y, resourcesManager.cactus, vbom);
	                    final Body body = PhysicsFactory.createBoxBody(physicsWorld, levelObject, BodyType.StaticBody, FIXTURE_DEF);
	                    body.setUserData("cactus");
	                    physicsWorld.registerPhysicsConnector(new PhysicsConnector(levelObject, body, true, false));
	                    //levelObject.setSize(width, height);
	                }

	                else
	                {
	                    throw new IllegalArgumentException();
	                }

	                levelObject.setCullingEnabled(true);

	                return levelObject;
	            }
	        });
        	
    		levelLoader.loadLevelFromAsset(activity.getAssets(), "level/" + levelID + ".lvl");
        	
        }
        
        // LEVEL 3 -----> MOUNTAIN 
        else if (levelID == 3)
        {
        	final SimpleLevelLoader levelLoader = new SimpleLevelLoader(vbom);
            final FixtureDef FIXTURE_DEF = PhysicsFactory.createFixtureDef(0, 0.01f, 0.5f);
            levelLoader.registerEntityLoader(new EntityLoader<SimpleLevelEntityLoaderData>(LevelConstants.TAG_LEVEL)
    	    {
    	        public IEntity onLoadEntity(final String pEntityName, final IEntity pParent, final Attributes pAttributes, final SimpleLevelEntityLoaderData pSimpleLevelEntityLoaderData) throws IOException 
    	        {
    	            final int width = SAXUtils.getIntAttributeOrThrow(pAttributes, LevelConstants.TAG_LEVEL_ATTRIBUTE_WIDTH);
    	            final int height = SAXUtils.getIntAttributeOrThrow(pAttributes, LevelConstants.TAG_LEVEL_ATTRIBUTE_HEIGHT);
    		        camera.setBounds(0, 0, width, height); // here we set camera bounds
    		        camera.setBoundsEnabled(true);
    		
    		        return GameScene.this;
    	        }
            });
            
        	levelLoader.registerEntityLoader(new EntityLoader<SimpleLevelEntityLoaderData>(TAG_ENTITY)
		    {
		        public IEntity onLoadEntity(final String pEntityName, final IEntity pParent, final Attributes pAttributes, final SimpleLevelEntityLoaderData pSimpleLevelEntityLoaderData) throws IOException
		        {
		            final int x = SAXUtils.getIntAttributeOrThrow(pAttributes, TAG_ENTITY_ATTRIBUTE_X);
		            final int y = SAXUtils.getIntAttributeOrThrow(pAttributes, TAG_ENTITY_ATTRIBUTE_Y);
		            final int width = SAXUtils.getIntAttributeOrThrow(pAttributes, TAG_ENTITY_ATTRIBUTE_WIDTH);
		            final int height = SAXUtils.getIntAttributeOrThrow(pAttributes, TAG_ENTITY_ATTRIBUTE_HEIGHT);
		            final String type = SAXUtils.getAttributeOrThrow(pAttributes, TAG_ENTITY_ATTRIBUTE_TYPE);
		            
		            final Sprite levelObject;


	                // --------------- PLATFORMS AND PLAYER ------------------------------------------------------------------------------------
		            if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLAYER))
	                {
	                    player = new Player(x, y, vbom, camera,  physicsWorld)
	                    {
	                        @Override
	                        public void onDie()
	                        {
	                            if (!gameOverDisplayed)
	                    	    {
	                    	        displayGameOverText(3);
	                    	    }
	                        }
	                    };
	                    levelObject = player;
	                    levelObject.setSize(width, height);
	                }
		            
		            else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLAYER_ONLINE))
	                {
	                    playerOnline = new PlayerOnline(x, y, vbom, camera,  physicsWorld)
	                    {
	                        @Override
	                        public void onDie()
	                        {
	                        	// Nothing to do here, GAME OVER is only displyed in the server not inthe client .
	                        }
	                    };
	                    levelObject = playerOnline;
	                    levelObject.setVisible(false);
	                    levelObject.setSize(width, height);
	                }
		            
		            else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_RINO))
	                {
	                    rEnemy = new RinoEnemy(x, y, vbom, camera,  physicsWorld)
	                    {
	                        @Override
	                        public void onDie()
	                        {
	                        }
	                    };
	                    levelObject = rEnemy;
	                    levelObject.setSize(width, height);
	                }
		            
		            else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_CRADLE))
	                {
	                    levelObject = new Sprite(x, y, resourcesManager.cradle, vbom);
	                    final Body body = PhysicsFactory.createBoxBody(physicsWorld, levelObject, BodyType.StaticBody, FIXTURE_DEF);
	                    body.setUserData("cradle");
	                    physicsWorld.registerPhysicsConnector(new PhysicsConnector(levelObject, body, true, false));
	                    //levelObject.setSize(width, height);
	                }
		            
		            else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_LITTLE_MOUNTAIN))
	                {
	                    levelObject = new Sprite(x, y, resourcesManager.littleMountain, vbom);
	                    final Body body = PhysicsFactory.createBoxBody(physicsWorld, levelObject, BodyType.StaticBody, FIXTURE_DEF);
	                    body.setUserData("littleMountain");
	                    physicsWorld.registerPhysicsConnector(new PhysicsConnector(levelObject, body, true, false));
	                    //levelObject.setSize(width, height);
	                }
		            
		            else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_MOUNTAIN_PLATFORM))
	                {
	                    levelObject = new Sprite(x, y, resourcesManager.mountainPlatform, vbom);
	                    final Body body = PhysicsFactory.createBoxBody(physicsWorld, levelObject, BodyType.StaticBody, FIXTURE_DEF);
	                    body.setUserData("mountainPlatform");
	                    physicsWorld.registerPhysicsConnector(new PhysicsConnector(levelObject, body, true, false));
	                    //levelObject.setSize(width, height);
	                }
		            
		            else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLATFORM))
	                {
	                    levelObject = new Sprite(x, y, resourcesManager.platform, vbom);
	                    final Body body = PhysicsFactory.createBoxBody(physicsWorld, levelObject, BodyType.StaticBody, FIXTURE_DEF);
	                    body.setUserData("platform");
	                    physicsWorld.registerPhysicsConnector(new PhysicsConnector(levelObject, body, true, false));
	                    //levelObject.setSize(width, height);
	                }
		            
		            else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLATFORM_SMALL))
	                {
	                    levelObject = new Sprite(x, y, resourcesManager.platformSmall, vbom);
	                    final Body body = PhysicsFactory.createBoxBody(physicsWorld, levelObject, BodyType.StaticBody, FIXTURE_DEF);
	                    body.setUserData("platformSmall");
	                    physicsWorld.registerPhysicsConnector(new PhysicsConnector(levelObject, body, true, false));
	                    //levelObject.setSize(width, height);
	                }
		            
		            else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_BIG_TREE))
	                {
	                    levelObject = new Sprite(x, y, resourcesManager.bigTree, vbom);
	                    final Body body = PhysicsFactory.createBoxBody(physicsWorld, levelObject, BodyType.StaticBody, FIXTURE_DEF);
	                    body.setUserData("bigTree");
	                    physicsWorld.registerPhysicsConnector(new PhysicsConnector(levelObject, body, true, false));
	                    //levelObject.setSize(width, height);
	                }
		            
		            else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_TRONC_PLATFORM))
	                {
	                    levelObject = new Sprite(x, y, resourcesManager.troncPlatform, vbom);
	                    final Body body = PhysicsFactory.createBoxBody(physicsWorld, levelObject, BodyType.StaticBody, FIXTURE_DEF);
	                    body.setUserData("troncPlatform");
	                    physicsWorld.registerPhysicsConnector(new PhysicsConnector(levelObject, body, true, false));
	                    //levelObject.setSize(width, height);
	                }
		            
		            else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_GRASS))
	                {
	                    levelObject = new Sprite(x, y, resourcesManager.grass, vbom);
	                    final Body body = PhysicsFactory.createBoxBody(physicsWorld, levelObject, BodyType.StaticBody, FIXTURE_DEF);
	                    body.setUserData("grass");
	                    physicsWorld.registerPhysicsConnector(new PhysicsConnector(levelObject, body, true, false));
	                    //levelObject.setSize(width, height);
	                }
		            
		            else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_GRASS_MAIN))
	                {
	                    levelObject = new Sprite(x, y, resourcesManager.grass_main, vbom);
	                    final Body body = PhysicsFactory.createBoxBody(physicsWorld, levelObject, BodyType.StaticBody, FIXTURE_DEF);
	                    body.setUserData("grassMain");
	                    physicsWorld.registerPhysicsConnector(new PhysicsConnector(levelObject, body, true, false));
	                    //levelObject.setSize(width, height);
	                }
		            
		            else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_TILE_GRASS))
	                {
	                    levelObject = new Sprite(x, y, resourcesManager.tile_grass, vbom);
	                    final Body body = PhysicsFactory.createBoxBody(physicsWorld, levelObject, BodyType.StaticBody, FIXTURE_DEF);
	                    body.setUserData("tileGrass");
	                    physicsWorld.registerPhysicsConnector(new PhysicsConnector(levelObject, body, true, false));
	                    //levelObject.setSize(width, height);
	                }
		            
		            else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_TRONC_SMALL))
	                {
	                    levelObject = new Sprite(x, y, resourcesManager.troncSmall, vbom);
	                    final Body body = PhysicsFactory.createBoxBody(physicsWorld, levelObject, BodyType.StaticBody, FIXTURE_DEF);
	                    body.setUserData("troncSmall");
	                    physicsWorld.registerPhysicsConnector(new PhysicsConnector(levelObject, body, true, false));
	                    //levelObject.setSize(width, height);
	                }
		            
		            else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_BUSH))
	                {
	                    levelObject = new Sprite(x, y, resourcesManager.bush, vbom);
	                    final Body body = PhysicsFactory.createBoxBody(physicsWorld, levelObject, BodyType.StaticBody, FIXTURE_DEF);
	                    body.setUserData("bush");
	                    physicsWorld.registerPhysicsConnector(new PhysicsConnector(levelObject, body, true, false));
	                    //levelObject.setSize(width, height);
	                }
		            
		            else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_KEY_BLUE))
	                {
	                    levelObject = new Sprite(x, y, resourcesManager.key_blue, vbom)
	                    {
	                        @Override
	                        protected void onManagedUpdate(float pSecondsElapsed) 
	                        {
	                            super.onManagedUpdate(pSecondsElapsed);
	                            if (player.collidesWith(this))
	                            {
	                            	player.hasKey = true;
	                            	player.hasKey = true;
	                            	final Sprite keyBlue = new Sprite(30, 340, resourcesManager.keyHUD, vbom);
	                     	    	gameHUD.attachChild(keyBlue);
	                                this.setVisible(false);
	                                this.setIgnoreUpdate(true);
	                            }
	                           
	                        }
	                    };
	                    levelObject.registerEntityModifier(new LoopEntityModifier(new ScaleModifier(1, 1, 1.3f)));
	                } 
		            
		            else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_KEY_RED))
	                {
	                    levelObject = new Sprite(x, y, resourcesManager.key_red, vbom)
	                    {
	                        @Override
	                        protected void onManagedUpdate(float pSecondsElapsed) 
	                        {
	                            super.onManagedUpdate(pSecondsElapsed);
	                            if (player.collidesWith(this))
	                            {
	                            	player.hasGreenKey = true;
	                            	player.hasGreenKey = true;
	                            	final Sprite keyRed = new Sprite(65, 340, resourcesManager.keyGreenHUD, vbom);
	                     	    	gameHUD.attachChild(keyRed);
	                                this.setVisible(false);
	                                this.setIgnoreUpdate(true);
	                            }
	                           
	                        }
	                    };
	                    levelObject.registerEntityModifier(new LoopEntityModifier(new ScaleModifier(1, 1, 1.3f)));
	                } 
		            
		            else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_SWITCH_GREEN_OFF))
	                {
	                    levelObject = new Sprite(x, y, resourcesManager.switch_green_off, vbom);
	                    final Body body = PhysicsFactory.createBoxBody(physicsWorld, levelObject, BodyType.StaticBody, FIXTURE_DEF);
	                    body.setUserData("switchGreenOff");
	                    physicsWorld.registerPhysicsConnector(new PhysicsConnector(levelObject, body, true, false));
	                    //levelObject.setSize(width, height);
	                }
		            
		            else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_COIN))
	                {
	                    levelObject = new Sprite(x, y, resourcesManager.coin_silver, vbom)
	                    {
	                        @Override
	                        protected void onManagedUpdate(float pSecondsElapsed) 
	                        {
	                            super.onManagedUpdate(pSecondsElapsed);
	                            if (player.collidesWith(this))
	                            {
	                                addToScore(3);
	                                ResourcesManager.getInstance().getCoinSound().play();
	                                this.setVisible(false);
	                                this.setIgnoreUpdate(true);
	                            }
	                           
	                        }
	                    };
	                    levelObject.registerEntityModifier(new LoopEntityModifier(new ScaleModifier(1, 1, 1.3f)));
	                    //levelObject.setSize(width, height);
	                } 
	                
	                else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_COIN_GOLD))
	                {
	                    levelObject = new Sprite(x, y, resourcesManager.coin_gold, vbom)
	                    {
	                        @Override
	                        protected void onManagedUpdate(float pSecondsElapsed) 
	                        {
	                            super.onManagedUpdate(pSecondsElapsed);
	                            if (player.collidesWith(this))
	                            {
	                                addToScore(5);
	                                ResourcesManager.getInstance().getCoinSound().play();
	                                this.setVisible(false);
	                                this.setIgnoreUpdate(true);
	                            }
	                           
	                        }
	                    };
	                    levelObject.registerEntityModifier(new LoopEntityModifier(new ScaleModifier(1, 1, 1.3f)));
	                    //levelObject.setSize(width, height);
	                } 
		            
	                else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_WATER_SYNTHETIC))
	                {
	                	levelObject = new Sprite(x, y, 160, 130, resourcesManager.waterSynthetic, vbom);
	                    final Body body = PhysicsFactory.createBoxBody(physicsWorld, levelObject, BodyType.StaticBody, FIXTURE_DEF);
	                    body.setUserData("waterSynthetic");
	                    physicsWorld.registerPhysicsConnector(new PhysicsConnector(levelObject, body, true, false));
	                }
	                
	                else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_COIN_BRONZE))
	                {
	                    levelObject = new Sprite(x, y, resourcesManager.coin_bronze, vbom)
	                    {
	                        @Override
	                        protected void onManagedUpdate(float pSecondsElapsed) 
	                        {
	                            super.onManagedUpdate(pSecondsElapsed);
	                            if (player.collidesWith(this))
	                            {
	                                addToScore(1);
	                                ResourcesManager.getInstance().getCoinSound().play();
	                                this.setVisible(false);
	                                this.setIgnoreUpdate(true);
	                            }
	                           
	                        }
	                    };
	                    levelObject.registerEntityModifier(new LoopEntityModifier(new ScaleModifier(1, 1, 1.3f)));
	                    //levelObject.setSize(width, height);
	                }
		            
	                else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_LEVEL_COMPLETE))
	                {
	                    levelObject = new Sprite(x, y, resourcesManager.cradle, vbom)
	                    {
	                        @Override
	                        protected void onManagedUpdate(float pSecondsElapsed) 
	                        {
	                            super.onManagedUpdate(pSecondsElapsed);
	                            if (player.hasKey && player.hasGreenKey) 
	                            {
	                            	this.setVisible(true);
	                            }
	                            else
	                            {
	                            	this.setVisible(false);
	                            }
	                            
	                            if (player.collidesWith(this) && player.hasKey && player.hasGreenKey)
	                            {
	                            	
	                            	if (score <= 35 && score >= 30)
	                            	{
	                            		ResourcesManager.getInstance().activity.setAccelerometerActivated(false);
	                            		player.body.setLinearVelocity(0, 0);
	                            		levelCompleteWindow.display(StarsCount.ONE, GameScene.this, camera);
	   	                                this.setIgnoreUpdate(true);
	   	                                ResourcesManager.getInstance().setLevelComplete(4);
	   	                                
	   	                                // Clear Scenary 1 Graphics ...
	   	                                engine.registerUpdateHandler(new TimerHandler(3.0f, new ITimerCallback()
	   	        		                {                                    
	   	        		                    @SuppressWarnings("deprecation")
											public void onTimePassed(final TimerHandler pTimerHandler)
	   	        		                    {
	   	        		                        pTimerHandler.reset();
	   	        		                        
	   	        		                        detachChild(levelCompleteWindow);
	   	    	                            	disposeScene(3);
		   	    	                 			
	   	    	                            	SceneManager.getInstance().loadGameScene(engine, 4);
	   	        		                        engine.unregisterUpdateHandler(pTimerHandler);
	   	        		                    }
	   	        		                }));
	                            	}
	                            	
	                            	else if (score > 35 && score < 45)
	                            	{
	                            		ResourcesManager.getInstance().activity.setAccelerometerActivated(false);
	                            		player.body.setLinearVelocity(0, 0);
	                            		levelCompleteWindow.display(StarsCount.TWO, GameScene.this, camera);
	   	                                this.setIgnoreUpdate(true);
	   	                                ResourcesManager.getInstance().setLevelComplete(4);
	   	                                
	   	                                // Clear Scenary 1 Graphics ...
	   	                                engine.registerUpdateHandler(new TimerHandler(3.0f, new ITimerCallback()
	   	        		                {                                    
	   	        		                    @SuppressWarnings("deprecation")
											public void onTimePassed(final TimerHandler pTimerHandler)
	   	        		                    {
	   	        		                        pTimerHandler.reset();
	   	        		                        
	   	        		                        detachChild(levelCompleteWindow);
	   	    	                            	disposeScene(3);
		   	    	                 			
	   	    	                            	SceneManager.getInstance().loadGameScene(engine, 4);
	   	        		                        engine.unregisterUpdateHandler(pTimerHandler);
	   	        		                    }
	   	        		                }));
	                            	}
	                            	
	                            	else
	                            	{
	                            		ResourcesManager.getInstance().activity.setAccelerometerActivated(false);
	                            		player.body.setLinearVelocity(0, 0);
	                            		levelCompleteWindow.display(StarsCount.THREE, GameScene.this, camera);
	   	                                this.setIgnoreUpdate(true);
	   	                                ResourcesManager.getInstance().setLevelComplete(4);
	   	                                
	   	                                // Clear Scenary 1 Graphics ...
	   	                                engine.registerUpdateHandler(new TimerHandler(3.0f, new ITimerCallback()
	   	        		                {                                    
	   	        		                    @SuppressWarnings("deprecation")
											public void onTimePassed(final TimerHandler pTimerHandler)
	   	        		                    {
	   	        		                        pTimerHandler.reset();
	   	        		                        
	   	        		                        detachChild(levelCompleteWindow);
	   	    	                            	disposeScene(3);
		   	    	                 			
	   	    	                            	SceneManager.getInstance().loadGameScene(engine, 4);
	   	        		                        engine.unregisterUpdateHandler(pTimerHandler);
	   	        		                    }
	   	        		                }));
	                            	}
	                            	
	                            	firstTouch = false;
	                            }
	                        }
	                    };
	                }
	                
		            
		            else
	                {
	                    throw new IllegalArgumentException();
	                }

	                levelObject.setCullingEnabled(true);

	                return levelObject;
	            }
	        });
        	
    		levelLoader.loadLevelFromAsset(activity.getAssets(), "level/" + levelID + ".lvl");
        }
        
        // ------------------------------------ LEVEL 4 ------------------------------------------------------------------------------------------------------
        else if (levelID == 4)
        {
        	final SimpleLevelLoader levelLoader = new SimpleLevelLoader(vbom);
            final FixtureDef FIXTURE_DEF = PhysicsFactory.createFixtureDef(0, 0.01f, 0.5f);
            levelLoader.registerEntityLoader(new EntityLoader<SimpleLevelEntityLoaderData>(LevelConstants.TAG_LEVEL)
    	    {
    	        public IEntity onLoadEntity(final String pEntityName, final IEntity pParent, final Attributes pAttributes, final SimpleLevelEntityLoaderData pSimpleLevelEntityLoaderData) throws IOException 
    	        {
    	            final int width = SAXUtils.getIntAttributeOrThrow(pAttributes, LevelConstants.TAG_LEVEL_ATTRIBUTE_WIDTH);
    	            final int height = SAXUtils.getIntAttributeOrThrow(pAttributes, LevelConstants.TAG_LEVEL_ATTRIBUTE_HEIGHT);
    		        camera.setBounds(0, 0, width, height); // here we set camera bounds
    		        camera.setBoundsEnabled(true);
    		
    		        return GameScene.this;
    	        }
            });
            
        	levelLoader.registerEntityLoader(new EntityLoader<SimpleLevelEntityLoaderData>(TAG_ENTITY)
		    {
		        public IEntity onLoadEntity(final String pEntityName, final IEntity pParent, final Attributes pAttributes, final SimpleLevelEntityLoaderData pSimpleLevelEntityLoaderData) throws IOException
		        {
		            final int x = SAXUtils.getIntAttributeOrThrow(pAttributes, TAG_ENTITY_ATTRIBUTE_X);
		            final int y = SAXUtils.getIntAttributeOrThrow(pAttributes, TAG_ENTITY_ATTRIBUTE_Y);
		            final int width = SAXUtils.getIntAttributeOrThrow(pAttributes, TAG_ENTITY_ATTRIBUTE_WIDTH);
		            final int height = SAXUtils.getIntAttributeOrThrow(pAttributes, TAG_ENTITY_ATTRIBUTE_HEIGHT);
		            final String type = SAXUtils.getAttributeOrThrow(pAttributes, TAG_ENTITY_ATTRIBUTE_TYPE);
		            
		            final Sprite levelObject;
		            
		            if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLAYER))
	                {
	                    player = new Player(x, y, vbom, camera,  physicsWorld)
	                    {
	                        @Override
	                        public void onDie()
	                        {
	                            if (!gameOverDisplayed)
	                    	    {
	                    	        displayGameOverText(4);
	                    	    }
	                        }
	                    };
	                    levelObject = player;
	                    levelObject.setSize(width, height);
	                }
		            
		            else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_CRADLE1))
	                {
	                    levelObject = new Sprite(x, y, resourcesManager.cradlePlatform1, vbom);
	                    final Body body = PhysicsFactory.createBoxBody(physicsWorld, levelObject, BodyType.StaticBody, FIXTURE_DEF);
	                    body.setUserData("cradle1");
	                    physicsWorld.registerPhysicsConnector(new PhysicsConnector(levelObject, body, true, false));
	                }
		            
		            else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_CRADLE2))
		            {
		            	levelObject = new Sprite(x, y, resourcesManager.cradlePlatform2, vbom);
	                    final Body body = PhysicsFactory.createBoxBody(physicsWorld, levelObject, BodyType.StaticBody, FIXTURE_DEF);
	                    body.setUserData("cradle2");
	                    physicsWorld.registerPhysicsConnector(new PhysicsConnector(levelObject, body, true, false));
		            }
		            
		            else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_CRADLE3))
		            {
		            	levelObject = new Sprite(x, y, resourcesManager.cradlePlatform3, vbom);
	                    final Body body = PhysicsFactory.createBoxBody(physicsWorld, levelObject, BodyType.StaticBody, FIXTURE_DEF);
	                    body.setUserData("cradle3");
	                    physicsWorld.registerPhysicsConnector(new PhysicsConnector(levelObject, body, true, false));
		            }
		            
		            else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_CRADLE4))
		            {
		            	levelObject = new Sprite(x, y, resourcesManager.cradlePlatform4, vbom);
	                    final Body body = PhysicsFactory.createBoxBody(physicsWorld, levelObject, BodyType.StaticBody, FIXTURE_DEF);
	                    body.setUserData("cradle4");
	                    physicsWorld.registerPhysicsConnector(new PhysicsConnector(levelObject, body, true, false));
		            }
		            
		            else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_CRADLE5))
		            {
		            	levelObject = new Sprite(x, y, resourcesManager.cradlePlatform5, vbom);
	                    final Body body = PhysicsFactory.createBoxBody(physicsWorld, levelObject, BodyType.StaticBody, FIXTURE_DEF);
	                    body.setUserData("cradle5");
	                    physicsWorld.registerPhysicsConnector(new PhysicsConnector(levelObject, body, true, false));
		            }
		            
		            else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_EXPULSOR))
		            {
		            	levelObject = new Sprite(x, y, resourcesManager.expulsor, vbom);
	                    final Body body = PhysicsFactory.createBoxBody(physicsWorld, levelObject, BodyType.StaticBody, FIXTURE_DEF);
	                    body.setUserData("expulsor");
	                    physicsWorld.registerPhysicsConnector(new PhysicsConnector(levelObject, body, true, false));
		            }
		            
		            else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_MINERAL))
		            {
		            	levelObject = new Sprite(x, y, resourcesManager.mineral, vbom);
	                    final Body body = PhysicsFactory.createBoxBody(physicsWorld, levelObject, BodyType.StaticBody, FIXTURE_DEF);
	                    body.setUserData("mineral");
	                    physicsWorld.registerPhysicsConnector(new PhysicsConnector(levelObject, body, true, false));
		            }
		            
		            else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_ENEMY1))
		            {
		            	zombie = new ZombiEnemy(x, y, vbom, camera,  physicsWorld)
		            	{            		
		            	};
		            	levelObject = zombie;
		            }
		            
		            else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_ENEMY2))
		            {
		            	nomo = new NomoEnemy(x, y, vbom, camera,  physicsWorld)
		            	{
		            	};
		            	levelObject = nomo;
		            }
		            
		            else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_MUSHROOM_TREE))
		            {
		            	levelObject = new Sprite(x, y, resourcesManager.mushroomTree, vbom);
	                    final Body body = PhysicsFactory.createBoxBody(physicsWorld, levelObject, BodyType.StaticBody, FIXTURE_DEF);
	                    body.setUserData("mushroomTree");
	                    physicsWorld.registerPhysicsConnector(new PhysicsConnector(levelObject, body, true, false));
		            }
		            
		            else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_BOMB))
	                {
		            	bomb = new Sprite(x, y, resourcesManager.bomb, vbom)
	                    {
	                        @Override
	                        protected void onManagedUpdate(float pSecondsElapsed) 
	                        {
	                            super.onManagedUpdate(pSecondsElapsed);
	                            if (player.collidesWith(this))
	                            {
	                            	player.hasBombs++;
	                            	gameHUD.detachChild(nBombs);         	
	                            	nBombs = new Text(40, 300, resourcesManager.font, "+" + player.hasBombs, new TextOptions(HorizontalAlign.LEFT), vbom);
	                            	gameHUD.attachChild(nBombs);
	                                this.setVisible(false);
	                                this.setIgnoreUpdate(true);
	                            }
	                           
	                        }
	                    };
	                    
	                    levelObject = bomb;
	                    levelObject.registerEntityModifier(new LoopEntityModifier(new ScaleModifier(1, 1, 1.3f)));
	                }

		            
		            else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_COIN))
	                {
	                    levelObject = new Sprite(x, y, resourcesManager.coin_silver, vbom)
	                    {
	                        @Override
	                        protected void onManagedUpdate(float pSecondsElapsed) 
	                        {
	                            super.onManagedUpdate(pSecondsElapsed);
	                            if (player.collidesWith(this))
	                            {
	                                addToScore(3);
	                                ResourcesManager.getInstance().getCoinSound().play();
	                                this.setVisible(false);
	                                this.setIgnoreUpdate(true);
	                            }
	                           
	                        }
	                    };
	                    levelObject.registerEntityModifier(new LoopEntityModifier(new ScaleModifier(1, 1, 1.3f)));
	                    //levelObject.setSize(width, height);
	                } 
	                
	                else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_COIN_GOLD))
	                {
	                    levelObject = new Sprite(x, y, resourcesManager.coin_gold, vbom)
	                    {
	                        @Override
	                        protected void onManagedUpdate(float pSecondsElapsed) 
	                        {
	                            super.onManagedUpdate(pSecondsElapsed);
	                            if (player.collidesWith(this))
	                            {
	                                addToScore(5);
	                                ResourcesManager.getInstance().getCoinSound().play();
	                                this.setVisible(false);
	                                this.setIgnoreUpdate(true);
	                            }
	                           
	                        }
	                    };
	                    levelObject.registerEntityModifier(new LoopEntityModifier(new ScaleModifier(1, 1, 1.3f)));
	                    //levelObject.setSize(width, height);
	                } 
		            
	                else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_COIN_BRONZE))
	                {
	                    levelObject = new Sprite(x, y, resourcesManager.coin_bronze, vbom)
	                    {
	                        @Override
	                        protected void onManagedUpdate(float pSecondsElapsed) 
	                        {
	                            super.onManagedUpdate(pSecondsElapsed);
	                            if (player.collidesWith(this))
	                            {
	                                addToScore(1);
	                                ResourcesManager.getInstance().getCoinSound().play();
	                                this.setVisible(false);
	                                this.setIgnoreUpdate(true);
	                            }
	                           
	                        }
	                    };
	                    levelObject.registerEntityModifier(new LoopEntityModifier(new ScaleModifier(1, 1, 1.3f)));
	                    //levelObject.setSize(width, height);
	                }
		            
	                else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_KEY_BLUE))
	                {
	                    levelObject = new Sprite(x, y, resourcesManager.key_blue, vbom)
	                    {
	                        @Override
	                        protected void onManagedUpdate(float pSecondsElapsed) 
	                        {
	                            super.onManagedUpdate(pSecondsElapsed);
	                            if (player.collidesWith(this))
	                            {
	                            	player.hasKey = true;
	                            	player.hasKey = true;
	                            	final Sprite keyBlue = new Sprite(30, 340, resourcesManager.keyHUD, vbom);
	                     	    	gameHUD.attachChild(keyBlue);
	                     	    	
	                     	    	if (resourcesManager.getLevelComplete() == 4)
	                     	    	{
	                     	    		gameHUD.attachChild(warningText);
	                     	    		
	                     	    		// DO flickering while period of time after touching the spikes 
	                		       		flicker = new playTimer(0.25f , new playTimer.ITimerCallback()
	                		       	    {
	                		            	boolean visible = false;
	                		       	        @Override
	                		       	        public void onTick()
	                		       	        {
	                		       	            warningText.setVisible(visible);
	                		       	            visible = !visible;
	                		       	        }
	                		       	      }
	                		       	    );
	                		       		engine.registerUpdateHandler(flicker);
	                		       		
		                     	    	engine.registerUpdateHandler(new TimerHandler(3.0f, new ITimerCallback()
		        		                {                                    
		        		                    public void onTimePassed(final TimerHandler pTimerHandler)
		        		                    {
		        		                        pTimerHandler.reset();
		        		                        flicker.reset();
		        		                        flicker.pause();
		        		                        engine.unregisterUpdateHandler(pTimerHandler);   
		        		                        gameHUD.detachChild(warningText);
		        		                    }
		        		                }));	
	                     	    	}
	                     	    	
	                                this.setVisible(false);
	                                switcher.setVisible(true);
	                                this.setIgnoreUpdate(true);
	                            }
	                           
	                        }
	                    };
	                    levelObject.registerEntityModifier(new LoopEntityModifier(new ScaleModifier(1, 1, 1.3f)));
	                } 
		            
	                else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_SWITCHER))
	                {
	                	switcher = new Switcher(x, y, vbom, camera,  physicsWorld)
		       			 {
		       				 @Override
		                        protected void onManagedUpdate(float pSecondsElapsed) 
		                        {
		                            super.onManagedUpdate(pSecondsElapsed);
		                            if (player.collidesWith(this))
		                            {
		                                player.switch1Touched = true;
		                            }
		                           
		                        }
		       			 };
		       			 levelObject = switcher;
	                     levelObject.setVisible(false);
	                }
		            
		            else
	                {
	                    throw new IllegalArgumentException();
	                }
		            
		            levelObject.setCullingEnabled(true);

	                return levelObject;
	            }
	        });
        	
    		levelLoader.loadLevelFromAsset(activity.getAssets(), "level/" + levelID + ".lvl");
        }
        
    }
    
	@SuppressWarnings("deprecation")
	@Override
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent)
	{
		if (firstTouch == false)
		{
			firstTouch = true;
			
			// ------------------------ SERVER TO CLIENT ------------------------------------------------------------------------------------------------------------------------
			
			// Send Player to be Loaded from SERVER to CLIENT
			if (ResourcesManager.getInstance().activity.mSocketServer != null)
			{
				final PlayerServer playerServer = (PlayerServer) ResourcesManager.getInstance().activity.mMessagePool.obtainMessage(streetJumper.FLAG_MESSAGE_SERVER_PLAYER);
				playerServer.set(resourcesManager.playerSelected, player.getX(), player.getY());
				ResourcesManager.getInstance().activity.mSocketServer.sendBroadcastServerMessage(playerServer);
				ResourcesManager.getInstance().activity.mMessagePool.recycleMessage(playerServer);
			}
			
			// Show SERVER player in CLIENT machine
			if (ResourcesManager.getInstance().activity.mSocketServer != null)
			{
				final PlayerSelectedServerMessage playerSelectedServerMessage = (PlayerSelectedServerMessage) ResourcesManager.getInstance().activity.mMessagePool.obtainMessage(streetJumper.FLAG_MESSAGE_SERVER_PLAYER_SELECTED);
		        playerSelectedServerMessage.set(ResourcesManager.getInstance().activity.mPlayerIDCounter++, player.getX(), player.getY());
				ResourcesManager.getInstance().activity.mSocketServer.sendBroadcastServerMessage(playerSelectedServerMessage);
				ResourcesManager.getInstance().activity.mMessagePool.recycleMessage(playerSelectedServerMessage);
			}
			
			// ------------------------ CLIENT TO SERVER ------------------------------------------------------------------------------------------------------------------------

			// Send Player to be Loaded from CLIENT to SERVER
			if (ResourcesManager.getInstance().activity.mSocketServer == null)
			{
				final PlayerClient playerClient = (PlayerClient) ResourcesManager.getInstance().activity.mMessagePool.obtainMessage(streetJumper.FLAG_MESSAGE_CLIENT_PLAYER);
				playerClient.set(resourcesManager.playerSelected, player.getX(), player.getY());
				ResourcesManager.getInstance().activity.mServerConnector.sendClientMessage(playerClient);
				ResourcesManager.getInstance().activity.mMessagePool.recycleMessage(playerClient);
			}
			
			// Show CLIENT player in SERVER machine
			if (ResourcesManager.getInstance().activity.mSocketServer == null)
			{
				final PlayerSelectedClientServerMessage playerSelectedClientServerMessage = (PlayerSelectedClientServerMessage) ResourcesManager.getInstance().activity.mMessagePool.obtainMessage(streetJumper.FLAG_MESSAGE_CLIENT_PLAYER_SELECTED_CLIENT);
				playerSelectedClientServerMessage.set(ResourcesManager.getInstance().activity.mPlayerIDCounter++, player.getX(), player.getY());
				ResourcesManager.getInstance().activity.mServerConnector.sendClientMessage(playerSelectedClientServerMessage);
				ResourcesManager.getInstance().activity.mMessagePool.recycleMessage(playerSelectedClientServerMessage);
			}
		}
		if (pSceneTouchEvent.isActionDown())
	    {
			this.isJumping = true;
			ResourcesManager.getInstance().getJumpSound().play();
			player.jump();        
	    }
		
		return true;
	}
	

	private void displayGameOverText(final int levelID)
	{
		// Reset camera player
	    camera.setChaseEntity(null);
		
	    // Set background to Black color (usually) and remove player HUD
		 camera.setHUD(null);
		 
		 // Clear some features not more needed
		 gameHUD.clearChildScene();
		 gameHUD.clearEntityModifiers();
		 gameHUD.clearTouchAreas();
		 gameHUD.clearUpdateHandlers();
		 disposeScene(levelID);
		 
		 this.setBackground(new Background(Color.RED));
		 
		 // Write the message
		 ResourcesManager.getInstance().loadGameFonts();
		 attachChild(new Text(camera.getCenterX(), camera.getCenterY(), resourcesManager.loadingFont, "GAME OVER... ! ", vbom));
		 
		 engine.registerUpdateHandler(new TimerHandler(2.0f, new ITimerCallback()
         {                                    
             public void onTimePassed(final TimerHandler pTimerHandler)
             {
                 pTimerHandler.reset();
                 SceneManager.getInstance().loadGameScene(engine, levelID);
                 engine.unregisterUpdateHandler(pTimerHandler);
             }
         }));
	}
	
	private ContactListener contactListener()
	{
	    ContactListener contactListener = new ContactListener()
	    {
	    	
	    	/**
	    	 * BEGIN COLISION BETWEEN PLAYER AND ANY OF SCENE OBJECT
	    	 * @see com.badlogic.gdx.physics.box2d.ContactListener#beginContact(com.badlogic.gdx.physics.box2d.Contact)
	    	 **/
	        public void beginContact(Contact contact)
	        {
	            final Fixture x1 = contact.getFixtureA();
	            final Fixture x2 = contact.getFixtureB();

	            if (x1.getBody().getUserData() != null && x2.getBody().getUserData() != null)
	            {
	                if (x2.getBody().getUserData().equals("player") || x1.getBody().getUserData().equals("player"))
	                {
	                    player.increaseFootContacts();
	                }
	            }
	            
	            /**
	             * ------------------------------------ LEVEL 1 ------------------------------------------------------------------------------------------------------
	             **/
	            
	            // Contact player - platform2 -----> dinamycBody (ascensor)
	            if (x1.getBody().getUserData().equals("platform2") && x2.getBody().getUserData().equals("player") && x2.getBody().getPosition().y > x1.getBody().getPosition().y)
	            {
	            	if (player.getColAscensor() == false)
	            	{
	            		player.setColAscensor(true);
	            		physicsWorld.setGravity(new Vector2(0, 10.0f));
		            	x1.getBody().setType(BodyType.DynamicBody);
		            	
		                engine.registerUpdateHandler(new TimerHandler(1.6f, new ITimerCallback()
		                {                                    
		                    public void onTimePassed(final TimerHandler pTimerHandler)
		                    {
		                        pTimerHandler.reset();
		                        engine.unregisterUpdateHandler(pTimerHandler);
		                        player.body.setLinearVelocity(0, 0);
		                        
		                        // WAIT until player lets the platform
		                        x1.getBody().setType(BodyType.StaticBody);
		                        player.body.setType(BodyType.StaticBody);
		                        physicsWorld.setGravity(new Vector2(0, -18.0f));
		                        player.body.setType(BodyType.DynamicBody);
		                        
		                    }
		                }));
	            	}
	            }
	            
	            // Contact player - SPIKES
	            if (x1.getBody().getUserData().equals("spikes") && x2.getBody().getUserData().equals("player") && x2.getBody().getPosition().y > x1.getBody().getPosition().y + 0.5)
	            {

	            	if (player.getSpikesColision() == false)
	            	{
	            		player.setSpikesColision(true);
	            		ResourcesManager.getInstance().getSpikeSound().play();
	            		// Control Impulse and direction
		            	if (player.body.getLinearVelocity().x > 0)
		            	{
		            		player.body.setLinearVelocity(-20, 0);
		            		player.body.applyLinearImpulse(-30.0f, 10.0f, player.body.getPosition().x - 20, player.body.getPosition().y);
		            	}
		            	
		            	else if (player.body.getLinearVelocity().x < 0)
		            	{
		            		player.body.setLinearVelocity(20, 0);
		            		player.body.applyLinearImpulse(30.0f, 10.0f, player.body.getPosition().x + 20, player.body.getPosition().y);

		            	}
		            	
		            	else
		            	{
		            		
		            		player.body.setLinearVelocity(player.body.getLinearVelocity().x, player.body.getLinearVelocity().y);
		            	}
	            		
		            	player.life = player.life - 1;
                        if (player.life == 2) gameHUD.detachChild(hurt3);
                        if (player.life == 1) gameHUD.detachChild(hurt2);
                        if (player.life == 0) gameHUD.detachChild(hurt1);
                        
		            	// DO flickering while period of time after touching the spikes 
			       		flicker = new playTimer(0.1f , new playTimer.ITimerCallback()
			       	    {
			            	boolean visible = false;
			       	        @Override
			       	        public void onTick()
			       	        {
			       	            player.setVisible(visible);
			       	            visible = !visible;
			       	        }
			       	      }
			       	    );
			       		engine.registerUpdateHandler(flicker);

		                engine.registerUpdateHandler(new TimerHandler(1.0f, new ITimerCallback()
		                {                                    
		                    public void onTimePassed(final TimerHandler pTimerHandler)
		                    {
		                        pTimerHandler.reset();
		                        flicker.pause();
		                        flicker.reset();
		                        if (player.isVisible() == false) player.setVisible(true);
		                        engine.unregisterUpdateHandler(pTimerHandler);
		                        engine.unregisterUpdateHandler(flicker);
		                    }
		                }));
	            	}
	            }
	            
	            // Contact Player ------> Enemy 
	            if (x1.getBody().getUserData().equals("enemy") && x2.getBody().getUserData().equals("player"))
	            {

	            	if (player.getEnemyColision() == false)
	            	{
	            		player.setEnemyColision(true);
	            		ResourcesManager.getInstance().getFlySound().play();
	            		// Control Impulse and direction
		            	if (player.body.getLinearVelocity().x > 0)
		            	{
		            		player.body.applyLinearImpulse(-20.0f, -20.0f, player.body.getPosition().x - 5, player.body.getPosition().y);
		            	}
		            	
		            	else if (player.body.getLinearVelocity().x < 0)
		            	{
		            		player.body.applyLinearImpulse(20.0f, -20.0f, player.body.getPosition().x + 5, player.body.getPosition().y);

		            	}
		            	
		            	else
		            	{
		            		
		            		player.body.setLinearVelocity(player.body.getLinearVelocity().x, player.body.getLinearVelocity().y);
		            	}
		            	
		            	player.life = player.life - 1;
                        if (player.life == 2) gameHUD.detachChild(hurt3);
                        if (player.life == 1) gameHUD.detachChild(hurt2);
                        if (player.life == 0) gameHUD.detachChild(hurt1);
	            		
		            	// DO flickering while period of time after touching the spikes 
			       		flicker = new playTimer(0.1f , new playTimer.ITimerCallback()
			       	    {
			            	boolean visible = false;
			       	        @Override
			       	        public void onTick()
			       	        {
			       	            player.setVisible(visible);
			       	            visible = !visible;
			       	        }
			       	      }
			       	    );
			       		engine.registerUpdateHandler(flicker);

		                engine.registerUpdateHandler(new TimerHandler(1.0f, new ITimerCallback()
		                {                                    
		                    public void onTimePassed(final TimerHandler pTimerHandler)
		                    {
		                        pTimerHandler.reset();
		                        flicker.pause();
		                        flicker.reset();
		                        if (player.isVisible() == false) player.setVisible(true);
		                        engine.unregisterUpdateHandler(pTimerHandler);
		                        engine.unregisterUpdateHandler(flicker);
		                    }
		                }));
	            	}
	            }
	            
	            // Contact Player ------> MINES (EXPLOSION) 
	            if (x1.getBody().getUserData().equals("mine") && x2.getBody().getUserData().equals("player"))
	            {

	            	if (player.getMineColision() == false)
	            	{
	            		explosion = new FireParticleSystem();
	            		ResourcesManager.getInstance().getExplosionSound().play();
		            	attachChild (explosion.build(engine, 500, 410));
		            	player.setMineColision(true);

		            	// Control Impulse and direction
		            	if (player.body.getLinearVelocity().x > 0)
		            	{
		            		player.body.applyLinearImpulse(-50.0f, 10.0f, player.body.getPosition().x - 5, player.body.getPosition().y);
		            	}
		            	
		            	else if (player.body.getLinearVelocity().x < 0)
		            	{
		            		player.body.applyLinearImpulse(50.0f, 10.0f, player.body.getPosition().x + 5, player.body.getPosition().y);

		            	}
		            	
		            	else
		            	{
		            		
		            		player.body.setLinearVelocity(player.body.getLinearVelocity().x, player.body.getLinearVelocity().y);
		            	}
		            	
		            	player.life = player.life - 1;
                        if (player.life == 2) gameHUD.detachChild(hurt3);
                        if (player.life == 1) gameHUD.detachChild(hurt2);
                        if (player.life == 0) gameHUD.detachChild(hurt1);

		            	// DO flickering while period of time after touching the spikes 
			       		flicker = new playTimer(0.1f , new playTimer.ITimerCallback()
			       	    {
			            	boolean visible = false;
			       	        @Override
			       	        public void onTick()
			       	        {
			       	            player.setVisible(visible);
			       	            visible = !visible;
			       	        }
			       	      }
			       	    );
			       		engine.registerUpdateHandler(flicker);
			       		
		                engine.registerUpdateHandler(new TimerHandler(1.0f, new ITimerCallback()
		                {                                    
		                    public void onTimePassed(final TimerHandler pTimerHandler)
		                    {
		                        pTimerHandler.reset();
		                        flicker.pause();
		                        flicker.reset();
		                        if (player.isVisible() == false) player.setVisible(true);
		                        engine.unregisterUpdateHandler(pTimerHandler);
		                        engine.unregisterUpdateHandler(flicker);
		                        detachChild(explosion.getParticleSystem());
		                        
		                    }
		                }));
	            	}
	            }           	
		            
            	if (x1.getBody().getUserData().equals("doorClosedMid") && x2.getBody().getUserData().equals("player"))
            	{
            		
            		if (player.hasKey == true)
            		{
            			if (playSound == false) 
                		{
                			ResourcesManager.getInstance().getKeySound().play();
                			playSound = true;
                		}
                		//gameHUD.detachChild(key);
                		kY.setAlpha(1.0f);
            		}
            		
            	}
            	
            	if (x1.getBody().getUserData().equals("doorClosedGreen") && x2.getBody().getUserData().equals("player"))
            	{
            		if (player.hasGreenKey == true)
            		{
            			if (playSoundGreen == false) 
                		{
                			ResourcesManager.getInstance().getKeySound().play();
                			playSoundGreen = true;
                		}
                		//gameHUD.detachChild(keyG);
                		kG.setAlpha(1.0f);
            		}
            	}
            	
            	/**
            	 * ------------------------------------- LEVEL 2 --------------------------------------------------------------------------
            	 **/
            	
            	// ----------------------------- SPRINGBOARDER-----------------------------------------------------------------------------
            	if (x1.getBody().getUserData().equals("player") && x2.getBody().getUserData().equals("springboarder"))
            	{
            		if (player.getY() > springboarder.getY() + 15)
            		{
            			// Animate SpringBoarder texture
                		springboarder.setRunning();
                		springboarder2.setRunning();
                		
                		// Apply a liniear (Y-axis) impulse to player (like jumping)
                		player.body.applyLinearImpulse(0.0f, 27.0f, player.body.getPosition().x, player.body.getPosition().y);
            		}
            	}
            	
            	// ----------------------------- WATER-----------------------------------------------------------------------------
            	if (x1.getBody().getUserData().equals("player") && x2.getBody().getUserData().equals("waterSynthetic"))
            	{
            		displayGameOverText(ResourcesManager.getInstance().getLevelComplete());
            	}
            	
            	// ----------------------------- BOXES -----------------------------------------------------------------------------
            	if (x1.getBody().getUserData().equals("player") && x2.getBody().getUserData().equals("boxWarning"))
            	{
            		engine.registerUpdateHandler(new TimerHandler(0.5f, new ITimerCallback()
      		        {                                    
							public void onTimePassed(final TimerHandler pTimerHandler)
							{
							    pTimerHandler.reset();
							    engine.unregisterUpdateHandler(pTimerHandler);
							    x2.getBody().setType(BodyType.DynamicBody);
							}
      		      }));
            	}
            	
            	if (x1.getBody().getUserData().equals("player") && x2.getBody().getUserData().equals("boxItemAlt"))
            	{
            		switcher.setVisible(true);
            		switcher2.setVisible(true);
            	}
            	
            	// ----------------------------- SWITCHER -----------------------------------------------------------------------------
            	if (x1.getBody().getUserData().equals("player") && x2.getBody().getUserData().equals("switcher") || x1.getBody().getUserData().equals("switcher") && x2.getBody().getUserData().equals("player"))
            	{
            		if (resourcesManager.getLevelComplete() == 2)
            		{
            			switcher.setRunning();
                		switcher2.setRunning();

                    	gameHUD.attachChild(warningText);
         	    		
         	    		// DO flickering while period of time after touching the spikes 
    		       		flicker = new playTimer(0.25f , new playTimer.ITimerCallback()
    		       	    {
    		            	boolean visible = false;
    		       	        @Override
    		       	        public void onTick()
    		       	        {
    		       	            warningText.setVisible(visible);
    		       	            visible = !visible;
    		       	        }
    		       	      }
    		       	    );
    		       		engine.registerUpdateHandler(flicker);
    		       		
             	    	engine.registerUpdateHandler(new TimerHandler(3.0f, new ITimerCallback()
		                {                                    
		                    public void onTimePassed(final TimerHandler pTimerHandler)
		                    {
		                        pTimerHandler.reset();
		                        flicker.reset();
		                        flicker.pause();
		                        engine.unregisterUpdateHandler(pTimerHandler);  
		                        gameHUD.detachChild(warningText);
		                    }
		                }));	
             	    }
            		
            		else if (resourcesManager.getLevelComplete() == 4)
            		{
            			switcher.setRunning();
            		}
            	}
            	
            	// ----------------------------- DESERT MINE -----------------------------------------------------------------------------
            	if (x1.getBody().getUserData().equals("player") && x2.getBody().getUserData().equals("buttonYellow"))
            	{
            		if (player.getMineColision() == false)
	            	{
	            		explosion = new FireParticleSystem();
	            		explosion2 = new FireParticleSystem();
	            		ResourcesManager.getInstance().getExplosionSound().play();
		            	attachChild (explosion.build(engine, 940, 365));
		            	attachChild (explosion2.build(engine, 2600, 200));
		            	player.setMineColision(true);

		            	// Control Impulse and direction
		            	if (player.body.getLinearVelocity().x > 0)
		            	{
		            		player.body.applyLinearImpulse(-50.0f, 10.0f, player.body.getPosition().x - 5, player.body.getPosition().y);
		            	}
		            	
		            	else if (player.body.getLinearVelocity().x < 0)
		            	{
		            		player.body.applyLinearImpulse(50.0f, 10.0f, player.body.getPosition().x + 5, player.body.getPosition().y);

		            	}
		            	
		            	else
		            	{
		            		
		            		player.body.setLinearVelocity(player.body.getLinearVelocity().x, player.body.getLinearVelocity().y);
		            	}
		            	
		            	player.life = player.life - 1;
                        if (player.life == 2) gameHUD.detachChild(hurt3);
                        if (player.life == 1) gameHUD.detachChild(hurt2);
                        if (player.life == 0) gameHUD.detachChild(hurt1);

		            	// DO flickering while period of time after touching the spikes 
			       		flicker = new playTimer(0.1f , new playTimer.ITimerCallback()
			       	    {
			            	boolean visible = false;
			       	        @Override
			       	        public void onTick()
			       	        {
			       	            player.setVisible(visible);
			       	            visible = !visible;
			       	        }
			       	      }
			       	    );
			       		engine.registerUpdateHandler(flicker);
			       		
		                engine.registerUpdateHandler(new TimerHandler(1.0f, new ITimerCallback()
		                {                                    
		                    public void onTimePassed(final TimerHandler pTimerHandler)
		                    {
		                        pTimerHandler.reset();
		                        flicker.pause();
		                        flicker.reset();
		                        if (player.isVisible() == false) player.setVisible(true);
		                        engine.unregisterUpdateHandler(pTimerHandler);
		                        engine.unregisterUpdateHandler(flicker);
		                        detachChild(explosion.getParticleSystem());
		                        detachChild(explosion2.getParticleSystem());
		                        
		                    }
		                }));

	            	}
            	}
            	
            	// ----------------------------- CHAPUZÓN (BOX - WATER) -----------------------------------------------------------------------------
            	if (x1.getBody().getUserData().equals("waterSynthetic") && x2.getBody().getUserData().equals("boxWarning"))
            	{
            		//float fontX = x2.getBody().getPosition().x;
            		//float fontY = x2.getBody().getPosition().y;
            		if (player.getBoxColision() == false)
            		{
            			player.setBoxColision(true);
            			attachChild (waterEx);
                		engine.registerUpdateHandler(new TimerHandler(3.0f, new ITimerCallback()
          		        {                                    
    							public void onTimePassed(final TimerHandler pTimerHandler)
    							{
    							    pTimerHandler.reset();
    							    engine.unregisterUpdateHandler(pTimerHandler);
    							    detachChild(waterEx);
    							}
          		      }));
            		}
            	}
            	
            	/**
            	 * ------------------------------------------ LEVEL 3 --------------------------------------------------------------------------------------
            	 **/
            	
            	if (x1.getBody().getUserData().equals("player") && x2.getBody().getUserData().equals("RinoEnemy"))
            	{
            		if (player.getRinoColision() == false)
            		{
            			player.setRinoColision(true);
            			player.life = player.life - 1;
                        if (player.life == 2) gameHUD.detachChild(hurt3);
                        if (player.life == 1) gameHUD.detachChild(hurt2);
                        if (player.life == 0) gameHUD.detachChild(hurt1);
                        
                        ResourcesManager.getInstance().getGorillaSound().play();
                        
                        // Control Impulse and direction
    	            	if (player.body.getLinearVelocity().x > 0)
    	            	{
    	            		player.body.applyLinearImpulse(-50.0f, 20.0f, player.body.getPosition().x - 5, player.body.getPosition().y);
    	            	}
    	            	
    	            	else if (player.body.getLinearVelocity().x < 0)
    	            	{
    	            		player.body.applyLinearImpulse(50.0f, 20.0f, player.body.getPosition().x + 5, player.body.getPosition().y);

    	            	}
    	            	
    	            	else
    	            	{
    	            		
    	            		player.body.setLinearVelocity(player.body.getLinearVelocity().x, player.body.getLinearVelocity().y);
    	            	}
    	            	
    	            	// DO flickering while period of time after touching the spikes 
    		       		flicker = new playTimer(0.1f , new playTimer.ITimerCallback()
    		       	    {
    		            	boolean visible = false;
    		       	        @Override
    		       	        public void onTick()
    		       	        {
    		       	            player.setVisible(visible);
    		       	            visible = !visible;
    		       	        }
    		       	      }
    		       	    );
    		       		engine.registerUpdateHandler(flicker);
    		       		
    	                engine.registerUpdateHandler(new TimerHandler(1.0f, new ITimerCallback()
    	                {                                    
    	                    public void onTimePassed(final TimerHandler pTimerHandler)
    	                    {
    	                        pTimerHandler.reset();
    	                        flicker.pause();
    	                        flicker.reset();
    	                        if (player.isVisible() == false) player.setVisible(true);
    	                        engine.unregisterUpdateHandler(pTimerHandler);
    	                        engine.unregisterUpdateHandler(flicker);
    	                        
    	                    }
    	                }));
    	                
            		}
            	}
            	
            	 // Contact Player ------> MINES (EXPLOSION) 
	            if (x1.getBody().getUserData().equals("player") && x2.getBody().getUserData().equals("switchGreenOff"))
	            {

	            	if (player.getMineColision() == false)
	            	{
	            		explosion = new FireParticleSystem();
	            		explosion2 = new FireParticleSystem();
	            		ResourcesManager.getInstance().getExplosionSound().play();
		            	attachChild (explosion.build(engine, 616, 175));
		            	attachChild (explosion2.build(engine, 1690, 165));
		            	player.setMineColision(true);

		            	// Control Impulse and direction
		            	if (player.body.getLinearVelocity().x > 0)
		            	{
		            		player.body.applyLinearImpulse(-50.0f, 20.0f, player.body.getPosition().x - 5, player.body.getPosition().y);
		            	}
		            	
		            	else if (player.body.getLinearVelocity().x < 0)
		            	{
		            		player.body.applyLinearImpulse(50.0f, 20.0f, player.body.getPosition().x + 5, player.body.getPosition().y);

		            	}
		            	
		            	else
		            	{
		            		
		            		player.body.setLinearVelocity(player.body.getLinearVelocity().x, player.body.getLinearVelocity().y);
		            	}
		            	
		            	player.life = player.life - 1;
                        if (player.life == 2) gameHUD.detachChild(hurt3);
                        if (player.life == 1) gameHUD.detachChild(hurt2);
                        if (player.life == 0) gameHUD.detachChild(hurt1);

		            	// DO flickering while period of time after touching the spikes 
			       		flicker = new playTimer(0.1f , new playTimer.ITimerCallback()
			       	    {
			            	boolean visible = false;
			       	        @Override
			       	        public void onTick()
			       	        {
			       	            player.setVisible(visible);
			       	            visible = !visible;
			       	        }
			       	      }
			       	    );
			       		engine.registerUpdateHandler(flicker);
			       		
		                engine.registerUpdateHandler(new TimerHandler(1.0f, new ITimerCallback()
		                {                                    
		                    public void onTimePassed(final TimerHandler pTimerHandler)
		                    {
		                        pTimerHandler.reset();
		                        flicker.pause();
		                        flicker.reset();
		                        if (player.isVisible() == false) player.setVisible(true);
		                        engine.unregisterUpdateHandler(pTimerHandler);
		                        engine.unregisterUpdateHandler(flicker);
		                        detachChild(explosion.getParticleSystem());
		                        detachChild(explosion2.getParticleSystem());
		                        
		                    }
		                }));
	            	}
	            }  
	            
	            
	          /**
	           * ------------------------------------------ LEVEL 4 --------------------------------------------------------------------------------------
	           **/
	          
	            // Contact player - Mineral
	            if (x1.getBody().getUserData().equals("player") && x2.getBody().getUserData().equals("mineral") && x1.getBody().getPosition().y > x2.getBody().getPosition().y + 2.0f)
	            {

	            	if (player.getSpikesColision() == false)
	            	{
	            		player.setSpikesColision(true);
	            		ResourcesManager.getInstance().getSpikeSound().play();
	            		// Control Impulse and direction
		            	if (player.body.getLinearVelocity().x > 0)
		            	{
		            		player.body.setLinearVelocity(-20, 0);
		            		player.body.applyLinearImpulse(-15.0f, 10.0f, player.body.getPosition().x - 20, player.body.getPosition().y);
		            	}
		            	
		            	else if (player.body.getLinearVelocity().x < 0)
		            	{
		            		player.body.setLinearVelocity(20, 0);
		            		player.body.applyLinearImpulse(15.0f, 10.0f, player.body.getPosition().x + 20, player.body.getPosition().y);

		            	}
		            	
		            	else
		            	{
		            		
		            		player.body.setLinearVelocity(player.body.getLinearVelocity().x, player.body.getLinearVelocity().y);
		            	}
	            		
		            	player.life = player.life - 1;
                        if (player.life == 2) gameHUD.detachChild(hurt3);
                        if (player.life == 1) gameHUD.detachChild(hurt2);
                        if (player.life == 0) gameHUD.detachChild(hurt1);
                        

		                engine.registerUpdateHandler(new TimerHandler(1.0f, new ITimerCallback()
		                {                                    
		                    public void onTimePassed(final TimerHandler pTimerHandler)
		                    {
		                        pTimerHandler.reset();
		                        if (player.isVisible() == false) player.setVisible(true);
		                        engine.unregisterUpdateHandler(pTimerHandler);
		                    }
		                }));
	            	}
	            }
            	
	            // Contact Player - Zombie Enemy
	            if (x1.getBody().getUserData().equals("player") && x2.getBody().getUserData().equals("enemy1"))
	            {
	            	if (player.getEnemyColision() == false)
	            	{
	            		player.setEnemyColision(true);
	            		ResourcesManager.getInstance().getFlySound().play();
	            		// Control Impulse and direction
		            	if (player.body.getLinearVelocity().x > 0)
		            	{
		            		player.body.applyLinearImpulse(-20.0f, -20.0f, player.body.getPosition().x - 5, player.body.getPosition().y);
		            	}
		            	
		            	else if (player.body.getLinearVelocity().x < 0)
		            	{
		            		player.body.applyLinearImpulse(20.0f, -20.0f, player.body.getPosition().x + 5, player.body.getPosition().y);

		            	}
		            	
		            	else
		            	{
		            		
		            		player.body.setLinearVelocity(player.body.getLinearVelocity().x, player.body.getLinearVelocity().y);
		            	}
		            	
		            	player.life = player.life - 1;
                        if (player.life == 2) gameHUD.detachChild(hurt3);
                        if (player.life == 1) gameHUD.detachChild(hurt2);
                        if (player.life == 0) gameHUD.detachChild(hurt1);
	            		
		            	// DO flickering while period of time after touching the spikes 
			       		flicker = new playTimer(0.1f , new playTimer.ITimerCallback()
			       	    {
			            	boolean visible = false;
			       	        @Override
			       	        public void onTick()
			       	        {
			       	            player.setVisible(visible);
			       	            visible = !visible;
			       	        }
			       	      }
			       	    );
			       		engine.registerUpdateHandler(flicker);

		                engine.registerUpdateHandler(new TimerHandler(1.0f, new ITimerCallback()
		                {                                    
		                    public void onTimePassed(final TimerHandler pTimerHandler)
		                    {
		                        pTimerHandler.reset();
		                        flicker.pause();
		                        flicker.reset();
		                        if (player.isVisible() == false) player.setVisible(true);
		                        engine.unregisterUpdateHandler(pTimerHandler);
		                        engine.unregisterUpdateHandler(flicker);
		                    }
		                }));
	            	}
	            }
	            
	            if (x1.getBody().getUserData() != null && x2.getBody().getUserData() != null)
	            {
	                if (x2.getBody().getUserData().equals("bomb") || x1.getBody().getUserData().equals("bomb"))
	                {
	                    if (bomb.getX() > 1600 && bomb.getX() < 1800)
	                    {
	                    	// KILL zombie
	                    	zombie.body.setActive(false);
		                    physicsWorld.destroyBody(zombie.body);
	                    	detachChild(zombie);
	                    }
	                }
	            }
	        }

	        /**
	         *END CONTACT COLISION BETWEEN PLAYER AND ANY OF PHYSICS SCENE OBJECT
	         * @see com.badlogic.gdx.physics.box2d.ContactListener#endContact(com.badlogic.gdx.physics.box2d.Contact)
	         **/
	        public void endContact(Contact contact)
	        {
	            final Fixture x1 = contact.getFixtureA();
	            final Fixture x2 = contact.getFixtureB();

	            if (x1.getBody().getUserData() != null && x2.getBody().getUserData() != null)
	            {
	                if (x2.getBody().getUserData().equals("player") || x1.getBody().getUserData().equals("player"))
	                {
	                    player.decreaseFootContacts();
	                }
	            }
	            
	            /**
	             * IN ALL CASES ----> WAITING TIME (3s) UNTIL IS ANOTHER COLISION.
	             **/
	             
	            if (x1.getBody().getUserData().equals("mine") && x2.getBody().getUserData().equals("player"))
	            {
	            	engine.registerUpdateHandler(new TimerHandler(3.0f, new ITimerCallback()
	                {                                    
	                    public void onTimePassed(final TimerHandler pTimerHandler)
	                    {
	                        pTimerHandler.reset();
	                        player.setMineColision(false);
	                        engine.unregisterUpdateHandler(pTimerHandler); 
	                    }
	                }));

	            }
	            
	            if (x1.getBody().getUserData().equals("spikes") && x2.getBody().getUserData().equals("player"))
	            {
	            	engine.registerUpdateHandler(new TimerHandler(3.0f, new ITimerCallback()
	                {                                    
	                    public void onTimePassed(final TimerHandler pTimerHandler)
	                    {
	                        pTimerHandler.reset();
	                        player.setSpikesColision(false);
	                        engine.unregisterUpdateHandler(pTimerHandler); 
	                    }
	                }));
	            }
	            
	            if (x1.getBody().getUserData().equals("enemy") && x2.getBody().getUserData().equals("player"))
	            {
	            	engine.registerUpdateHandler(new TimerHandler(3.0f, new ITimerCallback()
	                {                                    
	                    public void onTimePassed(final TimerHandler pTimerHandler)
	                    {
	                        pTimerHandler.reset();
	                        player.setEnemyColision(false);
	                        engine.unregisterUpdateHandler(pTimerHandler); 
	                    }
	                }));
	            }
	            
	            if (x1.getBody().getUserData().equals("platform2") && x2.getBody().getUserData().equals("player") && x2.getBody().getPosition().y > x1.getBody().getPosition().y)
	            {
	            	engine.registerUpdateHandler(new TimerHandler(3.0f, new ITimerCallback()
	                {                                    
	                    public void onTimePassed(final TimerHandler pTimerHandler)
	                    {
	                        pTimerHandler.reset();
	                        player.setColAscensor(false);
	                        x1.getBody().setType(BodyType.StaticBody);
	                        engine.unregisterUpdateHandler(pTimerHandler); 
	                    }
	                }));
	            	
	            	x1.getBody().setType(BodyType.DynamicBody);
	            }
	            
	            if (x1.getBody().getUserData().equals("waterMid") && x2.getBody().getUserData().equals("player"))
	            {
	            	engine.registerUpdateHandler(new TimerHandler(3.0f, new ITimerCallback()
	                {                                    
	                    public void onTimePassed(final TimerHandler pTimerHandler)
	                    {
	                        pTimerHandler.reset();
	                        player.setWaterColision(false);
	                        engine.unregisterUpdateHandler(pTimerHandler); 
	                    }
	                }));
	            }
	            
	            // ------------------------------ LEVEL 2 ------------------------------------------------------------------------
	            // ------------------------------ BOXES --------------------------------------------------------------------------
	            
	            if (x1.getBody().getUserData().equals("player") && x2.getBody().getUserData().equals("buttonYellow"))
	            {
	            	engine.registerUpdateHandler(new TimerHandler(3.0f, new ITimerCallback()
	                {                                    
	                    public void onTimePassed(final TimerHandler pTimerHandler)
	                    {
	                        pTimerHandler.reset();
	                        player.setMineColision(false);
	                        engine.unregisterUpdateHandler(pTimerHandler); 
	                    }
	                }));

	            }
	            
	            // ----------------------------------- LEVEL 3 ---------------------------------------------------------------------------
	            if (x1.getBody().getUserData().equals("player") && x2.getBody().getUserData().equals("switchGreenOff"))
	            {
	            	engine.registerUpdateHandler(new TimerHandler(3.0f, new ITimerCallback()
	                {                                    
	                    public void onTimePassed(final TimerHandler pTimerHandler)
	                    {
	                        pTimerHandler.reset();
	                        player.setMineColision(false);
	                        engine.unregisterUpdateHandler(pTimerHandler); 
	                    }
	                }));

	            }
	            
	            if(x1.getBody().getUserData().equals("player") && x2.getBody().getUserData().equals("RinoEnemy"))
	            {
	            	engine.registerUpdateHandler(new TimerHandler(3.0f, new ITimerCallback()
	                {                                    
	                    public void onTimePassed(final TimerHandler pTimerHandler)
	                    {
	                        pTimerHandler.reset();
	                        player.setRinoColision(false);
	                        engine.unregisterUpdateHandler(pTimerHandler); 
	                    }
	                }));
	            }
	            
	            if (x1.getBody().getUserData().equals("player") && x2.getBody().getUserData().equals("mineral"))
	            {
	            	engine.registerUpdateHandler(new TimerHandler(2.0f, new ITimerCallback()
	                {                                    
	                    public void onTimePassed(final TimerHandler pTimerHandler)
	                    {
	                        pTimerHandler.reset();
	                        player.setSpikesColision(false);
	                        engine.unregisterUpdateHandler(pTimerHandler); 
	                    }
	                }));
	            }
	            
	            if (x1.getBody().getUserData().equals("player") && x2.getBody().getUserData().equals("enemy1"))
	            {
	            	engine.registerUpdateHandler(new TimerHandler(3.0f, new ITimerCallback()
	                {                                    
	                    public void onTimePassed(final TimerHandler pTimerHandler)
	                    {
	                        pTimerHandler.reset();
	                        player.setEnemyColision(false);
	                        engine.unregisterUpdateHandler(pTimerHandler); 
	                    }
	                }));
	            }
	        }
	        

			
	        public void preSolve(Contact contact, Manifold oldManifold)
	        {

	        }

	        public void postSolve(Contact contact, ContactImpulse impulse)
	        {

	        }
 
	    };
	    return contactListener;
	}
	
	private void clearPhysicsWorld(PhysicsWorld physicsWorld)
	{
		Iterator<Joint> allMyJoints = physicsWorld.getJoints();
		while (allMyJoints.hasNext())
		{
			try
			{
				final Joint myCurrentJoint = allMyJoints.next();
				physicsWorld.destroyJoint(myCurrentJoint);
			} 
			catch (Exception localException)
			{
			}
		}
		
		Iterator<Body> localIterator = physicsWorld.getBodies();
		while (localIterator.hasNext())
		{
			if (!localIterator.hasNext())
			{
				physicsWorld.clearForces();
				physicsWorld.clearPhysicsConnectors();
				physicsWorld.reset();
				physicsWorld.dispose();
				physicsWorld = null;
				return;
			}
			try
			{
				physicsWorld.destroyBody(localIterator.next());
			} 
			catch (Exception localException)
			{
			}
		}
	}
	
	public void clearScene()
	{
		engine.runOnUpdateThread(new Runnable()
		{
			@Override
			public void run()
			{
				clearPhysicsWorld(physicsWorld);
				clearTouchAreas();
				clearUpdateHandlers();
			}
		});
	}
	
	/*public void touchParticleCollision()
	{	
    	player.life = player.life - 1;
        if (player.life == 2) gameHUD.detachChild(hurt3);
        if (player.life == 1) gameHUD.detachChild(hurt2);
        if (player.life == 0) gameHUD.detachChild(hurt1);

    	// DO flickering while period of time after touching the spikes 
   		flicker = new playTimer(0.1f , new playTimer.ITimerCallback()
   	    {
        	boolean visible = false;
   	        @Override
   	        public void onTick()
   	        {
   	            player.setVisible(visible);
   	            visible = !visible;
   	        }
   	      }
   	    );
   		engine.registerUpdateHandler(flicker);
   		
        engine.registerUpdateHandler(new TimerHandler(1.0f, new ITimerCallback()
        {                                    
            public void onTimePassed(final TimerHandler pTimerHandler)
            {
                pTimerHandler.reset();
                flicker.pause();
                flicker.reset();
                if (player.isVisible() == false) player.setVisible(true);
                engine.unregisterUpdateHandler(pTimerHandler);
                engine.unregisterUpdateHandler(flicker);            
            }
        }));
	}*/
	
	private void createBombSpriteControl()
	{
		Sprite bombControl = new Sprite(75, 75, resourcesManager.bombHUD, vbom)
		{			
	      @Override
	      public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) 
	      {
	    	  if (pSceneTouchEvent.isActionDown() && player.hasBombs > 0)
	    	  {
	    		  // Set bomb position to actual player position 
		    	  final Bomb bombLaunched = new Bomb(player.getX(), player.getY(), vbom, camera, physicsWorld);
		    	  SceneManager.getInstance().getGameScene().attachChild(bombLaunched);
		    	  bombLaunched.setVisible(true);
		    	  
		    	  //Launch granade to a unique direction and velocity
		    	  bombLaunched.body.applyLinearImpulse(10.0f, 10.0f, 10.0f, 10.0f);
		    	  
		    	  player.hasBombs--;
		    	  gameHUD.detachChild(nBombs);
		    	  nBombs = new Text(40, 300, resourcesManager.font, "+" + player.hasBombs, new TextOptions(HorizontalAlign.LEFT), vbom);
              	  gameHUD.attachChild(nBombs);
		    	  
		    	  // Remove grenade and execute explosion -----> particle system (like mines)
		    	  engine.registerUpdateHandler(new TimerHandler(2.0f, new ITimerCallback()
	              {                                    
	                    public void onTimePassed(final TimerHandler pTimerHandler)
	                    {
	                        pTimerHandler.reset();
	                        
	                        bombLaunched.body.setActive(false);
	                        physicsWorld.destroyBody(bombLaunched.body);
	                        SceneManager.getInstance().getGameScene().detachChild(bombLaunched);
	                        explosion = new FireParticleSystem();
	                        SceneManager.getInstance().getGameScene().attachChild (explosion.build(engine, bombLaunched.getX(), bombLaunched.getY()));
	                        resourcesManager.getExplosionSound().play();
	                        
	                        engine.unregisterUpdateHandler(pTimerHandler); 
	                    }
	              }));
		    	  engine.registerUpdateHandler(new TimerHandler(3.5f, new ITimerCallback()
	              {                                    
	                    public void onTimePassed(final TimerHandler pTimerHandler)
	                    {
	                        pTimerHandler.reset();
	                        SceneManager.getInstance().getGameScene().detachChild (explosion.getParticleSystem());
	                        engine.unregisterUpdateHandler(pTimerHandler); 
	                    }
	              }));
	    	  }
	    	  return true;
	      }	
		};
		gameHUD.registerTouchArea(bombControl);
		gameHUD.attachChild(bombControl);
	}
	
}
