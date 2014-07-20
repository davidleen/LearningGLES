package com.opengles.book.screen.snooker;

import android.content.Context;
import android.opengl.GLES20;
import com.opengles.book.LightSources;
import com.opengles.book.MatrixState;
import com.opengles.book.ShaderUtil;

import com.opengles.book.glsl.*;

import java.nio.FloatBuffer;

/**
 * {简单描述用途}
 * Created by davidleen29   qq:67320337
 * on 2014-7-16.
 */
public class BallShadowShader {

    public BallShadowShader(Context context) {
//初始化shader





        this(context,SphereWithLimitTexture.VERTEX_SIZE);

    }



    public BallShadowShader(Context context,int vertexSize) {
        initShader(context);
        vertices = new Vertices(new String[]{"aPosition" }, new int[]{SphereWithLimitTexture.VERTEX_POS_SIZE},vertexSize, mProgram);




    }



    int mProgram;//自定义渲染管线程序id


    public Vertices vertices;


    // 总变换矩阵属性
    private UniformMatrix4F finalMatrix;
    private UniformMatrix4F uMMatrix;

    private Uniform3fv_Float uLightLocationUniform;









    public void draw(int[] bufferIds,int numElements  ) {
        //指定使用某套shader程序
        GLES20.glUseProgram(mProgram);



        //将最终变换矩阵传入shader程序
        finalMatrix.bind();
        uLightLocationUniform.bind();
        uMMatrix.bind();



        vertices.draw(bufferIds,numElements);





    }


    //初始化shader
    public void initShader(Context mv) {
        //加载顶点着色器的脚本内容
        String mVertexShader = ShaderUtil.loadFromAssetsFile("shadow_buffer/vertex.glsl", mv.getResources());
        //加载片元着色器的脚本内容
        String   mFragmentShader = ShaderUtil.loadFromAssetsFile("shadow_buffer/frag.glsl", mv.getResources());
        //基于顶点着色器与片元着色器创建程序
        mProgram = ShaderUtil.createProgram(mVertexShader, mFragmentShader);


        finalMatrix = new UniformMatrix4F(mProgram, "uMVPMatrix", new Uniform.UniformBinder<float[]>() {
            @Override
            public float[] getBindValue() {


                return MatrixState.getFinalMatrix();
            }
        });



        uMMatrix = new UniformMatrix4F(mProgram, "uMMatrix", new Uniform.UniformBinder<float[]>() {
            @Override
            public float[] getBindValue() {


                return MatrixState.getMMatrix();
            }
        });




        uLightLocationUniform = new Uniform3fv_Float(mProgram, "uLightLocation", new Uniform.UniformBinder<float[]>() {
            @Override
            public float[] getBindValue() {
                return LightSources.lightPositionSun;
            }
        });


    }

}
