package com.opengles.book.glsl;

import android.opengl.GLES20;

import java.nio.FloatBuffer;

/**
 * shader  uniform config    for Uniform3fv
 *
 * Created by davidleen29   qq:67320337
 * on 14-6-3.
 */
public   class Uniform3fv_Float extends Uniform<float[]>  {


    public Uniform3fv_Float(int mProgram, String uniformName, UniformBinder<float[]> binder) {
        super(mProgram, uniformName, binder);
    }
    
     

    @Override
    protected void doBind(int uniformHandler, UniformBinder<float[]> binder) {



         GLES20.glUniform3fv(uniformHandler, 1, binder.getBindValue(),0);


    }
}
