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



 public   Camera camera=new Camera();
 public   ViewPort viewPort ;
  public  Project project  ;



   public void setCamera(float eyeX, float eyeY, float eyeZ, float targetX, float targetY, float targetZ, float upX, float upY, float upZ)
   {
       camera.set(  eyeX,   eyeY,   eyeZ,   targetX,   targetY,   targetZ,   upX,   upY,   upZ);
   }

    public void setViewPort(float left,float bottom, float width,float height)
    {
        viewPort=new ViewPort(left,bottom,width,height);
    }


    public void setProject(float left, float right, float bottom, float top, float near, float far)
    {

        project=new Project(left, right, bottom, top, near, far);
    }
    /**
     * 摄像机类
     */
    public static class Camera
    {
        Vector3 eye=Vector3.create();
        Vector3 lookAt=Vector3.create();
        Vector3 up=Vector3.create();


        public void set(float eyeX, float eyeY, float eyeZ, float targetX, float targetY, float targetZ, float upX, float upY, float upZ)
        {
            eye.set(eyeX, eyeY, eyeZ);
            lookAt.set(targetX,targetY,targetZ);
            up.set(upX,upY,upZ);
        }
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



    public void  applyCamera(){}
    public void applyViewPort(){}
    public void applyProject(){}


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
