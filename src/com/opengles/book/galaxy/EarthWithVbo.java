package com.opengles.book.galaxy;

import com.opengles.book.FloatUtils;
import com.opengles.book.LightSources;
import com.opengles.book.MatrixState;
import com.opengles.book.R;
import com.opengles.book.ShaderUtil;
import com.opengles.book.Sphere;

import android.content.Context;
import android.opengl.GLES20;
import android.util.Log;

public class EarthWithVbo implements ObjectDrawable {

	protected static final int VERTEX_POS_SIZE = 3;// xyz
	protected static final int VERTEX_NORMAL_SIZE = 3;// xyz
	protected static final int VERTEX_TEXCOORD0_SIZE = 2;// s t
	protected static final int VERTEX_TEXCOORD1_SIZE = 2;// s t

	protected static final int STRIP_SIZE = (VERTEX_POS_SIZE
			+ VERTEX_NORMAL_SIZE
			+ VERTEX_TEXCOORD0_SIZE + VERTEX_TEXCOORD1_SIZE)
			* FloatUtils.RATIO_FLOATTOBYTE;

	String TAG = "Triangle";
	int mProgram;// �Զ�����Ⱦ���߳���id
	int muMVPMatrixHandle;// �ܱ任��������id
	int mUMatrixHandle; // 物体变换矩阵句柄
	int maPositionHandle; // ����λ����������id
	int maDayTexCoorHandle; // �������������������id
	int maNightTexCoorHandle; // �������������������id
	int muLightLocationSun;
	int mabientLightHandler;// 环境光
	int mDiffuseLightHandler;// 散射光
	int mSpecLightHandler;// 反射光
	int mNormalPositionLoc;// 法向量

	int mCameraPositionHandler;// 相机位置
	String mVertexShader;// ������ɫ��
	String mFragmentShader;// ƬԪ��ɫ��
	int mMapLoc;
	protected Sphere sphere;

	int mMapLocImageId;
	int[] vboIds;
	int indicsLength;

	public EarthWithVbo(Context mv) {
		// ��ʼ�������������ɫ���
		initVertexData();
		// ��ʼ����ɫ��
		initShader(mv);

	}

	// ��ʼ�������������ɫ��ݵķ���
	public void initVertexData()
	{
		sphere = new Sphere();

	}

	// ��ʼ����ɫ��
	public void initShader(Context mv)
	{
		// ���ض�����ɫ���Ľű�����
		mVertexShader = ShaderUtil.loadFromAssetsFile("earth_vertex.glsl",
				mv.getResources());
		// ����ƬԪ��ɫ���Ľű�����
		mFragmentShader = ShaderUtil.loadFromAssetsFile("earth_frag.glsl",
				mv.getResources());
		// ���ڶ�����ɫ����ƬԪ��ɫ����������
		mProgram = ShaderUtil.createProgram(mVertexShader, mFragmentShader);

		Log.d(TAG, "mProgram" + mProgram);
		// ��ȡ�����ж���λ����������id
		maPositionHandle = GLES20.glGetAttribLocation(mProgram, "mPositionLoc");

		Log.d(TAG, "maPositionHandle" + maPositionHandle);

		mDiffuseLightHandler = GLES20.glGetUniformLocation(mProgram,
				"diffuseLight");

		mSpecLightHandler = GLES20.glGetUniformLocation(mProgram,
				"mSpecLight");
		// 日景id
		maDayTexCoorHandle = GLES20.glGetAttribLocation(mProgram,
				"mDayTexCoordLoc");
		maNightTexCoorHandle = GLES20.glGetAttribLocation(mProgram,
				"mNightTexCoordLoc");

		mNormalPositionLoc = GLES20.glGetAttribLocation(mProgram,
				"mNormalPositionLoc");

		// 总变换阵 id
		muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
		// 物体运动针id
		mUMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMMatrix");

		mMapLoc = GLES20.glGetUniformLocation(mProgram, "mMapLoc");

		muLightLocationSun = GLES20.glGetUniformLocation(mProgram,
				"uLightLocationSun");

		mabientLightHandler = GLES20.glGetUniformLocation(mProgram,
				"abientLight");

		mCameraPositionHandler = GLES20.glGetUniformLocation(mProgram,
				"cameraPosition");

		mMapLocImageId = ShaderUtil.loadTextureWithUtils(mv.getResources()
				.openRawResource(R.raw.earth_double));
		Log.d(TAG, "mMapLocImageId:" + mMapLocImageId);

	}

