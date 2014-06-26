package com.opengles.book.screen.cubeCollisionDemo;

import android.opengl.GLES20;
import com.bulletphysics.collision.shapes.TriangleIndexVertexArray;
import com.opengles.book.MatrixState;
import com.opengles.book.framework.Game;
import com.opengles.book.framework.Input;
import com.opengles.book.framework.impl.GLGame;
import com.opengles.book.screen.FrameBufferScreen;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL10Ext;
import java.util.List;

/**
 * 箱子碰撞模拟demo 界面
 * Created by davidleen29   qq:67320337
 * on 2014-6-25.
 */
public class CubeCollisionDemoScreen extends FrameBufferScreen {

    World world;

    final static float EYE_X=-3;//观察者的位置x
    final static float EYE_Y=10 ;//观察者的位置y
    final static float EYE_Z=30;//观察者的位置z

    final static float TARGET_X=1;//目标的位置x
    final static float TARGET_Y=20;//目的位置Y
    final static float TARGET_Z=-30;//目标的位置Z

    float ratio;

    public CubeCollisionDemoScreen(Game game ) {
        super(game);
         world=new World(game.getContext());
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        List<Input.TouchEvent> touchEvents = glGame.getInput().getTouchEvents();

        for(Input.TouchEvent touchEvent:touchEvents)
        {
             if(touchEvent.type== Input.TouchEvent.TOUCH_DOWN)
             {
                 world.add();
             }
            }






            world.onUpdate(deltaTime);
    }

    @Override
    public void pause() {
        super.pause();
        world.onPause();
    }

    @Override
    public void resume() {
        super.resume();
        //设置视窗大小及位置
        int width=glGraphics.getWidth();
        int height=glGraphics.getHeight();
        GLES20.glViewport(0, 0,width  , height);
        //计算透视投影的比例
          ratio = (float) width / height;

        world.onResume();

    }

    @Override
    public void dispose() {
        super.dispose();
        world.onDispose();
    }



    @Override
    protected void onPresent(float deltaData) {
        //调用此方法计算产生透视投影矩阵
        MatrixState.setFrustumProject(-ratio, ratio, -1, 1, 1, 10000);

        MatrixState.setCamera(
                EYE_X,   //人眼位置的X
                EYE_Y, 	//人眼位置的Y
                EYE_Z,   //人眼位置的Z
                TARGET_X, 	//人眼球看的点X
                TARGET_Y,   //人眼球看的点Y
                TARGET_Z,   //人眼球看的点Z
                0,
                1,
                0);

        world.onPresent(deltaData);




    }

    public void add()
    {

        TriangleIndexVertexArray  array;
    }
}
