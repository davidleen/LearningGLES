package com.opengles.book.screen.snooker;

import android.content.Context;
import android.opengl.GLES20;
import com.opengles.book.LightSources;
import com.opengles.book.MatrixState;
import com.opengles.book.ShaderUtil;
import com.opengles.book.framework.gl.CubeTexture;
import com.opengles.book.glsl.Uniform;
import com.opengles.book.glsl.Uniform3fv;
import com.opengles.book.glsl.Uniform4fv;
import com.opengles.book.glsl.UniformMatrix4F;

import java.nio.FloatBuffer;

/**
 *  shader 类  使用 cubeTexure
 * Created by davidleen29   qq:67320337
 * on 2014-7-16.
 */
public class CubeBallShader {

    public CubeBallShader(Context context) {
//初始化shader
        initShader(context);


        vertices = new Vertices(new String[]{"aPosition","aNormal" }, new int[]{SphereWithLimitTexture.VERTEX_POS_SIZE,SphereWithLimitTexture.VERTEX_NORMAL_SIZE}, mProgram);


        // this.index=index;


    }







    int mProgram;//自定义渲染管线程序id




    public Vertices vertices;


    // 总变换矩阵属性
    private UniformMatrix4F finalMatrix;
    Uniform4fv ambientLightUniform;// 环境光
    Uniform4fv diffuseLightUniform;// 散射光
    Uniform4fv specLightUniform;// 反射光
    //物体变换矩阵属性
    private UniformMatrix4F mMatrix;


    //灯光为摄像点的虚拟组合矩阵。
    private UniformMatrix4F mLightViewProj;
    //相机位置属性
    private Uniform3fv cameraUniform;
    //太阳光位置属性  //光源位置。
    private Uniform3fv sunLocationUniform;
    //物体变换矩阵属性

    private int textureHandler;
    private int shadowHandler;



    private float[] lightCameraViewProj= MatrixState.getNewMatrix();








    public void draw(int[] bufferIds,int numElement,CubeTexture cubeTexture,int shadowTextureId,float[] cameraViewProj) {

        MatrixState.copyMatrix(cameraViewProj,lightCameraViewProj);

        //指定使用某套shader程序
        GLES20.glUseProgram(mProgram);


        GLES20.glActiveTexture(GLES20.GL_TEXTURE_CUBE_MAP);
        cubeTexture.bind();

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, shadowTextureId);
        // Tell the texture uniform sampler to use this texture in the shader by binding to texture unit 0.
       // GLES20.glUniform1i(textureHandler, 0);
       // GLES20.glUniform1i(shadowHandler, 1);


        //最终矩阵绑定
        finalMatrix.bind();
        //物体移动矩阵绑定
        mMatrix.bind();
        //相机位置绑定
        cameraUniform.bind();
        //绑定光源处虚拟相机组合矩阵。
        mLightViewProj.bind();
        //重新注入太阳光位置 光线会转动
        sunLocationUniform.bind();


        ambientLightUniform.bind();// 环境光
        diffuseLightUniform.bind();// 散射光
        specLightUniform.bind();// 反射光
        //属性绑定

        vertices.draw(bufferIds, numElement);



    }


    //初始化shader
    public void initShader(Context mv) {


        //加载顶点着色器的脚本内容
        String  mVertexShader = ShaderUtil.loadFromAssetsFile("snooker_ball/vertex.glsl", mv.getResources());
        //加载片元着色器的脚本内容
        String    mFragmentShader = ShaderUtil.loadFromAssetsFile("snooker_ball/frag_cube_map.glsl", mv.getResources());
        //基于顶点着色器与片元着色器创建程序
        mProgram = ShaderUtil.createProgram(mVertexShader, mFragmentShader);


        finalMatrix = new UniformMatrix4F(mProgram, "uMVPMatrix", new Uniform.UniformBinder<float[]>() {
            @Override
            public float[] getBindValue() {


                return MatrixState.getFinalMatrix();
            }
        });



        textureHandler = GLES20.glGetUniformLocation(mProgram, "sTexture");
        shadowHandler = GLES20.glGetUniformLocation(mProgram, "shadowTexture");


        //uniform 属性绑定
        finalMatrix=new UniformMatrix4F(mProgram,"uMVPMatrix",new Uniform.UniformBinder<float[]>() {
            @Override
            public float[] getBindValue() {
                return MatrixState.getFinalMatrix();
            }
        });

        mMatrix=new UniformMatrix4F(mProgram,"uMMatrix",new Uniform.UniformBinder<float[]>() {
            @Override
            public float[] getBindValue() {
                return MatrixState.getMMatrix();
            }
        });
        cameraUniform=new Uniform3fv(mProgram,"cameraPosition",new Uniform.UniformBinder<FloatBuffer>() {
            @Override
            public FloatBuffer getBindValue() {
                return MatrixState.cameraFB;
            }
        });


        sunLocationUniform=new Uniform3fv(mProgram,"uLightLocation",new Uniform.UniformBinder<FloatBuffer>() {
            @Override
            public FloatBuffer getBindValue() {
                return LightSources.lightPositionFBSun;
            }
        });
        // 材料光属性
        ambientLightUniform=new Uniform4fv(mProgram,"abientLight",new Uniform.UniformBinder<FloatBuffer>() {
            @Override
            public FloatBuffer getBindValue() {
                return LightSources.ambientBuffer;
            }
        });

        specLightUniform=new Uniform4fv(mProgram,"lightSpecular",new Uniform.UniformBinder<FloatBuffer>() {
            @Override
            public FloatBuffer getBindValue() {
                return LightSources.specLightBuffer;
            }
        });
        diffuseLightUniform=new Uniform4fv(mProgram,"lightDiffuse",new Uniform.UniformBinder<FloatBuffer>() {
            @Override
            public FloatBuffer getBindValue() {
                return LightSources.diffuseBuffer;
            }
        });

        mLightViewProj=new UniformMatrix4F(mProgram,"uLightMVPMatrix",new Uniform.UniformBinder<float[]>() {
            @Override
            public float[] getBindValue() {
                return lightCameraViewProj;
            }
        });
    }


}
