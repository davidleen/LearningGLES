package com.opengles.book.galaxy;

import java.nio.FloatBuffer;
import java.util.Random;

import com.opengles.book.FloatUtils;
import com.opengles.book.MatrixState;
import com.opengles.book.ShaderUtil;

import android.content.Context;
import android.opengl.GLES20;
import android.util.Log;

/**
 * 星空
 * 
 * @author davidleen29
 * @create : 2013-12-11 下午11:51:22
 * 
 */
public class Celestial implements ObjectDrawable {

	private int UNIT_SIZE = 30;
	private int vCount = 1000;

	private FloatBuffer mVertexBuffer;

	private int mProgram;
	private float scaleSize;

	private int mPositionHandler;
	private int mMVPMatrixHandler;
	private int mPointSizeHandler;

	public Celestial(Context context)
	{

		this(context, 1000, 1);
	}

	public Celestial(Context context, int count, int scaleSize)
	{
		vCount = count;
		this.scaleSize = scaleSize;

		initVetexData();
		initShader(context);
	}

	/**
	 * \
	 * 生成顶点
	 */
	private void initVetexData()
	{

		float[] vetext = new float[vCount * 3];
		Random r = new Random();
		float x, y, z;
		for (int i = 0; i < vCount; i++)
		{
			double radiasLongitude = Math.PI * 2 * r.nextDouble();
			double radiasLatitude = Math.PI * (r.nextDouble() - 0.5);

			x = (float) (UNIT_SIZE * Math.cos(radiasLatitude) * Math
					.cos(radiasLongitude));
			y = (float) (UNIT_SIZE * Math.sin(radiasLatitude));
			z = (float) (UNIT_SIZE * Math.cos(radiasLatitude) * Math
					.sin(radiasLongitude));

			vetext[i * 3 + 0] = x;
			vetext[i * 3 + 1] = y;
			vetext[i * 3 + 2] = z;

			Log.d("TEST", "TEST   x:" + x + ",y:" + y + ",z:" + z);

		}

		mVertexBuffer = FloatUtils.FloatArrayToNativeBuffer(vetext);
	}

	private void initShader(Context context)
	{

		String vertexShader = ShaderUtil.loadFromAssetsFile(
				"celestial_vertex.glsl", context.getResources());
		String fragShader = ShaderUtil.loadFromAssetsFile(
				"celestial_frag.glsl", context.getResources());
		mProgram = ShaderUtil.createProgram(vertexShader, fragShader);

		mPositionHandler = GLES20.glGetAttribLocation(mProgram, "mPositionLoc");
		mMVPMatrixHandler = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
		mPointSizeHandler = GLES20.glGetUniformLocation(mProgram,
				"mStarSize");

	}

	@Override
	public void bind() {

	}

	@Override
	public void unBind() {

	}

	@Override
	public void draw() {
		GLES20.glUseProgram(mProgram);

		GLES20.glVertexAttribPointer(mPositionHandler, 3, GLES20.GL_FLOAT,
				false, 3 * FloatUtils.RATIO_FLOATTOBYTE, mVertexBuffer);
		GLES20.glEnableVertexAttribArray(mPositionHandler);
		GLES20.glUniformMatrix4fv(mMVPMatrixHandler, 1, false,
				MatrixState.getFinalMatrix(), 0);

		GLES20.glUniform1f(mPointSizeHandler, scaleSize);

		GLES20.glDrawArrays(GLES20.GL_POINTS, 0, vCount);
	}
}
