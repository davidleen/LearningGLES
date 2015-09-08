package com.opengles.book.framework.objects;

import com.opengles.book.math.Vector3;

/**
 * 光线追踪系统中 物体对象 基类
 * Created by davidleen29   qq:67320337
 * on 2014-6-18.
 */
public abstract class CObject {

//    物体表面环境光反射系数kAmbient
//             ，漫反射系数 kDiffuse
//             镜面
//                   反射系数 kSpecular
//    ，镜面反射强度(shininess)和环境反射强度(reflectivity)

    public Vector3 color;
    public Vector3 kAmbient;
    public Vector3 kDiffuse;
    public Vector3 kSpecular;
    public float shininess;
    public float reflectivity;

    public abstract  Vector3 getNormal(Vector3 point);
    public abstract  IntersectType isIntersected(CRay ray,Vector3 intersectedPosition);

    public abstract  boolean  isInShadow(CRay lightRay);
}
