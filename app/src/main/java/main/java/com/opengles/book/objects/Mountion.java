package com.opengles.book.objects;

import java.io.IOException;

import com.opengles.book.FloatUtils;
import com.opengles.book.MatrixState;
import com.opengles.book.R;
import com.opengles.book.ShaderUtil;
import com.opengles.book.galaxy.ObjectDrawable;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.opengl.GLES20;
import android.util.Log;
import com.opengles.book.screen.cubeCollisionDemo.GrayMap;


import static com.opengles.book.screen.GrayMapScreen.UNIT_SIZE;

/**
 * 山脉地形类
 * @author davidleen29 
 * @create : 2014-4-26 下午5:26:00
 * @{   简单的描述这个类用途}
 */
public class Mountion  implements ObjectDrawable
{
	
	protected String TAG=Mountion.this.getClass().getName();
	
	
	 
	
	protected static final int VERTEX_POS_SIZE = 3;// xyz
	//protected static final int VERTEX_NORMAL_SIZE = 3;// xyz
	protected static final int VERTEX_TEXCOORD0_SIZE = 2;// s t

	protected static final int STRIP_SIZE = (VERTEX_POS_SIZE
			//+ VERTEX_NORMAL_SIZE
			+ VERTEX_TEXCOORD0_SIZE)
			* FloatUtils.RATIO_FLOATTOBYTE;
	
	int mProgram;//自定义渲染管线程序id
    int muMVPMatrixHandle;//总变换矩阵引用id
    int maPositionHandle; //顶点位置属性引用id  
    int maTexCoorHandle; //顶点纹理坐标属性引用id  
    String mVertexShader;//顶点着色器
    String mFragmentShader;//片元着色器
     
	int clipPlaneHandle;//裁剪平面的引用id
	float[] clipEquation=new  float[]{1,0,0,10};

	 
    
    int graass_texId;
    int rock_texId;
    int[] vboIds;
    private Context context;
    int indexLength;


  //草地的id
  	int sTextureGrassHandle;
  	//石头的id
  	int sTextureRockHandle;
  	//起始x值
  	int landStartYHandle;
  	//长度
  	int landYSpanHandle;

	 



    private GrayMap grayMap;
 
		
		 
		@Override
		public void bind() {
			 
			//加载纹理。
		 
			try {
				graass_texId = ShaderUtil.loadTextureWithUtils(context.getAssets().open(path+"grass.png"));
				rock_texId = ShaderUtil.loadTextureWithUtils(context.getAssets().open(path+"rock.png"));
				
			} catch (IOException e) {
				 throw new RuntimeException("unable load texture ",e);
				 
			}
			vboIds = new int[2];

			GLES20.glGenBuffers(2, vboIds, 0);

			ShaderUtil.createVertexBuffer(GLES20.GL_ARRAY_BUFFER,
                    grayMap.vertexData,
					vboIds[0]);

			short[] indexData=grayMap.indexData;
			indexLength=indexData.length;
			ShaderUtil.createIndexBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER,
                    indexData,
					vboIds[1]);



           // ShaderUtil.bindBufferData(GLES20.GL_ARRAY_BUFFER,grayMap.vertexData,grayMap.vertexBuffSize,vboIds[0]);
            // ShaderUtil.bindBufferData(GLES20.GL_ELEMENT_ARRAY_BUFFER,grayMap.indexData,grayMap.indexBuffSize,vboIds[1]);


