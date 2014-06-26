package com.opengles.book.objects;

import android.content.Context;
import android.opengl.GLES20;

import com.opengles.book.FloatUtils;
import com.opengles.book.MatrixState;
import com.opengles.book.ShaderUtil;
import com.opengles.book.framework.gl.CubeTexture;
import com.opengles.book.galaxy.ObjectDrawable;

/**
 * 立方体构造  使用立体纹理
 * @author davidleen29
 *
 */
public class CubeDrawer extends AbstractSimple3DObject{
	
	private static    int  DEFAULT_UNIT_SIZE=1;

	

	private float[] vertexData;
	private short[] indexData;

    public CubeDrawer(Context context)
    {
        this(context,DEFAULT_UNIT_SIZE);
    }
	public CubeDrawer(Context context,int unitSize)
	{
		super(context);
		  vertexData=new float[]{
           //vertex          //normal
			1,1,1,   	       1,1,1,

			1,1,-1,         	1,1,-1,
			1,-1,1,            1,-1,1,
			1,-1,-1,           1,-1,-1,
			
			-1,1,1,	           -1,1,1,
			-1,1,-1,           -1,1,-1,
			-1,-1,1,           -1,-1,1,
			-1,-1,-1,         -1,-1,-1
			 
				
		};
		  for(int i=0;i<vertexData.length;i++)
		  {
			  vertexData[i]*=unitSize;
		  }
			
		 indexData=new short[]{
				
				//右侧面
				0,1,2,
				1,3,2


                 //前侧面
                 ,  4,0,6,
                 6,0,2

                 //左侧面
                 , 5,4,7,
                 7,4,6

                 //下侧面
                 ,    2,3,6,
                 6,3,7

                 //上侧面
                 , 4,5,1,
                 4,1,0

                 //后侧面
                 , 1,5,3,
                 3,5,7
				
		};
		

	}


    @Override
    protected float[] getVertexData() {
        return vertexData;
    }


    @Override
    protected short[] getIndexData() {
        return  indexData;
    }
}
