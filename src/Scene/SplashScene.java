package Scene;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.util.GLState;

import ResourcesManagment.ResourcesManager;
import ResourcesManagment.SceneManager.SceneType;
import Scene.BaseScene;

/**
 * @author Aitor Arque
 * @version 1.0
 */
public class SplashScene extends BaseScene
{
	
	private Sprite splash;
	
    @Override
    public void createScene()
    {
    	
    	splash = new Sprite(0, 0, resourcesManager.splash_region, vbom)
    	{
    	    @Override
    	    protected void preDraw(GLState pGLState, Camera pCamera) 
    	    {
    	       super.preDraw(pGLState, pCamera);
    	       pGLState.enableDither();
    	    }
    	};
    	        
    	splash.setScale(1.5f);
    	splash.setPosition(400, 230);
    	this.attachChild(splash);
    }

    @Override
    public void onBackKeyPressed()
    {

    }

    @Override
    public SceneType getSceneType()
    {
    	return SceneType.SCENE_SPLASH;
    }

    @Override
    public void disposeScene(int levelID)
    {
    	splash.detachSelf();
	    splash.dispose();
	    this.detachSelf();
	    this.dispose();
    }
}
