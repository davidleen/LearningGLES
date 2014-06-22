package com.opengles.book.framework.objects;

import com.opengles.book.math.Vector3;

/**
 * 球体对象
 * Created by davidleen29   qq:67320337
 * on 2014-6-18.
 */
public class CSphere extends CObject {


    public Vector3 center;
    public float radius;

    @Override
    public Vector3 getNormal(Vector3 point) {


        return Vector3.create().set(point).sub(center).nor();

    }

    //计算圆跟射线的交点
    @Override
    public IntersectType isIntersected(CRay ray, Vector3 intersectedPosition) {


        IntersectType result ;

        Vector3 m=ray.origin.cpy().sub(center);

        float b=Vector3.dotValue(m,ray.direction);
        float c=Vector3.dotValue(m,m)-radius*radius;

        Vector3.recycle(m);
        // Exit if r’s origin outside s (c > 0) and r pointing away from s (b > 0)
      // if (c > 0.0f && b > 0.0f) return IntersectType.MISS;
        float discriminant = b*b - c;
        // A negative discriminant corresponds to ray missing sphere
        if(discriminant<0.0f)
        {
            result=IntersectType.MISS;
        }else {
            if (discriminant < 0.0001f) //相切点
            {
                result = IntersectType.INTERSECTED;
            } else {
                result = IntersectType.INTERSECTED_IN;
            }


            // Ray now found to intersect sphere, compute smallest t value of intersection
            float   t = (float) (-b - Math.sqrt(discriminant));
            // If t is negative, ray started inside sphere so clamp t to zero
          if (t <= 0.0f)
              result = IntersectType.MISS;
            else
              intersectedPosition.set(ray.direction).mul(t).add(ray.origin);
        }



        return result;
    }
}
