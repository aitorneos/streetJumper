package ResourcesManagment;

import java.io.IOException;
import java.io.InputStream;
import org.andengine.audio.music.Music;
import org.andengine.audio.music.MusicFactory;
import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.engine.Engine;
import org.andengine.engine.camera.BoundCamera;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.font.IFont;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder.TextureAtlasBuilderException;
import org.andengine.opengl.texture.bitmap.BitmapTexture;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.io.in.IInputStreamOpener;
import org.andengine.util.debug.Debug;
import android.graphics.Color;
import com.PFC.PlatformJumper.streetJumper;


/**
 * @author Aitor Arque
 * @version 1.0
 */

@SuppressWarnings("deprecation")
public class ResourcesManager
{
    //---------------------------------------------
    // VARIABLES
    //---------------------------------------------
    
    private static final ResourcesManager INSTANCE = new ResourcesManager();
    
    public Engine engine;
    public streetJumper activity;
    public BoundCamera camera;
    public VertexBufferObjectManager vbom;
    public IFont font;
    public IFont fontText;
    public IFont loadingFont;
    public IFont loadingFont2;
    private int level = 1;
    public boolean loading= false;
    
    //---------------------------------------------
    // TEXTURES & TEXTURE REGIONS
    //---------------------------------------------
    
    // Splash textures
    public ITextureRegion splash_region;
    private BitmapTextureAtlas splashTextureAtlas;
    public BuildableBitmapTextureAtlas optionsTextureAtlas;
    
    // Menu and subregions (bottons) textures
    public ITextureRegion menu_background_region;
    public ITextureRegion scene_background_region;
    public ITextureRegion play_region;
    public ITextureRegion options_region;
    public ITextureRegion onlineOptions;
    public ITextureRegion load_region;
    public ITextureRegion options_player_selection;
    public ITextureRegion options_background_region;
	public ITextureRegion player1;
	public ITextureRegion player2;
	public ITextureRegion player3;
	public ITextureRegion player4;
	public ITextureRegion level_1;
	public ITextureRegion enemy1;
	public ITextureRegion enemy2;
	public ITextureRegion level_2;
	public ITextureRegion level_3;
	public ITextureRegion level_4;
	public ITextureRegion level_5;
	public ITiledTextureRegion playerSecondary;
	public ITextureRegion ARSel;
	public ITextureRegion optionsBack;
	public ITextureRegion menuBack;
	public ITextureRegion levelLoad;
	public ITextureRegion particleOption;
	public ITextureRegion shaderOption;
	public ITextureRegion yes;
	public ITextureRegion not;
	
	
	// Level Complete Window
	public ITextureRegion complete_window_region;
	public ITiledTextureRegion complete_stars_region;
        
 // Game Texture Scenarios
    public BuildableBitmapTextureAtlas gameTextureAtlas;
        
    // Game Texture Regions (Platforms, Coins, Enemies , etc...)
    
    // ------------------------------------ PLATFORMS ----------------------------------------------------------
    public ITextureRegion platform1_region;
    public ITextureRegion platform2_region;
    public ITextureRegion platform3_region;
    public ITextureRegion platform_castleLeft_region;
    public ITextureRegion platform_castleRight_region;
    public ITextureRegion platform_castleHalf_region;
    public ITextureRegion platform_dirtLeft_region;
    public ITextureRegion platform_dirtRight_region;
    public ITextureRegion platform_dirtHalf_region;
    public ITextureRegion grass;
    public ITextureRegion tile_grass;
    public ITextureRegion grass_main;
    public ITextureRegion arrowLeft;
    public ITextureRegion rock;
    public ITextureRegion sand;
    public ITextureRegion doorF;
    public ITextureRegion greenPlatform;
    public ITextureRegion rockHalfBigPlatform;
    public ITextureRegion block;
    public ITextureRegion tree;
    public ITextureRegion sun;
    public ITextureRegion bullet;
    public ITextureRegion buttonYellow;
    public ITextureRegion cradle;
    public ITextureRegion littleMountain;
    public ITextureRegion mountainPlatform;
    public ITextureRegion platform;
    public ITextureRegion platformSmall;
    public ITextureRegion bigTree;
    public ITextureRegion troncPlatform;
    public ITextureRegion troncSmall;
    public ITextureRegion cradlePlatform1;
    public ITextureRegion cradlePlatform2;
    public ITextureRegion cradlePlatform3;
    public ITextureRegion cradlePlatform4;
    public ITextureRegion cradlePlatform5;
    public ITextureRegion expulsor;
    public ITextureRegion mineral;
    public ITextureRegion mineral2;
    public ITextureRegion mineral3;
    public ITextureRegion mushroomTree;
    public ITextureRegion groundDirt;
    public ITextureRegion groundSnow;
    public ITextureRegion groundGrass;
    public ITextureRegion groundIce;
    public ITextureRegion groundRock;
    public ITextureRegion medalBronze;
    public ITextureRegion medalGold;
    public ITextureRegion medalSilver;
    public ITextureRegion rockGrass;
    public ITextureRegion rockGrassDown;
    public ITextureRegion rockSnow;
    public ITextureRegion rockSnowDown;
    public ITextureRegion starBronze;
    public ITextureRegion starGold;
    public ITextureRegion starSilver;
    public ITextureRegion planeBlue1;
    public ITextureRegion planeGreen1;
    public ITextureRegion planeRed1;
    public ITextureRegion planeYellow1;
    public ITextureRegion rockIce;
    
    // --------------------------------------- COINS ---------------------------------------------------------------
    public ITextureRegion coin_silver;
    public ITextureRegion coin_gold;
    public ITextureRegion coin_bronze;
    public ITextureRegion bomb;
    public ITextureRegion bombHUD;
    public ITextureRegion boxExplosiveAlt;
    public ITextureRegion boxItemAlt;
    public ITextureRegion boxWarning;
    public ITextureRegion cactus;
    public ITextureRegion snowHill;
    public ITextureRegion waterSynthetic;
    public ITextureRegion bush;
    public ITextureRegion key_blue;
    public ITextureRegion key_red;
    public ITextureRegion switch_green_off;
    
    // ------------------------------------------ ENEMIES ------------------------------------------------------------
    public ITiledTextureRegion flyEnemy;
    public ITextureRegion spike;
    
