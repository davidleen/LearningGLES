package com.opengles.book.framework.objects;

import com.opengles.book.math.Vector2;
import com.opengles.book.math.Vector3;

/**
 * 有限定范围的平面
 * Created by Administrator on 2014/6/23.
 */
public class CSquare extends CPlan {

    public Vector3 min;
    public Vector3 max;





    //计算方块跟射线的交点
    @Override
    public IntersectType isIntersected(CRay ray, Vector3 intersectedPosition) {


        IntersectType result ;
        // Compute the t value for the directed line ab intersecting the plane
        float t =(d - Vector3.dotValue(normal, ray.origin)) / Vector3.dotValue(normal, ray.direction);


        if(t>0.1f)   //如果改点在平面上  不相交 t 值大于一个阀值 才能说明相交。
        {

            result =IntersectType.INTERSECTED;
            intersectedPosition.set(ray.direction).mul(t).add(ray.origin);


           if(intersectedPosition.between(min,max))
                result =IntersectType.INTERSECTED;
            else
            {
                result=IntersectType.MISS;

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
