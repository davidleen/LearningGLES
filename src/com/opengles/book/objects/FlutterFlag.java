package com.opengles.book.objects;
 

	import com.opengles.book.FloatUtils;
import com.opengles.book.MatrixState;
 
import com.opengles.book.ShaderUtil;
import com.opengles.book.galaxy.ObjectDrawable;

	import android.content.Context;
 
import android.opengl.GLES20;
import android.util.Log;
 
/**
 * 红旗飘动
 * @author davidleen29 
 * 
 *  
 */
public   class FlutterFlag  implements ObjectDrawable {

	
	protected String TAG=FlutterFlag.this.getClass().getName();
	
	
	 
	
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
     
	
    final float WIDTH_SPAN=10f;//2.8f;//横向长度总跨度

	 
    
   
    int textureId;
    int[] vboIds;
     
    int indexLength;




	private int maStartAngleHandle;




	private int muWidthSpanHandle;




	public float currStartAngle=0;




	 



 
		
		 
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
		protected   float[] getVertexData()
		{
			
			
			return vetextData;
			
		} 
		
		/**
		 * 获取顶点索引数据
		 * @return
		 */
		protected   short[] getIndexData() 
		{
			
			
		 
			return indexData;
		}

		 
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
	   	  
	   	 
	   	 GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
	      GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
	        //将最终变换矩阵传入shader程序
	        GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(), 0);
	         
	         //Log.d(TAG, "alphaThreadHold:"+alphaThreadHold);
	        
	        //将本帧起始角度传入shader程序
	         GLES20.glUniform1f(maStartAngleHandle, currStartAngle);
	         //将横向长度总跨度传入shader程序
	         GLES20.glUniform1f(muWidthSpanHandle, WIDTH_SPAN);  
	         
	        
	        
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
 		offset += VERTEX_NORMAL_SIZE * FloatUtils.RATIO_FLOATTOBYTE;

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

		 
		
	    
	    public FlutterFlag(Context  context )
	    {    	
	     
	    	//初始化shader        
	    	initShader(context);
	    	initData();
	    	textureId = ShaderUtil.loadTextureWithUtils(context,"sky/sky.png" ,false);
			 
	    }
	    
	    
	    
	    float[] vetextData;
	    short[] indexData;
	    private void initData()
	    {
	    	int rowCount=(int)WIDTH_SPAN;
			int columnCount=(int)WIDTH_SPAN;
			int totalCount = rowCount * columnCount;
			int triangleCount = totalCount*2 ; // 一个方形// 2个三角形 
			int indicesCount = triangleCount * 3;// 一个三角形3个点
			
			int 	stride =STRIP_SIZE;
			 
			float[]	attributes = new float[totalCount
					* stride];
			short[]	indics = new short[indicesCount];

			float x, y, z;
			float pieceofImageS = 1.0f / rowCount  ;
			float pieceofImageT = 1.0f / columnCount ; 
			 
			int position = 0, indexPosition = 0;
			for (int i = 0; i < rowCount; i++)
			{
				 

				float s  =i * pieceofImageS; 
				x=i;
				 z=0;
				for (int j = 0; j < columnCount; j++)
				{
				         y=j;  

					attributes[position++] = x ;
					attributes[position++] = y  ;
					attributes[position++] = z ;
					// // 法向量值
					  attributes[position++] =x;
					  attributes[position++] =y;
					  attributes[position++] =z;
					 
					
					float t =  j * pieceofImageT ;  
					attributes[position++] = s; 
					attributes[position++] = t; 
				 
					 

				}
			}

			for (int i = 0; i < rowCount -1 ; i++)
			{

				for (int j = 0; j < columnCount  -1; j++)
				{

					// 划分四边形 变成2个三角形
					// v1_____v3
					// /| |
					// v0|_____|v2
					short v0 = (short) (i * columnCount + j);  
					short v1 = (short) ((i + 1) * columnCount + j);
					short v2 = (short) (i * columnCount + j + 1);
					short v3 = (short) ((i + 1) * columnCount + j + 1);
					indics[indexPosition++] = v0;
					indics[indexPosition++] = v1;
					indics[indexPosition++] = v3;

					indics[indexPosition++] = v0;
					indics[indexPosition++] = v3;
					indics[indexPosition++] = v2;
					 

				}
			}
			 
			
			vetextData=attributes;
			indexData=indics;
	    	
	    }
	    
	    
	    
	    //初始化shader
	    public void initShader(Context mv)
	    {
	    	//加载顶点着色器的脚本内容
	        mVertexShader=ShaderUtil.loadFromAssetsFile("flutteringflag/vertex.glsl", mv.getResources());
	        //加载片元着色器的脚本内容
	        mFragmentShader=ShaderUtil.loadFromAssetsFile("flutteringflag/frag.glsl", mv.getResources());
	        //基于顶点着色器与片元着色器创建程序
	        mProgram = ShaderUtil.createProgram(mVertexShader, mFragmentShader);
	        //获取程序中顶点位置属性引用id  
	        maPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");
	        //获取程序中顶点纹理坐标属性引用id  
	        maTexCoorHandle= GLES20.glGetAttribLocation(mProgram, "aTexCoor");
	        //获取程序中总变换矩阵引用id
	        muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");  
	     
	        maStartAngleHandle=GLES20.glGetUniformLocation(mProgram, "u_startAngle");  
	        //获取横向长度总跨度引用
	        muWidthSpanHandle=GLES20.glGetUniformLocation(mProgram, "uWidthSpan");  
	    }
	    
	    
	   
	    
	    
	    
	    
	    
	    
	    
	  
	 
}
