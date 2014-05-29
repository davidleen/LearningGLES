package com.opengles.book.framework.gl;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;

import java.io.IOException;
import java.io.InputStream;

public class Texture {

	String fileName;
	int textureId;
	int minFilter;
	int magFilter;
    private boolean isMipMap;

	Resources rs;

	public Texture(Resources rs, String fileName) {
		this.rs = rs;
		this.fileName = fileName;
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
            in.close();
        } catch (IOException e) {
            throw new RuntimeException("Couldn't load texture '" + fileName
                    + "'", e);
        }
			GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
			GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
            bitmap.recycle();
			setFilters(GLES20.GL_NEAREST, GLES20.GL_NEAREST);
			GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);


	}

    /**
     * call when the screen   recreated
     */
	public void reload() {
		load();
		bind();
		setFilters(minFilter, magFilter);
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
	}

	public void setFilters(int minFilter, int magFilter) {
		this.minFilter = minFilter;
		this.magFilter = magFilter;
//        if(isMipMap)
//        {
//            //使用MipMap线性纹理采样
//            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,
//                    GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR_MIPMAP_NEAREST);
//            //使用MipMap最近点纹理采样
//            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,
//                    GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR_MIPMAP_LINEAR);
//
//        }else
//        {
//            //使用MipMap线性纹理采样
//            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,
//                    GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
//            //使用MipMap最近点纹理采样
//            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,
//                    GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
//
//
//
//        }
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
				GLES20.GL_TEXTURE_MIN_FILTER,
				minFilter);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
				GLES20.GL_TEXTURE_MAG_FILTER,
				magFilter);
	}

	public void bind() {
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
	}

	public void dispose() {

		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
		int[] textureIds =
			{ textureId };
		GLES20.glDeleteTextures(1, textureIds, 0);
	}
}