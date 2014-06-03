package com.opengles.book.glsl;

import android.opengl.GLES20;

/**
 * shader  uniform config    for Matrix4F
 *
 * Created by davidleen29   qq:67320337
 * on 14-6-3.
 */
public abstract    class FloatArrayUniform extends Uniform<float[]> {


    public FloatArrayUniform(int mProgram, String uniformName, UniformBinder<float[]> binder) {
        super(mProgram, uniformName, binder);
    }


}
