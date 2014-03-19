package Scene;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.SpriteMenuItem;
import org.andengine.entity.scene.menu.item.decorator.ScaleMenuItemDecorator;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.util.GLState;
import ResourcesManagment.ResourcesManager;
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
	private final int MENU_ONLINE = 3;
	
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
	     final IMenuItem onlineItem = new ScaleMenuItemDecorator(new SpriteMenuItem(MENU_ONLINE, resourcesManager.onlineOptions, vbom), 1.2f, 1);
	     
	     menuChildScene.addMenuItem(playMenuItem);
	     menuChildScene.addMenuItem(optionsMenuItem);
	     menuChildScene.addMenuItem(loadMenuItem);
	     menuChildScene.addMenuItem(onlineItem);
	     
	     menuChildScene.buildAnimations();
	     menuChildScene.setBackgroundEnabled(false);
	     
	     playMenuItem.setPosition(playMenuItem.getX() + 255 , playMenuItem.getY() - 200);
	     optionsMenuItem.setPosition(optionsMenuItem.getX() + 255, optionsMenuItem.getY() - 202);
	     onlineItem.setPosition(onlineItem.getX() + 255, onlineItem.getY() - 202);
	     loadMenuItem.setPosition(loadMenuItem.getX() + 255, loadMenuItem.getY() - 205);
	     
	    menuChildScene.setOnMenuItemClickListener (this);
	     
	     setChildScene(menuChildScene);
	 }
	 

	@Override
	public void disposeScene(int levelID)
	{
		
	}
	
	@SuppressWarnings("deprecation")
	public boolean onMenuItemClicked(MenuScene pMenuScene, IMenuItem pMenuItem, float pMenuItemLocalX, float pMenuItemLocalY)
	{
	    switch(pMenuItem.getID())
	    {
	        case MENU_PLAY:
	        	
	            //Load Game Scene!
	            SceneManager.getInstance().loadGameScene(engine, ResourcesManager.getInstance().getLevelComplete());
	            return true;
	            
	        case MENU_OPTIONS:
	        	
	        	// Load Configuration menu
	            SceneManager.getInstance().loadOptionsMenu(engine);
	            return true;
	            
	        case MENU_ONLINE:
	        	
	        	for (int i = 0; i <= 3; ++i)
	        	{
	        		ResourcesManager.getInstance().activity.toast("WIFI INSTRUCCIONS :\n\n"
		        			+ "1. SINGLE PLAYER : PUSH SERVER BUTTON\n\n"
		        			+ "2.MULTIPLAYER : ONE MOVILE IN SERVER MODE AND OTHER IN CLIENT MODE (INTRODUCE SERVER IP SHOWED IN SERVER MOVILE)\n");
	        	}
	        	
	        	return true;
	            
	        default:
	            return false;
	    }
	}
	
}
