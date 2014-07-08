package com.opengles.book.math;

/**
 * 3D 轴对齐矩形边框类。
 * Created by davidleen29   qq:67320337
 * on 2014-6-24.
 */
public class AABB3 {

    //公共数据
    Vector3 min=Vector3.create();
    Vector3 max=Vector3.create();

    //Vector3 size(){}
    public AABB3( ) {

    }

    public AABB3(Vector3 min, Vector3 max) {
        this.min.set(min);
        this.max.set(max);
    }


    public void reset(Vector3 min,Vector3 max)
    {   this.min.set(min);
        this.max.set(max);

    }



    public void reset(float minX,float minY,float minZ,float maxX,float maxY,float maxZ)
    {   this.min.set(minX,minY,minZ);
        this.max.set(maxX,maxY,maxZ);

    }
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
      return  contains(point.x,point.y,point.z);

    }
    /**
     * 是否包含点。
     * @param   x,   y,   z
     * @return
     */
    public  boolean contains(float x, float y, float z)
    {
        if( x<min.x) return false;
        if( y<min.y) return false;
        if (z<min.z) return false;
        if( x>max.x) return false;
        if( y>max.y) return false;
        if( z>max.z) return false;

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
     /*
     * Woo提出的方法，先判断矩形边界框的哪个面会相交，
     * 再检测射线与包含这个面的平面的相交性。
     * 如果交点在盒子中，那么射线与矩形边界框相交，
     * 否则不存在相交
     */
    //和参数射线的相交性测试，如果不相交则返回值是一个非常大的数(大于1)
    //如果相交，返回相交时间t
    //t为0-1之间的值
    public float rayIntersect(
            Vector3 rayStart,//射线起点
            Vector3 rayDir,//射线长度和方向
            Vector3 returnNormal//可选的，相交点处法向量
    ){
        //如果未相交则返回这个大数
        final float kNoIntersection = Float.POSITIVE_INFINITY;
        //检查点在矩形边界内的情况，并计算到每个面的距离
        boolean inside = true;
        float xt, xn = 0.0f;
        if(rayStart.x<min.x){
            xt = min.x - rayStart.x;
            if(xt>rayDir.x){ return kNoIntersection; }
            xt /= rayDir.x;
            inside = false;
            xn = -1.0f;
        }
        else if(rayStart.x>max.x){
            xt = max.x - rayStart.x;
            if(xt<rayDir.x){ return kNoIntersection; }
            xt /= rayDir.x;
            inside = false;
            xn = 1.0f;
        }
        else{
            xt = -1.0f;
        }

        float yt, yn = 0.0f;
        if(rayStart.y<min.y){
            yt = min.y - rayStart.y;
            if(yt>rayDir.y){ return kNoIntersection; }
            yt /= rayDir.y;
            inside = false;
            yn = -1.0f;
        }
        else if(rayStart.y>max.y){
            yt = max.y - rayStart.y;
            if(yt<rayDir.y){ return kNoIntersection; }
            yt /= rayDir.y;
            inside = false;
            yn = 1.0f;
        }
        else{
            yt = -1.0f;
        }

        float zt, zn = 0.0f;
        if(rayStart.z<min.z){
            zt = min.z - rayStart.z;
            if(zt>rayDir.z){ return kNoIntersection; }
            zt /= rayDir.z;
            inside = false;
            zn = -1.0f;
        }
        else if(rayStart.z>max.z){
            zt = max.z - rayStart.z;
            if(zt<rayDir.z){ return kNoIntersection; }
            zt /= rayDir.z;
            inside = false;
            zn = 1.0f;
        }
        else{
            zt = -1.0f;
        }
        //是否在矩形边界框内？
        if(inside){
            if(returnNormal != null){
                returnNormal = rayDir.mul(-1);
                returnNormal.nor();
            }
            return 0.0f;
        }
        //选择最远的平面————发生相交的地方
        int which = 0;
        float t = xt;
        if(yt>t){
            which = 1;
            t=yt;
        }
        if(zt>t){
            which = 2;
            t=zt;
        }
        switch(which){
            case 0://和yz平面相交
            {
                float y=rayStart.y+rayDir.y*t;
                if(y<min.y||y>max.y){return kNoIntersection;}
                float z=rayStart.z+rayDir.z*t;
                if(z<min.z||z>max.z){return kNoIntersection;}
                if(returnNormal != null){
                    returnNormal.x = xn;
                    returnNormal.y = 0.0f;
                    returnNormal.z = 0.0f;
                }
            }
            break;
            case 1://和xz平面相交
            {
                float x=rayStart.x+rayDir.x*t;
                if(x<min.x||x>max.x){return kNoIntersection;}
                float z=rayStart.z+rayDir.z*t;
                if(z<min.z||z>max.z){return kNoIntersection;}
                if(returnNormal != null){
                    returnNormal.x = 0.0f;
                    returnNormal.y = yn;
                    returnNormal.z = 0.0f;
                }
            }
            break;
            case 2://和xy平面相交
            {
                float x=rayStart.x+rayDir.x*t;
                if(x<min.x||x>max.x){return kNoIntersection;}
                float y=rayStart.y+rayDir.y*t;
                if(y<min.y||y>max.y){return kNoIntersection;}
                if(returnNormal != null){
                    returnNormal.x = 0.0f;
                    returnNormal.y = 0.0f;
                    returnNormal.z = zn;
                }
            }
            break;
        }
        return t;//返回相交点参数值
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
