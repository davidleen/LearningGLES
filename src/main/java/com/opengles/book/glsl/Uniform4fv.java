package com.opengles.book.glsl;

import android.opengl.GLES20;

import java.nio.FloatBuffer;

/**
 * shader  uniform config    for Matrix4F
 *
 * Created by davidleen29   qq:67320337
 * on 14-6-3.
 */
public   class Uniform4fv extends FloatBufferUniform {


    public Uniform4fv(int mProgram, String uniformName, UniformBinder<FloatBuffer> binder) {
        super(mProgram, uniformName, binder);
    }

    @Override
    protected void doBind(int uniformHandler, UniformBinder<FloatBuffer> binder) {

         GLES20.glUniform4fv(uniformHandler,1,binder.getBindValue());

    }
}
