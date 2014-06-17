package com.opengles.book.screen;

import android.opengl.GLES20;
import android.util.DisplayMetrics;
import com.opengles.book.LightSources;
import com.opengles.book.MatrixState;

import com.opengles.book.ShaderUtil;
import com.opengles.book.framework.Game;
import com.opengles.book.framework.Input;
import com.opengles.book.framework.gl.LookAtCamera;
import com.opengles.book.framework.impl.GLScreen;
import com.opengles.book.galaxy.CameraController;
import com.opengles.book.glsl.FrameBufferManager;
import com.opengles.book.objects.*;

import java.util.List;

/**
 * FBO 使用范例
 * Created by davidleen29   qq:67320337
 * on 2014-6-11.
 */
public class FrameBufferDemoScreen extends GLScreen {

    private static   int texWidth=512;
    private static   int texHeight=512;



    FrameBufferManager.FrameBuffer frameBuffer;


    TwistCuboid obj;
    private LookAtCamera camera;
    CameraController cameraController;




    @Override
    public void update(float deltaTime) {
        List<Input.TouchEvent> touchEvents = glGame.getInput().getTouchEvents();
        cameraController.onTouchEvent(touchEvents);
    }

    @Override
    public void present(float deltaTime) {
        camera.setMatrices();


       frameBuffer.bind();



//        //render to texture using fbo;
        GLES20.glClearColor(0,0,0,1.0f);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        obj.draw();


        frameBuffer.show();






    }


    @Override
    public void pause() {

        obj.unBind();
         frameBuffer.delete();



    }


    @Override
    public void resume() {




//
        int width = texWidth;
        int height = texHeight                ;
        GLES20.glViewport(0, 0, width, height);
        float ratio = (float) width / height;






   //     textureId=  ShaderUtil.loadTextureWithUtils(game.getContext(), "sky/sky.png", false);

        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
       // GLES20.glEnable(GLES20.GL_CULL_FACE);



        // 设置光线位置
        LightSources.setSunLightPosition(1000, 1, 0);
        // 设置 三种光线
        LightSources.setAmbient(0.15f, 0.15f, 0.15f, 1f);
        LightSources.setDiffuse(0.5f, 0.5f, 0.25f, 1f);
        LightSources.setSpecLight(0.3f, 0.3f, 0.15f, 1f);




        camera=new LookAtCamera(2,1/ratio,1 ,1000);
        camera.setPosition(0.0f,0f, 20f);
        camera.setUp(0, 1, 0);
        camera.setLookAt(0f, 0f, 0f) ;
        cameraController=new CameraController(camera, glGame.getGLGraphics());
        camera.setMatrices();;

        obj.bind();



        // Enable texture mapping
        GLES20.glEnable(GLES20.GL_TEXTURE_2D);

        frameBuffer.create();

    }

    @Override
    public void dispose() {

    }

    public FrameBufferDemoScreen(Game game) {
        super(game);

         DisplayMetrics metrics= game.getContext().getResources().getDisplayMetrics();
        texWidth= metrics.widthPixels;
         texHeight=metrics.heightPixels;
        obj=new TwistCuboid(game.getContext());




        frameBuffer=new FrameBufferManager.FrameBuffer(game.getContext(),texWidth,texHeight);

    }




}
