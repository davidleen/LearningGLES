package com.opengles.book.utils;

import android.content.Context;
import android.opengl.GLES20;

import com.opengles.book.framework.gl.Texture;

import java.util.HashMap;

/**
 *  纹理id的集合类。
 * Created by davidleen29   qq:67320337
 * on 14-5-29.
 */
public class TextureMap extends HashMap<String,Texture> {


    public static final TextureMap instance=new TextureMap();

    public static void create(Context context,String fileName)
    {
        Texture   texture=new Texture(context.getResources(),fileName);
        instance.put(fileName,texture);
    }
    
    public static void dispose(String fileName)
    {
    	Texture   texture =instance.get(fileName);
    	if(texture!=null)
    	{
    		texture.dispose();
    		instance.remove(fileName); 
    	}
    }
    
    public static void bind(int glTextureId,String fileName)
    {
    	Texture   texture =instance.get(fileName);
    	if(texture!=null)
    	{
    		 GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
    	     texture.bind()  ;
    	}else
    	{
    		throw new RuntimeException("fileName  has not create a texure id ");
    	}
    	
    }


}
