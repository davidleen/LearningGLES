package com.opengles.book.glsl;

import java.nio.FloatBuffer;

/**
 * shader  uniform config    for Matrix4F
 *
 * Created by davidleen29   qq:67320337
 * on 14-6-3.
 */
public abstract    class FloatBufferUniform extends Uniform<FloatBuffer> {


    public FloatBufferUniform(int mProgram, String uniformName, UniformBinder<FloatBuffer> binder) {
        super(mProgram, uniformName, binder);
    }

     
}
