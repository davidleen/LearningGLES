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

    private BallShadowShader shader;



    public BallShadowDrawable( float radius, TextureRegion region,BallShadowShader shader) {

        initData(radius, region);
        this.shader=shader;




    }


    private void initData(float radius, TextureRegion region) {

        SphereWithLimitTexture sphere = new SphereWithLimitTexture(radius,  region);
        vertexData = sphere.attributes;
        indexData = sphere.indics;

    }




    public void draw( ) {
       shader.draw(vertexData,indexData);


    }







}
