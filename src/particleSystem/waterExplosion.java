package particleSystem;

import org.andengine.engine.Engine;
import org.andengine.entity.IEntity;
import org.andengine.entity.particle.SpriteParticleSystem;
import org.andengine.entity.particle.emitter.PointParticleEmitter;
import org.andengine.entity.particle.initializer.ExpireParticleInitializer;
import org.andengine.entity.particle.initializer.GravityParticleInitializer;
import org.andengine.entity.particle.initializer.VelocityParticleInitializer;
import org.andengine.entity.particle.modifier.*;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import ResourcesManagment.ResourcesManager;
import android.content.Context;

public class waterExplosion implements ParticleSystemFactory
{
    
    private TextureRegion mParticleTextureRegion;
    SpriteParticleSystem particleSystem;
    private VertexBufferObjectManager vbom = ResourcesManager.getInstance().vbom;
        
    @Override
    public void load(Context context, Engine engine)
    {
  
    }
    
    @Override
    public SpriteParticleSystem build(Engine engine, int fontX, int fontY)
    {

        //X & Y for the particles to spawn at.
	    final int particlesXSpawn = fontX;
	    final int particlesYSpawn = fontY;

	    //Max & min rate are the maximum particles per second and the minimum particles per second.
	    final float maxRate = 100;
	    final float minRate = 80;

	    //This variable determines the maximum particles in the particle system.
	    final int maxParticles = 100;

	    //Particle emitter which will set all of the particles at a ertain point when they are initialized.
	    final PointParticleEmitter pointParticleEmtitter = new PointParticleEmitter(particlesXSpawn, particlesYSpawn);

	    //Creating the particle system.
		particleSystem = new SpriteParticleSystem (pointParticleEmtitter, maxRate, minRate, maxParticles, ResourcesManager.getInstance().waterDrop, vbom);

	    //And now, lets create the initiallizers and modifiers.
	    //Velocity initiallizer - will pick a random velocity from -20 to 20 on the x & y axes. Play around with this value.
	    particleSystem.addParticleInitializer(new VelocityParticleInitializer<Sprite>(-80, 80, -80, 80));

	    //Acceleration initializer - gives all the particles the earth gravity (so they accelerate down).
	    particleSystem.addParticleInitializer(new GravityParticleInitializer<Sprite>());

	    //And now, adding an alpha modifier, so particles slowly fade out. This makes a particle go from alpha = 1 to alpha = 0 in 3 seconds, starting exactly when the particle is spawned.
	    particleSystem.addParticleModifier(new AlphaParticleModifier<Sprite>(1, 0, 0, 0));

	    //Lastly, expire modifier. Make particles die after 3 seconds - their alpha reached 0.
	    particleSystem.addParticleInitializer(new ExpireParticleInitializer<Sprite>(2));  

	    return particleSystem;
}

    @Override
    public String getTitle()
    {
            return "WaterParticleSystem";
    }

	public IEntity getParticleSystem() 
	{
		return (particleSystem);
	}

}