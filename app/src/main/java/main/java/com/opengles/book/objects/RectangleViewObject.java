package com.opengles.book.objects;

import android.content.Context;
import android.graphics.Bitmap;
import com.opengles.book.ShaderUtil;
import com.opengles.book.math.Vector2;
import com.opengles.book.math.Vector3;

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
       this(context,width,height,z,new Vector2(0,0));

    }


    public RectangleViewObject(Context context, float width, float height,float z,Vector2 center) {
        super(context);
        initData(width, height,z,center);


    }


    private void initData(float width,float height,float z,Vector2 center)
	{

        float offsetWidth=width/2; float offsetHeight=height/2;

		vertexData=new float[]
		        {
		        	-offsetWidth*UNIT_SIZE+center.x,-offsetHeight*UNIT_SIZE+center.y,z,
		        	0,1,
                        offsetWidth*UNIT_SIZE+center.x,-offsetHeight*UNIT_SIZE+center.y,z,
		        	1,1,
                        offsetWidth*UNIT_SIZE+center.x, offsetHeight*UNIT_SIZE+center.y,z,
		        	1,0,
		        	-offsetWidth*UNIT_SIZE+center.x, offsetHeight*UNIT_SIZE+center.y,z,
		        	0,0
		        };
		
		indexData=new short[]{
				
			0,1,2,0,2,3
				
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
