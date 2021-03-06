package com.giants3.android.openglesframework.framework.objects;

import com.giants3.android.openglesframework.framework.math.Vector3;

/**
 * 平面对象
 * Created by davidleen29   qq:67320337
 * on 2014-6-18.
 */
public class CPlan extends CObject {


    public  float d;    //d = dot(n,p) for a given point p on the plane
    public Vector3 normal;   // Plane normal. Points x on the plane satisfy Dot(n,x) = d

    CPlan ComputePlane(Vector3 a, Vector3 b, Vector3 c)
    {
        CPlan p=new CPlan();
        Vector3 v1=Vector3.create().set(b).sub(a);
        Vector3 v2=Vector3.create().set(c).sub(a);
        Vector3 crossResult=Vector3.create();
        Vector3.crossValue(v1, v2,crossResult);
        p.normal =crossResult.nor();
        Vector3.recycle(v1);
        Vector3.recycle(v2);
        p.d = Vector3.dotValue(p.normal, a);
        return p;
    }
    @Override
    public Vector3 getNormal(Vector3 point) {
        return normal.cpy();

    }

    //计算方块跟射线的交点
    @Override
    public IntersectType isIntersected(CRay ray, Vector3 intersectedPosition) {


        IntersectType result ;
          // Compute the t value for the directed line ab intersecting the plane
        float t =(d - Vector3.dotValue(normal, ray.origin)) / Vector3.dotValue(normal, ray.direction);


        if(t>0.1f)
        {

            result =IntersectType.INTERSECTED;
            intersectedPosition.set(ray.direction).mul(t).add(ray.origin);


        }else
        {
            result=IntersectType.MISS;
        }




        return result;
    }

    @Override
    public boolean isInShadow(CRay lightRay) {
        return false;
    }
}
