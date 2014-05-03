package com.opengles.book.objects;

import java.util.Random;

import com.opengles.book.BuildConfig;
import com.opengles.book.FloatUtils;

import android.content.Context;
import android.util.Log;

/**
 * 天空穹
 * @author davidleen29 
 * @create : 2014-4-29 下午11:24:07
 * @{   简单的描述这个类用途}
 */
public class Sky  extends AbstractSimpleObject{
	
	
	float[] vertexData;
	short[] indexData;
	private int angleSpanIndegree=5;
	public String path="sky/";

	public Sky(Context context) {
		super(context);
		createData(1000);
		 
	}

	@Override
	protected float[] getVertexData() {
		 
		return vertexData;
	}

	@Override
	protected short[] getIndexData() {
		 
		return indexData;
	}

	@Override
	protected String getBitmapFileName() {
		 
		return path+"sky.png";
	}

	
	
	public void createData(float r)
	{
		
		 
		int rowCount = 90 / angleSpanIndegree  ;
		int columnCount = 360 / angleSpanIndegree ;
		 
		
		int totalCount=rowCount*columnCount;
		
		 
		float angleSpanInRadian = (float) Math.toRadians(angleSpanIndegree);
		float[] attributes = new float[totalCount * STRIP_SIZE];
		short[] indics = new short[totalCount*2*3];

		float x, y, z;
		float pieceofImageS = 1.0f / rowCount ;
		float pieceofImageT = 1.0f / columnCount;
		 
		Log.d(TAG, "totalCount:" + totalCount);
		int position = 0, indexPosition = 0;
		for (int i = 0; i < rowCount; i++)
		{
			float rowAngle = (float) (i * angleSpanInRadian  );
			float sinRow = (float) Math.sin(rowAngle);
			float cosRow = (float) Math.cos(rowAngle);

			 
			y = (float) (sinRow);
			for (int j = 0; j < columnCount; j++)
			{
				float columnAngle = j * angleSpanInRadian;
				x = (float) (cosRow
						* Math.cos(columnAngle));

				z = (float) (cosRow
						* Math.sin(columnAngle));

				
				//xyz
				attributes[position++] = x * r;
				attributes[position++] = y * r;
				attributes[position++] = z * r;
 
				// �������
				float s =   i * pieceofImageS;  
				float t =   j* pieceofImageT  ; // 日景位置t
				attributes[position++] = s;
				attributes[position++] = t;
 
				
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
				 

			}
		}
		 
		
		vertexData=attributes;
		indexData=indics;

	}
	
	@Override
	public boolean isMixMap()
	{
		return false;
	}
}
