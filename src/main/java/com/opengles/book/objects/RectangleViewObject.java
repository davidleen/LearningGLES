package com.opengles.book.objects;

import android.content.Context;
import android.graphics.Bitmap;
import com.opengles.book.ShaderUtil;

/**
 * 平面的通用对象
 * @author Administrator
 *
 */
public    class RectangleViewObject extends NewAbstractSimpleObject{


	private static final int UNIT_SIZE=1;

	float[] vertexData;
	short[] indexData;



	public RectangleViewObject(Context context, int width, int height ) {
		this(context,width,height,0);

	   
		}


    public RectangleViewObject(Context context, float width, float height,float z) {
        super(context);
        initData(width, height,z);


    }


    private void initData(float width,float height,float z)
	{
		vertexData=new float[]
		        {
		        	-width*UNIT_SIZE,-height*UNIT_SIZE,z,
		        	0,0,
		        	 width*UNIT_SIZE,-height*UNIT_SIZE,z,
		        	1,0,
		        	 width*UNIT_SIZE, height*UNIT_SIZE,z,
		        	1,1,
		        	-width*UNIT_SIZE, height*UNIT_SIZE,z,
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
