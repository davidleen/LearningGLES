package com.opengles.book.math;

/**
 * 3D 轴对齐矩形边框类。
 * Created by davidleen29   qq:67320337
 * on 2014-6-24.
 */
public class AABB3 {

    //公共数据
    Vector3 min;
    Vector3 max;

    //Vector3 size(){}

    /**
     * 求中心点
     * @param center
     */
    public void center(Vector3 center)
    {
        center.set(min).add(max).mul(0.5f);
    }


    /**
     * 是否包含点。
     * @param point
     * @return
     */
    public  boolean contains(Vector3 point)
    {
        if(point.x<min.x) return false;
        if(point.y<min.y) return false;
        if(point.z<min.z) return false;
        if(point.x>max.x) return false;
        if(point.y>max.y) return false;
        if(point.z>max.z) return false;

        return  true;

    }


    /**
     * 求距离该点最近的点
     * @param point
     * @param resultPoint    结果返回点。
     */
    public void closetPointTo(Vector3 point,Vector3 resultPoint)
    {
//        //在每一维度上将 point 推向 矩形边界。
//        if(point.x<min.x)
//        {
//            resultPoint.x=min.x;
//        }else if(point.x>max.x)
//        {
//            resultPoint.x=max.x;
//
//        }else
//        {
//            resultPoint.x=point.x;
//        }
//
//
//
//
//        resultPoint.x=

    }


    /**
     * 检测与圆相交的点。
     * @param center
     * @param radius
     * @param intersectPoint   相交点存放位置。
     * @return
     */
    public boolean intersectSphere(Vector3 center,float radius,Vector3 intersectPoint)
    {


        return false;
    }


    /**
     * 检测射线跟矩形空间的相交情况
     *
     * @param rayOrigin
     * @param rayDirection
     * @param resultPoint
     * @return
     */
    public float  rayIntersect(Vector3 rayOrigin ,Vector3 rayDirection,Vector3 resultPoint)
    {


        return 0;
    }

    /**
     * 判断是否为空  即 矩形参数是否合理。
     * @return
     */
    public boolean  isEmpty()
    {
        return (min.x>max.x)||(min.y>max.y)||(min.z>max.z);
    }




}
