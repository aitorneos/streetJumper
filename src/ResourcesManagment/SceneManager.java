package ResourcesManagment;

import org.andengine.engine.Engine;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.ui.IGameInterface.OnCreateSceneCallback;

import Scene.BaseScene;
import Scene.GameScene;
import Scene.LoadingScene;
import Scene.MainMenuScene;
import Scene.OptionsScene;
import Scene.SplashScene;

/**
 * @author Aitor Arque
 * @version 1.0
 */
public class SceneManager
{
    //---------------------------------------------
    // SCENES
    //---------------------------------------------
    
    private BaseScene splashScene;
    private BaseScene menuScene;
    private BaseScene gameScene = null;
    private BaseScene loadingScene;
    private BaseScene optionsScene;
    
    //---------------------------------------------
    // VARIABLES
    //---------------------------------------------
    
    private static final SceneManager INSTANCE = new SceneManager();
    
    private SceneType currentSceneType = SceneType.SCENE_SPLASH;
    
    private BaseScene currentScene;
    
    private Engine engine = ResourcesManager.getInstance().engine;
    
    public enum SceneType
    {
        SCENE_SPLASH,
        SCENE_MENU,
        SCENE_OPTIONS,
        SCENE_GAME,
        SCENE_LOADING,
    }
    
    //---------------------------------------------
    // CLASS LOGIC
    //---------------------------------------------
    
    public void setScene(BaseScene scene)
    {
        engine.setScene(scene);
        currentScene = scene;
        currentSceneType = scene.getSceneType();
    }
    
    public void setScene(SceneType sceneType)
    {
        switch (sceneType)
        {
            case SCENE_MENU:
            	
                setScene(menuScene);
                break;
                
            case SCENE_GAME:
            	
                setScene(gameScene);
                break;
                
            case SCENE_SPLASH:
            	
                setScene(splashScene);
                break;
                
            case SCENE_LOADING:
            	
                setScene(loadingScene);
                break;
                
            case SCENE_OPTIONS:
            	
            	setScene(optionsScene);
            	break;
                
            default:
                break;
        }
    }
    
    public void createSplashScene(OnCreateSceneCallback pOnCreateSceneCallback)
    {
        ResourcesManager.getInstance().loadSplashScreen();
        splashScene = new SplashScene();
        currentScene = splashScene;
        pOnCreateSceneCallback.onCreateSceneFinished(splashScene);
    }
    
    private void disposeSplashScene()
    {
        ResourcesManager.getInstance().unloadSplashScreen();
        splashScene.disposeScene(1);
        splashScene = null;
    }
    
    private void disposeOptionsScene()
    {
    	ResourcesManager.getInstance().unloadOptionsScreen();
    	optionsScene.disposeScene(1);
    	optionsScene = null;
    }
    
    public void createMenuScene()
    {
    	ResourcesManager.getInstance().unloadSplashScreen();
    	ResourcesManager.getInstance().loadMenuResources();
        menuScene = new MainMenuScene();
        loadingScene = new LoadingScene();
        currentScene = menuScene;
        SceneManager.getInstance().setScene(menuScene);
        disposeSplashScene();
    }
    
    public void createOptionsScene()
    {
    	ResourcesManager.getInstance().unloadMenuTextures();
    	ResourcesManager.getInstance().loadOptionsGraphics();
    	optionsScene = new OptionsScene();
    	currentScene = optionsScene;
    	SceneManager.getInstance().setScene(optionsScene);
    	
    }
    
    
    public void loadGameScene(final Engine mEngine, final int reourcesLevel)
    {
    	gameScene = null;
    	ResourcesManager.getInstance().loading = true;
    	loadingScene.disposeScene(1);
    	loadingScene = new LoadingScene();
    	loadingScene.createScene();
        setScene(loadingScene);
        if (reourcesLevel == 1) ResourcesManager.getInstance().unloadMenuTextures();
        mEngine.registerUpdateHandler(new TimerHandler(0.15f, new ITimerCallback() 
        {
            public void onTimePassed(final TimerHandler pTimerHandler) 
            {
                mEngine.unregisterUpdateHandler(pTimerHandler);
                ResourcesManager.getInstance().loadGameResources(reourcesLevel);
                gameScene = new GameScene();
                setScene(gameScene);
                ResourcesManager.getInstance().loading = false;

            }
        }));
    }
    

public void loadMenuScene(final Engine mEngine)
{
    setScene(loadingScene);
    currentScene = menuScene;
    mEngine.registerUpdateHandler(new TimerHandler(1.0f, new ITimerCallback() 
    {
        public void onTimePassed(final TimerHandler pTimerHandler) 
        {
            mEngine.unregisterUpdateHandler(pTimerHandler);
            ResourcesManager.getInstance().loadMenuTextures();
            setScene(menuScene);
        }
    }));
}

public void loadOptionsMenu(final Engine mEngine)
{
	setScene(loadingScene);
	menuScene.disposeScene(1);
	mEngine.registerUpdateHandler(new TimerHandler(0.1f, new ITimerCallback() 
	{

		@Override
		public void onTimePassed(TimerHandler pTimerHandler)
		{
			mEngine.unregisterUpdateHandler(pTimerHandler);
            ResourcesManager.getInstance().loadOptionsGraphics();
            ResourcesManager.getInstance().loadOptionsTextures();
            createOptionsScene();
			
		}
		
	}));
}

public GameScene getGameScene ()
{
	return (GameScene) (gameScene);
}

public void setGameScene ()
{
	gameScene = null;
}
    
    //---------------------------------------------
    // GETTERS AND SETTERS
    //---------------------------------------------
    
    public static SceneManager getInstance()
    {
        return INSTANCE;
    }
    
    public SceneType getCurrentSceneType()
    {
        return currentSceneType;
    }
    
    public BaseScene getCurrentScene()
    {
        return currentScene;
    }
}
