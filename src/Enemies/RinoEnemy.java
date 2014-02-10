package Enemies;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import ResourcesManagment.ResourcesManager;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;


public class RinoEnemy extends AnimatedSprite
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
	
	public RinoEnemy(float pX, float pY, VertexBufferObjectManager vbo, Camera camera, PhysicsWorld physicsWorld)
	{
		super(pX, pY, ResourcesManager.getInstance().rinoEnemy , vbo);
		createPhysics(camera, physicsWorld);
	}
	
	public RinoEnemy(float pX, float pY, VertexBufferObjectManager vbo, Camera camera, PhysicsWorld physicsWorld, ITiledTextureRegion enemy)
	{
		super(pX, pY, enemy , vbo);
		createPhysics(camera, physicsWorld);
	}
	
	
	// ---------------------------------------------
	// CLASS LOGIC
	// ---------------------------------------------
	
	private void createPhysics(final Camera camera, final PhysicsWorld physicsWorld)
	{		
		
		body = PhysicsFactory.createBoxBody(physicsWorld, this, BodyType.DynamicBody, PhysicsFactory.createFixtureDef(0, 0, 0));

		body.setUserData("RinoEnemy");
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
				if (getX() >= 515 && body.getLinearVelocity().x > 0)
				{
					setFlippedHorizontal(true);
					body.setLinearVelocity(-2.0f, 0.0f);
				}
				
				if (getX() <= 340 && body.getLinearVelocity().x < 0)
				{
					setFlippedHorizontal(false);
					body.setLinearVelocity(2.0f, 0.0f);
				}
				
	        }
		});
	}
	
	public void setRunning()
	{
		canRun = true;
	    final long[] ENEMY_ANIMATE = new long[] { 150, 150, 150, 150 };       
	    animate(ENEMY_ANIMATE, 0, 3, true);
	}
	

	public void onDie()
	{
		
	}
	
	/**
	 * Destroy the physics associated to animated sprite
	 **/
	public void removePhysics(final PhysicsWorld mphysicsWorld, Scene scene) 
	{
        final PhysicsConnector SlimeEnemyPhysicsConnector = mphysicsWorld.getPhysicsConnectorManager().findPhysicsConnectorByShape(this);

        mphysicsWorld.unregisterPhysicsConnector(SlimeEnemyPhysicsConnector);
        mphysicsWorld.destroyBody(SlimeEnemyPhysicsConnector.getBody());
	}
	
	@Override
	public void dispose()
	{
		this.detachSelf();
		this.dispose();
	}
}
