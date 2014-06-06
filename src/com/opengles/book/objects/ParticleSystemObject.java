package com.opengles.book.objects;

import java.nio.FloatBuffer;
import java.util.Random;

import android.content.Context;
import android.opengl.GLES20;

import com.opengles.book.FloatUtils;
import com.opengles.book.ShaderUtil;
import com.opengles.book.Vertices;
import com.opengles.book.framework.gl.Texture;
import com.opengles.book.galaxy.ObjectDrawable;
import com.opengles.book.glsl.Uniform;
import com.opengles.book.glsl.Uniform.UniformBinder;
import com.opengles.book.glsl.Uniform1fv;
import com.opengles.book.glsl.Uniform3fv;
import com.opengles.book.glsl.Uniform4fv;
import com.opengles.book.utils.TextureMap;

/**
 * 粒子系统生成对象
 * @author davidleen29
 *
 */
public class ParticleSystemObject implements ObjectDrawable{
	public static final int NUM_PARTICLE=100;
	float[] mParticleData=new float[NUM_PARTICLE*7];
	int drawType=GLES20.GL_POINTS;

    short[] mParticleIndicesData=new short[NUM_PARTICLE];
    private Vertices vertices;
    private Uniform1fv timeUniform;
    //粒子系统中心位置。
    private  Uniform3fv  centerPositionUniform;
    private  Uniform4fv  uColorUniform;
    
    private Texture smokeTexture;
    
    
    
    private float[] uTime=new float[]{0.0f};
    private float[] centerPosition=new float[]{0,0,0};
    private FloatBuffer  centerPositionBuffer =FloatUtils.FloatArrayToNativeBuffer(centerPosition);
    
    private float[] uColor=new float[]{0,0,0,0};
    private FloatBuffer   uColorBuffer =FloatUtils.FloatArrayToNativeBuffer(uColor);
   
    
    Random generator=new Random();
    
    
    int mProgram;
    public static final String textureMapFileName="particleSystemObject/smoke.png";
	public ParticleSystemObject(Context context)
	{
		
		//generate particle system data;
		
		
		for(int i=0;i<NUM_PARTICLE;i++)
		{
			 // Lifetime of particle
	           mParticleData[i * 7 + 0] = generator.nextFloat();

	           // End position of particle
	           mParticleData[i * 7 + 1] = generator.nextFloat() * 2.0f - 1.0f;
	           mParticleData[i * 7 + 2] = generator.nextFloat() * 2.0f - 1.0f;
	           mParticleData[i * 7 + 3] = generator.nextFloat() * 2.0f - 1.0f;
	           
	           // Start position of particle
	           mParticleData[i * 7 + 4] = generator.nextFloat() * 0.25f - 0.125f;
	           mParticleData[i * 7 + 5] = generator.nextFloat() * 0.25f - 0.125f;
	           mParticleData[i * 7 + 6] = generator.nextFloat() * 0.25f - 0.125f;                   
	           mParticleIndicesData[i]=(short)i;
		}

        // 创建program
        String mVertexShader = ShaderUtil.loadFromAssetsFile(
                "particleSystemObject/vertex.glsl",
                context.getResources());
        String mFragmentShader = ShaderUtil.loadFromAssetsFile(
                "particleSystemObject/frag.glsl",
                context.getResources());
        mProgram = ShaderUtil.createProgram(mVertexShader, mFragmentShader);

		vertices=new Vertices(new String[]{"lifeTime","endPosition","startPosition"}, new int[]{1, 3, 3},mProgram );
		
		timeUniform=new Uniform1fv(mProgram, "uTime", new UniformBinder<float[]>() {

			@Override
			public float[] getBindValue() {
				 
				return uTime;
			}
		});
		
		
		centerPositionUniform=new Uniform3fv(mProgram, "centerPosition", new UniformBinder<FloatBuffer>() {

			@Override
			public FloatBuffer getBindValue() {
				 
				return centerPositionBuffer;
			}
		})  ;
		uColorUniform=new Uniform4fv(mProgram, "uColor", new UniformBinder<FloatBuffer>() {

			@Override
			public FloatBuffer getBindValue() {
				 
				return uColorBuffer;
			}
		}) ;
		 
		smokeTexture=new Texture(context.getResources(), textureMapFileName);
		
		 
	}
	@Override
	public void bind() {
		 
		 vertices.create(mParticleData,mParticleIndicesData);
		 smokeTexture.reload();
		
	}
	@Override
	public void unBind() {
		 vertices.dispose();
		 smokeTexture.dispose();
		
	}
	@Override
	public void draw() {
		 GLES20.glUseProgram(mProgram);
		 
		 
		 
		 GLES20.glEnable(GLES20.GL_BLEND);
		 GLES20.glBlendFunc(GLES20.GL_BLEND_SRC_ALPHA, GLES20.GL_ONE);
		 GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
		 smokeTexture.bind();
//		 GLES20.glEnable(GLES20.GL_TEXTURE_2D);
	     timeUniform.bind();
	     uColorUniform.bind();
	     centerPositionUniform.bind();
		 vertices.bind();
         vertices.draw(drawType);
         vertices.unbind();
		 
		 
		 
	}
	
 
	@Override
	public void update(float deltaTime) {
		 
		uTime[0]+=deltaTime;
		if(uTime[0]>=1.0)
		{
			uTime[0]=0;
			
			centerPosition[0]=generator.nextFloat()*2-1;
			centerPosition[1]=generator.nextFloat()*2-1;
			centerPosition[2]=generator.nextFloat()*2-1; 
			centerPositionBuffer.put(centerPosition);
			centerPositionBuffer.flip();
			
//			uColor[0]=1.0f;
//			uColor[1]=0.0f;
//			uColor[2]=0; 
//			uColor[3]=0.5f;
			uColor[0]=Math.min(generator.nextFloat()+0.5f,1);
			uColor[1]=Math.min(generator.nextFloat()+0.5f,1);
			uColor[2]=Math.min(generator.nextFloat()+0.5f,1); 
			uColor[3]=0.5f;
			uColorBuffer.put(uColor);
			uColorBuffer.flip();
			
		}
		
		
	}
	
	
	
	 
	
}
