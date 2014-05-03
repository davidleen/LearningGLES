package com.opengles.book.framework.impl;

import java.util.List;

import com.opengles.book.framework.Input.TouchEvent;

import android.view.View.OnTouchListener;

 

public interface TouchHandler extends OnTouchListener {
    public boolean isTouchDown(int pointer);
    
    public int getTouchX(int pointer);
    
    public int getTouchY(int pointer);
    
    public List<TouchEvent> getTouchEvents();
}
