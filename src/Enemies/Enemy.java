package Enemies;

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


public class Enemy extends AnimatedSprite
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
	
	public Enemy(float pX, float pY, VertexBufferObjectManager vbo, Camera camera, PhysicsWorld physicsWorld)
	{
		super(pX, pY, ResourcesManager.getInstance().flyEnemy , vbo);
		createPhysics(camera, physicsWorld);
		//camera.setChaseEntity(this);
	}
	
	
	// ---------------------------------------------
	// CLASS LOGIC
	// ---------------------------------------------
	
	private void createPhysics(final Camera camera, final PhysicsWorld physicsWorld)
	{		
		
		body = PhysicsFactory.createBoxBody(physicsWorld, this, BodyType.KinematicBody, PhysicsFactory.createFixtureDef(0, 0, 0));

		body.setUserData("enemy");
		body.setFixedRotation(true);
		
		physicsWorld.registerPhysicsConnector(new PhysicsConnector(this, body, true, false)
		{
			@Override
	        public void onUpdate(float pSecondsElapsed)
	        {
				super.onUpdate(pSecondsElapsed);
				camera.onUpdate(0.1f);
				
				if (getY() >= 480)
				{					
					onDie();
				}
				
				// Update enemy's movement
				if (getX() >= 420 && body.getLinearVelocity().x > 0)
				{
					body.setLinearVelocity(-0.5f, 0.0f);
				}
				
				if (getX() <= 280 && body.getLinearVelocity().x < 0)
				{
					body.setLinearVelocity(0.5f, 0.0f);
				}
				
	        }
		});
	}
	
	public void setRunning()
	{
		canRun = true;
	    final long[] ENEMY_ANIMATE = new long[] { 150, 150, 150 };       
	    animate(ENEMY_ANIMATE, 0, 2, true);
	}
	

	public void onDie()
	{
		
	}
	
	/**
	 * Destroy the physics associated to animated sprite
	 **/
	public void removePhysics(final PhysicsWorld mphysicsWorld, Scene scene) 
	{
        final PhysicsConnector EnemyPhysicsConnector = mphysicsWorld.getPhysicsConnectorManager().findPhysicsConnectorByShape(this);

        mphysicsWorld.unregisterPhysicsConnector(EnemyPhysicsConnector);
        mphysicsWorld.destroyBody(EnemyPhysicsConnector.getBody());
	}
	
	
	@Override
	public void dispose()
	{
		this.detachSelf();
		this.dispose();
	}
}