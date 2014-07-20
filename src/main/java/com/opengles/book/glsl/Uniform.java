package com.opengles.book.glsl;

import android.opengl.GLES20;

/**
 *
 * Created by davidleen29   qq:67320337
 * on 14-6-3.
 */
public  abstract class Uniform<T> {

	
	boolean isUpdate=false;

    int uniformHandler;
    UniformBinder<T> binder;
    public Uniform(int mProgram, String uniformName,UniformBinder<T> binder)
    {
        uniformHandler= GLES20.glGetUniformLocation(mProgram,uniformName);
        if(uniformHandler==-1)
            throw new RuntimeException("can not find uniform :"+ uniformName+" in programe :"+mProgram);
        this.binder=binder;
        notifyChanged();
    }


    public  void bind()
    {
//    	if(isUpdate)
//    	{
    		doBind(uniformHandler,binder);
//    		isUpdate=false;
//    	}
    }

    protected abstract void doBind(int  uniformHandler,UniformBinder<T> binder);

    public interface UniformBinder<T>
    {
        public T getBindValue();
    }
    
    public void notifyChanged()
    {
    	isUpdate=true;
    }
}
