package com.opengles.book.framework;

import android.util.Log;
import com.opengles.book.BuildConfig;

import java.util.ArrayList;
import java.util.List;

public class Pool<T> {

    private String TAG="Pool";
    public interface PoolObjectFactory<T> {
        public T createObject();
    }

    private final List<T> freeObjects;
    private final PoolObjectFactory<T> factory;
    private final int maxSize;
    private  long reuseCount;
    private long createCount;

    public Pool(PoolObjectFactory<T> factory, int maxSize) {
        this.factory = factory;
        this.maxSize = maxSize;
        this.freeObjects = new ArrayList<T>(maxSize);
        reuseCount=0;reuseCount=0;
    }

    public T newObject() {
        T object = null;

        if (freeObjects.size() == 0) {
            object = factory.createObject();
            if(BuildConfig.DEBUG)
             Log.d(TAG, "create "+object.getClass().getName()+"object time :" + (++createCount));
        }
        else {
            object = freeObjects.remove(freeObjects.size() - 1);

         //   Log.d(TAG,"reuse "+object.getClass().getName()+" time :"+(++reuseCount));
        }
        return object;
    }

    public void free(T object) {
        if (freeObjects.size() < maxSize)
            freeObjects.add(object);
    }
}
