package com.opengles.book.objects;


import android.content.Context;
import android.opengl.GLES20;
import com.opengles.book.FloatUtils;
import com.opengles.book.MatrixState;
import com.opengles.book.ShaderUtil;
import com.opengles.book.Vertices;
import com.opengles.book.framework.gl.CubeTexture;
import com.opengles.book.galaxy.ObjectDrawable;
import com.opengles.book.glsl.Uniform;
import com.opengles.book.glsl.Uniform1fv;
import com.opengles.book.glsl.UniformMatrix4F;

/**
 * 
 * @author davidleen29 
 * @create : 2014-4-25 下午11:44:25
 * @{  简单3D对象  shader 都是固定的   抽取公用方法和属性。}
 */
public abstract class AbstractSimple3DObject   {


	protected String TAG=AbstractSimple3DObject.this.getClass().getName();



    String vShaderStr  =
            "attribute vec3 a_position; \n "+
            "attribute vec3 a_normal; \n "  +
            "uniform mat4 uMVPMatrix;    //总变换矩阵  \n"  +
            "varying vec3 v_normal;   \n"+
                    "varying vec3 v_position;   \n"+
            " void main()   \n "+
            "{ "+
            " gl_Position = uMVPMatrix * vec4(a_position,1); //根据总变换矩阵计算此次绘制此顶点位置   \n  "+
            " v_normal = a_normal;  \n "+
                    " v_position = a_position;   \n "+
            "}  ";
    String fShaderStr  =
            " precision mediump float;  \n "+
            "varying vec3 v_normal;  \n "+
                    "varying vec3 v_position;  \n "+
            "uniform samplerCube s_texture;  \n " +
            "void main()  \n " +
            "{   \n "+
            " gl_FragColor = textureCube(s_texture, v_position); \n  "+
            "}  \n ";




	protected static final int VERTEX_POS_SIZE = 3;// xyz
	 protected static final int VERTEX_NORMAL_SIZE = 3;// xyz


	protected static final int STRIP_SIZE = (VERTEX_POS_SIZE+
			  VERTEX_NORMAL_SIZE
			 )
			* FloatUtils.RATIO_FLOATTOBYTE;
	int mProgram;//自定义渲染管线程序id



	private Vertices vertices;


    // 总变换矩阵属性
    private UniformMatrix4F finalMatrix;


















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


		public void draw(CubeTexture texture) {
			 //指定使用某套shader程序
	   	 GLES20.glUseProgram(mProgram);
	   	 //绑定纹理

            texture.bind();

	        //将最终变换矩阵传入shader程序
            finalMatrix.bind();



            vertices.bind();
            vertices.draw();
            vertices.unbind();






		}




	    public AbstractSimple3DObject(Context context)
	    {    	
	     
	    	//初始化shader        
	    	initShader(context);


              vertices=new Vertices(new String[]{"a_position","a_normal"},new int[]{VERTEX_POS_SIZE,VERTEX_NORMAL_SIZE},mProgram);
	    }
	    
	    
	    
	    //初始化shader
	    public void initShader(Context mv)
	    {
	    	//加载顶点着色器的脚本内容

	        //基于顶点着色器与片元着色器创建程序
	        mProgram = ShaderUtil.createProgram(vShaderStr, fShaderStr);




            finalMatrix=new UniformMatrix4F(mProgram,"uMVPMatrix",new Uniform.UniformBinder<float[]>() {
                @Override
                public float[] getBindValue() {
                    return MatrixState.getFinalMatrix();
                }
            });




		 
	    
	    }
	    
	    
	   
	    

	    


	 
}
