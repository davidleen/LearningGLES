package com.opengles.book.objects;
 

	import com.opengles.book.FloatUtils;
import com.opengles.book.MatrixState;
 
import com.opengles.book.ShaderUtil;
import com.opengles.book.galaxy.ObjectDrawable;

	import android.content.Context;
 
import android.opengl.GLES20;
import android.util.Log;
 
/**
 * 
 * @author davidleen29 
 * @create : 2014-4-25 下午11:44:25
 * @{  简单对象  shader 都是固定的   抽取公用方法和属性。}
 */
public abstract class AbstractObject  implements ObjectDrawable {

	
	protected String TAG=AbstractObject.this.getClass().getName();
	
	
	 
	
	protected static final int VERTEX_POS_SIZE = 3;// xyz
	protected static final int VERTEX_NORMAL_SIZE = 3;// xyz
	protected static final int VERTEX_TEXCOORD0_SIZE = 2;// s t

	protected static final int STRIP_SIZE = (VERTEX_POS_SIZE
			 + VERTEX_NORMAL_SIZE
			+ VERTEX_TEXCOORD0_SIZE)
			* FloatUtils.RATIO_FLOATTOBYTE;
	
	int mProgram;//自定义渲染管线程序id
    int muMVPMatrixHandle;//总变换矩阵引用id
    int maPositionHandle; //顶点位置属性引用id  
    int maTexCoorHandle; //顶点纹理坐标属性引用id  
    String mVertexShader;//顶点着色器
    String mFragmentShader;//片元着色器
     
	
    int alphaThreadHoldHandler;

	 
    

    int textureId;
    int[] vboIds;
     
    int indexLength;




	 



 
		
		 
		@Override
		public void bind() {
			 
			 
			vboIds = new int[2];

			GLES20.glGenBuffers(2, vboIds, 0);

			ShaderUtil.createVertexBuffer(GLES20.GL_ARRAY_BUFFER,
					getVertexData(),
					vboIds[0]);
			
			short[] indexData=getIndexData();
			indexLength=indexData.length;
			ShaderUtil.createIndexBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER,
					indexData,
					vboIds[1]);

			Log.d(TAG, "vboIds:" + vboIds[0] + "," + vboIds[1]);
			
			
			
		}

		/**
		 * 获取顶点数据（位置，纹理）
		 * @return
		 */
		protected abstract float[] getVertexData() ;
		
		/**
		 * 获取顶点索引数据
		 * @return
		 */
		protected abstract short[] getIndexData() ;

		 
		@Override
		public void unBind() {
			//移除纹理
			if(textureId!=-1)
			GLES20.glDeleteTextures(1, new int[]{textureId},0);
			GLES20.glDeleteBuffers(2, vboIds, 0);
			
		}

		@Override
		public void draw() {
			 //指定使用某套shader程序
	   	 GLES20.glUseProgram(mProgram); 
	   	 //绑定纹理
	   	 int newTextureId=getTextureId();
	   	 if(newTextureId!=-1)
	   	    textureId=newTextureId;
	   	 GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
	     GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
	        //将最终变换矩阵传入shader程序
	        GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(), 0);


	        
	         //Log.d(TAG, "alphaThreadHold:"+alphaThreadHold);
	        
	      
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

		 
		
	    
	    public AbstractObject(Context  context )
	    {    	
	     
	    	//初始化shader        
	    	initShader(context);
	    }
	    
	    
	    
	    //初始化shader
	    public void initShader(Context mv)
	    {
	    	//加载顶点着色器的脚本内容
	        mVertexShader=ShaderUtil.loadFromAssetsFile("abstract_simple_object/vertex.glsl", mv.getResources());
	        //加载片元着色器的脚本内容
	        mFragmentShader=ShaderUtil.loadFromAssetsFile("abstract_simple_object/frag.glsl", mv.getResources());
	        //基于顶点着色器与片元着色器创建程序
	        mProgram = ShaderUtil.createProgram(mVertexShader, mFragmentShader);
	        //获取程序中顶点位置属性引用id  
	        maPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");
	        //获取程序中顶点纹理坐标属性引用id  
	        maTexCoorHandle= GLES20.glGetAttribLocation(mProgram, "aTexCoor");
	        //获取程序中总变换矩阵引用id
	        muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");  
	    
	        //获取透明度阀值的引用id
	        alphaThreadHoldHandler = GLES20.glGetUniformLocation(mProgram, "alphaThreadHold");  
		 
	    
	    }
	    
	    
	   
	    
	    /**
	     * 获取纹理id
	     * @return
	     */
	    protected  abstract int getTextureId();
	    
	    
	    
	    

	  
	 
}
