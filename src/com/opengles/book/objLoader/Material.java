package com.opengles.book.objLoader;

import android.util.Log;

import com.opengles.book.FloatUtils;
import com.opengles.book.LightSources;
import junit.framework.Assert;

import java.nio.FloatBuffer;
import java.util.ArrayList;

/**
 * 材料元素
 * Created by davidleen29   qq:67320337
 * on 14-4-16.
 */
public class Material {

    public static final String DEFAULT_PNG = "default.png";
	/**
     * 材料名称
     */
    String name;
    /**
     * 环境光
     */
    public float[] ambientColor; //ambient color
    public float[] diffuseColor;
    public float[] specularColor;
    public float alpha;
    public float shine;
    public int illum;
    public  String textureFile;
    
    /**
     * d factor 
		 Specifies the dissolve for the current material.
		 
		 "factor" is the amount this material dissolves into the background.  A 
		factor of 1.0 is fully opaque.  This is the default when a new material 
		is created.  A factor of 0.0 is fully dissolved (completely 
		transparent).
     */
    public float dissolve ;

    /**
     *  Specifies the sharpness of the reflections from the local reflection
     map.  If a material does not have a local reflection map defined in its
     material definition, sharpness will apply to the global reflection map
     defined in PreView.

     "value" can be a number from 0 to 1000.  The default is 60.  A high
     value results in a clear reflection of objects in the reflection map.
     */
    public float sharpness=60;



    public Material()
    {
        this("default");
    }
    public Material(String name) {
        super();
        this.name=name;
        //初始化数据
         ambientColor=new float[ ]{0.15f, 0.15f, 0.15f};
         diffuseColor=new float[]{0.5f, 0.5f, 0.25f};
         specularColor=new float[]{0.3f, 0.3f, 0.15f};
         alpha=1;
         textureFile=DEFAULT_PNG;

    }

    public void setAmbientColor(float v, float v1, float v2) {
    	if(v>0||v1>0||v2>0) 
    	{
        ambientColor[0]=v;
        ambientColor[1]=v1;
        ambientColor[2]=v2;}
    }

    public void setDiffuseColor(float v, float v1, float v2) {
    	if(v>0||v1>0||v2>0) 
    	{
        diffuseColor[0]=v;
        diffuseColor[1]=v1;
        diffuseColor[2]=v2;
    	}

    }

    public void setSpecularColor(float v, float v1, float v2) {
    	if(v>0||v1>0||v2>0) 
    	{
        specularColor[0]=v;
        specularColor[1]=v1;
        specularColor[2]=v2;
    	}
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }
    
    public void setDissolve(float dissolve) {
        this.dissolve = dissolve;
    }


    public void setShine(float shine) {
        this.shine = shine;
    }

    public float getShine() {
        return shine;
    }

    public void setIllum(int illum) {
        this.illum = illum;
    }

    public int getIllum() {
        return illum;
    }

    public void setTextureFile(String textureFile) {
        this.textureFile = textureFile;
    }

    public String getTextureFile() {
        return textureFile;
    }




    public static class MaterialList extends ArrayList<Material>
    {

        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public Material findByName(String name)
        {
            Assert.assertTrue("name must not be null",name!=null);

            for(Material material:this)
            {
            	  
                if(name.equals(material.name))
                    return material;
            }
            
            Log.e("Test", "==== no found material by name:"+name);
            return null;

        }

    }

}


