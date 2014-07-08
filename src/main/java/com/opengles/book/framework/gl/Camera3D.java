package com.opengles.book.framework.gl;

import com.opengles.book.MatrixState;
import com.opengles.book.math.Vector3;

/**
 * 摄像头信息类
 */
public class Camera3D {


    public float eyeX;
    public float eyeY;
    public float eyeZ;



    public float targetX;
    public float targetY;
    public float targetZ;
    public float upX;
    public float upY;
    public float upZ;

    public Camera3D(float eyeX, float eyeY, float eyeZ, float targetX, float targetY, float targetZ, float upX, float upY, float upZ) {
        this.eyeX = eyeX;
        this.eyeY = eyeY;
        this.eyeZ = eyeZ;
        this.targetX = targetX;
        this.targetY = targetY;
        this.targetZ = targetZ;
        this.upX = upX;
        this.upY = upY;
        this.upZ = upZ;
    }

    public void setCamera()
    {

        MatrixState.setCamera(
                eyeX,   //人眼位置的X
                eyeY,    //人眼位置的Y
                eyeZ,   //人眼位置的Z
                targetX,    //人眼球看的点X
                targetY,   //人眼球看的点Y
                targetZ,   //人眼球看的点Z
                upX,
                upY,
                upZ);

    }

}