    // ---------------------------------------------- KEYS AND DOORS ------------------------------------------------------
    public ITextureRegion keyY;
    public ITextureRegion keyG;
    public ITextureRegion doorClosed;
    public ITextureRegion doorClosedGreen;
    public ITextureRegion keyHUD;
    public ITextureRegion keyGreenHUD;
    public ITextureRegion hurtHUD;
    public ITextureRegion starHUD;
    
    // ------------------------------------------------- OTHERS ---------------------------------------------------------------
    public ITextureRegion mine;
    public ITextureRegion stalactitaLeft;
    public ITextureRegion stalactitaRight;
    public ITextureRegion waterShader;
    public ITextureRegion waterDis;
    public ITexture waterDisplacement;
    public ITiledTextureRegion switcher;
    public ITiledTextureRegion springboarder;
    
    
    // Textures definitions for physic effects
    public ITextureRegion waterDrop;
    public ITextureRegion fireDrop;
    public ITextureRegion mineExplosion;
    public ITextureRegion rainFire;
    public ITextureRegion expulsorParticle;
    
    //Sound and effects
    private Sound explosion;
    private Sound jump;
    private Sound coin;
    private Sound machine;
    private Sound spikeS;
    private Sound gorilla;
    private Sound flySound;
    private Sound openK;
    private Music backgroundMusic;
    
    private BuildableBitmapTextureAtlas menuTextureAtlas;
    
    public ITiledTextureRegion player_region;
    public ITiledTextureRegion playerOnline_region;
    public ITiledTextureRegion rinoEnemy;
    public int playerSelected = 0;
    public float playerVelocity = 0;
    
    // Augmented Reality or Normal MODE selection
    private boolean AR = false;
    public int playerOnlineSelected = 3;
    public boolean controlLoaded = false;
    public boolean shaderActivated = false;
    public boolean particlesActivated = false;
    public boolean showOnline = false;

    
    //---------------------------------------------
    // CLASS LOGIC
    //---------------------------------------------

    // ------------------------------------- LOAD RESOURCES ------------------------------------------------------------------
    public void loadMenuResources()
    {
        loadMenuGraphics();
        loadMenuAudio();
        loadMenuFonts();
    }
    
    public void loadTextFont()
    {
    	this.loadWalkDaWalkOneFont();
    }
    
    public void loadGameResources(int resourceLevel)
    {
    	if (resourceLevel == 1)
    	{
            loadGameGraphics();
            loadGameFonts();
            loadGameAudio();
    	}
    	
    	else if (resourceLevel == 2)
    	{
    		loadGameGraphics_Level2();
            loadGameFonts();
            loadGameAudio();
    	}
    	
    	else if (resourceLevel == 3)
    	{
    		loadGameGraphics_Level3();
            loadGameFonts();
            loadGameAudio();
    	}
    	
    	else if (resourceLevel == 4)
    	{
    		loadGameGraphics_Level4();
            loadGameFonts();
            loadGameAudio();
    	}
    	
    	else if (resourceLevel == 5)
    	{
    		loadGameGraphics_Level5();
            loadGameFonts();
            loadGameAudio();
    	}
    }
    
    
    // ------------------------------------- LOAD GRAPHICS RESOURCES  ------------------------------------------------------------------
    public void loadSplashScreen()
    {
    	
    	BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
    	splashTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
    	splash_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(splashTextureAtlas, activity, "splash.png", 0, 0);
    	splashTextureAtlas.load();
    
    }
    
