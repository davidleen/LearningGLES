package com.opengles.book.screen.snooker;

import android.provider.ContactsContract;
import com.opengles.book.framework.Input;
import com.opengles.book.framework.MVP;
import com.opengles.book.framework.gl.Camera3D;
import com.opengles.book.math.AABB3;
import com.opengles.book.math.Vector3;

/**
 * 视角辅助类。
 * 摄像机 沿着桌子边沿 移动。
 * Created by davidleen29   qq:67320337
 * on 2014-7-9.
 */
public class CameraHelper {

    Camera3D camera3D;
    private AABB3 aabb3;

    /**
     * 摄像机的移动范围 为这个立方体的四面壁
     * @param aabb3
     */
    public CameraHelper(Camera3D camera3D,AABB3 aabb3)
    {

        this.camera3D=camera3D;

        this.aabb3=aabb3;
    }


    float previousX;
    float previousY;
    public void  onEvent(Input.TouchEvent event)
    {



        float deltaX=0;
        float deltaY=0;
        switch (event.type)
        {
            case Input.TouchEvent.TOUCH_DOWN:



                previousX=event.x;
                previousY=event.y;
                break;
            case
                    Input.TouchEvent.TOUCH_DRAGGED:

                deltaX=event.x-previousX;
                deltaY=event.y-previousY;
                previousX=event.x;
                previousY=event.y;
                break;

            case    Input.TouchEvent.TOUCH_UP:


                break;
        }









        float xStep=deltaX ;


        //横向移动的距离太小  不认为 是移动。
        if(Math.abs(xStep)<1f)
        {
            return ;
        }


        Vector3 center=Vector3.create();
        aabb3.center(center);
        //抛弃Y轴计算量  此处 仅计算 xz 旋转幅度
        center.y=camera3D.eyeY;

        Vector3 ray=Vector3.create().set(camera3D.eyeX,camera3D.eyeY,camera3D.eyeZ).sub(center);
        //绕y 轴旋转
        ray.rotate(-xStep,0,1,0);


        Vector3 result=Vector3.create();

        Vector3 N=Vector3.create();



        boolean  hasFind=false;
        //射线中心点在center  方向 ray;
        //前面平面
        N.set(0,0, -1);
        float  d=-aabb3.max.z;
        //计算与4个平面相交情况
        if(!hasFind&&getNewCameraPosition(N,ray,d,center,result))
        {

            hasFind=true;
        }
        //后面平面
           N.set(0,0, 1);
           d=aabb3.min.z;
        //计算与4个平面相交情况
        if(!hasFind&&getNewCameraPosition(N,ray,d,center,result))
        {

            hasFind=true;

        }

        //右边面平面
        N.set( -1,0, 0);
        d=aabb3.min.x;
        //计算与4个平面相交情况
        if(!hasFind&&getNewCameraPosition(N,ray,d,center,result))
        {

            hasFind=true;

        }

        //左边面平面
        N.set(1,0, 0);
        d=aabb3.min.x;
        //计算与4个平面相交情况
        if(!hasFind&&getNewCameraPosition(N,ray,d,center,result))
        {

            hasFind=true;

        }



        if(hasFind) {
            camera3D.eyeX = result.x;
            camera3D.eyeY = result.y;
            camera3D.eyeZ = result.z;
        }



        Vector3.recycle(result);
        Vector3.recycle(N);


        float yStep=deltaY/12;

        if(Math.abs(yStep)>0.1f)
        {
            //设置摄像头高度
            camera3D.eyeY = Math.min(Math.max(camera3D.eyeY + yStep, aabb3.min.y), aabb3.max.y);
        }



    }


    /**
     *
     * @param normal   平面方向量
     * @param direction  //射线方向向量
     * @param d        //平面距离
     * @param p0       //射线起点
     * @param result  //返回结果
     * @return    是否有相交。
     */
    private boolean getNewCameraPosition(Vector3 normal, Vector3 direction,float d,Vector3 p0,Vector3 result)
    {
        //计算射线 与平面的相交
        //t=(d-p0*n)/d*n
        //


        float dotValue=Vector3.dotValue(direction,normal);
       // if(Math.abs(dotValue)>0.0001f)//相交
            if(dotValue<0)//相交
        {
            float t=(d-Vector3.dotValue(p0,normal))/dotValue;


            if(t<=0 )
            {
                return false;
            }

            result.set(direction).mul(t).add(p0);

            //交点是否在空间内部
            return aabb3.contains(result);



        }

        return false;


    }

    /**
     *  判断camera 位置 是否在aabb的墙上
     * @return
     */
    public boolean  isCameraOnWall()
    {
        //y 是否在高度范围内。

      if( camera3D.eyeY>aabb3.max.y&&camera3D.eyeY<aabb3.min.y)
      {
          return false;
      }
       //如果有点在x z 线上
      if(camera3D.eyeX==aabb3.min.x||camera3D.eyeX==aabb3.max.x||camera3D.eyeZ==aabb3.min.z||camera3D.eyeZ==aabb3.max.z)
      {
          return true;
      }

        //
        return false;



    }
}
