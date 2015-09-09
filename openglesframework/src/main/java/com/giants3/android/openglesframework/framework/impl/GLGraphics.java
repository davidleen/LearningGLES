package com.giants3.android.openglesframework.framework.impl;

 

import android.opengl.GLSurfaceView;

public class GLGraphics {
    GLSurfaceView glView;
    
    
    GLGraphics(GLSurfaceView glView) {
        this.glView = glView;
    }
    
    
    
    public int getWidth() {
        return glView.getWidth();
    }
    
    public int getHeight() {
        return glView.getHeight();
    }
}
