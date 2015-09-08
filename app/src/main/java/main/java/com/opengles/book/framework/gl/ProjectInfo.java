package com.opengles.book.framework.gl;

import com.opengles.book.MatrixState;

/**
 * 透视方程信息
 * Created by davidleen29   qq:67320337
 * on 2014-6-27.
 */
public class ProjectInfo {

    public float left;
    public float right;
    public float bottom;
    public float top;
    public  float near;
    public  float  far;

    public ProjectInfo(float left, float right, float bottom, float top, float near, float far) {
        this.left = left;
        this.right = right;
        this.bottom = bottom;
        this.top = top;
        this.near = near;
        this.far = far;
    }

    /**
     * 设置视觉投影
     */
    public void setFrustum()
    {
        MatrixState.setFrustumProject(left, right, bottom, top, near, far);
    }

    /**
     * 设置正交投影
     */
    public void setOrtho()
    {
        MatrixState.setOrthoProject(left, right, bottom, top, near, far);
    }



    /**
     * 根据屏幕上的点计算该点 在远近平面上的坐标值。
     * @return
     */
    public   float[][] calculateNearFarPosition(
            float x, float y, float width, float height   //屏幕参数
           )//平面参数
    {

        ProjectInfo projectInfo=this;


        float  x0=x-width/2;
        float y0=height/2-y;

        //摄像机坐标系中的 近面点
        float xNear=(projectInfo.right-projectInfo.left)*(x0/width);
        float yNear=(projectInfo.top-projectInfo.bottom)*(y0/height);

//        float xNear=( projectInfo.left*2)*(x0/width);
//        float yNear=( projectInfo.top*2)*(y0/height);

        float zNear=-projectInfo.near;

        //摄像机坐标系中的 远面点
        float factor=projectInfo.far/projectInfo.near;
        float xFar=xNear*factor;
        float yFar=yNear*factor;
        float zFar=-projectInfo.far;




        float[]  nearPosition= MatrixState.fromCameraToWorld(new float[]{xNear,yNear,zNear});

        float[] farPosition =MatrixState.fromCameraToWorld(new float[]{xFar,yFar,zFar});



        return new float[ ][]{nearPosition,farPosition};


    }
}
