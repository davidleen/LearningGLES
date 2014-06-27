package com.opengles.book.objects;

import com.opengles.book.MatrixState;
import com.opengles.book.framework.gl.ProjectInfo;

/**
 * 交叉计算类。
 * Created by davidleen29   qq:67320337
 * on 2014-6-27.
 */
public class IntersectUtil {


    /**
     * 根据屏幕上的点计算该点 在远近平面上的坐标值。
     * @return
     */
    public static float[][] calculateNearFarPosition(
                                        float x, float y, float width, float height,   //屏幕参数
                                        ProjectInfo projectInfo)//平面参数
    {



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
