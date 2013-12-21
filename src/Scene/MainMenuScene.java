package Scene;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.SpriteMenuItem;
import org.andengine.entity.scene.menu.item.decorator.ScaleMenuItemDecorator;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.util.GLState;

import ResourcesManagment.SceneManager;
import ResourcesManagment.SceneManager.SceneType;

/**
 * @author Aitor Arque
 * @version 1.0
 */

public class MainMenuScene extends BaseScene implements IOnMenuItemClickListener
{

	private MenuScene menuChildScene;
	private final int MENU_PLAY = 0;
	private final int MENU_OPTIONS = 1;
	private final int MENU_LOAD = 2;
	
	 @Override
	    public SceneType getSceneType()
	    {
		 
		 	return SceneType.SCENE_MENU;	    
		
	    }
	 
	 @Override
	 public void onBackKeyPressed()
	 {
	     System.exit(0);
	 }
	 
	 private void createBackground()
	 {
	     attachChild(new Sprite(400, 240, resourcesManager.menu_background_region, vbom)
	     {
	         @Override
	         protected void preDraw(GLState pGLState, Camera pCamera) 
	         {
	             super.preDraw(pGLState, pCamera);
	             pGLState.enableDither();
	         }
	     });
	 }
	 
	 @Override
	 public void createScene()
	 {
		 //ResourcesManager.getInstance().camera.set(0, 0, 800, 480);
	     createBackground();
	     createMenuChildScene();
	 }
	 
	 private void createMenuChildScene()
	 {
	     menuChildScene = new MenuScene(camera);
	     menuChildScene.setPosition(-250, 200);
	     
	     final IMenuItem playMenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(MENU_PLAY, resourcesManager.play_region, vbom), 1.2f, 1);
	     final IMenuItem optionsMenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(MENU_OPTIONS, resourcesManager.options_region, vbom), 1.2f, 1);
	     final IMenuItem loadMenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(MENU_LOAD, resourcesManager.load_region, vbom), 1.2f, 1);
	     
	     menuChildScene.addMenuItem(playMenuItem);
	     menuChildScene.addMenuItem(optionsMenuItem);
	     menuChildScene.addMenuItem(loadMenuItem);
	     
	     menuChildScene.buildAnimations();
	     menuChildScene.setBackgroundEnabled(false);
	     
	     playMenuItem.setPosition(playMenuItem.getX() + 255 , playMenuItem.getY() - 165);
	     optionsMenuItem.setPosition(optionsMenuItem.getX() + 255, optionsMenuItem.getY() - 167);
	     loadMenuItem.setPosition(loadMenuItem.getX() + 255, loadMenuItem.getY() - 170);
	     
	    menuChildScene.setOnMenuItemClickListener (this);
	     
	     setChildScene(menuChildScene);
	 }
	 

	@Override
	public void disposeScene(int levelID)
	{
		
	}
	
	public boolean onMenuItemClicked(MenuScene pMenuScene, IMenuItem pMenuItem, float pMenuItemLocalX, float pMenuItemLocalY)
	{
	    switch(pMenuItem.getID())
	    {
	        case MENU_PLAY:
	        	
	            //Load Game Scene!
	            SceneManager.getInstance().loadGameScene(engine, 1);
	            return true;
	            
	        case MENU_OPTIONS:
	        	
	        	// Load Configuration menu
	            SceneManager.getInstance().loadOptionsMenu(engine);
	            return true;
	            
	        default:
	            return false;
	    }
	}
	
}
