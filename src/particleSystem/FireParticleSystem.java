package particleSystem;

import org.andengine.engine.Engine;
import org.andengine.entity.particle.emitter.CircleParticleEmitter;
import org.andengine.entity.particle.ParticleSystem;
import org.andengine.entity.particle.SpriteParticleSystem;
import org.andengine.entity.particle.initializer.*;
import org.andengine.entity.particle.modifier.*;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import ResourcesManagment.ResourcesManager;
import android.content.Context;
import particleSystem.ParticleSystemFactory;

public class FireParticleSystem implements ParticleSystemFactory
{

    private VertexBufferObjectManager vbom = ResourcesManager.getInstance().vbom;
    private SpriteParticleSystem particleSystem = null;
        
    @Override
    public void load(Context context, Engine engine)
    {
    }
    
    @Override
    public SpriteParticleSystem build(Engine engine, float fontX, float fontY)
    {
    	//X & Y for the particles to spawn at.
	    final float particlesXSpawn = fontX;
	    final float particlesYSpawn = fontY;

	    //Max & min rate are the maximum particles per second and the minimum particles per second.
	    final float maxRate = 150;
	    final float minRate = 100;

	    //This variable determines the maximum particles in the particle system.
	    final int maxParticles = 300;

	    //Particle emitter which will set all of the particles at a ertain point when they are initialized.
	    final CircleParticleEmitter circleParticleEmtitter = new CircleParticleEmitter(particlesXSpawn, particlesYSpawn, 1.0f);

	    //Creating the particle system.
		particleSystem = new SpriteParticleSystem (circleParticleEmtitter, maxRate, minRate, maxParticles, ResourcesManager.getInstance().fireDrop.deepCopy(), vbom);

		//And now, lets create the initiallizers and modifiers.
	    //Velocity initiallizer - will pick a random velocity from -20 to 20 on the x & y axes. Play around with this value.
	    particleSystem.addParticleInitializer(new VelocityParticleInitializer<Sprite>(-75, 75, -75, 75));

	    //Acceleration initializer - gives all the particles the earth gravity (so they accelerate down).
	    particleSystem.addParticleInitializer(new GravityParticleInitializer<Sprite>());

	    //And now, adding an alpha modifier, so particles slowly fade out. This makes a particle go from alpha = 1 to alpha = 0 in 3 seconds, starting exactly when the particle is spawned.
	    //particleSystem.addParticleModifier(new AlphaParticleModifier<Sprite>(1, 0, 0, 0));

	    //Lastly, expire modifier. Make particles die after 3 seconds - their alpha reached 0.
	    particleSystem.addParticleInitializer(new ExpireParticleInitializer<Sprite>(1.5f));  
	    
	    return particleSystem;
}

    @Override
    public String getTitle() 
    {
            return "FireParticleSystem";
    }
    
    public ParticleSystem<Sprite> getParticleSystem()
    {
		return (this.particleSystem);	
    }
    
    
}
