package Players;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import ResourcesManagment.ResourcesManager;
import ResourcesManagment.SceneManager;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;


public abstract class Player extends AnimatedSprite
{
	// ---------------------------------------------
	// VARIABLES
	// ---------------------------------------------
	
	public Body body;
	private boolean canRun = false;
	private int footContacts = 0;
	public int impulse;
	private int levelID;
	public boolean hasKey;
	public boolean hasGreenKey;
	public int life = 3;
	public int time = 80;
	private boolean mineColision;
	private boolean spikesColision;
	private boolean enemy;
	private boolean slimeE;
	private boolean stopAccelerometer;
	private boolean colideAscensor;
	private boolean waterColision;
	private boolean boxTouched;
	public boolean switch1Touched;
	public boolean switch2Touched;

	
	// ---------------------------------------------
	// CONSTRUCTOR
	// ---------------------------------------------
	
	public Player(float pX, float pY, VertexBufferObjectManager vbo, Camera camera, PhysicsWorld physicsWorld)
	{
		super(pX, pY, ResourcesManager.getInstance().player_region, vbo);
		mineColision = false;
		hasGreenKey = false;
		hasKey = false;
		canRun = false;
		spikesColision = false;
		stopAccelerometer = false;
		colideAscensor = false;
		waterColision = false;
		boxTouched = false;
		switch1Touched = false;
		switch2Touched = false;
		levelID = ResourcesManager.getInstance().getLevelComplete();
		footContacts = 0;
		impulse = 0;
		createPhysics(camera, physicsWorld);
		camera.setChaseEntity(this);
	}
	
	
	// ---------------------------------------------
	// CLASS LOGIC METHODS
	// ---------------------------------------------
	
	private void createPhysics(final Camera camera, final PhysicsWorld physicsWorld)
	{		
		
		body = PhysicsFactory.createBoxBody(physicsWorld, this, BodyType.DynamicBody, PhysicsFactory.createFixtureDef(0, 0, 0));

		body.setUserData("player");
		body.setFixedRotation(true);
		
		physicsWorld.registerPhysicsConnector(new PhysicsConnector(this, body, true, false)
		{
			@Override
	        public void onUpdate(float pSecondsElapsed)
	        {
				super.onUpdate(pSecondsElapsed);
				camera.onUpdate(0.1f);
				
				if (life == 0 || time <= 0 || getY() > 800 || getY() < 0)
				{					
					onDie();
				}
				
				// Comprovem que no segueixo "Corrent" fora del tamny de la camera
				if (getX() >= 2970 || getX() < 5)
				{
					setAc(true);
				}
				else
				{
					setAc(false);
				}
				
				if (levelID == 2)
				{
					if (SceneManager.getInstance().getGameScene().playerSpecial.sps.getY() >= getY())
					{
						
					}
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
	
	public void jump()
	{
		if (this.footContacts < 1) 
	    {
	        return; 
	    }
	    body.setLinearVelocity(new Vector2(body.getLinearVelocity().x, 12)); 
	
	}

	public void onDie()
	{
		// TODO Auto-generated method stub
		
	}
	
	public void increaseFootContacts()
	{
	    this.footContacts++;
	}

	public void decreaseFootContacts()
	{
	    this.footContacts--;
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
	
	@Override
	public void dispose()
	{
		this.detachSelf();
		this.dispose();
	}
	
	// ---------------------------------------------
	// GETTERS AND SETTERS
	// ---------------------------------------------
	
	public boolean getMineColision()
	{
		return (this.mineColision);
	}
	
	public void setMineColision(boolean colision)
	{
		this.mineColision = colision;
	}
	
	public boolean getSpikesColision()
	{
		return (this.spikesColision);
	}
	
	public void setSpikesColision(boolean colision)
	{
		this.spikesColision = colision;
	}
	
	public boolean getEnemyColision()
	{
		return (this.enemy);
	}
	
	public void setEnemyColision(boolean colision)
	{
		this.enemy = colision;
	}
	
	public boolean getSlimeColision()
	{
		return (this.slimeE);
	}
	
	public void setSlimeColision(boolean colision)
	{
		this.slimeE = colision;
	}
	
	public boolean getAc()
	{
		return (this.stopAccelerometer);
	}
	
	public void setAc(boolean mov)
	{
		this.stopAccelerometer = mov;
	}
	
	public boolean getColAscensor()
	{
		return (this.colideAscensor);
	}
	
	public void setColAscensor(boolean colision)
	{
		this.colideAscensor = colision;
	}
	
	public boolean getWaterColision()
	{
		return (this.waterColision);
	}
	
	public void setWaterColision(boolean col)
	{
		this.waterColision = col;
	}
	
	public boolean getBoxColision()
	{
		return (this.boxTouched);
	}
	
	public void setBoxColision(boolean box)
	{
		this.boxTouched = box;
	}	
}