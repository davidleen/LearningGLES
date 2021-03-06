package com.opengles.book.objects;

import android.content.Context;
import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLUtils;


/**
 * 天空穹
 * @author davidleen29 
 * @create : 2014-4-29 下午11:24:07
 * @{   简单的描述这个类用途}
 */
public class NewSky extends NewAbstractSimpleObject{


	float[] vertexData;
	short[] indexData;
	private int angleSpanIndegree=5;

	public NewSky(Context context) {
		 this(context,100);
	}


    public NewSky(Context context, int radian) {
        super(context);
        createData(radian);

    }

	@Override
	protected float[] getVertexData() {
		 
		return vertexData;
	}

	@Override
	protected short[] getIndexData() {
		 
		return indexData;
	}

	 
	
	
	public void createData(float r)
	{
		
		 
		int rowCount = 90 / angleSpanIndegree  ;
		int columnCount = 360 / angleSpanIndegree ;
		 
		
		int totalCount=(rowCount+1)*(columnCount+1);
		
		 
		float angleSpanInRadian = (float) Math.toRadians(angleSpanIndegree);
		float[] attributes = new float[totalCount * STRIP_SIZE];
		short[] indics = new short[totalCount*2*3];

		float x, y, z;
		float pieceofImageT = 1.0f / rowCount ;
		float pieceofImageS = 1.0f / columnCount;
		 

		int position = 0, indexPosition = 0;
		for (int i = rowCount; i>=0; i--)
		{
			float rowAngle = (float) (i * angleSpanInRadian   );
			float sinRow = (float) Math.sin(rowAngle);
			float cosRow = (float) Math.cos(rowAngle);

			 
			y = (float) (sinRow);
			for (int j = 0; j <=columnCount; j++)
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
 

				float s =  j  * pieceofImageS ;  
				float t =  i * pieceofImageT   ;
				attributes[position++] = s;
				attributes[position++] = t;
 
				
			}
		}

		for (int i = 0; i < rowCount ; i++)
		{

			for (int j = 0; j < columnCount ; j++)
			{

				// ����ڵ���
				// v1_____v3
				// /| |
				// v0|_____|v2
				short v0 = (short) (i * (columnCount+1) + j); // ��ǰ�ڵ�
				short v1 = (short) ((i + 1) * (columnCount+1) + j);
				short v2 = (short) (i * (columnCount+1) + j + 1);
				short v3 = (short) ((i + 1) * (columnCount+1) + j + 1);
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




}
