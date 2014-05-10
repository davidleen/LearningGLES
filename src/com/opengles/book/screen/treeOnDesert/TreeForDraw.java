package com.opengles.book.screen.treeOnDesert;

 

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import com.opengles.book.MatrixState;
import com.opengles.book.ShaderUtil;
import com.opengles.book.galaxy.ObjectDrawable;
import com.opengles.book.objects.AbstractSimpleObject;
 

import android.content.Context;
import android.opengl.GLES20;
import static com.opengles.book.screen.TreeOnDesertScreen.*;

public class TreeForDraw extends AbstractSimpleObject
{
	
	private String path="tree_on_desert/";
	 float[]   vertexData;
	 short[]  indexData;
	 static int  textureId=-1;
    public TreeForDraw(Context context)
    {
    	super(context);
    	 
    	initVertexData();
    	//三
    	if(textureId==-1)
    		textureId=ShaderUtil.loadTextureWithUtils(context, path+"tree.png", false);
    	 
    }
    //初始化顶点数据的方法
    public void initVertexData()
    {
    	 
          vertexData=new float[]
        {
        	-UNIT_SIZE*3,0,0,//position;
        	0,1,					//texture;
            UNIT_SIZE*3,0,0,  
            1,1,
            UNIT_SIZE*3,UNIT_SIZE*5,0,
            1,0,
            
            
            UNIT_SIZE*3,UNIT_SIZE*5,0,
            1,0,
            -UNIT_SIZE*3,UNIT_SIZE*5,0,
            0,0,
            -UNIT_SIZE*3,0,0,
            0,1
        };
          
          indexData=   new short[]
  	            {
  	          		0,1,2,3,4,5
  	          		 
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
	protected int getTextureId() {
		 
		return textureId;
	}
    
    
}