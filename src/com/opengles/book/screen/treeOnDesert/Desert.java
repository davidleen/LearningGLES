package com.opengles.book.screen.treeOnDesert;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import com.opengles.book.FloatUtils;
import com.opengles.book.MatrixState;
import com.opengles.book.R;
import com.opengles.book.ShaderUtil;
import com.opengles.book.galaxy.ObjectDrawable;
import com.opengles.book.objects.AbstractSimpleObject;
 

import android.content.Context;
import android.opengl.GLES20;

import static com.opengles.book.screen.TreeOnDesertScreen.*;
 

//表示沙漠的纹理矩形
public class Desert  extends AbstractSimpleObject
{	
	
	
	private String path="tree_on_desert/";
 
	 
    
    
    float[] vertexData;
    short[] indexData;
   
    
    public Desert(Context  context, int width,int height)
    {    	
    	
    	super(context);
    	 
    	//初始化顶点坐标与着色数据
    	initVertexData(width,height);
    	 
    }
    
    //初始化顶点坐标与着色数据的方法
    public void initVertexData(int width,int height)
    {
     
          vertexData =new float[]
        {
    		-UNIT_SIZE*width,0,-UNIT_SIZE*height,//position
    		0,0,									//texture
    		UNIT_SIZE*width,0,-UNIT_SIZE*height,
    		0,6,
    		UNIT_SIZE*width,0,UNIT_SIZE*height,
    		6,6,
    		
    		-UNIT_SIZE*width,0,-UNIT_SIZE*height,
    		6,6,
    		UNIT_SIZE*width,0,UNIT_SIZE*height,
    		6,0,
    		-UNIT_SIZE*width,0,UNIT_SIZE*height,
    		0,0
        };
        
          indexData=   new short[]
	            {
	          		0, 1,2,3,4,5 
	          		 
	            } ;
     
        
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
		 
		return path+"desert.bmp";
	}
    
}