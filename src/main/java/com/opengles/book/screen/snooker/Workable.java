package com.opengles.book.screen.snooker;

import com.opengles.book.framework.Input;

import java.util.List;

/**
 * {简单描述用途}
 * Created by davidleen29   qq:67320337
 * on 2014-9-10.
 */
public interface Workable {


    public void resume();
    public void pause();
    public void update(float deltaTime);
    public void present(float deltaTime);
    public boolean onTouchEvent( Input.TouchEvent  event );

    public void add(Workable workable);
    public void remove(Workable workable);
    public boolean hasChild();
}
