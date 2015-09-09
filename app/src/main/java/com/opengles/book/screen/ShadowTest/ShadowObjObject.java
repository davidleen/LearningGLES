package com.opengles.book.screen.ShadowTest;

import android.content.Context;
import android.opengl.GLES20;

import com.giants3.android.openglesframework.framework.MatrixState;
import com.opengles.book.ShaderUtil;
import com.opengles.book.Vertices;

import com.opengles.book.galaxy.ObjectDrawable;
import com.opengles.book.glsl.Uniform;
import com.opengles.book.glsl.UniformMatrix4F;
import com.opengles.book.objLoader.*;

import java.util.*;

/**
 * 
 * @author davidleen29
 * @create : 2014-1-7 下午9:22:33
 *         .obj 文件对象 绘制
 */
public class ShadowObjObject implements ObjectDrawable {



	protected static final int VERTEX_POS_SIZE = 3;// xyz
	protected static final int VERTEX_NORMAL_SIZE = 3;// xyz
	protected static final int VERTEX_TEXTURE_CORD_SIZE = 2;// s t

    protected static final int VERTEX_STRIDE=VERTEX_POS_SIZE+VERTEX_NORMAL_SIZE+VERTEX_TEXTURE_CORD_SIZE;


	String TAG = ShadowObjObject.this.getClass().getName();

	int mProgram;// 着色器id


   // 总变换矩阵属性
    private UniformMatrix4F finalMatrix;


//    //太阳光位置属性  //光源位置。
//    private Uniform3fv sunLocationUniform;


	// int textureId;
	protected ObjModel model;

	private Context context;





    //顶点绘制类
    private Vertices attributeWrap;



    public ShadowObjObject(Context context, ObjModel objModel)
	{

		this.context = context;
        this.model=objModel;





		// 创建program
		String mVertexShader = ShaderUtil.loadFromAssetsFile(
				getVertexFileName(),
				context.getResources());
		String mFragmentShader = ShaderUtil.loadFromAssetsFile(
				getFragmentFileName(),
				context.getResources());
		mProgram = ShaderUtil.createProgram(mVertexShader, mFragmentShader);


        //uniform 属性绑定
        finalMatrix=new UniformMatrix4F(mProgram,"uMVPMatrix",new Uniform.UniformBinder<float[]>() {
            @Override
            public float[] getBindValue() {
                return MatrixState.getFinalMatrix();
            }
        });

//        objectMatrix=new UniformMatrix4F(mProgram,"uMMatrix",new Uniform.UniformBinder<float[]>() {
//            @Override
//            public float[] getBindValue() {
//                return MatrixState.getMMatrix();
//            }
//        });
//
//        sunLocationUniform=new Uniform3fv(mProgram,"uLightLocation",new Uniform.UniformBinder<FloatBuffer>() {
//            @Override
//            public FloatBuffer getBindValue() {
//                return LightSources.lightPositionFBSun;
//            }
//        });

        attributeWrap=new Vertices(new String[]{"aPosition" },new int[]{VERTEX_POS_SIZE },VERTEX_STRIDE,mProgram);












		onCreate(mProgram);
	}



	@Override
	public void bind() {

        attributeWrap.create(model.vertexData,model.indexData);


		// 预先绑定固定值 不需要在绘制时候重新绑定。
		GLES20.glUseProgram(mProgram);

		GLES20.glUseProgram(0);




	}

	@Override
	public void unBind() {
        attributeWrap.dispose();


	}

	@Override
	public void draw() {
		GLES20.glUseProgram(mProgram);



        //属性绑定
        attributeWrap.bind();
        //最终矩阵绑定
        finalMatrix.bind();


        //重新注入太阳光位置 光线会转动
//        sunLocationUniform.bind();

		List<ObjModelPart> partList = model.parts;

		int size = partList.size();
		for (int i = 0; i < size; i++) {
			ObjModelPart part = partList.get(i);
			if(part.length<=0) continue;

            attributeWrap.draw(GLES20.GL_TRIANGLES,
                    part.index, part.length
            );

        }
        attributeWrap.unbind();


	}








	
	

	

	
	

	
	protected void onCreate(int mProgram)
	{
		//let sub class do any more  for get handler ;
	}




	
	/**
	 * @return
	 */
	protected String getFragmentFileName() {
		return "shadowtest/shadow_frag.glsl";
	}

	/**
	 * @return
	 */
	protected String getVertexFileName() {
		return "shadowtest/shadow_vertex.glsl";
	}


    @Override
    public void update(float deltaTime) {

    }

    public AABB getBoundary() {
        return model.boundary;
    }



}
