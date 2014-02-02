package particleSystem;

import org.andengine.engine.Engine;
import org.andengine.entity.particle.ParticleSystem;
import org.andengine.entity.particle.SpriteParticleSystem;
import org.andengine.entity.particle.emitter.PointParticleEmitter;
import org.andengine.entity.particle.initializer.ExpireParticleInitializer;
import org.andengine.entity.particle.initializer.GravityParticleInitializer;
import org.andengine.entity.particle.initializer.VelocityParticleInitializer;
import org.andengine.entity.particle.modifier.*;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import ResourcesManagment.ResourcesManager;
import android.content.Context;

public class waterParticleSystem implements ParticleSystemFactory
{
    
    private TextureRegion mParticleTextureRegion;
    private VertexBufferObjectManager vbom = ResourcesManager.getInstance().vbom;
        
    @Override
    public void load(Context context, Engine engine)
    {
  
    }
    
    @Override
    public SpriteParticleSystem build(Engine engine, float fontX, float fontY, ITextureRegion texture)
    {

        //X & Y for the particles to spawn at.
	    final float particlesXSpawn = fontX;
	    final float particlesYSpawn = fontY;

	    //Max & min rate are the maximum particles per second and the minimum particles per second.
	    final float maxRate = 4;
	    final float minRate = 2;

	    //This variable determines the maximum particles in the particle system.
	    final int maxParticles = 50;

	    //Particle emitter which will set all of the particles at a ertain point when they are initialized.
	    final PointParticleEmitter pointParticleEmtitter = new PointParticleEmitter(particlesXSpawn, particlesYSpawn);

	    //Creating the particle system.
		final SpriteParticleSystem particleSystem = new SpriteParticleSystem (pointParticleEmtitter, maxRate, minRate, maxParticles, texture, vbom);

	    //And now, lets create the initiallizers and modifiers.
	    //Velocity initiallizer - will pick a random velocity from -20 to 20 on the x & y axes. Play around with this value.
	    particleSystem.addParticleInitializer(new VelocityParticleInitializer<Sprite>(-30, 30, -20, 20));

	    //Acceleration initializer - gives all the particles the earth gravity (so they accelerate down).
	    particleSystem.addParticleInitializer(new GravityParticleInitializer<Sprite>());
	    
	    particleSystem.addParticleModifier(new AlphaParticleModifier<Sprite>(1, 0, 0, 0));

	    //Lastly, expire modifier. Make particles die after 3 seconds - their alpha reached 0.
	    particleSystem.addParticleInitializer(new ExpireParticleInitializer<Sprite>(12));  

	    return particleSystem;
}

    @Override
    public String getTitle()
    {
            return "WaterParticleSystem";
    }

	@Override
	public ParticleSystem<Sprite> build(Engine engine, float fontX, float fontY) {
		// TODO Auto-generated method stub
		return null;
	}

}
