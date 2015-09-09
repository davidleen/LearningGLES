package com.giants3.android.openglesframework.framework.gl;

import android.opengl.GLES20;
import com.giants3.android.openglesframework.framework.math.Vector2;

/**
 * 视窗对象
 * Created by Administrator on 2014/7/8.
 */
public class ViewPort {

    /**
     * 视窗类。
     */

   public int left; public int bottom; public int width; public int height;

    public ViewPort(int left, int bottom, int width, int height) {
        this.left = left;
        this.bottom = bottom;
        this.width = width;
        this.height = height;
    }

    public void apply() {


        GLES20.glViewport(left, bottom, width, height);
    }
}
