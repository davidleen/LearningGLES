package com.opengles.book.objects;


import android.content.Context;
import android.opengl.GLES20;
import com.opengles.book.FloatUtils;
import com.opengles.book.MatrixState;
import com.opengles.book.ShaderUtil;
import com.opengles.book.Vertices;
import com.opengles.book.galaxy.ObjectDrawable;
import com.opengles.book.glsl.Uniform;
import com.opengles.book.glsl.Uniform1fv;
import com.opengles.book.glsl.UniformMatrix4F;

/**
 * 
 * @author davidleen29 
 * @create : 2014-4-25 下午11:44:25
 * @{  简单对象  shader 都是固定的   抽取公用方法和属性。}
 */
public abstract class NewAbstractSimpleObject   {


	protected String TAG=NewAbstractSimpleObject.this.getClass().getName();





	protected static final int VERTEX_POS_SIZE = 3;// xyz
	 protected static final int VERTEX_NORMAL_SIZE = 3;// xyz
	protected static final int VERTEX_TEXCOORD_SIZE = 2;// s t

	protected static final int STRIP_SIZE = (VERTEX_POS_SIZE
			//+ VERTEX_NORMAL_SIZE
			+ VERTEX_TEXCOORD_SIZE)
			* FloatUtils.RATIO_FLOATTOBYTE;
	int mProgram;//自定义渲染管线程序id


    String mVertexShader;//顶点着色器
    String mFragmentShader;//片元着色器

	private Vertices vertices;
   // int alphaThreadHoldHandler;

    // 总变换矩阵属性
    private UniformMatrix4F finalMatrix;
    //物体变换矩阵属性
    private Uniform1fv alphaThreadHoldUniform;

    //透明度检测的阀值 （0-1）
    private float[] alphaThreadHoldValue=new float[]{0.0f};















		public void bind() {




			vertices.create(getVertexData(),getIndexData());


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



		public void unBind() {
			 vertices.dispose();
		}


		public void draw(int textureId) {
			 //指定使用某套shader程序
	   	 GLES20.glUseProgram(mProgram);

           if(textureId!=0)
               GLES20.glEnable(GLES20.GL_TEXTURE_2D);

	   	  GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
	      GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
	        //将最终变换矩阵传入shader程序
            finalMatrix.bind();
            alphaThreadHoldUniform.bind();








            vertices.bind();
            vertices.draw();
            vertices.unbind();

            if(textureId!=0)
                GLES20.glDisable(GLES20.GL_TEXTURE_2D);




		}




	    public NewAbstractSimpleObject(Context context)
	    {    	
	     
	    	//初始化shader        
	    	initShader(context);


              vertices=new Vertices(new String[]{"aPosition","aTexCoor"},new int[]{VERTEX_POS_SIZE,VERTEX_TEXCOORD_SIZE},mProgram);
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




            finalMatrix=new UniformMatrix4F(mProgram,"uMVPMatrix",new Uniform.UniformBinder<float[]>() {
                @Override
                public float[] getBindValue() {
                    return MatrixState.getFinalMatrix();
                }
            });

            alphaThreadHoldUniform=new Uniform1fv(mProgram,"alphaThreadHold",new Uniform.UniformBinder<float[]>() {
                @Override
                public float[] getBindValue() {
                    return alphaThreadHoldValue;
                }
            });



		 
	    
	    }
	    
	    
	   
	    

	    
	    
	    
	    
	    /**
	     * 添加透明度检测  小于该值的片元将被丢弃
	     * @param alphaThreadHold
	     */
	    public void addAlphaTest(float alphaThreadHold)
	    {
            alphaThreadHoldValue[0]=alphaThreadHold;
	    }




	 
}
