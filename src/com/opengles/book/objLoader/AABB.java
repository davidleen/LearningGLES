package com.opengles.book.objLoader;

import com.opengles.book.math.Vector3;

/**
 * 标准立方体 维度
 * Created by davidleen29   qq:67320337
 * on 14-5-27.
 */
public class AABB {




   public Vector3 min=new Vector3();

    public Vector3 max=new Vector3();

   public Vector3 center=new Vector3();

    public void update(float x, float y, float z) {

        max.x=Math.max(max.x,x);
        max.y=Math.max(max.y,y);
        max.z=Math.max(max.z, z);

        min.x=Math.min(min.x, x);
        min.y=Math.min(min.y,y);
        min.z=Math.min(min.z,z);

    }


    public float getYSpan()
    {
        return max.y-min.y;
    }

    public float getXSpan()
    {
        return max.x-min.x;
    }

    public float getZSpan()
    {
        return max.z-min.z;
    }

    public Vector3 getCenter()
    {
       return  center.set(max).sub(min).mul(0.5f);
    }


    public float getMaxSpan()
    {
        return Math.max(Math.max(getYSpan(),getXSpan()),getZSpan());
    }

}
