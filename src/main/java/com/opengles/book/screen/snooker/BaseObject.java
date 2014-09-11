package com.opengles.book.screen.snooker;

import com.opengles.book.framework.Input;

import java.util.List;

/**
 * {简单描述用途}
 * Created by davidleen29   qq:67320337
 * on 2014-9-10.
 */
public class BaseObject implements  Workable {
    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void update(float deltaTime) {

    }

    @Override
    public void present(float deltaTime) {

    }

    @Override
    public boolean onTouchEvent(Input.TouchEvent event) {
        return false;
    }


    @Override
    public void add(Workable workable) {

    }

    @Override
    public void remove(Workable workable) {

    }

    @Override
    public boolean hasChild() {
        return false;
    }
}
