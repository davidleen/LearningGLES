package com.opengles.book.framework.objects;

import com.opengles.book.math.Vector2;
import com.opengles.book.math.Vector3;

/**
 * Created by Administrator on 2014/6/23.
 */
public class CSquare extends CPlan {

    public Vector2 min;
    public Vector2 max;





    //计算方块跟射线的交点
    @Override
    public IntersectType isIntersected(CRay ray, Vector3 intersectedPosition) {


        IntersectType result ;
        // Compute the t value for the directed line ab intersecting the plane
        float t =(d - Vector3.dotValue(normal, ray.origin)) / Vector3.dotValue(normal, ray.direction);


        if(t>0.0001f)
        {

            result =IntersectType.INTERSECTED;
            intersectedPosition.set(ray.direction).mul(t).add(ray.origin);

            float x =intersectedPosition.x;
            float y=intersectedPosition.y;
            if(min.x<x&&min.y<y&&max.x>x&&max.y>y)
                result =IntersectType.INTERSECTED;
            else
            {result=IntersectType.MISS;

            }



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
