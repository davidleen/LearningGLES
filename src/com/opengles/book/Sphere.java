package com.opengles.book;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.Random;

import android.util.Log;
import com.opengles.book.R;

/**
 * 球体
 * 
 * @author davidleen29
 * 
 */
public class Sphere {

	private static final String TAG = "Sphere";
	protected FloatBuffer mVertexBuffer;// 顶点数据缓冲
	protected ShortBuffer mIndicsBuffer;// 索引数据缓冲
	public float[] attributes;
	public short[] indics;

	private float r;

	int angleSpanIndegree = 10;
	int rowCount = 180 / angleSpanIndegree + 1;
	int columnCount = 360 / angleSpanIndegree + 1;
	int totalCount = rowCount * columnCount;
	int triangleCount = 360 / angleSpanIndegree * 360 / angleSpanIndegree; // 一个方形
																			// 2个三角形
	int indicesCount = triangleCount * 3;// 一个三角形3个点
	int stride;

	public Sphere()
	{
		this(5);
	}

	public Sphere(float r)
	{
		createData(r);
	}

	public void createData(float r)
	{
		this.r = r;
		stride = getVertextSize(true, false, true, true);
		float angleSpanInRadian = (float) Math.toRadians(angleSpanIndegree);
		attributes = new float[totalCount
				* stride];
		indics = new short[indicesCount];

		float x, y, z;
		float pieceofImageS = 1.0f / (columnCount - 1);
		float pieceofImageT = 1.0f / (columnCount - 1);
		float vectorLength;
		Random random = new Random();
		Log.d(TAG, "totalCount:" + totalCount);
		int position = 0, indexPosition = 0;
		for (int i = 0; i < rowCount; i++)
		{
			float rowAngle = (float) (i * angleSpanInRadian - Math.PI / 2);
			float sinRow = (float) Math.sin(rowAngle);
			float cosRow = (float) Math.cos(rowAngle);

			Log.d(TAG, "row:" + i);
			Log.d(TAG, "sinRow:" + sinRow + ",cosRow:" + cosRow);
			y = (float) (sinRow);
			for (int j = 0; j < columnCount; j++)
			{
				float columnAngle = j * angleSpanInRadian;
				x = (float) (cosRow
						* Math.cos(columnAngle));

				z = (float) (cosRow
						* Math.sin(columnAngle));

				attributes[position++] = x * r;
				attributes[position++] = y * r;
				attributes[position++] = z * r;

				// attributes[position++] = random.nextInt((int) r);
				// attributes[position++] = random.nextInt((int) r);
				// attributes[position++] = random.nextInt((int) r);
				// // 法向量值
				attributes[position++] = x;
				attributes[position++] = y;
				attributes[position++] = z;
				// �������
				float s = 1 - j * pieceofImageS; // 日景位置s
				float t = 1 - i * pieceofImageT - 0.5f; // 日景位置t
				attributes[position++] = s;
				attributes[position++] = t;

				attributes[position++] = s; // 夜景位置s
				attributes[position++] = t
						+ 0.5f;// 夜景位置t
				if (BuildConfig.DEBUG)
					Log.d(TAG, "index:" + (i * columnCount + j) + ",---x:" + x
							+ ",y:"
							+ y + ",z:" + z + "-----st==s:" + s + ",t:" + t);

			}
		}

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
		if (BuildConfig.DEBUG)
			Log.d(TAG, "totalCount:" + totalCount + ",position:" + position
					+ ",indexPosition:" + indexPosition);
		mVertexBuffer = FloatUtils.FloatArrayToNativeBuffer(attributes);
		mIndicsBuffer = FloatUtils.ShortArrayToNativeBuffer(indics);

	}

	int getVertextSize(boolean hasNormal, boolean hasColor,
			boolean hasTexCoord, boolean hasTexCoord1)
	{
		// 默认空间位置 3
		int size = 3;
		if (hasNormal)
			size += 3;
		if (hasColor)
			size += 4;
		if (hasTexCoord)
			size += 2;
		if (hasTexCoord1)
			size += 2;

		return size;
	}

	protected int getStride()
	{

		return stride;

	}
}
