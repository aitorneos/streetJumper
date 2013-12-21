package Scene;

import ResourcesManagment.ResourcesManager;
import ResourcesManagment.SceneManager.SceneType;

import org.andengine.entity.scene.background.Background;
import org.andengine.entity.text.Text;
import org.andengine.util.adt.color.Color;

import Scene.BaseScene;

public class LoadingScene extends BaseScene
{

	@Override
	public void createScene()
	{
	    setBackground(new Background(Color.BLACK));
	    attachChild(new Text(400, 240, resourcesManager.font, "Loading...", vbom));
	}

	@Override
	public void onBackKeyPressed()
	{
		return;
		
	}

	@Override
	public SceneType getSceneType()
	{
		
		return SceneType.SCENE_LOADING;
	}

	@Override
	public void disposeScene(int levelID)
	{
		
	}

}
