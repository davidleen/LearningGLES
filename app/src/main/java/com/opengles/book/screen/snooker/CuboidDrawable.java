package com.opengles.book.screen.snooker;


import com.giants3.android.openglesframework.framework.gl.CubeTexture;

/**
 * 长方体对象绘制类。  使用环境纹理
 * Created by davidleen29   qq:67320337
 * on 2014-7-9.
 */
public class CuboidDrawable   {


    CubeTexture cubeTexture;
    CubeBallShader shader;

    float[] vertexData;
    short[] indexData;
    int numElements;
    int[] bufferIds;

//    public CuboidDrawable(  float xLength, float yLength, float zLength,CubeTexture texture, CubeBallShader shader
//    ) {
//        CuboidWithCubeTexture   cuboidWithCubeTexture=new CuboidWithCubeTexture(xLength,yLength,zLength);
//        vertexData=cuboidWithCubeTexture.vertexData;
//        indexData=cuboidWithCubeTexture.indexData;
//        numElements=indexData.length;
//        this.cubeTexture=texture;
//        this.shader=shader;
//
//    }
    public CuboidDrawable(  float[] vertexData, short[] indexData, CubeTexture texture, CubeBallShader shader
    ) {
      this.vertexData=vertexData;
        this.indexData=indexData;
        numElements=indexData.length;
        this.cubeTexture=texture;
        this.shader=shader;

    }


    public void bind() {

        bufferIds=shader.vertices.create(vertexData,indexData);
    }


    public void unBind() {
        shader.vertices.dispose(bufferIds);

    }


    public void draw( int shadowTextureId,float[] cameraViewProj) {
        shader.draw(bufferIds,numElements,cubeTexture, shadowTextureId,  cameraViewProj);
    }



}
