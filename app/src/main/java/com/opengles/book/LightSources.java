package com.opengles.book;

import com.giants3.android.openglesframework.framework.utils.FloatUtils;

import java.nio.FloatBuffer;
 

public class LightSources {
	static float[] ambient = new float[4] ;
	public static FloatBuffer ambientBuffer=  FloatUtils.FloatArrayToNativeBuffer(ambient);
    static
    {
        setAmbient(0.15f, 0.15f, 0.15f, 1f);
    }

	public static float[] diffuse = new float[4] ;
	public static FloatBuffer diffuseBuffer=FloatUtils.FloatArrayToNativeBuffer(diffuse);
    static
    {
        setDiffuse(0.5f, 0.5f, 0.25f, 1f);
    }
	public static float[] specLight = new float[4] ;
	public static FloatBuffer specLightBuffer=FloatUtils.FloatArrayToNativeBuffer(specLight);
    static
    {
        setSpecLight(0.3f, 0.3f, 0.15f, 1f);
    }
	public static void setAmbient(int r, int g, int b, int a)
	{

		setAmbient(r / 256f, g / 256f, b / 256f, a / 256f);
	}

	public static void setAmbient(float r, float g, float b, float a)
	{

		ambient[0] = r;
		ambient[1] = g;
		ambient[2] = b;
		ambient[3] = a;



        ambientBuffer.put(ambient);
        ambientBuffer.flip();
	}

	public static void setDiffuse(float r, float g, float b, float a)
	{
		diffuse[0] = r;
		diffuse[1] = g;
		diffuse[2] = b;
		diffuse[3] = a;


        diffuseBuffer.put(diffuse);
        diffuseBuffer.flip();
	}
	
	
	
	
	
	public static void setSpecLight(float r, float g, float b, float a)
	{
		specLight[0] = r;
		specLight[1] = g;
		specLight[2] = b;
		specLight[3] = a;

        specLightBuffer.put(specLight);
        specLightBuffer.flip();


	}


	public static float[] lightPositionSun = new float[3];
    public static FloatBuffer lightPositionFBSun= FloatUtils
            .FloatArrayToNativeBuffer(lightPositionSun);
    static
    {
        setSunLightPosition(0,0,1000);
    }

	/**
	 * 设置太阳位置
	 * 
	 * @param x
	 * @param y
	 * @param z
	 */
	public static void setSunLightPosition(float x, float y, float z)
	{
		lightPositionSun[0] = x;
		lightPositionSun[1] = y;
		lightPositionSun[2] = z;
        lightPositionFBSun.put(lightPositionSun);
        lightPositionFBSun.flip();

	}
}
