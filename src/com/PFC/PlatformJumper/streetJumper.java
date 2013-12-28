package com.PFC.PlatformJumper;

import java.io.IOException;

import org.andengine.engine.Engine;
import org.andengine.engine.LimitedFPSEngine;
import org.andengine.engine.options.ConfigChooserOptions;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.camera.BoundCamera;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.WakeLockOptions;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.extension.augmentedreality.BaseAugmentedRealityGameActivity;
import org.andengine.input.sensor.acceleration.AccelerationData;
import org.andengine.input.sensor.acceleration.IAccelerationListener;

import ResourcesManagment.ResourcesManager;
import ResourcesManagment.SceneManager;
import android.view.Display;
import android.view.KeyEvent;

/**
 *
 * @author Aitor Arque Arnaiz (1194443)
 */

public class streetJumper extends BaseAugmentedRealityGameActivity implements IAccelerationListener
{
	
	//------------------ Class Variables --------------------------------------
	
	private BoundCamera camera;
	private ResourcesManager resourcesManager;
	private boolean activated = false;
   
	//------------------------------------------------------------------------
	
	// ----------------------- Methods ---------------------------------------
	
	
	/*
	 Initialize screen options 
	 */
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
	    return engineOptions;
    }
	
	
	/*
	 Method that limits the FPS (frames per second) to 60 Herz due to 
	 the diferent frame rate technology
	 */
	@Override
	public Engine onCreateEngine(EngineOptions pEngineOptions) 
	{
	    return new LimitedFPSEngine(pEngineOptions, 60);
	}

    public void onCreateResources(OnCreateResourcesCallback pOnCreateResourcesCallback) throws IOException
    {
    	
    	 ResourcesManager.prepareManager(mEngine, this, camera, getVertexBufferObjectManager());
    	 resourcesManager = ResourcesManager.getInstance();
    	 //this.enableAccelerationSensor(this);
    	 pOnCreateResourcesCallback.onCreateResourcesFinished();
        
    }

    public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback) throws IOException
    {
    	
    	SceneManager.getInstance().createSplashScene(pOnCreateSceneCallback);
    }

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
    protected void onDestroy()
    {
    	super.onDestroy();
    	this.disableAccelerationSensor();
        System.exit(0);	
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

}
