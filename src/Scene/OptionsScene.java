package Scene;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.SpriteMenuItem;
import org.andengine.entity.scene.menu.item.decorator.ScaleMenuItemDecorator;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder.TextureAtlasBuilderException;
import org.andengine.opengl.util.GLState;

import ResourcesManagment.ResourcesManager;
import ResourcesManagment.SceneManager;
import ResourcesManagment.SceneManager.SceneType;

public class OptionsScene extends BaseScene implements IOnMenuItemClickListener
{
	
	private MenuScene menuChildScene;
	private int player_sel = 0;
	
	private final int MENU_PLAYER_SEL = 0;
	private final int PLAYER1_SEL = 1;
	private final int PLAYER2_SEL = 2;
	private final int PLAYER3_SEL = 3;
	private final int PLAYER4_SEL = 4;
	private final int AR_SEL = 5;
	private final int BACK = 6;
	
	private void createBackground()
	 {
	     attachChild(new Sprite(400, 240, resourcesManager.options_background_region, vbom)
	     {
	         @Override
	         protected void preDraw(GLState pGLState, Camera pCamera) 
	         {
	             super.preDraw(pGLState, pCamera);
	             pGLState.enableDither();
	         }
	     });
	 }
	
	private void createMenuChildScene()
	{
		
		 menuChildScene = new MenuScene(camera);
	     menuChildScene.setPosition(-250, 200);
	     
	     final Sprite background =  new Sprite(350, 230, resourcesManager.optionsBack, vbom);
	     final IMenuItem player = new ScaleMenuItemDecorator(new SpriteMenuItem(MENU_PLAYER_SEL, resourcesManager.options_player_selection, vbom), 1.2f, 1);
	     final IMenuItem arSel = new ScaleMenuItemDecorator(new SpriteMenuItem(AR_SEL, resourcesManager.ARSel, vbom), 1.2f, 1);
	     
	     menuChildScene.addMenuItem(player);
	     menuChildScene.addMenuItem(arSel);
  
	     menuChildScene.buildAnimations();
	     menuChildScene.setBackgroundEnabled(false);
	     
	     player.setPosition(player.getX() + 195 , player.getY() - 150);
	     arSel.setPosition(arSel.getX() + 205 , arSel.getY() - 130);
 
	     menuChildScene.setOnMenuItemClickListener(this);
	     setChildScene(menuChildScene);
	     attachChild(background);
	}
	
	@Override
    public void createScene()
    {
		createBackground();
		createMenuChildScene();
    }
	

	@Override
	public void onBackKeyPressed()
	{
		ResourcesManager.getInstance().unloadOptionsScreen();
		SceneManager.getInstance().loadMenuScene(engine);
		
	}

	@Override
	public SceneType getSceneType()
	{
		return SceneType.SCENE_OPTIONS;
	}

	 @Override
    public void disposeScene(int levelID)
    {
    }

	@Override
	public boolean onMenuItemClicked(MenuScene pMenuScene, IMenuItem pMenuItem, float pMenuItemLocalX, float pMenuItemLocalY)
	{
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/options_menu/");

		 switch(pMenuItem.getID())
	    {
		 
	        case MENU_PLAYER_SEL:
	        	
	            //Load Corresponding texture ---> player selected
	        	switch (player_sel)
	        	{
	        	
		        	case 0:
		        		
		        		player_sel = 1;
		        		//resourcesManager.playerOnlineSelected = 0;
		        		resourcesManager.playerSelected = 0;
		       	     	resourcesManager.player1 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(resourcesManager.optionsTextureAtlas, activity, "player1.png");
		       	     	final IMenuItem player1Sel = new ScaleMenuItemDecorator(new SpriteMenuItem(PLAYER1_SEL, resourcesManager.player1, vbom), 1.2f, 1);
		       	     	menuChildScene.addMenuItem(player1Sel);
		       	     	player1Sel.setPosition(player1Sel.getX() + 200 , player1Sel.getY() - 190);
		        		break;
		        		
		        	case 1:
		        		
		        		player_sel = 2;
		        		resourcesManager.playerSelected = 1;
		        		//resourcesManager.playerOnlineSelected = 1;
		        		
		        		// UNLOAD previous texture !!!
		        		menuChildScene.clearChildScene();
		        		createScene();
		        		
		        		resourcesManager.player2 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(resourcesManager.optionsTextureAtlas, activity, "player2.png");
		       	     	final IMenuItem player2Sel = new ScaleMenuItemDecorator(new SpriteMenuItem(PLAYER2_SEL, resourcesManager.player2, vbom), 1.2f, 1);
		       	     	menuChildScene.addMenuItem(player2Sel);
		       	     	player2Sel.setPosition(player2Sel.getX() + 200 ,player2Sel.getY() -100);
		        		break;
		        		
		        	case 2:
		        		
		        		player_sel = 3;
		        		resourcesManager.playerSelected = 2;
		        		//resourcesManager.playerOnlineSelected = 2;
		        		
		        		// UNLOAD previous texture !!!
		        		menuChildScene.clearChildScene();
		        		createScene();
		        		
		        		resourcesManager.player3 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(resourcesManager.optionsTextureAtlas, activity, "player3.png");
		       	     	final IMenuItem player3Sel = new ScaleMenuItemDecorator(new SpriteMenuItem(PLAYER3_SEL, resourcesManager.player3, vbom), 1.2f, 1);
		       	     	menuChildScene.addMenuItem(player3Sel);
		       	     	player3Sel.setPosition(player3Sel.getX() + 200 ,player3Sel.getY() -190);
		        		break;
		        		
		        	case 3:
		        		
		        		player_sel = 0;
		        		resourcesManager.playerSelected = 3;
		        		//resourcesManager.playerOnlineSelected = 3;
		        		
		        		// UNLOAD previous texture !!!
		        		menuChildScene.clearChildScene();
		        		createScene();
		        		
		        		resourcesManager.player4 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(resourcesManager.optionsTextureAtlas, activity, "player4.png");
		       	     	final IMenuItem player4Sel = new ScaleMenuItemDecorator(new SpriteMenuItem(PLAYER4_SEL, resourcesManager.player4, vbom), 1.2f, 1);
		       	     	menuChildScene.addMenuItem(player4Sel);
		       	     	player4Sel.setPosition(player4Sel.getX() + 200 ,player4Sel.getY() -190);
		        		break;
		        		
		        	default:
		        		break;
	        	}
	        	
	        	try
	        	{
	        		resourcesManager.optionsTextureAtlas.unload();
	        		resourcesManager.optionsTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
	        		resourcesManager.optionsTextureAtlas.load();
	    		} 
	        	
	        	catch (TextureAtlasBuilderException e)
	        	{
	    			e.printStackTrace();
	    		}
	        	
	        	return true;
	        	
	        case AR_SEL: 
	        			ResourcesManager.getInstance().setAR(!ResourcesManager.getInstance().getAR());
	        			return true;
	            
	        default:
	            return false;
	    }
	}

}
