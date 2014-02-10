package Shader;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.entity.Entity;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.shader.PositionTextureCoordinatesShaderProgram;
import org.andengine.opengl.shader.ShaderProgram;
import org.andengine.opengl.shader.constants.ShaderProgramConstants;
import org.andengine.opengl.shader.exception.ShaderProgramException;
import org.andengine.opengl.shader.exception.ShaderProgramLinkException;
import org.andengine.opengl.texture.PixelFormat;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegionFactory;
import org.andengine.opengl.texture.render.RenderTexture;
import org.andengine.opengl.util.GLState;
import org.andengine.opengl.vbo.attribute.VertexBufferObjectAttributes;
import org.andengine.util.adt.color.Color;
import android.opengl.GLES20;
 
public class WaterSurfaceEntity extends Entity 
{
 
        private Engine engine;
        boolean first = true;
 
        WaterShaderProgram waterShader;
 
        public RenderTexture waterTexture, displacementTexture;
        public Sprite RenderTextureBackgroundSprite, back, dis;
        private static float secondsElapsed;
       
        float Frequency = 50f; // bigger = more waves per pixels
        float Amplitude = 0.025f; // bigger = higher waves
        float speedMultiplicator = 5f;
 
        public WaterSurfaceEntity(float pX, float pY, Sprite pBackground, Sprite pDis, Engine pEngine) 
        {
                super(pX, pY);
 
                engine=pEngine;
                waterTexture = new RenderTexture(engine.getTextureManager(), (int) pBackground.getWidth(), (int) pBackground.getHeight(), PixelFormat.RGBA_8888);
                displacementTexture = new RenderTexture(engine.getTextureManager(), (int) pBackground.getWidth(), (int) pBackground.getHeight(), PixelFormat.RGBA_8888);
 
                back = pBackground;
                dis = pDis;
                waterShader = new WaterShaderProgram();
        }
       
        public void setFrequency(float pFrequency)
        {
                Frequency=pFrequency;
        }
       
        public float getFrequency()
        {
                return Frequency;
        }
       
        public void setAmplitude(float pAmplitude)
        {
                Amplitude=pAmplitude;
        }
       
        public float getAmplitude()
        {
                return Amplitude;
        }
       
        public void setSpeedMultiplicator(float pspeedMultiplicator)
        {
                speedMultiplicator=pspeedMultiplicator;
        }
       
        public float getSpeedMultiplicator()
        {
                return speedMultiplicator;
        }
       
        @Override
        protected void onManagedUpdate(final float pSecondsElapsed) {
                super.onManagedUpdate(pSecondsElapsed);
                secondsElapsed += pSecondsElapsed*speedMultiplicator;
        }
 
        @Override
        protected void preDraw(final GLState pGLState, final Camera pCamera) {
                super.preDraw(pGLState, pCamera);
        }
 
        @Override
        protected void draw(final GLState pGLState, final Camera pCamera) 
        {
 
                if (first) 
                {
                        first = false;
                        waterTexture.init(pGLState);
                        displacementTexture.init(pGLState);
 
                        final ITextureRegion renderTextureATextureRegion = TextureRegionFactory.extractFromTexture(waterTexture);
 
                        RenderTextureBackgroundSprite = new Sprite(0, 0, renderTextureATextureRegion , engine.getVertexBufferObjectManager()) 
                        {
                                @Override
                                protected void preDraw(final GLState pGLState,
                                        final Camera pCamera) 
                                {
                                        setShaderProgram(waterShader);
                                       
                                        super.preDraw(pGLState, pCamera);
                                        GLES20.glUniform1f(WaterShaderProgram.sUniformTime, secondsElapsed);
                                        GLES20.glUniform1f(WaterShaderProgram.sUniformFrequency, Frequency);
                                        GLES20.glUniform1f(WaterShaderProgram.sUniformAmplitude, Amplitude);    
                                }
                        };
                }
               
                displacementTexture.begin(pGLState, false, true, Color.TRANSPARENT);
                {
                        dis.onDraw(pGLState, pCamera);
                }
                displacementTexture.end(pGLState);
 
                waterTexture.begin(pGLState, false, true, Color.TRANSPARENT);
                {
                        back.onDraw(pGLState, pCamera);
                }
                waterTexture.end(pGLState);
 
                {
                        pGLState.pushProjectionGLMatrix();
                        {
                                GLES20.glActiveTexture(GLES20.GL_TEXTURE1);
                                displacementTexture.bind(pGLState, GLES20.GL_TEXTURE1);
                                GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
 
                                RenderTextureBackgroundSprite.onDraw(pGLState, pCamera);
                        }
                        pGLState.popProjectionGLMatrix();
                }
 
        }
 
