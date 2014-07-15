package com.opengles.book.screen.snooker;


import android.content.Context;
import android.opengl.GLES20;
import com.opengles.book.*;
import com.opengles.book.framework.gl.TextureRegion;
import com.opengles.book.glsl.Uniform;
import com.opengles.book.glsl.Uniform1fv;
import com.opengles.book.glsl.Uniform3fv;
import com.opengles.book.glsl.UniformMatrix4F;

import java.nio.FloatBuffer;


/**
 * 绘制阴影用的球体    记录到光线的距离。
 *
 * @author davidleen29
 */
public class BallShadowDrawable {


    float[] vertexData;
    short[] indexData;


    public BallShadowDrawable(Context context, float radius) {

        this(context, radius, null);
    }

    public BallShadowDrawable(Context context, float radius, TextureRegion region) {

//初始化shader
        initShader(context);


        vertices = new Vertices(new String[]{"aPosition" }, new int[]{VERTEX_POS_SIZE},VERTEX_POS_SIZE+VERTEX_TEXCOORD_SIZE, mProgram);
        initData(radius, region);

        // this.index=index;


    }


    private void initData(float radius, TextureRegion region) {

        SphereWithLimitTexture sphere = new SphereWithLimitTexture(radius, false, region);
        vertexData = sphere.attributes;
        indexData = sphere.indics;

    }

    protected float[] getVertexData() {

        return vertexData;
    }

    protected short[] getIndexData() {

        return indexData;
    }

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


    // 总变换矩阵属性
    private UniformMatrix4F finalMatrix;
    private UniformMatrix4F uMMatrix;

    private Uniform3fv uLightLocationUniform;


    //透明度检测的阀值 （0-1）
    private float[] alphaThreadHoldValue = new float[]{0.0f};


    public void bind() {


        vertices.create(getVertexData(), getIndexData());


    }


    public void unBind() {
        vertices.dispose();
    }


    public void draw( ) {
        //指定使用某套shader程序
        GLES20.glUseProgram(mProgram);




        //将最终变换矩阵传入shader程序
        finalMatrix.bind();
        uLightLocationUniform.bind();
        uMMatrix.bind();


        vertices.bind();
        vertices.draw();
        vertices.unbind();


    }


    //初始化shader
    public void initShader(Context mv) {
        //加载顶点着色器的脚本内容
        mVertexShader = ShaderUtil.loadFromAssetsFile("shadow_buffer/vertex.glsl", mv.getResources());
        //加载片元着色器的脚本内容
        mFragmentShader = ShaderUtil.loadFromAssetsFile("shadow_buffer/frag.glsl", mv.getResources());
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


    /**
     * 添加透明度检测  小于该值的片元将被丢弃
     *
     * @param alphaThreadHold
     */
    public void addAlphaTest(float alphaThreadHold) {
        alphaThreadHoldValue[0] = alphaThreadHold;
    }


}