            Log.d(TAG, "vboIds:" + vboIds[0] + "," + vboIds[1]);
			
			
			
		}

		 
		@Override
		public void unBind() {
			//移除纹理
			GLES20.glDeleteTextures(2, new int[]{graass_texId,rock_texId},0);
			GLES20.glDeleteBuffers(2, vboIds, 0);
			
		}

		@Override
		public void draw() {
			 //指定使用某套shader程序
	   	 GLES20.glUseProgram(mProgram); 
	   	 //绑定纹理
	       
	   	 //绑定纹理
	        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
	        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, graass_texId);
	        GLES20.glActiveTexture(GLES20.GL_TEXTURE1);
			GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, rock_texId);
			GLES20.glUniform1i(sTextureGrassHandle, 0);//使用0号纹理
	        GLES20.glUniform1i(sTextureRockHandle, 1); //使用1号纹理
	        GLES20.glUniform4f(clipPlaneHandle, clipEquation[0], clipEquation[1], clipEquation[2], clipEquation[3]);
	        //邦定过程纹理参数
	    	 
	    	GLES20.glUniform1f(landStartYHandle, 0);
	    	GLES20.glUniform1f(landYSpanHandle, 10);
	      	 
	        
	        //将最终变换矩阵传入shader程序
	        GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(), 0); 
	        
	        
	        // 启用位置向量数据
			GLES20.glEnableVertexAttribArray(maPositionHandle);
			// 启用法向量数据
			//GLES20.glEnableVertexAttribArray( );
			//启用纹理
			GLES20.glEnableVertexAttribArray(maTexCoorHandle);
	        
	        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vboIds[0]);
			GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, vboIds[1]);
			
			int offset = 0;
			int stride = STRIP_SIZE;
			GLES20.glVertexAttribPointer
					(
							maPositionHandle,
							VERTEX_POS_SIZE,
							GLES20.GL_FLOAT,
							false,
							stride,
							offset
					);

			// 启用法向量
			offset += VERTEX_POS_SIZE * FloatUtils.RATIO_FLOATTOBYTE;

//			GLES20.glVertexAttribPointer
//					(
//							VERTEX_NORMAL_INDEX,
//							VERTEX_NORMAL_SIZE,
//							GLES20.GL_FLOAT,
//							false,
//							stride,
//							offset
//					);
//
//			offset += VERTEX_NORMAL_SIZE * FloatUtils.RATIO_FLOATTOBYTE;

			GLES20.glVertexAttribPointer
					(
							maTexCoorHandle,
							VERTEX_TEXCOORD0_SIZE,
							GLES20.GL_FLOAT,
							false,
							stride,
							offset
					);
	        
	    
	       
	        //绘制纹理矩形
	        GLES20.glDrawElements(GLES20.GL_TRIANGLES, indexLength,
					GLES20.GL_UNSIGNED_SHORT, 0);

	        
	        //can close these handler below  at will
			// jiechu
			GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
			GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, 0);
			// 启用位置向量数据
			GLES20.glDisableVertexAttribArray(maPositionHandle);
			// 禁用法向量数据
			//GLES20.glDisableVertexAttribArray(VERTEX_NORMAL_INDEX);
			//禁用纹理向量数据
			GLES20.glDisableVertexAttribArray(maTexCoorHandle);
			
		}

		 
		
	 
	    
	    
	    
	    //初始化shader
	    public void initShader(Context mv)
	    {
	    	//加载顶点着色器的脚本内容
	        mVertexShader=ShaderUtil.loadFromAssetsFile(path+"vertex.glsl", mv.getResources());
	        //加载片元着色器的脚本内容
	        mFragmentShader=ShaderUtil.loadFromAssetsFile(path+"frag.glsl", mv.getResources());
	        //基于顶点着色器与片元着色器创建程序
	        mProgram = ShaderUtil.createProgram(mVertexShader, mFragmentShader);
	        //获取程序中顶点位置属性引用id  
	        maPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");
	        //获取程序中顶点纹理坐标属性引用id  
	        maTexCoorHandle= GLES20.glGetAttribLocation(mProgram, "aTexCoor");
	        //获取程序中总变换矩阵引用id
	        muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");  
	    
	        //过程纹理开始Y值
	        
	        //纹理
			//草地
			sTextureGrassHandle=GLES20.glGetUniformLocation(mProgram, "sTextureGrass");
			//石头
			sTextureRockHandle=GLES20.glGetUniformLocation(mProgram, "sTextureRock");
			//x位置
			landStartYHandle=GLES20.glGetUniformLocation(mProgram, "landStartY");
			//x最大
			landYSpanHandle=GLES20.glGetUniformLocation(mProgram, "landYSpan");	
		 
	    
			clipPlaneHandle=GLES20.glGetUniformLocation(mProgram, "u_clipPlane");
			
	    }
	    
	String path="gray_map/";

	 
	




	public Mountion(Context context) {
		this.context=context;
		//初始化shader        
    	initShader(context);

        grayMap=GrayMap.load(context,"gray_map/land.png");


	}




    @Override
    public void update(float deltaTime) {

    }
}
