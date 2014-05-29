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

public class MoonWithVbo implements ObjectDrawable
{
	protected static final int VERTEX_POS_INDEX = 0;// xyz
	protected static final int VERTEX_NORMAL_INDEX = 1;// xyz
	protected static final int VERTEX_TEXCOORD0_INDEX = 2;// s t
	protected static final int VERTEX_TEXCOORD1_INDEX = 3;// s t
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

	int muLightLocationSun;
	int mabientLightHandler;// 环境光
	int mDiffuseLightHandler;// 散射光
	int mSpecLightHandler;// 反射光
	

	int mCameraPositionHandler;// 相机位置
	String mVertexShader;// ������ɫ��
	String mFragmentShader;// ƬԪ��ɫ��
	int mMapLoc;
	protected Sphere sphere;

	int mMapLocImageId;



	int[] vboIds;
	int indicsLength;

	public MoonWithVbo(Context mv)
	{

		initVertexData();

		initShader(mv);

	}

	public void initVertexData()
	{
		sphere = new Sphere(3);

	}

	// ��ʼ����ɫ��
	public void initShader(Context mv)
	{
		// ���ض�����ɫ���Ľű�����
		mVertexShader = ShaderUtil.loadFromAssetsFile("moon_vertex.glsl",
				mv.getResources());
		// ����ƬԪ��ɫ���Ľű�����
		mFragmentShader = ShaderUtil.loadFromAssetsFile("moon_frag.glsl",
				mv.getResources());
		// ���ڶ�����ɫ����ƬԪ��ɫ����������
		mProgram = ShaderUtil.createProgram(mVertexShader, mFragmentShader);

		GLES20.glBindAttribLocation(mProgram, VERTEX_POS_INDEX, "mPositionLoc");
		GLES20.glBindAttribLocation(mProgram, VERTEX_NORMAL_INDEX,
				"mNormalPositionLoc");
		GLES20.glBindAttribLocation(mProgram, VERTEX_TEXCOORD0_INDEX,
				"mDayTexCoordLoc");
		GLES20.glBindAttribLocation(mProgram, VERTEX_TEXCOORD1_INDEX,
				"mNightTexCoordLoc");
		// it is import here . relink programss let bind work
		GLES20.glLinkProgram(mProgram);

		Log.d(TAG, "mProgram" + mProgram);

		mDiffuseLightHandler = GLES20.glGetUniformLocation(mProgram,
				"diffuseLight");

		mSpecLightHandler = GLES20.glGetUniformLocation(mProgram,
				"mSpecLight");

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
				.openRawResource(R.raw.moon_double));
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

		// 预先绑定固定值 不需要在绘制时候重新绑定。
		GLES20.glUseProgram(mProgram);
		// 绑定固定值， 会变换的数值 在draw中绑定。
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
	public void unBind() {
		GLES20.glDeleteBuffers(2, vboIds, 0);

	}

	@Override
	public void draw() {

		GLES20.glUseProgram(mProgram);
 

		 

		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mMapLocImageId);

		// 启用位置向量数据
		GLES20.glEnableVertexAttribArray(VERTEX_POS_INDEX);
		// 启用法向量数据
		GLES20.glEnableVertexAttribArray(VERTEX_NORMAL_INDEX);
		GLES20.glEnableVertexAttribArray(VERTEX_TEXCOORD0_INDEX);
		GLES20.glEnableVertexAttribArray(VERTEX_TEXCOORD1_INDEX);

		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vboIds[0]);
		GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, vboIds[1]);

		int offset = 0;
		int stride = STRIP_SIZE;
		GLES20.glVertexAttribPointer
				(
						VERTEX_POS_INDEX,
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
						VERTEX_NORMAL_INDEX,
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
						VERTEX_TEXCOORD1_INDEX,
						VERTEX_TEXCOORD1_SIZE,
						GLES20.GL_FLOAT,
						false,
						stride,
						offset
				);

		offset += VERTEX_TEXCOORD0_SIZE * FloatUtils.RATIO_FLOATTOBYTE;

		GLES20.glVertexAttribPointer
				(
						VERTEX_TEXCOORD0_INDEX,
						VERTEX_TEXCOORD0_SIZE,
						GLES20.GL_FLOAT,
						false,
						stride,
						offset
				);

		// 总变换矩阵
		GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false,
				MatrixState.getFinalMatrix(), 0);

		// 物体移动变换矩阵。
		GLES20.glUniformMatrix4fv(mUMatrixHandle, 1, false,
				MatrixState.getMMatrix(), 0);

		// 注入太阳光位置 光线会转动
		GLES20.glUniform3fv(muLightLocationSun, 1,
				LightSources.lightPositionFBSun);

		GLES20.glDrawElements(GLES20.GL_TRIANGLES, indicsLength,
				GLES20.GL_UNSIGNED_SHORT, 0);

		// jiechu
		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
		GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, 0);
		// 启用位置向量数据
		GLES20.glDisableVertexAttribArray(VERTEX_POS_INDEX);
		// 启用法向量数据
		GLES20.glDisableVertexAttribArray(VERTEX_NORMAL_INDEX);
		GLES20.glDisableVertexAttribArray(VERTEX_TEXCOORD0_INDEX);
		GLES20.glDisableVertexAttribArray(VERTEX_TEXCOORD1_INDEX);
	}

    @Override
    public void update(float deltaTime) {

    }
}
