package com.opengles.book.glsl;

import android.opengl.GLES20;

import java.nio.FloatBuffer;

/**
 * shader  uniform config    for Matrix4F
 *
 * Created by davidleen29   qq:67320337
 * on 14-6-3.
 */
public   class Uniform1fv extends FloatArrayUniform {


    public Uniform1fv(int mProgram, String uniformName, UniformBinder<float[]> binder) {
        super(mProgram, uniformName, binder);
    }

    @Override
    protected void doBind(int uniformHandler, UniformBinder<float[]> binder) {

         GLES20.glUniform1fv(uniformHandler, 1, binder.getBindValue(),0);

    }
}
