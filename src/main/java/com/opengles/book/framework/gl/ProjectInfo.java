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
}
