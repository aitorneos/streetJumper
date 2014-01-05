package Players;

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
import ResourcesManagment.SceneManager;


public abstract class PlayerSpecial extends AnimatedSprite
{
	// ---------------------------------------------
	// VARIABLES
	// ---------------------------------------------
	
	public Body body;
	private boolean canRun = false;
	public int impulse;
	public int time = 80;
	public int life = 3;

	
	// ---------------------------------------------
	// CONSTRUCTOR
	// ---------------------------------------------
	
	public PlayerSpecial(float pX, float pY, VertexBufferObjectManager vbo, Camera camera, PhysicsWorld physicsWorld)
	{
		super(pX, pY, ResourcesManager.getInstance().playerSecondary, vbo);
		canRun = false;
		impulse = 0;
		createPhysics(camera, physicsWorld);
	}
	
	
	// ---------------------------------------------
	// CLASS LOGIC METHODS
	// ---------------------------------------------
	
	private void createPhysics(final Camera camera, final PhysicsWorld physicsWorld)
	{		
		
		body = PhysicsFactory.createBoxBody(physicsWorld, this, BodyType.DynamicBody, PhysicsFactory.createFixtureDef(0, 0, 0));

		body.setUserData("playerSpecial");
		body.setFixedRotation(true);
		
		physicsWorld.registerPhysicsConnector(new PhysicsConnector(this, body, true, false)
		{
			@Override
	        public void onUpdate(float pSecondsElapsed)
	        {
				super.onUpdate(pSecondsElapsed);
				camera.onUpdate(0.1f);
				
				// Update enemy's movement
				if (getX() >= 1100 && body.getLinearVelocity().x > 0)
				{
					setFlippedHorizontal(true);
					body.setLinearVelocity(-2.0f, 0.0f);
				}
				
				if (getX() <= 730 && body.getLinearVelocity().x < 0)
				{
					setFlippedHorizontal(false);
					body.setLinearVelocity(2.0f, 0.0f);
				}
				
				// Look if can shoot to player ... limitated range
				if (SceneManager.getInstance().getGameScene().player.body.getPosition().x > 725 && SceneManager.getInstance().getGameScene().player.body.getPosition().x < 1050)
				{
					
				}
	        }
		});
	}
	
	public void setRunning()
	{
		canRun = true;
	    final long[] PLAYER_ANIMATE = new long[] { 100, 100, 100 };       
	    animate(PLAYER_ANIMATE, 0, 2, true);
	}
	

	public void onDie()
	{
		// TODO Auto-generated method stub
		
	}
	
	
	/**
	 * Destroy the physics associated to animated sprite
	 **/
	public void removePhysics(final PhysicsWorld mphysicsWorld, Scene scene) 
	{
        final PhysicsConnector PlayerPhysicsConnector = mphysicsWorld.getPhysicsConnectorManager().findPhysicsConnectorByShape(this);

        mphysicsWorld.unregisterPhysicsConnector(PlayerPhysicsConnector);
        mphysicsWorld.destroyBody(PlayerPhysicsConnector.getBody());
	}
	
	
	// ---------------------------------------------
	// GETTERS AND SETTERS
	// ---------------------------------------------
}
