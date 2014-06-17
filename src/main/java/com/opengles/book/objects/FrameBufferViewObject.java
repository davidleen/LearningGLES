package com.opengles.book.objects;

import android.content.Context;
import android.graphics.Bitmap;
import com.opengles.book.ShaderUtil;

/**
 * 平面的通用对象
 * @author Administrator
 *
 */
public    class FrameBufferViewObject extends NewAbstractSimpleObject{


	private static final int UNIT_SIZE=1;

	float[] vertexData;
	short[] indexData;



	public FrameBufferViewObject(Context context, int width, int height) {
		super(context);
		 initData(width, height);

	   
		}
	
	
	private void initData(int width,int height)
	{
		vertexData=new float[]
		        {
		        	-width*UNIT_SIZE,0,-height*UNIT_SIZE,
		        	0,0,
		        	 width*UNIT_SIZE,0,-height*UNIT_SIZE,
		        	1,0,
		        	 width*UNIT_SIZE,0, height*UNIT_SIZE,
		        	1,1,
		        	-width*UNIT_SIZE,0, height*UNIT_SIZE,
		        	0,1 
		        };
		
		indexData=new short[]{
				
			2,1,0,0,3,2
				
		};
		
		
	}
	@Override
	protected float[] getVertexData() {
		 
		return vertexData;
	}
	@Override
	protected short[] getIndexData() {
		 
		return indexData;
	}

	 




}
