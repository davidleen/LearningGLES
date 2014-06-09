package com.opengles.book.glsl;

import java.nio.FloatBuffer;

import android.opengl.GLES20;

import com.opengles.book.FloatUtils;

public class NewFloatUniform {
	
	
	public static final int SiZE_1FV=1;
	public static  final int SiZE_2FV=2;
	public static final int SiZE_3FV=3;
	public static final int SiZE_4FV=4;
	public static  final int SiZE_MATRIXFV=16;
	
	public float[]  data;
	public FloatBuffer buffer;
	public int uniformHandler;
	public int size;
	
	/**
	 * 
	 * @param size 
	 */
	public NewFloatUniform(int size)
	{
		
		switch(size)
		{
			case SiZE_1FV  :
			case SiZE_2FV :
			case SiZE_3FV :
			case SiZE_4FV:
			case SiZE_MATRIXFV:
			break;
			default :
			throw new RuntimeException(" float size  must be  1,2,3,4, 16 ");
		}
		
		this.size=size;
		data=new float[size]; 
		buffer=FloatUtils.FloatArrayToNativeBuffer(data); 
		setData(data);
	}
	
	
	
	public void setUniform(int mProgram,String uniformName)
	{
		uniformHandler=GLES20.glGetUniformLocation(mProgram,uniformName);
	}
	
	public void bind()
	{
		switch(size)
		{
			case  SiZE_1FV:
				GLES20.glUniform1fv(uniformHandler, 1,buffer);
			break;
			case  SiZE_2FV:
				GLES20.glUniform2fv(uniformHandler, 1,buffer);
				break;
			case  SiZE_3FV:
				GLES20.glUniform3fv(uniformHandler, 1,buffer);
				break;
			case  SiZE_4FV:
				GLES20.glUniform4fv(uniformHandler, 1,buffer);
				break;
			case  SiZE_MATRIXFV:
				GLES20.glUniformMatrix4fv(uniformHandler, 1,false,buffer);
				break;
		}
		
				
				 
	}
	
	public void setData(float... data)
	{
		buffer.put(data);
		buffer.flip();
		
	}
	
	
	public void dispose()
	{
		
		 
		 
	}
}
