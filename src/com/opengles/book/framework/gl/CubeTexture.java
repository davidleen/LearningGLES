package com.opengles.book.framework.gl;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;

import java.io.IOException;
import java.io.InputStream;

public class CubeTexture {

	String fileName;
	int textureId;
	int minFilter;
	int magFilter;

	Resources rs;

	public CubeTexture(Resources rs, String fileName) {
		this.rs = rs;
		this.fileName = fileName;
		load();
	}

	private void load() {

		int[] textureIds = new int[1];
		GLES20.glGenTextures(1, textureIds, 0);
		textureId = textureIds[0];


//// Bind the texture object
//        glBindTexture(GL_TEXTURE_CUBE_MAP, textureId);
//// Load the cube face - Positive X
//        glTexImage2D(GL_TEXTURE_CUBE_MAP_POSITIVE_X, 0, GL_RGB, 1, 1,
//                0, GL_RGB, GL_UNSIGNED_BYTE, &cubePixels[0]);
//// Load the cube face - Negative X
//        glTexImage2D(GL_TEXTURE_CUBE_MAP_NEGATIVE_X, 0, GL_RGB, 1, 1,
//                0, GL_RGB, GL_UNSIGNED_BYTE, &cubePixels[1]);
//// Load the cube face - Positive Y
//        glTexImage2D(GL_TEXTURE_CUBE_MAP_POSITIVE_Y, 0, GL_RGB, 1, 1,
//                0, GL_RGB, GL_UNSIGNED_BYTE, &cubePixels[2]);
//// Load the cube face - Negative Y
//        glTexImage2D(GL_TEXTURE_CUBE_MAP_NEGATIVE_Y, 0, GL_RGB, 1, 1,
//                0, GL_RGB, GL_UNSIGNED_BYTE, &cubePixels[3]);
//// Load the cube face - Positive Z
//        glTexImage2D(GL_TEXTURE_CUBE_MAP_POSITIVE_Z, 0, GL_RGB, 1, 1,
//                0, GL_RGB, GL_UNSIGNED_BYTE, &cubePixels[4]);
//// Load the cube face - Negative Z
//        glTexImage2D(GL_TEXTURE_CUBE_MAP_NEGATIVE_Z, 0, GL_RGB, 1, 1,
//                0, GL_RGB, GL_UNSIGNED_BYTE, &cubePixels[5]);

		InputStream in = null;
        Bitmap bitmap;
		try {
			in = rs.getAssets().open(fileName);
			  bitmap = BitmapFactory.decodeStream(in);
            in.close();

        } catch (IOException e) {
            throw new RuntimeException("Couldn't load texture '" + fileName
                    + "'", e);
        } finally {
            if (in != null)
                try {
                    in.close();
                } catch (IOException e) {
                }
        }
			GLES20.glBindTexture(GLES20.GL_TEXTURE_CUBE_MAP, textureId);

            //顺序很重要
			GLUtils.texImage2D(GLES20.GL_TEXTURE_CUBE_MAP_POSITIVE_X, 0, bitmap, 0);
            GLUtils.texImage2D(GLES20.GL_TEXTURE_CUBE_MAP_NEGATIVE_X, 0, bitmap, 0);

            GLUtils.texImage2D(GLES20.GL_TEXTURE_CUBE_MAP_POSITIVE_Y, 0, bitmap, 0);
            GLUtils.texImage2D(GLES20.GL_TEXTURE_CUBE_MAP_NEGATIVE_Y, 0, bitmap, 0);

            GLUtils.texImage2D(GLES20.GL_TEXTURE_CUBE_MAP_POSITIVE_Z, 0, bitmap, 0);
            GLUtils.texImage2D(GLES20.GL_TEXTURE_CUBE_MAP_NEGATIVE_Z, 0, bitmap, 0);
            bitmap.recycle();
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