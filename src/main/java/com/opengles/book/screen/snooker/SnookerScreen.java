package com.opengles.book.screen.snooker;

import android.opengl.GLES20;
import com.opengles.book.framework.Game;
import com.opengles.book.framework.MVP;
import com.opengles.book.framework.gl.Camera3D;
import com.opengles.book.framework.gl.ProjectInfo;
import com.opengles.book.framework.gl.ViewPort;
import com.opengles.book.screen.FrameBufferScreen;

/**
 * 桌球游戏界面
 * Created by davidleen29 on 2014/7/8.
 */
public class SnookerScreen  extends FrameBufferScreen{
    private static final float MAX_AABB_LENGTH = 100;
    final static float EYE_X=0;//观察者的位置x
    final static float EYE_Y=  5 ;//观察者的位置y
    final static float EYE_Z= 3;//观察者的位置z

    final static float TARGET_X=0;//目标的位置x
    final static float TARGET_Y=0;//目的位置Y
    final static float TARGET_Z=0;//目标的位置Z
    final static float NEAR=1;
    final static float FAR=100;
    private ProjectInfo projectInfo;
    private Camera3D camera;
    private ViewPort viewPort;

   // private MVP mvp;
    public SnookerScreen(Game game) {
        super(game);

        //设置视窗大小及位置
        int width=glGraphics.getWidth();
        int height=glGraphics.getHeight();
        viewPort=new ViewPort(0,0,width,height);

        //计算透视投影的比例
        float  ratio = (float) width / height;
        projectInfo=new ProjectInfo(-ratio, ratio, -1, 1, NEAR, FAR);
        camera=new Camera3D( EYE_X,   //人眼位置的X
                EYE_Y, 	//人眼位置的Y
                EYE_Z,   //人眼位置的Z
                TARGET_X, 	//人眼球看的点X
                TARGET_Y,   //人眼球看的点Y
                TARGET_Z,   //人眼球看的点Z
                0,
                20,
                0);



    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
    }

    @Override
    public void pause() {
        super.pause();
    }

    @Override
    public void resume() {
        super.resume();

        viewPort.apply();
        projectInfo.setFrustum();
        camera.setCamera();


    }

    @Override
    public void dispose() {
        super.dispose();
    }



    @Override
    protected void onPresent(float deltaData) {

    }
}
