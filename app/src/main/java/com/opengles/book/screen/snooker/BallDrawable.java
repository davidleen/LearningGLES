package com.opengles.book.screen.snooker;


import com.giants3.android.openglesframework.framework.gl.TextureRegion;

/**
 * 球体通用对象通用对象
 *
 * @author Administrator
 */
public class BallDrawable {


    float[] vertexData;
    short[] indexData;
    BallShader ballShader;
    int[]  bufferIds;

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

        ballShader.draw( bufferIds,indexData.length,textureId,shadowTextureId,cameraViewProj);
    }

    public void bind() {

        bufferIds=   ballShader.vertices.create(vertexData,indexData);

    }

    public void unBind() {
        ballShader.vertices.dispose(bufferIds);
    }
}
