package com.opengles.book.screen;

import android.graphics.Bitmap;
import android.opengl.GLES20;
import com.opengles.book.LightSources;
import com.opengles.book.MatrixState;
import com.opengles.book.ShaderUtil;
import com.opengles.book.framework.Game;
import com.opengles.book.framework.Input.TouchEvent;
import com.opengles.book.framework.gl.LookAtCamera;
import com.opengles.book.framework.impl.GLScreen;
import com.opengles.book.framework.objects.LightTracing;
import com.opengles.book.galaxy.CameraController;
import com.opengles.book.glsl.FrameBufferManager;
import com.opengles.book.objects.RectangleViewObject;

import java.util.List;

/**
 *  a screen for generate various object by  .obj file
 */
public   class LightTracingScreen extends FrameBufferScreen {




    int width;int height;
    float ratio ;

//    private CameraController cameraController;
//    LookAtCamera  camera;


    RectangleViewObject obj;

    int textureId;

	public LightTracingScreen(Game game) {
		super(game);
        width = glGame.getGLGraphics().getWidth();
        height = glGame.getGLGraphics().getHeight();

        ratio=(float)height/width;

        obj=new RectangleViewObject(game.getContext(),width,height);




	}

	@Override
	public void update(float deltaTime) {
        super.update(  deltaTime);

		List<TouchEvent> touchEvents = glGame.getInput().getTouchEvents();
    //    cameraController.onTouchEvent(touchEvents);


	}



	@Override
	public void onPresent(float deltaTime) {
        // 清除颜色
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT
                | GLES20.GL_COLOR_BUFFER_BIT);
//        MatrixState.pushMatrix();
//         camera.setMatrices();
//       MatrixState.popMatrix();



        GLES20.glViewport(0, 0, width, height);

        MatrixState.setOrthoProject(-width/2, width/2, -height/2, height/2, 1, 1000);


        //需要调整镜头
        MatrixState.setCamera(0, 0, 10, 0f, 0f, 0f, 0f, 1f, 0.0f);
        MatrixState.setInitStack();

        MatrixState.pushMatrix();
       // MatrixState.rotate(30,1,0,0);
        obj.draw(textureId);
		MatrixState.popMatrix();


	}

	@Override
	public void pause() {
         super.pause();
       obj.unBind();
		GLES20.glDisable(GLES20.GL_DEPTH_TEST);
		// GLES20.glDisable(GLES20.GL_CULL_FACE);

	}

	@Override
	public void resume() {
       super.resume();

		GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

		GLES20.glEnable(GLES20.GL_DEPTH_TEST);

		// GLES20.glEnable(GLES20.GL_CULL_FACE);










		// 设置光线位置
		LightSources.setSunLightPosition(1000, 1, 0);
		// 设置 三种光线
		LightSources.setAmbient(0.15f, 0.15f, 0.15f, 1f);
		LightSources.setDiffuse(0.5f, 0.5f, 0.25f, 1f);
		LightSources.setSpecLight(0.3f, 0.3f, 0.15f, 1f);



        LookAtCamera camera;
        camera=new LookAtCamera(2, ratio,500 ,50000);
        camera.setPosition(0.0f,0f, 1000f);
        camera.setUp(0, 1, 0);
        camera.setLookAt(0f, 0f, 0f);

//        width=400;
//        height=400;
//        cameraController=new CameraController(camera, glGame.getGLGraphics());
        int bitmapWidth=400;

        LightTracing tracer=new LightTracing(bitmapWidth,(int)(bitmapWidth*ratio), camera );
        Bitmap bitmap=tracer.trace();
        textureId=  ShaderUtil.loadTextureWithUtils(bitmap,false);
        bitmap.recycle();

       // textureId=ShaderUtil.loadTextureWithUtils(game.getContext(),"sky/sky.png",false);
        obj.bind();



	}

	@Override
	public void dispose() {

       // super.dispose();
	}




}
