package Animated_Features;
import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

import ResourcesManagment.ResourcesManager;

public class SpringBoarder extends AnimatedSprite
{
	//---------------------------------------------
	// VARIABLES
	// ---------------------------------------------
	
	public Body body;
	private boolean canRun = false;

	//private IAreaShape shape;
	
	// ---------------------------------------------
	// CONSTRUCTOR
	// ---------------------------------------------
	
	public SpringBoarder(float pX, float pY, VertexBufferObjectManager vbo, Camera camera, PhysicsWorld physicsWorld)
	{
		super(pX, pY, ResourcesManager.getInstance().springboarder , vbo);
		createPhysics(camera, physicsWorld);
	}
	
	
	// ---------------------------------------------
	// CLASS LOGIC
	// ---------------------------------------------
	
	private void createPhysics(final Camera camera, final PhysicsWorld physicsWorld)
	{		
		
		body = PhysicsFactory.createBoxBody(physicsWorld, this, BodyType.KinematicBody, PhysicsFactory.createFixtureDef(0, 0, 0));

		body.setUserData("springboarder");
		body.setFixedRotation(true);
			
		physicsWorld.registerPhysicsConnector(new PhysicsConnector(this, body, true, false)
		{
			@Override
	        public void onUpdate(float pSecondsElapsed)
	        {
				super.onUpdate(pSecondsElapsed);
				camera.onUpdate(0.1f);
				
	        }
		});
	}
		
	public void setRunning()
	{
		canRun = true;
	    final long[] ENEMY_ANIMATE = new long[] { 200, 200 ,200};       
	    animate(ENEMY_ANIMATE, 0, 2, false);
	}
	

	public void onDie()
	{
		
	}
	
	/**
	 * Destroy the physics associated to animated sprite
	 **/
	public void removePhysics(final PhysicsWorld mphysicsWorld, Scene scene) 
	{
        final PhysicsConnector SpringBoarder = mphysicsWorld.getPhysicsConnectorManager().findPhysicsConnectorByShape(this);

        mphysicsWorld.unregisterPhysicsConnector(SpringBoarder);
        mphysicsWorld.destroyBody(SpringBoarder.getBody());
	}
	
	@Override
	public void dispose()
	{
		this.detachSelf();
		this.dispose();
	}

}
