package Shader;

import org.andengine.engine.Engine;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.opengl.shader.PositionTextureCoordinatesShaderProgram;
import org.andengine.opengl.shader.ShaderProgram;
import org.andengine.opengl.shader.constants.ShaderProgramConstants;
import org.andengine.opengl.shader.exception.ShaderProgramException;
import org.andengine.opengl.shader.exception.ShaderProgramLinkException;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.util.GLState;
import org.andengine.opengl.vbo.attribute.VertexBufferObjectAttributes;

import android.opengl.GLES20;

public class WaterMaskEffectShader extends Object
{
    
    private static final WaterMaskEffectShader INSTANCE = new WaterMaskEffectShader();
   
    public static float time = 0.0f;
    public static final float frequency = 35f; // bigger = more waves per pixels
    public static final float amplitude = 0.003f; // bigger = higher waves
    public static float speed = 4.0f;
   
    public static ITexture maskTexture;

    public WaterMaskEffectShader() 
    {
    }

    public static WaterMaskEffectShader getInstance()
    {
            return INSTANCE;
    }
    
    public static void resetTime ()
    {
    	WaterMaskEffectShader.time = 0.0f;
    }
   
    public static void setup(Engine pEngine, ITexture pMaskTexture, float t, float s)
    {
        maskTexture = pMaskTexture;
        speed = s;
        WaterMaskEffectShader.time = t;
        IUpdateHandler update = new IUpdateHandler() 
        {
           @Override
           public void reset() 
           {
        	   this.reset();
           }

           @Override
           public void onUpdate(float pSecondsElapsed) 
           {
        	   	   
        	   WaterMaskEffectShader.time += (pSecondsElapsed * speed);
           }
       };
        pEngine.registerUpdateHandler(update);
    }
   
    public static WaterMaskEffectShaderProgram getShaderProgram()
    {         
            return WaterMaskEffectShaderProgram.getInstance();
    }

    public static class WaterMaskEffectShaderProgram extends ShaderProgram 
    {
    	private static WaterMaskEffectShaderProgram instance;

    	public static final String FRAGMENTSHADER =  "precision lowp float;\n"
            + "uniform lowp sampler2D " + ShaderProgramConstants.UNIFORM_TEXTURE_1 + ";\n"
            + "uniform lowp sampler2D " + ShaderProgramConstants.UNIFORM_TEXTURE_0 + ";\n"
            + "varying mediump vec2 " + ShaderProgramConstants.VARYING_TEXTURECOORDINATES + ";\n"                          
            + "uniform float timedelta; \n"
            + "uniform float frequency; \n"
            + "uniform float amplitude; \n"        
            + "void main() \n"
            + "{ \n"
            + "vec4 v_color = vec4(1.0); \n"
            + "vec2 maskAlpha = texture2D("+ ShaderProgramConstants.UNIFORM_TEXTURE_1 + ", vec2("+ ShaderProgramConstants.VARYING_TEXTURECOORDINATES + ".x/6.0," + ShaderProgramConstants.VARYING_TEXTURECOORDINATES + ".y/6.0)).xy; \n"
            + "float t = " + ShaderProgramConstants.VARYING_TEXTURECOORDINATES + ".y + maskAlpha.y *0.1-0.15 + (sin ("+ ShaderProgramConstants.VARYING_TEXTURECOORDINATES +".x * frequency + timedelta) * amplitude);\n"
            + "vec4 backgroundColor = v_color * texture2D("+ ShaderProgramConstants.UNIFORM_TEXTURE_0 + ", vec2(" + ShaderProgramConstants.VARYING_TEXTURECOORDINATES + ".x, t)); \n"
            + "gl_FragColor = backgroundColor; \n"
            + "} \n";