        public static class WaterShaderProgram extends ShaderProgram 
        {
               
                public static final String FRAGMENTSHADER =
                                "precision lowp float;\n"
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
                                + "float t = " + ShaderProgramConstants.VARYING_TEXTURECOORDINATES + ".y + maskAlpha.y *0.1-0.15 + (sin ("+ ShaderProgramConstants.VARYING_TEXTURECOORDINATES +".x *frequency+timedelta)*amplitude);\n"
                                + "vec4 backgroundColor = v_color * texture2D("+ ShaderProgramConstants.UNIFORM_TEXTURE_0 + ", vec2(" + ShaderProgramConstants.VARYING_TEXTURECOORDINATES + ".x, t)); \n"
                                + "gl_FragColor = backgroundColor; \n"
                                + "} \n";
 
                private WaterShaderProgram() 
                {
                        super(PositionTextureCoordinatesShaderProgram.VERTEXSHADER, FRAGMENTSHADER);
                }
 
                public static int sUniformModelViewPositionMatrixLocation = ShaderProgramConstants.LOCATION_INVALID;
                public static int sUniformTexture0Location = ShaderProgramConstants.LOCATION_INVALID;
                public static int sUniformTexture1Location = ShaderProgramConstants.LOCATION_INVALID;
                public static int sUniformTime = ShaderProgramConstants.LOCATION_INVALID;
                public static int sUniformFrequency = ShaderProgramConstants.LOCATION_INVALID;
                public static int sUniformAmplitude = ShaderProgramConstants.LOCATION_INVALID;
               
                public static final String TIME = "timedelta";
                public static final String FREQUENCY = "frequency";
                public static final String AMPLITUDE = "amplitude";
 
                @Override
                protected void link(final GLState pGLState)throws ShaderProgramLinkException
                {
                        GLES20.glBindAttribLocation(this.mProgramID, ShaderProgramConstants.ATTRIBUTE_POSITION_LOCATION, ShaderProgramConstants.ATTRIBUTE_POSITION);
                        GLES20.glBindAttribLocation(this.mProgramID, ShaderProgramConstants.ATTRIBUTE_TEXTURECOORDINATES_LOCATION, ShaderProgramConstants.ATTRIBUTE_TEXTURECOORDINATES);
 
                        super.link(pGLState);
 
                        WaterShaderProgram.sUniformModelViewPositionMatrixLocation = this.getUniformLocation(ShaderProgramConstants.UNIFORM_MODELVIEWPROJECTIONMATRIX);
                        WaterShaderProgram.sUniformTexture0Location = this.getUniformLocation(ShaderProgramConstants.UNIFORM_TEXTURE_1);
                        WaterShaderProgram.sUniformTexture1Location = this.getUniformLocation(ShaderProgramConstants.UNIFORM_TEXTURE_0);
                        WaterShaderProgram.sUniformTime = this.getUniformLocation(WaterShaderProgram.TIME);
                        WaterShaderProgram.sUniformFrequency = this.getUniformLocation(WaterShaderProgram.FREQUENCY);
                        WaterShaderProgram.sUniformAmplitude = this.getUniformLocation(WaterShaderProgram.AMPLITUDE);
                }
 
                @Override
                public void bind(final GLState pGLState,final VertexBufferObjectAttributes pVertexBufferObjectAttributes)
                {
                        GLES20.glDisableVertexAttribArray(ShaderProgramConstants.ATTRIBUTE_COLOR_LOCATION);
 
                        super.bind(pGLState, pVertexBufferObjectAttributes);
 
                        GLES20.glUniformMatrix4fv(WaterShaderProgram.sUniformModelViewPositionMatrixLocation, 1, false, pGLState.getModelViewProjectionGLMatrix(), 0);
                        GLES20.glUniform1i(WaterShaderProgram.sUniformTexture0Location, 1);
                        GLES20.glUniform1i(WaterShaderProgram.sUniformTexture1Location, 0);
                }
 
                @Override
                public void unbind(final GLState pGLState)throws ShaderProgramException 
                {
                        GLES20.glEnableVertexAttribArray(ShaderProgramConstants.ATTRIBUTE_COLOR_LOCATION);
                        super.unbind(pGLState);
                }
        }
 
       
}