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
public class ShadowDrawable {


    float[] vertexData;
    short[] indexData;
   int[] bufferIds;
    private BallShadowShader shader;






    public ShadowDrawable(float[] vertexData,short[] indexData,BallShadowShader shader)
    {
        this.vertexData=vertexData;
        this.indexData=indexData;
        this.shader=shader                ;
    }






    public void draw( ) {
       shader.draw(bufferIds,indexData.length );


    }




    public void bind() {

     bufferIds=   shader.vertices.create(vertexData,indexData);

    }

    public void unBind() {
        shader.vertices.dispose(bufferIds);
    }


}