	    public static int sUniformModelViewPositionMatrixLocation = ShaderProgramConstants.LOCATION_INVALID;
	    public static int sUniformTexture0Location = ShaderProgramConstants.LOCATION_INVALID;
	    public static int sUniformTexture1Location = ShaderProgramConstants.LOCATION_INVALID;
	    public static int sUniformTimeLocation = ShaderProgramConstants.LOCATION_INVALID;
	    public static int sUniformFrequencyLocation = ShaderProgramConstants.LOCATION_INVALID;
	    public static int sUniformAmplitudeLocation = ShaderProgramConstants.LOCATION_INVALID;
	   
	    public static WaterMaskEffectShaderProgram getInstance() 
	    {
	            if (instance == null)
	                    instance = new WaterMaskEffectShaderProgram();
	            return instance;
	    }

	    private WaterMaskEffectShaderProgram() 
	    {
	            super(PositionTextureCoordinatesShaderProgram.VERTEXSHADER,FRAGMENTSHADER);
	    }
	    
	
	    @Override
	    protected void link(final GLState pGLState)throws ShaderProgramLinkException 
	    {
	            GLES20.glBindAttribLocation(this.mProgramID,ShaderProgramConstants.ATTRIBUTE_POSITION_LOCATION,ShaderProgramConstants.ATTRIBUTE_POSITION);
	            GLES20.glBindAttribLocation(this.mProgramID,ShaderProgramConstants.ATTRIBUTE_TEXTURECOORDINATES_LOCATION,ShaderProgramConstants.ATTRIBUTE_TEXTURECOORDINATES);
	
	            super.link(pGLState);
	
	            WaterMaskEffectShaderProgram.sUniformModelViewPositionMatrixLocation = this.getUniformLocation(ShaderProgramConstants.UNIFORM_MODELVIEWPROJECTIONMATRIX);
	            WaterMaskEffectShaderProgram.sUniformTexture0Location = this.getUniformLocation(ShaderProgramConstants.UNIFORM_TEXTURE_0);
	            WaterMaskEffectShaderProgram.sUniformTexture1Location = this.getUniformLocation(ShaderProgramConstants.UNIFORM_TEXTURE_1);
	            WaterMaskEffectShaderProgram.sUniformTimeLocation = this.getUniformLocation("timedelta");
	            WaterMaskEffectShaderProgram.sUniformFrequencyLocation = this.getUniformLocation("frequency");
	            WaterMaskEffectShaderProgram.sUniformAmplitudeLocation = this.getUniformLocation("amplitude");
	    }
	
	    @Override
	    public void bind(final GLState pGLState,final VertexBufferObjectAttributes pVertexBufferObjectAttributes)
	    {
	            GLES20.glDisableVertexAttribArray(ShaderProgramConstants.ATTRIBUTE_COLOR_LOCATION);
	
	            super.bind(pGLState, pVertexBufferObjectAttributes);
	           
	            maskTexture.bind(pGLState, GLES20.GL_TEXTURE1);
	            GLES20.glActiveTexture(GLES20.GL_TEXTURE0);//<--- very important
	           
	            GLES20.glUniformMatrix4fv(WaterMaskEffectShaderProgram.sUniformModelViewPositionMatrixLocation, 1,false, pGLState.getModelViewProjectionGLMatrix(), 0);
	            GLES20.glUniform1i(WaterMaskEffectShaderProgram.sUniformTexture0Location, 0);
	            GLES20.glUniform1i(WaterMaskEffectShaderProgram.sUniformTexture1Location, 1);
	            GLES20.glUniform1f(sUniformTimeLocation, time);
	            GLES20.glUniform1f(sUniformFrequencyLocation, frequency);
	            GLES20.glUniform1f(sUniformAmplitudeLocation, amplitude);
	    }
	
	    @Override
	    public void unbind(final GLState pGLState) throws ShaderProgramException 
	    {
	            GLES20.glEnableVertexAttribArray(ShaderProgramConstants.ATTRIBUTE_COLOR_LOCATION);
	            super.unbind(pGLState);
	    }
   
    }
}
