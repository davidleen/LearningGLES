package com.giants3.android.openglesframework.framework.gl;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;

import java.io.IOException;
import java.io.InputStream;

public class CubeTexture {

	String[] fileNames;
	public int textureId;
	int minFilter;
	int magFilter;

	Resources rs;

	public CubeTexture(Resources rs, String fileName) {
		 this(rs,new String[]{fileName});
	}


    public CubeTexture(Resources rs, String[] fileNames) {
        this.rs = rs;
        this.fileNames = fileNames;
        load();
    }

	private void load() {

		int[] textureIds = new int[1];
		GLES20.glGenTextures(1, textureIds, 0);
		textureId = textureIds[0];
        GLES20.glBindTexture(GLES20.GL_TEXTURE_CUBE_MAP, textureId);

        int mapLength=fileNames.length;
        if(mapLength==1)  //只有意图  全景 使用
        {

        Bitmap bitmap;


		try {
            InputStream in 	  = rs.getAssets().open(fileNames[0]);
			  bitmap = BitmapFactory.decodeStream(in);
            in.close();

        } catch (IOException e) {
            throw new RuntimeException("Couldn't load texture '" + fileNames[0]
                    + "'", e);
        }




            //顺序很重要
			GLUtils.texImage2D(GLES20.GL_TEXTURE_CUBE_MAP_POSITIVE_X, 0, bitmap, 0);
            GLUtils.texImage2D(GLES20.GL_TEXTURE_CUBE_MAP_NEGATIVE_X, 0, bitmap, 0);

            GLUtils.texImage2D(GLES20.GL_TEXTURE_CUBE_MAP_POSITIVE_Y, 0, bitmap, 0);
            GLUtils.texImage2D(GLES20.GL_TEXTURE_CUBE_MAP_NEGATIVE_Y, 0, bitmap, 0);

            GLUtils.texImage2D(GLES20.GL_TEXTURE_CUBE_MAP_POSITIVE_Z, 0, bitmap, 0);
            GLUtils.texImage2D(GLES20.GL_TEXTURE_CUBE_MAP_NEGATIVE_Z, 0, bitmap, 0);
            bitmap.recycle();


        }else
        if(mapLength==6)
        {

            Bitmap bitmap;
            for(int i=0;i<mapLength;i++)
            {
                try {
                   InputStream in = rs.getAssets().open(fileNames[i]);
                   bitmap = BitmapFactory.decodeStream(in);
                    in.close();

                } catch (IOException e) {
                    throw new RuntimeException("Couldn't load texture '" + fileNames[i]
                            + "'", e);
                }
                //顺序很重要
                GLUtils.texImage2D(GLES20.GL_TEXTURE_CUBE_MAP_POSITIVE_X+i, 0, bitmap, 0);
                bitmap.recycle();

            }


        }
        else
        {
            throw  new RuntimeException("cube map  must  use one file  for all or six file for each side ");
        }


        setFilters(GLES20.GL_NEAREST, GLES20.GL_NEAREST);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_CUBE_MAP, 0);
	}

	public void reload() {
		load();
		bind();
		setFilters(minFilter, magFilter);
		GLES20.glBindTexture(GLES20.GL_TEXTURE_CUBE_MAP, 0);
	}

	public void setFilters(int minFilter, int magFilter) {
		this.minFilter = minFilter;
		this.magFilter = magFilter;

		GLES20.glTexParameterf(GLES20.GL_TEXTURE_CUBE_MAP,
				GLES20.GL_TEXTURE_MIN_FILTER,
				minFilter);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_CUBE_MAP,
				GLES20.GL_TEXTURE_MAG_FILTER,
				magFilter);
	}

	public void bind() {

		GLES20.glBindTexture(GLES20.GL_TEXTURE_CUBE_MAP, textureId);
	}

	public void dispose() {

		GLES20.glBindTexture(GLES20.GL_TEXTURE_CUBE_MAP, textureId);
		int[] textureIds =
			{ textureId };
		GLES20.glDeleteTextures(1, textureIds, 0);
	}
}