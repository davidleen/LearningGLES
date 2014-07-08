package com.opengles.book.framework;

import com.opengles.book.MatrixState;
import com.opengles.book.framework.gl.ProjectInfo;
import com.opengles.book.math.Vector2;
import com.opengles.book.math.Vector3;

/**
 * 3D 立体空间 场景参数
 * 摄像机设置
 * 视觉空间设置
 * 投影空间参数
 * Created by davidleen29   qq:67320337
 * on 2014-7-8.
 */
public class MVP {



    Camera camera;
    ViewPort viewPort;
    Project project;


    /**
     * 摄像机类
     */
    public static class Camera
    {
        Vector3 eye;
        Vector3 lookAt;
        Vector3 up;

    }

    /**
     * 视窗类。
     */
    public static class ViewPort
    {

        float width;
        float height;

       public Vector2 center;


        public ViewPort(float left, float bottom, float width, float height) {

            center=new Vector2(left+width/2,bottom+height/2);
            this.width = width;
            this.height = height;
        }
    }

    /**
     * 投影参数类。
     */
    public static class Project
    {

        public float left;
        public float right;
        public float bottom;
        public float top;
        public  float near;
        public  float  far;

        public Project(float left, float right, float bottom, float top, float near, float far) {
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


    }



    public void  updateCamera(){}
    public void setViewPort(){}
    public void setProject(){}


    /**
     *
     * 根据屏幕上的点计算该点 在远近平面上的坐标值。
     *
     * @param x    屏幕上坐标点   输入参数
     * @param y    屏幕上坐标点   输入参数
     * @param near   对应近平面上的空间坐标   输出参数
     * @param far   对应远平面上的空间坐标   输出参数
     */
    public void calculateNearFarPosition(
            float x, float y ,Vector3 near,Vector3 far  //屏幕参数
    )//平面参数
    {
         float width=viewPort.width;
        float height=viewPort.height;
        Project projectInfo=project;


        float  x0=x-width/2;
        float y0=height/2-y;

        //摄像机坐标系中的 近面点
        float xNear=(projectInfo.right-projectInfo.left)*(x0/width);
        float yNear=(projectInfo.top-projectInfo.bottom)*(y0/height);


        float zNear=-projectInfo.near;

        //摄像机坐标系中的 远面点
        float factor=projectInfo.far/projectInfo.near;
        float xFar=xNear*factor;
        float yFar=yNear*factor;
        float zFar=-projectInfo.far;




        float[]  nearPosition= MatrixState.fromCameraToWorld(new float[]{xNear,yNear,zNear});

        float[] farPosition =MatrixState.fromCameraToWorld(new float[]{xFar,yFar,zFar});
        near.set(nearPosition[0],nearPosition[1],nearPosition[2]);
        far.set(farPosition[0],farPosition[1],farPosition[2]);


    }

}