	@Override
	public void bind()
	{
		indicsLength = sphere.indics.length;
		vboIds = new int[2];

		GLES20.glGenBuffers(2, vboIds, 0);

		ShaderUtil.createVertexBuffer(GLES20.GL_ARRAY_BUFFER,
				sphere.attributes,
				vboIds[0]);
		ShaderUtil.createIndexBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER,
				sphere.indics,
				vboIds[1]);

		Log.d(TAG, "vboIds:" + vboIds[0] + "," + vboIds[1]);

		// 预先注入固定值。 会变化的值 在draw中调整。
		GLES20.glUseProgram(mProgram);

		// 注入环境光光数据
		GLES20.glUniform4fv(mabientLightHandler, 1,
				LightSources.ambientBuffer);

		// 注入散射光数据
		GLES20.glUniform4fv(mDiffuseLightHandler, 1,
				LightSources.diffuseBuffer);

		// 注入散射光数据
		GLES20.glUniform4fv(mSpecLightHandler, 1,
				LightSources.specLightBuffer);

		// 注入相机位置数据
		GLES20.glUniform3fv(mCameraPositionHandler, 1,
				MatrixState.cameraFB);

		GLES20.glUseProgram(0);
	}

	@Override
	public void unBind()
	{

		GLES20.glDeleteBuffers(2, vboIds, 0);

	}

	@Override
	public void draw() {

		GLES20.glUseProgram(mProgram);

		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mMapLocImageId);

		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vboIds[0]);
		GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, vboIds[1]);

		// 启用位置向量数据
		GLES20.glEnableVertexAttribArray(maPositionHandle);
		// 启用法向量数据
		GLES20.glEnableVertexAttribArray(mNormalPositionLoc);
		GLES20.glEnableVertexAttribArray(maDayTexCoorHandle);
		GLES20.glEnableVertexAttribArray(maNightTexCoorHandle);
		int stride = STRIP_SIZE;

		int offset = 0;

		GLES20.glVertexAttribPointer
				(
						maPositionHandle,
						VERTEX_POS_SIZE,
						GLES20.GL_FLOAT,
						false,
						stride,
						offset
				);

		// 启用法向量
		offset += VERTEX_POS_SIZE * FloatUtils.RATIO_FLOATTOBYTE;

		GLES20.glVertexAttribPointer
				(
						mNormalPositionLoc,
						VERTEX_NORMAL_SIZE,
						GLES20.GL_FLOAT,
						false,
						stride,
						offset
				);

		// 启用日景纹理
		offset += VERTEX_NORMAL_SIZE * FloatUtils.RATIO_FLOATTOBYTE;

		GLES20.glVertexAttribPointer
				(
						maDayTexCoorHandle,
						VERTEX_TEXCOORD0_SIZE,
						GLES20.GL_FLOAT,
						false,
						stride,
						offset
				);

		offset += VERTEX_TEXCOORD0_SIZE * FloatUtils.RATIO_FLOATTOBYTE;

		GLES20.glVertexAttribPointer
				(
						maNightTexCoorHandle,
						VERTEX_TEXCOORD1_SIZE,
						GLES20.GL_FLOAT,
						false,
						stride,
						offset
				);

		GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false,
				MatrixState.getFinalMatrix(), 0);

		GLES20.glUniformMatrix4fv(mUMatrixHandle, 1, false,
				MatrixState.getMMatrix(), 0);

		// 注入太阳光位置 光线会转动
		GLES20.glUniform3fv(muLightLocationSun, 1,
				LightSources.lightPositionFBSun);
		 
	 
		GLES20.glDrawElements(GLES20.GL_TRIANGLES, indicsLength,
				GLES20.GL_UNSIGNED_SHORT, 0);

		GLES20.glDisableVertexAttribArray(maPositionHandle);

		GLES20.glDisableVertexAttribArray(mNormalPositionLoc);
		GLES20.glDisableVertexAttribArray(maDayTexCoorHandle);
		GLES20.glDisableVertexAttribArray(maNightTexCoorHandle);

		// jiechu
		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
		GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, 0);

	}

    @Override
    public void update(float deltaTime) {

    }
}
