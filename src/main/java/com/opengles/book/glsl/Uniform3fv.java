package com.opengles.book.glsl;

import android.opengl.GLES20;

import java.nio.FloatBuffer;

/**
 * shader  uniform config    for Uniform3fv
 *
 * Created by davidleen29   qq:67320337
 * on 14-6-3.
 */
public   class Uniform3fv extends FloatBufferUniform {


    public Uniform3fv(int mProgram, String uniformName, UniformBinder<FloatBuffer> binder) {
        super(mProgram, uniformName, binder);
    }
    
     

    @Override
    protected void doBind(int uniformHandler, UniformBinder<FloatBuffer> binder) {



         GLES20.glUniform3fv(uniformHandler, 1, binder.getBindValue());


    }
}
