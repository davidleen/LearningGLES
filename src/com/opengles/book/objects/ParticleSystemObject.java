package com.opengles.book.objects;

import java.util.Random;

import com.opengles.book.Vertices;

public class ParticleSystemObject {
	public static final int NUM_PARTICLE=100;
	float[] mParticleData=new float[NUM_PARTICLE*7];
	
	
	private Vertices attributeWraper;
	public ParticleSystemObject()
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
		
		//attributeWraper=new Vertices(new String[]{}, new int[]{1, 3, 3},mProgram );
		
	}
}
