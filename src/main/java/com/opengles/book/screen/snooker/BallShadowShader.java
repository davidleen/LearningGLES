package com.opengles.book.screen.snooker;

import android.content.Context;
import android.opengl.GLES20;
import com.opengles.book.LightSources;
import com.opengles.book.MatrixState;
import com.opengles.book.ShaderUtil;
import com.opengles.book.Vertices;
import com.opengles.book.glsl.Uniform;
import com.opengles.book.glsl.Uniform3fv;
import com.opengles.book.glsl.Uniform4fv;
import com.opengles.book.glsl.UniformMatrix4F;

import java.nio.FloatBuffer;

/**
 * {简单描述用途}
 * Created by davidleen29   qq:67320337
 * on 2014-7-16.
 */
public class BallShadowShader {

    public BallShadowShader(Context context) {
//初始化shader
        initShader(context);

        vertices = new Vertices(new String[]{"aPosition" }, new int[]{SphereWithLimitTexture.VERTEX_POS_SIZE},SphereWithLimitTexture.VERTEX_SIZE, mProgram);




    }




    int mProgram;//自定义渲染管线程序id


    private Vertices vertices;


    // 总变换矩阵属性
    private UniformMatrix4F finalMatrix;
    private UniformMatrix4F uMMatrix;

    private Uniform3fv uLightLocationUniform;









    public void draw(float[] vertexData,short[] indexData ) {
        //指定使用某套shader程序
        GLES20.glUseProgram(mProgram);

        vertices.create(vertexData,indexData);


        //将最终变换矩阵传入shader程序
        finalMatrix.bind();
        uLightLocationUniform.bind();
        uMMatrix.bind();


        vertices.bind();
        vertices.draw();
        vertices.unbind();


        vertices.dispose();

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

        uLightLocationUniform = new Uniform3fv(mProgram, " uLightLocation", new Uniform.UniformBinder<FloatBuffer>() {
            @Override
            public FloatBuffer getBindValue() {
                return LightSources.lightPositionFBSun;
            }
        });


        uMMatrix = new UniformMatrix4F(mProgram, "uMMatrix", new Uniform.UniformBinder<float[]>() {
            @Override
            public float[] getBindValue() {


                return MatrixState.getMMatrix();
            }
        });



    }

}
