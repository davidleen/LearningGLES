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


        return Vector3.create().sub(center).nor();

    }

    @Override
    public IntersectType isIntersected(CRay ray, float distance) {
        return null;
    }
}
