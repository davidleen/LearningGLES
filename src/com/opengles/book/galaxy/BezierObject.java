package com.opengles.book.galaxy;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.opengl.GLES20;
import android.util.Log;

import com.opengles.book.BuildConfig;
import com.opengles.book.FloatUtils;
import com.opengles.book.LightSources;
import com.opengles.book.MatrixState;
import com.opengles.book.R;
import com.opengles.book.ShaderUtil;
import com.opengles.book.bn.BNPosition;
import com.opengles.book.bn.BezierUtil;

public class BezierObject implements ObjectDrawable {

	private static final String TAG = "BezierObject";

	protected static final int VERTEX_POS_INDEX = 0;// xyz
	protected static final int VERTEX_NORMAL_INDEX = 1;// xyz
	protected static final int VERTEX_TEXCOORD0_INDEX = 2;// s t
	protected static final int VERTEX_TEXCOORD1_INDEX = 3;// s1 t1

	protected static final int VERTEX_POS_SIZE = 3;// xyz
	protected static final int VERTEX_NORMAL_SIZE = 3;// xyz
	protected static final int VERTEX_TEXCOORD0_SIZE = 2;// s t
	protected static final int VERTEX_TEXCOORD1_SIZE = 2;// s t

	protected int STRIP_SIZE;
	int mProgram;//
	int muMVPMatrixHandle;// 总变换矩阵
	int mUMatrixHandle; // 物体变换矩阵句柄

	int muLightLocationSun; // 太阳光位置
	int mabientLightHandler;// 环境光
	int mDiffuseLightHandler;// 散射光
	int mSpecLightHandler;// 反射光

	int mCameraPositionHandler;// 相机位置
	int mMapLoc;
	int mMapLocImageId;

	float[] vetexs;
	short[] indics;

	public BezierObject(Context context)
	{

		// 以下是贝赛尔曲线的实现代码
		BezierUtil.al.clear();// 清空数据点列表

		// 加入数据点
		BezierUtil.al.add(new BNPosition(0, 1));
		BezierUtil.al.add(new BNPosition(1, 2));
		BezierUtil.al.add(new BNPosition(2, 3));
		BezierUtil.al.add(new BNPosition(3, 4));
		BezierUtil.al.add(new BNPosition(4,5));
		BezierUtil.al.add(new BNPosition(5, 7));
		BezierUtil.al.add(new BNPosition(7, 8));
		BezierUtil.al.add(new BNPosition(9, 11));

		int nRow = 100;

		List<BNPosition> bezierArray;

		bezierArray = BezierUtil.getBezierData(1.0f / nRow);

		int count = bezierArray.size();
		int roundCount = 360;
		STRIP_SIZE = VERTEX_POS_SIZE + VERTEX_NORMAL_SIZE
				+ VERTEX_TEXCOORD0_SIZE + VERTEX_TEXCOORD1_SIZE;
		vetexs = new float[count * roundCount * STRIP_SIZE];

		indics = new short[count * roundCount * 2 * 3];
		BNPosition bnp;
		// 计算曲线 旋转后 在3d 空间上的位置
		int index = 0;
		for (int i = 0; i < count; i++) {

			bnp = bezierArray.get(i);

			// 绕x 轴旋转 y为半径值。
			float r = Math.abs(bezierArray.get(i).y);
			float s = 1.0f / 360;
			double radias;
			for (int j = 0; j < roundCount; j++)
			{

				radias = Math.toRadians(j);
				float y = (float) (r * Math.cos(radias));
				float z = (float) (r * Math.sin(radias));

				vetexs[index++] = bnp.x;
				vetexs[index++] = y;
				vetexs[index++] = z;

				vetexs[index++] = bnp.x;
				vetexs[index++] = y;
				vetexs[index++] = z;

				vetexs[index++] = s;
				vetexs[index++] = j / (float) roundCount;

				vetexs[index++] = s;
				vetexs[index++] = j / (float) roundCount;

			}

		}

		int rowCount = count;
		int columnCount = roundCount;
		int indexPosition = 0;
		for (int i = 0; i < rowCount - 1; i++)
		{

			for (int j = 0; j < columnCount - 1; j++)
			{

				// ����ڵ���
				// v1_____v3
				// /| |
				// v0|_____|v2
				short v0 = (short) (i * columnCount + j); // ��ǰ�ڵ�
				short v1 = (short) ((i + 1) * columnCount + j);
				short v2 = (short) (i * columnCount + j + 1);
				short v3 = (short) ((i + 1) * columnCount + j + 1);
				indics[indexPosition++] = v0;
				indics[indexPosition++] = v1;
				indics[indexPosition++] = v3;

				indics[indexPosition++] = v0;
				indics[indexPosition++] = v3;
				indics[indexPosition++] = v2;
				if (BuildConfig.DEBUG)
				{
					Log.d(TAG, "i:" + i + ",j:" + j);
					Log.d(TAG, "v0:" + v0 + ",v1:" + v1 + ",v3:" + v3);
					Log.d(TAG, "v0:" + v0 + ",v3:" + v3 + ",v2:" + v2);
				}

			}
		}
		initShader(context);
	}

	private void initShader(Context mv)
	{
		// ���ض�����ɫ���Ľű�����
		String mVertexShader = ShaderUtil.loadFromAssetsFile(
				"moon_vertex.glsl",
				mv.getResources());
		// ����ƬԪ��ɫ���Ľű�����
		String mFragmentShader = ShaderUtil.loadFromAssetsFile(
				"moon_frag.glsl",
				mv.getResources());
		// ���ڶ�����ɫ����ƬԪ��ɫ����������
		mProgram = ShaderUtil.createProgram(mVertexShader, mFragmentShader);

		GLES20.glBindAttribLocation(mProgram, VERTEX_POS_INDEX, "mPositionLoc");
		GLES20.glBindAttribLocation(mProgram, VERTEX_NORMAL_INDEX,
				"mNormalPositionLoc");
		GLES20.glBindAttribLocation(mProgram, VERTEX_TEXCOORD0_INDEX,
				"mDayTexCoordLoc");

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

	int indicsLength;
	int[] vboIds;

	@Override
	public void bind() {
		indicsLength = indics.length / 3;

		vboIds = new int[2];

		GLES20.glGenBuffers(2, vboIds, 0);

		ShaderUtil.createVertexBuffer(GLES20.GL_ARRAY_BUFFER,
				vetexs,
				vboIds[0]);
		ShaderUtil.createIndexBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER,
				indics,
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

}
