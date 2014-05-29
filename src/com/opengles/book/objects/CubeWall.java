package com.opengles.book.objects;

import android.content.Context;
import android.opengl.GLES20;

import com.opengles.book.FloatUtils;
import com.opengles.book.MatrixState;
import com.opengles.book.ShaderUtil;
import com.opengles.book.framework.gl.CubeTexture;
import com.opengles.book.galaxy.ObjectDrawable;

/**
 * 天空墙对象
 * @author davidleen29
 *
 */
public class CubeWall implements ObjectDrawable{
	
	private static int  UNIT_SIZE=1;
	private CubeTexture texture;
	
	private int[] vboIds;
	
	private float[] vertexData;
	private short[] indexData;
	private int mProgram;
	private int maPositionHandle;
	private int cubeMapHandler;
	private int muMVPMatrixHandle;
	
	public CubeWall(CubeTexture texture)
	{
		
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
			  vertexData[i]*=UNIT_SIZE;
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
	public void bind() {
		
		
		vboIds = new int[2];

		GLES20.glGenBuffers(2, vboIds, 0);

		ShaderUtil.createVertexBuffer(GLES20.GL_ARRAY_BUFFER,
                vertexData,
				vboIds[0]);

		 
		 
		ShaderUtil.createIndexBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER,
				indexData,
				vboIds[1]);
		texture.reload();
		
	}

	@Override
	public void unBind() {
		 texture.dispose();
		
	}

	@Override
	public void draw() {
		 
		 //指定使用某套shader程序
	   	 GLES20.glUseProgram(mProgram);


           GLES20.glEnable(GLES20.GL_TEXTURE_CUBE_MAP);
//	     	 //绑定纹理  GL_TEXTURE0 对应 0 
           texture.bind();
          GLES20.glUniform1f(cubeMapHandler,0); 
          //注入总变换矩阵
	        GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(), 0);
         
          
          // 启用位置向量数据
			GLES20.glEnableVertexAttribArray(maPositionHandle);
			  GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vboIds[0]);
				GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, vboIds[1]);

			  GLES20.glDrawElements(GLES20.GL_TRIANGLES, indexData.length,
                      GLES20.GL_UNSIGNED_SHORT, 0);
			  
			  GLES20.glDisable(GLES20.GL_TEXTURE_CUBE_MAP);

	      
	}

	@Override
	public void update(float deltaTime) {
		// TODO Auto-generated method stub
		
	}
	
	 //初始化shader
    public void initShader(Context mv)
    {
    	//加载顶点着色器的脚本内容
      String  mVertexShader=ShaderUtil.loadFromAssetsFile("simple_cube/vertex.glsl", mv.getResources());
        //加载片元着色器的脚本内容
      String   mFragmentShader=ShaderUtil.loadFromAssetsFile("simple_cube/frag.glsl", mv.getResources());
        //基于顶点着色器与片元着色器创建程序
        mProgram = ShaderUtil.createProgram(mVertexShader, mFragmentShader);
      //获取程序中顶点位置属性引用id  
        maPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");
        //立体纹理对应的id
        cubeMapHandler= GLES20.glGetUniformLocation(mProgram, "sTexture");
        //获取程序中总变换矩阵引用id
        muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");  
     
        	
    }

}
