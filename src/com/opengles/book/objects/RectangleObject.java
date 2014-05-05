package com.opengles.book.objects;

import android.content.Context;

/**
 * 平面的通用对象
 * @author Administrator
 *
 */
public class RectangleObject extends AbstractSimpleObject{

	
	private static final int UNIT_SIZE=1;
	
	private String textureFileName;
	float[] vertexData;
	short[] indexData;
	public RectangleObject(Context context, String textureFileName,int width,int height) {
		super(context);
		 this.textureFileName=textureFileName;
		 initData(width, height);
		 
		 
	}
	
	private void initData(int width,int height)
	{
		vertexData=new float[]
		        {
		        	-width/2*UNIT_SIZE,0,-height/2*UNIT_SIZE,
		        	0,0,
		        	 width/2*UNIT_SIZE,0,-height/2*UNIT_SIZE,
		        	1,0,
		        	 width/2*UNIT_SIZE,0, height/2*UNIT_SIZE,
		        	1,1,
		        	
		        	-width/2*UNIT_SIZE,0, height/2*UNIT_SIZE,
		        	0,1 
		        };
		
		indexData=new short[]{
				
			0,1,2,2,3,	0
				
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
	@Override
	protected String getBitmapFileName() {
		 
		return textureFileName;
	}
	
	
	
	
	

}
