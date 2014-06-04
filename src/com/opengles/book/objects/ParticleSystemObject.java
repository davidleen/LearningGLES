package com.opengles.book.objects;

import java.util.Random;

import android.content.Context;
import com.opengles.book.ShaderUtil;
import com.opengles.book.Vertices;
import com.opengles.book.glsl.Uniform1fv;

public class ParticleSystemObject {
	public static final int NUM_PARTICLE=100;
	float[] mParticleData=new float[NUM_PARTICLE*7];

    short[] mParticleIndicesData=new short[NUM_PARTICLE];
    private Vertices vertices;
    private Uniform1fv timeUniform;

    //
    int mProgram;
	public ParticleSystemObject(Context context)
	{
		
		//generate particle system data;
		Random generator=new Random();
		
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
			
		}

        // 创建program
        String mVertexShader = ShaderUtil.loadFromAssetsFile(
                "particleSystemObject/vertex.glsl",
                context.getResources());
        String mFragmentShader = ShaderUtil.loadFromAssetsFile(
                "particleSystemObject/frag.glsl",
                context.getResources());
        mProgram = ShaderUtil.createProgram(mVertexShader, mFragmentShader);

		 vertices=new Vertices(new String[]{}, new int[]{1, 3, 3},mProgram );
		
	}
}
