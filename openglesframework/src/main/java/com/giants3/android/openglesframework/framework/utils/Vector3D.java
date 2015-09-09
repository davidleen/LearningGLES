package com.giants3.android.openglesframework.framework.utils;

/**
 * 向量的功能类
 * Created by davidleen29   qq:67320337
 * on 14-4-11.
 */
public class Vector3D {


    public static void normalize(float[] normal)
    {

        float length=0;
        for(float value:normal)
        {

            length+=Math.pow(value,2);
        }
        if(length<=0) return ;
        int arrayLength = normal.length;
        for (int i = 0; i < arrayLength            ; i++) {

            normal[i]/=length;
        }

    }
}
