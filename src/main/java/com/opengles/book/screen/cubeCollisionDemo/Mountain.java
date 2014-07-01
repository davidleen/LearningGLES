package com.opengles.book.screen.cubeCollisionDemo;

import android.content.Context;
import android.opengl.GLES20;
import com.opengles.book.*;
import com.opengles.book.galaxy.ObjectDrawable;
import com.opengles.book.glsl.Uniform;
import com.opengles.book.glsl.Uniform4fv;
import com.opengles.book.glsl.UniformMatrix4F;

import java.io.IOException;
import java.nio.FloatBuffer;

/**
 * 山脉纹理绘制
 */
public class Mountain implements ObjectDrawable
{
	
	protected String TAG=Mountain.this.getClass().getName();


    String path="gray_map/";
    // 总变换矩阵属性
    private UniformMatrix4F finalMatrix;
	
	int mProgram;//自定义渲染管线程序id





    String mVertexShader;//顶点着色器
    String mFragmentShader;//片元着色器
     
	int clipPlaneHandle;//裁剪平面的引用id
	float[] clipEquation=new  float[]{1,0,0,1000};

	 
    
    int graass_texId;
    int rock_texId;

    private Context context;



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

            vertices.create(grayMap.vertexData,grayMap.indexData);


//            ShaderUtil.bindBufferData(GLES20.GL_ARRAY_BUFFER,grayMap.vertexData,grayMap.vertexBuffSize,vboIds[0]);
//            ShaderUtil.bindBufferData(GLES20.GL_ELEMENT_ARRAY_BUFFER,grayMap.indexData,grayMap.indexBuffSize,vboIds[1]);

			
			
			
		}

		 
		@Override
		public void unBind() {

			//移除纹理
			GLES20.glDeleteTextures(2, new int[]{graass_texId,rock_texId},0);
//            //移除顶点
//			GLES20.glDeleteBuffers(2, vboIds, 0);
            vertices.dispose();
			
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

            finalMatrix.bind();

	        
	        


            vertices.bind();
            vertices.draw();
            vertices.unbind();
			
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


            //uniform 属性绑定
            finalMatrix=new UniformMatrix4F(mProgram,"uMVPMatrix",new Uniform.UniformBinder<float[]>() {
                @Override
                public float[] getBindValue() {
                    return MatrixState.getFinalMatrix();
                }
            });


	    
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
	    


	 
	




	public Mountain(Context context,GrayMap grayMap) {
		this.context=context;
		//初始化shader        
    	initShader(context);
		this.grayMap=grayMap;

        vertices=   new Vertices(new String[]{"aPosition","aTexCoor"},new int[]{GrayMap.VERTEX_POS_SIZE,GrayMap.VERTEX_TEXTURE_SIZE},mProgram);



    }

    private Vertices   vertices ;



    @Override
    public void update(float deltaTime) {

    }
}
