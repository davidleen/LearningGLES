package com.opengles.book.screen.snooker;


import android.opengl.GLES20;
import com.opengles.book.*;

import android.content.Context;

import com.opengles.book.framework.gl.TextureRegion;
import com.opengles.book.glsl.*;

import java.nio.FloatBuffer;


/**
 * 球体通用对象通用对象
 *
 * @author Administrator
 */
public class BallDrawable {


    float[] vertexData;
    short[] indexData;
    BallShader ballShader;

    private void initData(float radius, TextureRegion region) {

        SphereWithLimitTexture sphere = new SphereWithLimitTexture(radius, region);
        vertexData = sphere.attributes;
        indexData = sphere.indics;

    }


    public BallDrawable(   float radius, TextureRegion region,BallShader shader) {

        initData(radius,region);
        this.ballShader=shader;
    }



    public void draw( int textureId,int shadowTextureId,float[] cameraViewProj) {

        ballShader.draw(vertexData,indexData,textureId,shadowTextureId,cameraViewProj);
    }

}