    private void loadMenuGraphics()
    {
    	BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/menu/");
    	menuTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 1024, 1024, TextureOptions.BILINEAR);
    	menu_background_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "menu_background.png");
    	play_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "play.png");
    	load_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "load.png");
    	options_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "options.png");
    	onlineOptions = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "online.png");
    	       
    	try 
    	{
    	    this.menuTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
    	    this.menuTextureAtlas.load();
    	} 
    	catch (final TextureAtlasBuilderException e)
    	{
    	        Debug.e(e);
    	}
    }
    
    public void loadOptionsGraphics()
    {
    	BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/menu/");
    	optionsTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 2048, 2048, TextureOptions.BILINEAR);
    	options_background_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(optionsTextureAtlas, activity, "options_menu.png");
    	options_player_selection = BitmapTextureAtlasTextureRegionFactory.createFromAsset(optionsTextureAtlas, activity, "player_selection.png");
    	levelLoad = BitmapTextureAtlasTextureRegionFactory.createFromAsset(optionsTextureAtlas, activity, "levelLoader.png");
    	
    	BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/options_menu/");
    	ARSel = BitmapTextureAtlasTextureRegionFactory.createFromAsset(optionsTextureAtlas, activity, "ar.png");
    	optionsBack = BitmapTextureAtlasTextureRegionFactory.createFromAsset(optionsTextureAtlas, activity, "BlankPanel-1.png");
    	player1 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(optionsTextureAtlas, activity, "player1.png");
		player2 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(optionsTextureAtlas, activity, "player2.png");		        		
		player3 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(optionsTextureAtlas, activity, "player3.png");
		player4 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(optionsTextureAtlas, activity, "player4.png");
		
		level_1 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(optionsTextureAtlas, activity, "level_1.png");
		level_2 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(optionsTextureAtlas, activity, "level_2.png");
		level_3 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(optionsTextureAtlas, activity, "level_3.png");
		level_4 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(optionsTextureAtlas, activity, "level_4.png");
		level_5 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(optionsTextureAtlas, activity, "level_5.png");
		shaderOption = BitmapTextureAtlasTextureRegionFactory.createFromAsset(optionsTextureAtlas, activity, "shaders.png");
		particleOption = BitmapTextureAtlasTextureRegionFactory.createFromAsset(optionsTextureAtlas, activity, "particles.png");
		yes = BitmapTextureAtlasTextureRegionFactory.createFromAsset(optionsTextureAtlas, activity, "yes.png");
		not = BitmapTextureAtlasTextureRegionFactory.createFromAsset(optionsTextureAtlas, activity, "not.png");
    	
    	try
    	{
			this.optionsTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
			this.optionsTextureAtlas.load();
		} 
    	
    	catch (TextureAtlasBuilderException e)
    	{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }

    private void loadGameGraphics()
    {
    	
    	// --------------------------- BACKGROUND LOADING ---------------------------------------------
    	
    	BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/game/level1/");
        gameTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 2048, 2048, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        scene_background_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "level1_background.png");
        complete_window_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "levelCompleteWindow.png");
        complete_stars_region = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, "star.png", 2, 1);
      
        
     // --------------------------- OBJECTS LOADING ---------------------------------------------------------------------------------------
       
        // ------------------------- PLATFORMS --------------------------------------------------------------------------------------------
        platform2_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "plankPlatform.png");
        platform_castleLeft_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "castleCliffLeftAlt.png");
        platform_castleRight_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "castleCliffRightAlt.png");
        platform_castleHalf_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "castleHalf.png");
      
        // -------------------------------------- KEYS AND SPIKES -------------------------------------------------------------------------------------------------
        
        spike = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "spikes.png");
        keyY = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "keyYellow.png");
        keyG = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "keyGreen.png");
        snowHill = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "snowHill.png");
        
        // ------------------------------- COINS ---------------------------------------------------------------------------------------------
        coin_silver = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "coin_silver.png");
        coin_gold = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "coin_gold.png");
        coin_bronze = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "coin_bronze.png");
        keyHUD = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "hud_keyYellow.png");
        keyGreenHUD = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "hud_keyGreen.png");
        doorClosed = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "door_closedMid.png");
        doorClosedGreen = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "lock_green.png");
        doorF = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "doorFinal.png");
        hurtHUD = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "heart.png");
        mine = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "mine.png");
        
        // --------------------------------------------- PHYSIC SPRITE ---------------------------------------------------------------------
        waterDrop = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "waterDropTexture.png");
        mineExplosion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "explosion.png");
        stalactitaLeft = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "snowLedgeLeft.png");
        stalactitaRight = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "snowLedgeRight.png");
        
        // ------------------------------------ ENEMIES ----------------------------------------------------------------------------------------------------------
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/game/enemies/");
        flyEnemy = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, "fly_normal.png", 3, 1);
        
     // --------------------------- PLAYER SELECTION LOADING-------------------------------------------------------------------------------------------------------
        
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/game/Players/");
        if (playerSelected == 0) player_region = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, "player.png", 3, 1);
        if (playerSelected == 1) player_region = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, "player2.png", 3, 1);
        if (playerSelected == 2) player_region = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, "player3.png", 3, 1);
        if (playerSelected == 3) player_region = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, "player4.png", 3, 1);
        playerSecondary = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, "player5.png", 3, 1);
        if (playerSelected == 0) playerOnline_region = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, "player.png", 3, 1);
        if (playerSelected == 1) playerOnline_region = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, "player2.png", 3, 1);
        if (playerSelected == 2) playerOnline_region = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, "player3.png", 3, 1);
        if (playerSelected == 3) playerOnline_region = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, "player4.png", 3, 1);
      
        // --------------------------- SHADERS TEXTURES-------------------------------------------------------------------------------------------------------
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/Shading_Textures/");
        waterShader = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "water.png");
        try {
			waterDisplacement = new BitmapTexture(engine.getTextureManager(),new IInputStreamOpener() {
			    @Override
			    public InputStream open() throws IOException {
			        return activity.getAssets().open("gfx/Shading_Textures/waterdisplacement.png");
			    }
			});
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
       
        
        // --------------------------- LOAD TEXTURE ATLAS AND TEXTURE REGIONS -------------------------------------------------------------------------------------
        try 
        {
            this.gameTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
            this.gameTextureAtlas.load();
            waterDisplacement.load();
        } 
        catch (final TextureAtlasBuilderException e)
        {
            Debug.e(e);
        }        
    }
    
    private void loadGameGraphics_Level2()
    {
    	BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/game/level2/");
    	gameTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 3048, 2048, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        scene_background_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "level_2.png");
        
     // --------------------------- HUD LOADING-------------------------------------------------------------------------------------------------------
        
        starHUD = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "star.png");
        
        
     // --------------------------- PLATFORM/S LOADING-------------------------------------------------------------------------------------------------------
        greenPlatform = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "plat.png");
        block = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "block.png");
        tree = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "tree.png");
        bullet = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "bullet.png");
        buttonYellow = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "buttonYellow.png");
        
     // --------------------------- BOXES AND OTHERS LOADING-------------------------------------------------------------------------------------------------------
        bomb = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "bomb.png");
        boxExplosiveAlt = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "boxExplosiveAlt.png");
        boxItemAlt = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "boxItemAlt.png");
        boxWarning = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "boxWarning.png");
        cactus = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "cactus.png");
        arrowLeft = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "arrow_left.png");
        rock = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "rock.png");
        switcher = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, "switcher.png", 3, 1);
        springboarder = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, "springboarder.png", 3, 1);
        waterSynthetic = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, "water.png", 3, 1);
        waterDrop = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "waterDropTexture.png");
        sun = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "sun.png");
        sand = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "sand.png");
        rockHalfBigPlatform = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "rockHalfBigPlatform.png");
        
        
     // --------------------------- LEVEL 1 HUD LOADING-------------------------------------------------------------------------------------------------------
        
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/game/level1/");
        coin_silver = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "coin_silver.png");
        coin_gold = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "coin_gold.png");
        coin_bronze = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "coin_bronze.png");
        keyHUD = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "hud_keyYellow.png");
        keyGreenHUD = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "hud_keyGreen.png");
        doorF = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "doorFinal.png");
        complete_window_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "levelCompleteWindow.png");
        complete_stars_region = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, "star.png", 2, 1);
        mineExplosion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "explosion.png");
        
        // --------------------------- PLAYER SELECTION LOADING-------------------------------------------------------------------------------------------------------
        
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/game/Players/");
        if (playerSelected == 0) player_region = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, "player.png", 3, 1);
        if (playerSelected == 1) player_region = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, "player2.png", 3, 1);
        if (playerSelected == 2) player_region = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, "player3.png", 3, 1);
        if (playerSelected == 3) player_region = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, "player4.png", 3, 1);
        playerSecondary = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, "player5.png", 3, 1);
        if (controlLoaded == false) playerOnline_region = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, "player.png", 3, 1);
        if (playerSelected == 0) playerOnline_region = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, "player.png", 3, 1);
        if (playerSelected == 1) playerOnline_region = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, "player2.png", 3, 1);
        if (playerSelected == 2) playerOnline_region = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, "player3.png", 3, 1);
        if (playerSelected == 3) playerOnline_region = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, "player4.png", 3, 1);
            
        // --------------------------- LOAD TEXTURE ATLAS AND TEXTURE REGIONS -------------------------------------------------------------------------------------
        try 
        {
            this.gameTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
            this.gameTextureAtlas.load();
        } 
        catch (final TextureAtlasBuilderException e)
        {
            Debug.e(e);
        }        
    }
    
    private void loadGameGraphics_Level3()
    {
    	BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/game/level3/");
    	gameTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 2048, 1024, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        scene_background_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "level3_background.png");
        
        // ------------------------------ PLATFORMS TYPES --------------------------------------------------------------------------------------------------------
        
        cradle = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "cradle.png");
        littleMountain = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "littleMountain.png");
        mountainPlatform = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "mountainPlatform.png");
        platform = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "platform.png");
        platformSmall = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "platformSmall.png");
        bigTree = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "tree.png");
        troncPlatform = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "troncPlatform.png");
        troncSmall = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "troncSmall.png");
        bush = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "bush.png");
        key_blue = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "key_blue.png");
        key_red = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "key_red.png");
        switch_green_off = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "switch_green_off.png");
        waterSynthetic = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "waterSynthetic.png");
        mineExplosion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "explosion.png");
        rainFire = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "spark.png");
        grass = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "grass.png");
        grass_main = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "grass_main.png");
        tile_grass = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "tile_grass.png");
        
        keyHUD = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "hud_keyBlue.png");
        keyGreenHUD = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "hud_keyRed.png");
        rinoEnemy = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, "enemy.png", 4, 1);
        
     // --------------------------- HUD LOADING-------------------------------------------------------------------------------------------------------
 
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/game/level1/");
        starHUD = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "heart.png");
        coin_silver = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "coin_silver.png");
        coin_gold = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "coin_gold.png");
        coin_bronze = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "coin_bronze.png");
        complete_window_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "levelCompleteWindow.png");
        complete_stars_region = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, "star.png", 2, 1);
        
        // --------------------------- PLAYER SELECTION LOADING-------------------------------------------------------------------------------------------------------
        
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/game/Players/");
        if (playerSelected == 0) player_region = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, "player.png", 3, 1);
        if (playerSelected == 1) player_region = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, "player2.png", 3, 1);
        if (playerSelected == 2) player_region = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, "player3.png", 3, 1);
        if (playerSelected == 3) player_region = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, "player4.png", 3, 1);
        if (controlLoaded == false) playerOnline_region = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, "player.png", 3, 1);
        if (playerSelected == 0) playerOnline_region = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, "player.png", 3, 1);
        if (playerSelected == 1) playerOnline_region = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, "player2.png", 3, 1);
        if (playerSelected == 2) playerOnline_region = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, "player3.png", 3, 1);
        if (playerSelected == 3) playerOnline_region = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, "player4.png", 3, 1);
        
        // --------------------------- SHADERS TEXTURES-------------------------------------------------------------------------------------------------------
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/Shading_Textures/");
        waterShader = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "water4.png");
        waterDis = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "waterdisplacement.png");
        
     // --------------------------- LOAD TEXTURE ATLAS AND TEXTURE REGIONS -------------------------------------------------------------------------------------
        try 
        {
            this.gameTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
            this.gameTextureAtlas.load();
        } 
        catch (final TextureAtlasBuilderException e)
        {
            Debug.e(e);
        }        
    }
    
    private void loadGameGraphics_Level4()
    {
    	BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/game/level4/");
    	gameTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 2048, 1024, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        scene_background_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "background.png");
        
        // --------------------------------------- PLATFORMS LEVEL 4 -------------------------------------------------------------------------------------
        cradlePlatform1 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "cradlePlatform1.png");
        cradlePlatform2 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "cradlePlatform2.png");
        cradlePlatform3 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "cradlePlatform3.png");
        cradlePlatform4 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "cradlePlatform4.png");
        cradlePlatform5 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "cradlePlatform5.png");
        expulsor = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "expulsor.png");
        mineral = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "mineral.png");
        mineral2 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "mineral2.png");
        mineral3 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "mineral3.png");
        mushroomTree = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "mushroomTree.png");
        expulsorParticle = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "expulsorParticle.png");
        bomb = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "bomb.png");
        bombHUD = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "bombHUD.png");
        mineExplosion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "explosion.png");
        enemy1 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "enemy1.png");
        enemy2 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "enemy2.png");
        cradle = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "cradle.png");
        
        
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/game/level1/");
        complete_window_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "levelCompleteWindow.png");
        complete_stars_region = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, "star.png", 2, 1);
        coin_silver = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "coin_silver.png");
        coin_gold = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "coin_gold.png");
        coin_bronze = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "coin_bronze.png");
        hurtHUD = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "heart.png");
        
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/game/level2/");
        switcher = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, "switcher.png", 3, 1);
        
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/game/level3/");
        key_blue = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "key_blue.png");
        keyHUD = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "hud_keyBlue.png");
        
        // --------------------------- PLAYER SELECTION LOADING-------------------------------------------------------------------------------------------------------
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/game/Players/");
        if (playerSelected == 0) player_region = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, "player.png", 3, 1);
        if (playerSelected == 1) player_region = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, "player2.png", 3, 1);
        if (playerSelected == 2) player_region = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, "player3.png", 3, 1);
        if (playerSelected == 3) player_region = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, "player4.png", 3, 1);
        if (controlLoaded == false) playerOnline_region = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, "player.png", 3, 1);
        if (playerSelected == 0) playerOnline_region = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, "player.png", 3, 1);
        if (playerSelected == 1) playerOnline_region = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, "player2.png", 3, 1);
        if (playerSelected == 2) playerOnline_region = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, "player3.png", 3, 1);
        if (playerSelected == 3) playerOnline_region = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, "player4.png", 3, 1);
        
        try 
        {
            this.gameTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
            this.gameTextureAtlas.load();
        } 
        catch (final TextureAtlasBuilderException e)
        {
            Debug.e(e);
        }      
    }
    
    private void loadGameGraphics_Level5()
    {
    	BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/game/level5/");
    	gameTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 3048, 1024, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        scene_background_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "background.png");
        
        // ---------------------------------------- COMPLETE WINDOW ---------------------------------------------------------------------------------
        complete_window_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "levelCompleteWindow.png");
        complete_stars_region = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, "star.png", 2, 1);
        
        // ------------------------------------------- PLATFORMS && TILES ---------------------------------------------------------------------------
        groundDirt = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "groundDirt.png");
        groundGrass = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "groundGrass.png");
        groundIce = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "groundIce.png");
        groundRock = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "groundRock.png");
        groundSnow = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "groundSnow.png");
        medalBronze = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "medalBronze.png");
        medalGold = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "medalGold.png");
        medalSilver = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "medalSilver.png");
        rockGrass = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "rockGrass.png");
        rockGrassDown = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "rockGrassDown.png");
        rockSnow = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "rockSnow.png");
        rockSnowDown = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "rockSnowDown.png");
        starBronze = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "starBronze.png");
        starGold = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "starGold.png");
        starSilver = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "starSilver.png");
        hurtHUD = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "heart.png");
        planeBlue1 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "planeBlue1.png");
        planeRed1 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "planeRed1.png");
        planeGreen1 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "planeGreen1.png");
        planeYellow1 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "planeYellow1.png");
        rockIce = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "rockIce.png");
        
        // --------------------------- PLAYER SELECTION LOADING-------------------------------------------------------------------------------------------------------
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/game/Players/");
        if (playerSelected == 0) player_region = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, "player.png", 3, 1);
        if (playerSelected == 1) player_region = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, "player2.png", 3, 1);
        if (playerSelected == 2) player_region = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, "player3.png", 3, 1);
        if (playerSelected == 3) player_region = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, "player4.png", 3, 1);
        if (controlLoaded == false) playerOnline_region = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, "player.png", 3, 1);
        if (playerSelected == 0) playerOnline_region = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, "player.png", 3, 1);
        if (playerSelected == 1) playerOnline_region = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, "player2.png", 3, 1);
        if (playerSelected == 2) playerOnline_region = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, "player3.png", 3, 1);
        if (playerSelected == 3) playerOnline_region = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, "player4.png", 3, 1);
        
        try 
        {
            this.gameTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
            this.gameTextureAtlas.load();
        } 
        catch (final TextureAtlasBuilderException e)
        {
            Debug.e(e);
        }      
    }
    
    
    // ------------------------------------- LOAD GAME FONTS ------------------------------------------------------------------
    public void loadMenuFonts()
    {
        this.loadToonFont();
    }
    
	public void loadGameFonts()
	{
		if (this.getLevelComplete() == 1)
		{
			this.loadBodieMFHollyFont();
		}
		  
		else if (this.getLevelComplete() == 2)
		{
			 this.loadBurnstowFont();
		}
		
		else if (this.getLevelComplete() == 3)
		{
			this.loadWalkDaWalkOneFont();
		}
		  
		else if (this.getLevelComplete() == 4)
		{
			this.loadBlackbookTwoFont();
		}
		
		else if (this.getLevelComplete() == 5)
		{
			this.loadKenFont();
		}
	}
    
    private void loadToonFont()
    {
    	FontFactory.setAssetBasePath("font/");
        final ITexture mainFontTexture = new BitmapTextureAtlas(activity.getTextureManager(), 1024, 1024, TextureOptions.BILINEAR_PREMULTIPLYALPHA);

        font = FontFactory.createStrokeFromAsset(activity.getFontManager(), mainFontTexture, activity.getAssets(), "2toonv2s.ttf", 120, true, Color.RED, 4, Color.DKGRAY);
        font.load(); 
    }
    
    public void loadBodieMFHollyFont()
    {
    	FontFactory.setAssetBasePath("font/");
        final ITexture mainFontTexture = new BitmapTextureAtlas(activity.getTextureManager(), 1024, 1024, TextureOptions.BILINEAR_PREMULTIPLYALPHA);

        font = FontFactory.createStrokeFromAsset(activity.getFontManager(), mainFontTexture, activity.getAssets(), "Snowtop Caps.ttf", 60, true, Color.GRAY, 3, Color.LTGRAY);
        font.load(); 
        
        loadingFont = FontFactory.createStrokeFromAsset(activity.getFontManager(), mainFontTexture, activity.getAssets(), "Snowtop Caps.ttf", 100, true, Color.GRAY, 3, Color.LTGRAY);
        loadingFont.load(); 
    }
    
    public void loadBurnstowFont()
    {
    	FontFactory.setAssetBasePath("font/");
        final ITexture mainFontTexture = new BitmapTextureAtlas(activity.getTextureManager(), 1024, 1024, TextureOptions.BILINEAR_PREMULTIPLYALPHA);

        font = FontFactory.createStrokeFromAsset(activity.getFontManager(), mainFontTexture, activity.getAssets(), "BURNSTOW.ttf", 100, true, Color.DKGRAY, 3, Color.BLACK);
        font.load(); 
        
        loadingFont = FontFactory.createStrokeFromAsset(activity.getFontManager(), mainFontTexture, activity.getAssets(), "BURNSTOW.ttf", 125, true, Color.GRAY, 3, Color.LTGRAY);
        loadingFont.load(); 
    }
    
    private void loadWalkDaWalkOneFont()
    {
    	FontFactory.setAssetBasePath("font/");
        final ITexture mainFontTexture = new BitmapTextureAtlas(activity.getTextureManager(), 1024, 1024, TextureOptions.BILINEAR_PREMULTIPLYALPHA);

        font = FontFactory.createStrokeFromAsset(activity.getFontManager(), mainFontTexture, activity.getAssets(), "orange juice 2.0.ttf", 75, true, Color.LTGRAY, 1, Color.GREEN);
        font.load(); 
        
        loadingFont = FontFactory.createStrokeFromAsset(activity.getFontManager(), mainFontTexture, activity.getAssets(), "orange juice 2.0.ttf", 100, true, Color.LTGRAY , 1, Color.GREEN);
        loadingFont.load(); 
    }
    
    private void loadBlackbookTwoFont()
    {
    	FontFactory.setAssetBasePath("font/");
        final ITexture mainFontTexture = new BitmapTextureAtlas(activity.getTextureManager(), 1024, 1024, TextureOptions.BILINEAR_PREMULTIPLYALPHA);

        font = FontFactory.createStrokeFromAsset(activity.getFontManager(), mainFontTexture, activity.getAssets(), "Ming in Bling.ttf", 75, true, Color.LTGRAY, 2, Color.WHITE);
        font.load(); 
        
        loadingFont = FontFactory.createStrokeFromAsset(activity.getFontManager(), mainFontTexture, activity.getAssets(), "Ming in Bling.ttf", 100, true, Color.LTGRAY , 3, Color.DKGRAY);
        loadingFont.load(); 
    }
    
    private void loadKenFont()
    {
    	FontFactory.setAssetBasePath("font/");
        final ITexture mainFontTexture = new BitmapTextureAtlas(activity.getTextureManager(), 1024, 1024, TextureOptions.BILINEAR_PREMULTIPLYALPHA);

        font = FontFactory.createStrokeFromAsset(activity.getFontManager(), mainFontTexture, activity.getAssets(), "kenvector_future.ttf", 60, true, Color.BLUE, 2, Color.WHITE);
        font.load(); 
        
        loadingFont = FontFactory.createStrokeFromAsset(activity.getFontManager(), mainFontTexture, activity.getAssets(), "kenvector_future.ttf", 85, true, Color.LTGRAY , 3, Color.DKGRAY);
        loadingFont.load(); 
    }
    // ------------------------------------- LOAD AUDIO (SOUND && MUSIC) ------------------------------------------------------------------
    private void loadMenuAudio()
    { 
    }
   
    private void loadGameAudio()
    {
    	SoundFactory.setAssetBasePath("gfx/sounds/");
    	MusicFactory.setAssetBasePath("gfx/sounds/");
    	
    	if (this.level == 1)
    	{
    		
    		// LOAD COIN COLLISION SOUND EFFECT
        	try 
        	{    
        		this.coin = SoundFactory.createSoundFromAsset(engine.getSoundManager(), activity.getApplicationContext(), "moneda.ogg");
        		//this.coin.setLoaded(true);
        	} 
        	catch (IllegalStateException e) 
        	{   
        		e.printStackTrace();
        	} 
        	catch (IOException e) 
        	{   
        		e.printStackTrace();
        	}
        	
        	// LOAD MINE EXPLOSION SOUND EFFECT
        	try 
        	{    
        		this.explosion = SoundFactory.createSoundFromAsset(engine.getSoundManager(), activity.getApplicationContext(), "explosion.ogg");
        		//this.explosion.setLoaded(true);
        	} 
        	catch (IllegalStateException e) 
        	{   
        		e.printStackTrace();
        	} 
        	catch (IOException e) 
        	{   
        		e.printStackTrace();
        	}
        	
        	// LOAD SPIKE COLISION SOUND EFFECT
        	try 
        	{    
        		this.spikeS = SoundFactory.createSoundFromAsset(engine.getSoundManager(), activity.getApplicationContext(), "spike.ogg");
        		//this.explosion.setLoaded(true);
        	} 
        	catch (IllegalStateException e) 
        	{   
        		e.printStackTrace();
        	} 
        	catch (IOException e) 
        	{   
        		e.printStackTrace();
        	}
        	
        	// LOAD GAME SCENE BACKGROUND MUSIC
        	try 
        	{
				this.backgroundMusic = MusicFactory.createMusicFromAsset(engine.getMusicManager(), activity.getApplicationContext(),"POL-rocket-station-short.ogg");
			} 
        	catch (IllegalStateException e) 
        	{
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
        	catch (IOException e) 
        	{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	    this.backgroundMusic.setVolume(0.075f, 0.075f);
    	    this.backgroundMusic.setLooping(true);
    	    
    	    // LOAD FLY ATTACK 
        	try
        	{
        	    this.flySound = SoundFactory.createSoundFromAsset(engine.getSoundManager(), activity.getApplicationContext(),"flyAttack.ogg");
        	}
        	catch (IOException e)
        	{
        	    e.printStackTrace();
        	}
        	
        	// LOAD OPEN KEYS
        	try
        	{
        	    this.openK = SoundFactory.createSoundFromAsset(engine.getSoundManager(), activity.getApplicationContext(),"openKey.ogg");
        	}
        	catch (IOException e)
        	{
        	    e.printStackTrace();
        	}
        	
        	// LOAD JUMP SOUND
        	try 
        	{    
        		this.jump = SoundFactory.createSoundFromAsset(engine.getSoundManager(), activity.getApplicationContext(), "jump.ogg");
        		//this.explosion.setLoaded(true);
        	} 
        	catch (IllegalStateException e) 
        	{   
        		e.printStackTrace();
        	} 
        	catch (IOException e) 
        	{   
        		e.printStackTrace();
        	}
    		
    	}
    	else if (this.level == 2)
    	{
    		
    		// LOAD COIN COLLISION SOUND EFFECT
        	try 
        	{    
        		this.coin = SoundFactory.createSoundFromAsset(engine.getSoundManager(), activity.getApplicationContext(), "moneda.ogg");
        		//this.coin.setLoaded(true);
        	} 
        	catch (IllegalStateException e) 
        	{   
        		e.printStackTrace();
        	} 
        	catch (IOException e) 
        	{   
        		e.printStackTrace();
        	}
        	
        	// LOAD MINE EXPLOSION SOUND EFFECT
        	try 
        	{    
        		this.explosion = SoundFactory.createSoundFromAsset(engine.getSoundManager(), activity.getApplicationContext(), "explosion.ogg");
        		//this.explosion.setLoaded(true);
        	} 
        	catch (IllegalStateException e) 
        	{   
        		e.printStackTrace();
        	} 
        	catch (IOException e) 
        	{   
        		e.printStackTrace();
        	}
        	
        	// LOAD GAME SCENE BACKGROUND MUSIC
        	try 
        	{
				this.backgroundMusic = MusicFactory.createMusicFromAsset(engine.getMusicManager(), activity.getApplicationContext(),"POL-treasure-match-short.ogg");
			} 
        	catch (IllegalStateException e) 
        	{
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
        	catch (IOException e) 
        	{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	    this.backgroundMusic.setVolume(0.075f, 0.075f);
    	    this.backgroundMusic.setLooping(true);
    	    
    	    // LOAD MACHINE GUN SOUND
        	try 
        	{    
        		this.machine = SoundFactory.createSoundFromAsset(engine.getSoundManager(), activity.getApplicationContext(), "machineGun.ogg");
        		this.machine.setLooping(true);
        		this.machine.setVolume(0.1f, 0.05f);
        	} 
        	catch (IllegalStateException e) 
        	{   
        		e.printStackTrace();
        	} 
        	catch (IOException e) 
        	{   
        		e.printStackTrace();
        	}
        	
        	// LOAD JUMP SOUND
        	try 
        	{    
        		this.jump = SoundFactory.createSoundFromAsset(engine.getSoundManager(), activity.getApplicationContext(), "jump.ogg");
        		//this.explosion.setLoaded(true);
        	} 
        	catch (IllegalStateException e) 
        	{   
        		e.printStackTrace();
        	} 
        	catch (IOException e) 
        	{   
        		e.printStackTrace();
        	}
    		
    	}
    	else if (this.level == 3)
    	{
    		// LOAD GAME SCENE BACKGROUND MUSIC
        	try 
        	{
				this.backgroundMusic = MusicFactory.createMusicFromAsset(engine.getMusicManager(), activity.getApplicationContext(),"jungle.ogg");
			} 
        	catch (IllegalStateException e) 
        	{
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
        	catch (IOException e) 
        	{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	    this.backgroundMusic.setVolume(0.075f, 0.075f);
    	    this.backgroundMusic.setLooping(true);
    	    
    	    
    		// LOAD COIN COLLISION SOUND EFFECT
        	try 
        	{    
        		this.coin = SoundFactory.createSoundFromAsset(engine.getSoundManager(), activity.getApplicationContext(), "moneda.ogg");
        		//this.coin.setLoaded(true);
        	} 
        	catch (IllegalStateException e) 
        	{   
        		e.printStackTrace();
        	} 
        	catch (IOException e) 
        	{   
        		e.printStackTrace();
        	}
        	
        	// LOAD GORILLA SOUND
        	try 
        	{    
        		this.gorilla = SoundFactory.createSoundFromAsset(engine.getSoundManager(), activity.getApplicationContext(), "Strong_Punch-Mike.ogg");
        		//this.explosion.setLoaded(true);
        	} 
        	catch (IllegalStateException e) 
        	{   
        		e.printStackTrace();
        	} 
        	catch (IOException e) 
        	{   
        		e.printStackTrace();
        	}
        	
        	// LOAD MINE EXPLOSION SOUND EFFECT
        	try 
        	{    
        		this.explosion = SoundFactory.createSoundFromAsset(engine.getSoundManager(), activity.getApplicationContext(), "explosion.ogg");
        		//this.explosion.setLoaded(true);
        	} 
        	catch (IllegalStateException e) 
        	{   
        		e.printStackTrace();
        	} 
        	catch (IOException e) 
        	{   
        		e.printStackTrace();
        	}
        	
        	// LOAD JUMP SOUND
        	try 
        	{    
        		this.jump = SoundFactory.createSoundFromAsset(engine.getSoundManager(), activity.getApplicationContext(), "jump.ogg");
        		//this.explosion.setLoaded(true);
        	} 
        	catch (IllegalStateException e) 
        	{   
        		e.printStackTrace();
        	} 
        	catch (IOException e) 
        	{   
        		e.printStackTrace();
        	}
    	} 
    	
    	else if (this.level == 4)
    	{
    		
    		// LOAD GAME SCENE BACKGROUND MUSIC
        	try 
        	{
				this.backgroundMusic = MusicFactory.createMusicFromAsset(engine.getMusicManager(), activity.getApplicationContext(),"POL-boring-cavern-short.ogg");
			} 
        	catch (IllegalStateException e) 
        	{
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
        	catch (IOException e) 
        	{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	    this.backgroundMusic.setVolume(0.25f, 0.25f);
    	    this.backgroundMusic.setLooping(true);
    	    
    	 // LOAD MINE EXPLOSION SOUND EFFECT
        	try 
        	{    
        		this.explosion = SoundFactory.createSoundFromAsset(engine.getSoundManager(), activity.getApplicationContext(), "explosion.ogg");
        		//this.explosion.setLoaded(true);
        	} 
        	catch (IllegalStateException e) 
        	{   
        		e.printStackTrace();
        	} 
        	catch (IOException e) 
        	{   
        		e.printStackTrace();
        	}
    	    
    		// LOAD JUMP SOUND
        	try 
        	{    
        		this.jump = SoundFactory.createSoundFromAsset(engine.getSoundManager(), activity.getApplicationContext(), "jump.ogg");
        		//this.explosion.setLoaded(true);
        	} 
        	catch (IllegalStateException e) 
        	{   
        		e.printStackTrace();
        	} 
        	catch (IOException e) 
        	{   
        		e.printStackTrace();
        	}
        	
        	// LOAD COIN COLLISION SOUND EFFECT
        	try 
        	{    
        		this.coin = SoundFactory.createSoundFromAsset(engine.getSoundManager(), activity.getApplicationContext(), "moneda.ogg");
        		//this.coin.setLoaded(true);
        	} 
        	catch (IllegalStateException e) 
        	{   
        		e.printStackTrace();
        	} 
        	catch (IOException e) 
        	{   
        		e.printStackTrace();
        	}
        	
        	// LOAD SPIKE COLISION SOUND EFFECT
        	try 
        	{    
        		this.spikeS = SoundFactory.createSoundFromAsset(engine.getSoundManager(), activity.getApplicationContext(), "spike.ogg");
        		//this.explosion.setLoaded(true);
        	} 
        	catch (IllegalStateException e) 
        	{   
        		e.printStackTrace();
        	} 
        	catch (IOException e) 
        	{   
        		e.printStackTrace();
        	}
        	
        	// LOAD FLY ATTACK 
        	try
        	{
        	    this.flySound = SoundFactory.createSoundFromAsset(engine.getSoundManager(), activity.getApplicationContext(),"flyAttack.ogg");
        	}
        	catch (IOException e)
        	{
        	    e.printStackTrace();
        	}
    	}
    	
    	else if (this.level == 5)
    	{
    		// LOAD JUMP SOUND
        	try 
        	{    
        		this.jump = SoundFactory.createSoundFromAsset(engine.getSoundManager(), activity.getApplicationContext(), "jump.ogg");
        		//this.explosion.setLoaded(true);
        	} 
        	catch (IllegalStateException e) 
        	{   
        		e.printStackTrace();
        	} 
        	catch (IOException e) 
        	{   
        		e.printStackTrace();
        	}
        	
        	// LOAD COIN COLLISION SOUND EFFECT
        	try 
        	{    
        		this.coin = SoundFactory.createSoundFromAsset(engine.getSoundManager(), activity.getApplicationContext(), "moneda.ogg");
        		//this.coin.setLoaded(true);
        	} 
        	catch (IllegalStateException e) 
        	{   
        		e.printStackTrace();
        	} 
        	catch (IOException e) 
        	{   
        		e.printStackTrace();
        	}
    	}
    }
    
    
    // ------------------------------------- UNLOAD RESOURCES ------------------------------------------------------------------
    public void unloadSplashScreen()
    {
    	
    	splashTextureAtlas.unload();
    	splash_region = null;

    }
    
    public void unloadOptionsScreen()
    {
    	optionsTextureAtlas.unload();
    }
    
    public void unloadMenuTextures()
    {
        menuTextureAtlas.unload();
    }
        
    public void loadMenuTextures()
    {
        menuTextureAtlas.load();
    }
    
    public void loadOptionsTextures()
    {
    	optionsTextureAtlas.load();
    }
    
    
    public void unloadGameTextures(int levelID)
    {
    	if (levelID == 1)
    	{
    		gameTextureAtlas.unload();
        	gameTextureAtlas.clearTextureAtlasSources();
        	font.unload();
        	loadingFont.unload();
        	waterDisplacement.unload();
        	player_region = null;
        	platform1_region = null;
        	platform2_region = null;
        	platform3_region = null;
        	coin_silver = null;
        	coin_bronze = null;
        	coin_gold = null;
        	platform_castleLeft_region = null;
        	platform_castleRight_region = null;
        	platform_castleHalf_region = null;
        	platform_dirtLeft_region = null;
        	platform_dirtRight_region = null;
        	platform_dirtHalf_region = null;
        	spike = null;
        	keyY = null;
        	doorClosed = null;
        	keyHUD = null;
        	hurtHUD = null;
        	waterDrop = null;
        	waterShader = null;
    	}
    	
    	else if (levelID == 2)
    	{
    		gameTextureAtlas.unload();
        	gameTextureAtlas.clearTextureAtlasSources();
        	font.unload();
        	loadingFont.unload();
        	player_region = null;
        	playerSecondary = null;
        	coin_silver = null;
        	coin_bronze = null;
        	coin_gold = null;
	    	arrowLeft = null;
	        rock = null;
	        sand = null;
	        rockHalfBigPlatform = null;
	        bomb = null;
	        boxExplosiveAlt = null;
	        boxItemAlt = null;
	        boxWarning = null;
	        cactus = null;
	        block = null;
	        greenPlatform = null;
	        rockHalfBigPlatform = null;
	        sun = null;
	        tree = null;
	        waterSynthetic = null;
	        starHUD = null;
    	}
    	
    	else if (levelID == 3)
    	{
    		gameTextureAtlas.unload();
        	gameTextureAtlas.clearTextureAtlasSources();
        	font.unload();
        	loadingFont.unload();
        	waterSynthetic = null;
        	grass = null;
        	grass_main = null;
        	tile_grass = null;
        	cradle = null;
        	mountainPlatform = null;
        	bigTree = null;
        	platform = null;
        	platformSmall = null;
        	troncPlatform = null;
        	troncSmall = null;
        	bush = null;
        	key_blue = null;
        	key_red = null;
        	switch_green_off = null;
        	coin_silver = null;
        	coin_bronze = null;
        	coin_gold = null;
        	starHUD = null;
    	}
    	
    	else if (levelID == 4)
    	{
    		gameTextureAtlas.unload();
        	gameTextureAtlas.clearTextureAtlasSources();
        	font.unload();
        	loadingFont.unload();
        	coin_silver = null;
        	coin_bronze = null;
        	coin_gold = null;
        	cradlePlatform1 = null;
            cradlePlatform2 = null;
            cradlePlatform3 = null;
            cradlePlatform4 = null;
            cradlePlatform5 = null;
            expulsor = null;
            mineral = null;
            mineral2 = null;
            mineral3 = null;
            mushroomTree = null;
            expulsorParticle = null;
            bomb = null;
            bombHUD = null;
            mineExplosion = null;
            complete_window_region = null;
            complete_stars_region = null;
            hurtHUD = null;
            switcher = null;
            key_blue = null;
            keyHUD = null;
    	}
    	
    }
    
    public void unloadGameSounds()
    {
    	if (this.level == 1)
    	{
    		this.backgroundMusic.stop();
        	this.coin.stop();
        	this.explosion.stop();
        	this.flySound.stop();
        	this.openK.stop();
        	this.spikeS.stop();
    	}
    	
    	else if (this.level == 2)
    	{
    		this.backgroundMusic.stop();
        	this.coin.stop();
        	this.explosion.stop();
    	}
    	
    	else if (this.level == 3)
    	{
    		this.backgroundMusic.stop();
    	}
    	
    	else if (this.level == 4)
    	{
    		this.backgroundMusic.stop();
    	}
    }

    
    /**
     * @param engine
     * @param activity
     * @param camera
     * @param vbom
     * <br><br>
     * We use this method at beginning of game loading, to prepare Resources Manager properly,
     * setting all needed parameters, so we can latter access them from different classes (eg. scenes)
     */
    public static void prepareManager(Engine engine, streetJumper activity, BoundCamera camera, VertexBufferObjectManager vbom)
    {
        getInstance().engine = engine;
        getInstance().activity = activity;
        getInstance().camera = camera;
        getInstance().vbom = vbom;
    }
    
    //---------------------------------------------
    // GETTERS AND SETTERS
    //---------------------------------------------
    
    public static ResourcesManager getInstance()
    {
        return INSTANCE;
    }
    
    public Sound getCoinSound()
    {
    	return (this.coin);
    }
    
    public Sound getExplosionSound()
    {
    	return (this.explosion);
    }
    
    public Sound getSpikeSound()
    {
    	return (this.spikeS);
    }
    
    public Music getSceneMusic()
    {
    	return (this.backgroundMusic);
    }
    
    public boolean getAR()
    {
    	return (this.AR);
    }
    
    public void setAR (boolean selection)
    {
    	this.AR = selection;
    }
    
    public Sound getFlySound()
    {
    	return (this.flySound);
    }
    
    public Sound getKeySound()
    {
    	return (this.openK);
    }
    
	public int getLevelComplete()
	{
		return (this.level);
	}
	
	public void setLevelComplete(int complete)
	{
		this.level = complete;
	}
	
	public Sound getMachineSound()
	{
		return (this.machine);
	}
	
	public Sound getJumpSound()
	{
		return (this.jump);
	}
	
	public Sound getGorillaSound()
	{
		return (this.gorilla);
	}
}
