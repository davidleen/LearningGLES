package com.opengles.book.framework.objects;

import com.opengles.book.framework.Pool;
import com.opengles.book.math.Vector3;

/**
 * 射线对象
 * Created by davidleen29   qq:67320337
 * on 2014-6-18.
 */
public class CRay {

    public Vector3 origin=Vector3.create() ;
    public Vector3 direction=Vector3.create() ;

    private CRay() {
    }

    public static CRay createCRay() {

        CRay ray=rayPool.newObject();
        ray.direction=Vector3.create();
        ray.origin=Vector3.create();
        return ray;
    }


    /**
     * 释放
     * @param cRay
     */
    public static void recycle(CRay cRay)
    {

        Vector3 temp=cRay.direction;
        cRay.direction=null;
        Vector3.recycle(temp);
        temp=cRay.origin;
        cRay.origin=null;
        Vector3.recycle(temp);
        rayPool.free(cRay);


    }

    /**
     * 根据传入的时间获取射线上的点。
      * @param t
     * @return
     */
    public Vector3 getPoint(float t)
    {
        return direction.cpy().mul(t).add(origin);
    }


    private static Pool<CRay> rayPool=new Pool<CRay>(new Pool.PoolObjectFactory<CRay>() {
        @Override
        public CRay createObject() {
            return new CRay();
        }
    },100);

}
