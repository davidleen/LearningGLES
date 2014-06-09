package com.opengles.book.objects;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.Random;

import android.util.Log;

import com.opengles.book.BuildConfig;
import com.opengles.book.FloatUtils;
import com.opengles.book.R;

/**
 * 球体构造
 * 
 * @author davidleen29
 * 
 */
public class Sphere {

	private static final String TAG = "Sphere";
	 
	public float[] attributes;
	public short[] indics;
    protected static final int VERTEX_POS_SIZE = 3;// xyz
    protected static final int VERTEX_NORMAL_SIZE = 3;// xyz
    protected static final int VERTEX_TEXCOORD_SIZE = 2;// s t


             ;

	int angleSpanIndegree = 10;
	int rowCount = 180 / angleSpanIndegree + 1;
	int columnCount = 360 / angleSpanIndegree + 1;
	int totalCount = rowCount * columnCount;
	int triangleCount = totalCount*2 ; // 一个方形// 2个三角形
																			
	int indicesCount = triangleCount * 3;// 一个三角形3个点
	 

	public Sphere()
	{
		this(5);
	}

	public Sphere(float r)
	{
		createData(r,false);
	}

    public Sphere(float r,boolean hasNormal)
    {
        createData(r,true);
    }

	public void createData(float r,boolean  hasNormal)
	{
		 
		int 	stride =
                VERTEX_POS_SIZE+
                        (hasNormal? VERTEX_NORMAL_SIZE:0)+
                + VERTEX_TEXCOORD_SIZE;


		float angleSpanInRadian = (float) Math.toRadians(angleSpanIndegree);
		attributes = new float[totalCount
				* stride];
		indics = new short[indicesCount];

		float x, y, z;
		float pieceofImageS = 1.0f / (rowCount - 1);
		float pieceofImageT = 1.0f / (columnCount - 1);
		 
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
                if(hasNormal)
                {
				attributes[position++] = x ;
				attributes[position++] = y ;
				attributes[position++] =z ;
                }
				// �������
				float s = 1 - j * pieceofImageS; 
				float t = 1 - i * pieceofImageT ;
				attributes[position++] = s;
				attributes[position++] = t;
			 
				 

			}
		}

		for (int i = 0; i < rowCount - 1; i++)
		{

			for (int j = 0; j < columnCount - 1; j++)
			{

				// 划分四边形 变成2个三角形
				// v1_____v3
				// /| |
				// v0|_____|v2
				short v0 = (short) (i * columnCount + j); //
				short v1 = (short) ((i + 1) * columnCount + j);
				short v2 = (short) (i * columnCount + j + 1);
				short v3 = (short) ((i + 1) * columnCount + j + 1);
				indics[indexPosition++] = v0;
				indics[indexPosition++] = v1;
				indics[indexPosition++] = v3;

				indics[indexPosition++] = v0;
				indics[indexPosition++] = v3;
				indics[indexPosition++] = v2;
				 

			}
		}
		 
		 

	}

	 

	 
}
