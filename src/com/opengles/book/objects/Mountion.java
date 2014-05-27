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
	float[] clipEquation=new  float[]{1,0,0,1};

	 
    
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
					getVertexData(),
					vboIds[0]);
			
			short[] indexData=getIndexData();
			indexLength=indexData.length;
			ShaderUtil.createIndexBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER,
					indexData,
					vboIds[1]);

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

	 
	
	private float[] vertices;

	private short[] indexData;



	public Mountion(Context context) {
		this.context=context;
		//初始化shader        
    	initShader(context);
		initData(context);
	}

	private void initData(Context context) {
	float[][]	yArray=loadLandforms(context , path+"land.png");
        
	int rows=yArray.length;
	int cols=yArray[0].length;
		int vCount=cols*rows ;//共有点数  共有方块数
          vertices =new float[vCount*STRIP_SIZE];//每个顶点xyz三个坐标 st 纹理坐标
        int count=0;//顶点计数器
        float halfRows=rows/2;
        float halfCols=cols/2;
        
        //16 为纹理图切割块数
        float sizew=16.0f/cols;//列数
    	float sizeh=16.0f/rows;//行数
        for(int j=0;j<rows;j++)
        {
        	for(int i=0;i<cols;i++) 
        	{        		
        		
        		//xyz st
        		vertices[count++]=(i-halfCols)*UNIT_SIZE;                 
        		vertices[count++]=yArray[j][i];
        		vertices[count++]=(j-halfRows)*UNIT_SIZE;     
        		//st 
        		vertices[count++]=i*sizew;
        				vertices[count++]=j*sizeh;
        		 
        		
        	 
        	}
        }
        
        //每个方块由2个三角形组成， 每个三角形3个顶点。
        int indexCount=vCount*2*3;
          indexData=new short[indexCount];
        count=0;
        for(int j=0;j<rows-1;j++)
        {
        	for(int i=0;i<cols-1;i++) 
        	{ 
        		
        		short p1=(short) (j*rows+i);
        		short p2=(short) (j*rows+i+1);
        		short p3=(short) ((j+1)*rows+i );
        		short p4=(short) ((j+1)*rows+i+1 );
        		
        		indexData[count++]=p1;
        		indexData[count++]=p2;	
        		indexData[count++]=p3;
        		
        		indexData[count++]=p3;
        		indexData[count++]=p2;	
        		indexData[count++]=p4;
        		
        		 
        		
        	}
        	
        }
		
	}

	 
	protected float[] getVertexData() {
		 
		return vertices;
	}
 
	protected short[] getIndexData() {
		 
		return indexData;
	}

	 
	 
	
	
	
	//从灰度图片中加载陆地上每个顶点的高度
		public   float[][] loadLandforms(Context context,String fileName)
		{
			
			 
			    final float LAND_HIGH_ADJUST=-2f;//陆地的高度调整值
			    final float LAND_HIGHEST=20f;//陆地最大高差  
			
			Bitmap bt=null;
			try {
				bt = BitmapFactory.decodeStream(context.getAssets().open(fileName));
			} catch (IOException e) {
				 throw new RuntimeException("no found image "+fileName);
				 
			}
			int colsPlusOne=bt.getWidth(); 
			int rowsPlusOne=bt.getHeight(); 
			float[][] result=new float[rowsPlusOne][colsPlusOne];
			for(int i=0;i<rowsPlusOne;i++)
			{
				for(int j=0;j<colsPlusOne;j++)
				{
					int color=bt.getPixel(j,i);
					int r=Color.red(color);
					int g=Color.green(color); 
					int b=Color.blue(color);
					int h=(r+g+b)/3;
					result[i][j]=h*LAND_HIGHEST/255+LAND_HIGH_ADJUST;  
				}
			}
			return result;
		}

		
		
		//自动切分纹理产生纹理数组的方法
	    public float[] generateTexCoor(int bw,int bh)
	    {
	    	float[] result=new float[bw*bh*6*2]; 
	    	float sizew=16.0f/bw;//列数
	    	float sizeh=16.0f/bh;//行数
	    	int c=0;
	    	for(int i=0;i<bh;i++)
	    	{
	    		for(int j=0;j<bw;j++)
	    		{
	    			//每行列一个矩形，由两个三角形构成，共六个点，12个纹理坐标
	    			float s=j*sizew;
	    			float t=i*sizeh;
	    			
	    			result[c++]=s;
	    			result[c++]=t;
	    			
	    			result[c++]=s;
	    			result[c++]=t+sizeh;
	    			
	    			result[c++]=s+sizew;
	    			result[c++]=t;
	    			
	    			result[c++]=s+sizew;
	    			result[c++]=t;
	    			
	    			result[c++]=s;
	    			result[c++]=t+sizeh;
	    			
	    			result[c++]=s+sizew;
	    			result[c++]=t+sizeh;    			
	    		}
	    	}
	    	return result;
	    }


    @Override
    public void update(float deltaTime) {

    }
}
