package com.opengles.book;

import java.io.IOException;
import java.io.InputStream;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import com.opengles.book.R;

public class Texture {

	String fileName;
	int textureId;
	int minFilter;
	int magFilter;
	public int width;
	public int height;
	Resources rs;

	public Texture(Resources rs, String fileName) {
		this.rs = rs;
		this.fileName = fileName;
		load();
	}

	private void load() {

		int[] textureIds = new int[1];
		GLES20.glGenTextures(1, textureIds, 0);
		textureId = textureIds[0];

		InputStream in = null;
		try {
			in = rs.getAssets().open(fileName);
			Bitmap bitmap = BitmapFactory.decodeStream(in);
			GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
			GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
			setFilters(GLES20.GL_NEAREST, GLES20.GL_NEAREST);
			GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
			width = bitmap.getWidth();
			height = bitmap.getHeight();
			bitmap.recycle();
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
	}

	public void reload() {
		load();
		bind();
		setFilters(minFilter, magFilter);
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
	}

	public void setFilters(int minFilter, int magFilter) {
		this.minFilter = minFilter;
		this.magFilter = magFilter;

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