package com.opengles.book.objects;

import android.content.Context;
import android.opengl.GLES20;

import com.opengles.book.FloatUtils;
import com.opengles.book.MatrixState;
import com.opengles.book.ShaderUtil;
import com.opengles.book.framework.gl.CubeTexture;
import com.opengles.book.galaxy.ObjectDrawable;

/**
 * 立方体构造
 * @author davidleen29
 *
 */
public class CubeDrawer extends NewAbstractSimpleObject{
	
	private   int  unitSize=1;
	private CubeTexture texture;
	

	private float[] vertexData;
	private short[] indexData;

    public CubeDrawer(Context context)
    {
        this(context,1);
    }
	public CubeDrawer(Context context,int unitSize)
	{
		super(context);
		  vertexData=new float[]{
			1,1,1,	
			1,1,-1,
			1,-1,1,
			1,-1,-1,
			
			-1,1,1,	
			-1,1,-1,
			-1,-1,1,
			-1,-1,-1
			 
				
		};
		  for(int i=0;i<vertexData.length;i++)
		  {
			  vertexData[i]*=unitSize;
		  }
			
		 indexData=new short[]{
				
				//右侧面
				0,1,2,
				0,2,3,
				
				//前侧面
				0,3,4,
				3,7,4,
				
				//左侧面
				4,7,5,
				7,6,5,
				
				//下侧面
				2,3,6,
				2,6,7,
				
				//上侧面
				0,1,5,
				0,5,4,
				
				//后侧面
				1,2,5,
				2,5,6
				
		};
		
		this.texture=texture;
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
