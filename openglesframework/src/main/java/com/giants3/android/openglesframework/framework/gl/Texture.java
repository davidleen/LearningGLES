package com.giants3.android.openglesframework.framework.gl;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import com.giants3.android.openglesframework.framework.math.Rectangle;

import java.io.IOException;
import java.io.InputStream;

public class Texture {

	String fileName;
	public int textureId;
    private boolean isMipMap;

    public int width;
    public int height;

    private int[] xy;

	Resources rs;

	public Texture(Resources rs, String fileName) {
	this(rs,fileName,false);
	}

    public Texture(Resources rs, String fileName,boolean isMipMap) {
      this(rs,fileName,isMipMap,null);
    }


    public Texture(Resources rs, String fileName,boolean isMipMap, int[] xy) {
        this.rs = rs;
        this.fileName = fileName;
        this.isMipMap=isMipMap;
        this.xy=xy;
        load();
    }
    /**
     * call when the screen created
     */
	private void load() {

		int[] textureIds = new int[1];
		GLES20.glGenTextures(1, textureIds, 0);
		textureId = textureIds[0];

		InputStream in = null;
        Bitmap bitmap;
		try {
			in = rs.getAssets().open(fileName);
			  bitmap = BitmapFactory.decodeStream(in);
            width=bitmap.getWidth();
            height=bitmap.getHeight();
            in.close();
        } catch (IOException e) {
            throw new RuntimeException("Couldn't load texture '" + fileName
                    + "'", e);
        }


         	GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);

//            if(xy==null) {
                //实际加载纹理,换成这个方法后，如果图片格式有问题，会抛出图片格式异常，不再会误显示其他异常
                GLUtils.texImage2D
                        (
                                GLES20.GL_TEXTURE_2D, //纹理类型
                                0,
                                GLUtils.getInternalFormat(bitmap),
                                bitmap, //纹理图像
                                GLUtils.getType(bitmap),
                                0 //纹理边框尺寸
                        );
//            }
//            else
//            {
//
//                GLUtils.texSubImage2D
//                        (
//                                GLES20.GL_TEXTURE_2D, //纹理类型
//                                0,
//
//                                    xy[0],xy[1],
//
//                                bitmap, //纹理图像
//                                GLUtils.getInternalFormat(bitmap),
//                                GLUtils.getType(bitmap)
//                        );
//
//             //   GLES20.glTexSubImage2D();
//
//
//            }
            bitmap.recycle();
			setFilters( );
			GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);




	}

    /**
     * call when the screen   recreated
     */
	public void reload() {
		load();
		bind();
		setFilters( );
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
	}

	public void setFilters( ) {

        if(isMipMap)
        {
            //使用MipMap线性纹理采样
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,
                    GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR_MIPMAP_NEAREST);
            //使用MipMap最近点纹理采样
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,
                    GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR_MIPMAP_LINEAR);

        }else
        {
            //使用MipMap线性纹理采样
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,
                    GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
            //使用MipMap最近点纹理采样
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,
                    GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);



        }

	}

	public void bind() {
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
	}

	public void dispose() {


		int[] textureIds =
			{ textureId };
		GLES20.glDeleteTextures(1, textureIds, 0);
	}
}