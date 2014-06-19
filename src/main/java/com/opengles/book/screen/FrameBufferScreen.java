package com.opengles.book.screen;

import android.opengl.GLES20;
import android.util.DisplayMetrics;
import com.opengles.book.LightSources;
import com.opengles.book.framework.Game;
import com.opengles.book.framework.Input;
import com.opengles.book.framework.gl.FPSCounter;
import com.opengles.book.framework.gl.LookAtCamera;
import com.opengles.book.framework.impl.GLScreen;
import com.opengles.book.galaxy.CameraController;
import com.opengles.book.glsl.FrameBufferManager;
import com.opengles.book.objects.TwistCuboid;

import java.util.List;

/**
 * 使用fbo的 GL_SCREEN   可以继承 所有绘制的对象都将存在fbo 上  然后绘制到系统输出上。。  frameId=0  默认为系统的。
 * Created by davidleen29   qq:67320337
 * on 2014-6-11.
 */
public abstract class FrameBufferScreen extends GLScreen {




    FrameBufferManager.FrameBuffer frameBuffer;






    @Override
    public void update(float deltaTime) {



    }

    @Override
    public final void  present(float deltaTime) {


       frameBuffer.bind();

        onPresent(deltaTime);

        frameBuffer.show();






    }


    @Override
    public void pause() {


         frameBuffer.delete();



    }


    @Override
    public void resume() {

        frameBuffer.create();

    }

    @Override
    public void dispose() {

    }

    public FrameBufferScreen(Game game) {
        super(game);

         DisplayMetrics metrics= game.getContext().getResources().getDisplayMetrics();

        frameBuffer=new FrameBufferManager.FrameBuffer(game.getContext(),metrics.widthPixels,metrics.heightPixels);

    }



    protected abstract void onPresent(float  deltaData);


}
