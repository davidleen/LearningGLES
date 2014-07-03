package com.opengles.book.screen.dollDemo;

import com.opengles.book.framework.Game;
import com.opengles.book.screen.FrameBufferScreen;

import java.util.List;

/**
 *  人偶场景
 * Created by davidleen29   qq:67320337
 * on 2014-7-3.
 */
public class DollDemoScreen extends FrameBufferScreen{

    private Doll doll;
    private RigidObjObject[]  rigidObjObjects;

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
    }

    @Override
    public void pause() {
        super.pause();
        for (RigidObjObject objObject:rigidObjObjects)
        {
            objObject.unBind();
        }
    }

    @Override
    public void resume() {
        super.resume();
        for (RigidObjObject objObject:rigidObjObjects)
        {
            objObject.bind();
        }
    }

    @Override
    public void dispose() {
        super.dispose();
    }

    public DollDemoScreen(Game game) {
        super(game);



    }

    @Override
    protected void onPresent(float deltaData) {

    }
}
