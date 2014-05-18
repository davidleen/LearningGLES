package com.opengles.book.framework.gl;

 
import android.opengl.GLES20;

import com.opengles.book.MatrixState;
import com.opengles.book.framework.impl.GLGraphics;
import com.opengles.book.math.Vector2;
import com.opengles.book.math.Vector3;
 
 

public class OrthoCamera {
    public final Vector2 position;
    public float zoom;
    public final float frustumWidth;
    public final float frustumHeight;
    
    
    final GLGraphics glGraphics;
    
    public OrthoCamera(GLGraphics glGraphics, float frustumWidth, float frustumHeight) {
        this.glGraphics = glGraphics;
        this.frustumWidth = frustumWidth;
        this.frustumHeight = frustumHeight;
        this.position = new Vector2(frustumWidth / 2, frustumHeight / 2);
        this.zoom = 1.0f;
    }
    
    public void setViewportAndMatrices() {
        
        GLES20.glViewport(0, 0, glGraphics.getWidth(), glGraphics.getHeight());
        MatrixState.setOrthoProject(  
        
          position.x - frustumWidth * zoom / 2, 
                    position.x + frustumWidth * zoom/ 2, 
                    position.y - frustumHeight * zoom / 2, 
                    position.y + frustumHeight * zoom/ 2, 
                    1, 10);
        
        MatrixState.setCamera(0,  10, 0f, 0f, 0f, 0f, 0f, 0f,   -1f);
        
        
        //camera
        MatrixState.setInitStack();
    }
    
    public void touchToWorld(Vector2 touch) {
        touch.x = (touch.x / (float) glGraphics.getWidth()) * frustumWidth * zoom;
        touch.y = (1 - touch.y / (float) glGraphics.getHeight()) * frustumHeight * zoom;
        touch.add(position).sub(frustumWidth * zoom / 2, frustumHeight * zoom / 2);
    }
}
