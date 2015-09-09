package com.giants3.android.openglesframework.framework.impl;

import java.util.List;

import com.giants3.android.openglesframework.framework.Input;

import android.view.View.OnTouchListener;

 

public interface TouchHandler extends OnTouchListener {
    public boolean isTouchDown(int pointer);
    
    public int getTouchX(int pointer);
    
    public int getTouchY(int pointer);
    
    public List<Input.TouchEvent> getTouchEvents();
}
