package com.opengles.book.screen.cubeCollisionDemo;

import android.opengl.GLES20;
import com.bulletphysics.collision.shapes.TriangleIndexVertexArray;
import com.opengles.book.MatrixState;
import com.opengles.book.framework.Game;
import com.opengles.book.framework.Input;
import com.opengles.book.framework.gl.Camera3D;
import com.opengles.book.framework.gl.ProjectInfo;
import com.opengles.book.framework.impl.GLGame;
import com.opengles.book.framework.impl.GLScreen;
import com.opengles.book.math.Vector3;
import com.opengles.book.objects.IntersectUtil;
import com.opengles.book.screen.FrameBufferScreen;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL10Ext;
import javax.vecmath.Tuple3f;
import javax.vecmath.Vector3f;
import java.util.List;
import java.util.Random;

/**
 * 箱子碰撞模拟demo 界面
 * Created by davidleen29   qq:67320337
 * on 2014-6-25.
 */
public class CubeCollisionDemoScreen extends FrameBufferScreen {

    World world;

    final static float EYE_X=0;//观察者的位置x
    final static float EYE_Y=10 ;//观察者的位置y
    final static float EYE_Z=30;//观察者的位置z

    final static float TARGET_X=0;//目标的位置x
    final static float TARGET_Y=10;//目的位置Y
    final static float TARGET_Z=-30;//目标的位置Z
    final static float NEAR=1;
    final static float FAR=10000;

    ProjectInfo projectInfo ;
    Camera3D camera;


    float ratio;

    public CubeCollisionDemoScreen(Game game ) {
        super(game);
         world=new World(game.getContext());


    }
    Random random=new Random();
    public float timeForGenerateObject=0;
    public static final  float TARGET_GENERATE_RATING=3f;
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        List<Input.TouchEvent> touchEvents = glGame.getInput().getTouchEvents();

        for(Input.TouchEvent touchEvent:touchEvents)
        {
             if(touchEvent.type== Input.TouchEvent.TOUCH_DOWN)
             {


                 Vector3f newPosition=new Vector3f();
                 Vector3f direction=new Vector3f();
                 simulateTouch(touchEvent.x,touchEvent.y,newPosition,direction);
                 world.addBullet(newPosition, direction);


             }
        }


        //定时往屏幕丢掷物体。


        timeForGenerateObject+=deltaTime;
        if(timeForGenerateObject>TARGET_GENERATE_RATING)
        {
            timeForGenerateObject-=TARGET_GENERATE_RATING;

            //随机生成抛掷点
            float x=random.nextInt(glGraphics.getWidth());
            float y=random.nextInt(glGraphics.getHeight()/4);   //抛掷点上半屏幕

            Vector3f newPosition=new Vector3f();
            Vector3f direction=new Vector3f();
            simulateTouch(x,y,newPosition,direction);
        world.add(newPosition, direction);
        }




            world.onUpdate(deltaTime);
    }


    /**
     * 在位置 xy 处 执行点击
     * @param x
     * @param y
     */
    private void simulateTouch(float x, float y,Vector3f newPosition,Vector3f direction)
    {
        float[][]  positions=    IntersectUtil.calculateNearFarPosition(x,y,glGraphics.getWidth(),glGraphics.getHeight(),projectInfo);


        newPosition.set(positions[0][0],positions[0][1],positions[0][2]);

        direction.set(positions[1][0]-positions[0][0],positions[1][1]-positions[0][1],positions[1][2]-positions[0][2]);

        direction.normalize();


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
        projectInfo=new ProjectInfo(-ratio, ratio, -1, 1, NEAR, FAR);
        camera=new Camera3D( EYE_X,   //人眼位置的X
                EYE_Y, 	//人眼位置的Y
                EYE_Z,   //人眼位置的Z
                TARGET_X, 	//人眼球看的点X
                TARGET_Y,   //人眼球看的点Y
                TARGET_Z,   //人眼球看的点Z
                0,
                1,
                0);

        world.onResume();

    }

    @Override
    public void dispose() {
        super.dispose();
        world.onDispose();
    }



    @Override
    public void onPresent(float deltaData) {

        //调用此方法计算产生透视投影矩阵
        projectInfo.setFrustum();
        camera.setCamera();
        world.onPresent(deltaData);




    }


}
