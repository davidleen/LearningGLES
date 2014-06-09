package com.opengles.book.glsl;

import android.opengl.GLES20;
import com.opengles.book.objects.Shader;

/**
 * shader  uniform config    for Matrix4F
 *
 * Created by davidleen29   qq:67320337
 * on 14-6-3.
 */
public   class UniformMatrix4F extends FloatArrayUniform {


    public UniformMatrix4F(int mProgram, String uniformName, UniformBinder<float[]> binder) {
        super(mProgram, uniformName, binder);
    }

    @Override
    protected void doBind(int uniformHandler, UniformBinder<float[]> binder) {

         GLES20.glUniformMatrix4fv(uniformHandler,1,false,binder.getBindValue(),0);

    }
}
