package com.opengles.book.glsl;

import android.opengl.GLES20;
import android.util.Log;
import com.opengles.book.framework.exceptions.UniformLocationNoFoundException;

/**
 *
 * Created by davidleen29   qq:67320337
 * on 14-6-3.
 */
public  abstract class Uniform<T> {

	private static final String TAG="Uniform";
	boolean isUpdate=false;

    int uniformHandler;
    UniformBinder<T> binder;
    public Uniform(int mProgram, String uniformName,UniformBinder<T> binder)
    {
        uniformHandler= GLES20.glGetUniformLocation(mProgram,uniformName);
        if(uniformHandler==-1)
        {
              Log.e(TAG, "program: "+ mProgram +" does not have  uniform :"+ uniformName );
            //   throw new UniformLocationNoFoundException( String.valueOf(mProgram), uniformName  );
        }

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
