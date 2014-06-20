package com.opengles.book.screen;

import android.graphics.Bitmap;
import android.opengl.GLES20;
import com.opengles.book.LightSources;
import com.opengles.book.MatrixState;
import com.opengles.book.ShaderUtil;
import com.opengles.book.framework.Game;
import com.opengles.book.framework.Input.TouchEvent;
import com.opengles.book.framework.gl.LookAtCamera;
import com.opengles.book.framework.objects.LightTracing;
import com.opengles.book.galaxy.CameraController;
import com.opengles.book.objects.RectangleViewObject;

import java.util.List;

/**
 *  a screen for generate various object by  .obj file
 */
public   class LightTracingScreen extends FrameBufferScreen {







    private CameraController cameraController;
    LookAtCamera  camera;


    RectangleViewObject obj;

    int textureId;

	public LightTracingScreen(Game game) {
		super(game);


        obj=new RectangleViewObject(game.getContext(),20,20);




	}

	@Override
	public void update(float deltaTime) {
        super.update(  deltaTime);

		List<TouchEvent> touchEvents = glGame.getInput().getTouchEvents();
        cameraController.onTouchEvent(touchEvents);


	}



	@Override
	public void onPresent(float deltaTime) {

        MatrixState.pushMatrix();
        camera.setMatrices();
        MatrixState.popMatrix();
		// 清除颜色
		GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT
				| GLES20.GL_COLOR_BUFFER_BIT);



		MatrixState.pushMatrix();
		MatrixState.rotate(90,1,0,0);


        obj.draw(textureId);





		MatrixState.popMatrix();


	}

	@Override
	public void pause() {
        super.pause();
       obj.unBind();
		GLES20.glDisable(GLES20.GL_DEPTH_TEST);
		 GLES20.glDisable(GLES20.GL_CULL_FACE);

	}

	@Override
	public void resume() {
        super.resume();

		GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

		GLES20.glEnable(GLES20.GL_DEPTH_TEST);
		 GLES20.glEnable(GLES20.GL_CULL_FACE);
		int width = glGame.getGLGraphics().getWidth();
		int height = glGame.getGLGraphics().getHeight();
		GLES20.glViewport(0, 0, width, height);
		float ratio = (float) width / height;


		// 设置光线位置
		LightSources.setSunLightPosition(1000, 1, 0);
		// 设置 三种光线
		LightSources.setAmbient(0.15f, 0.15f, 0.15f, 1f);
		LightSources.setDiffuse(0.5f, 0.5f, 0.25f, 1f);
		LightSources.setSpecLight(0.3f, 0.3f, 0.15f, 1f);




        camera=new LookAtCamera(2,1/ratio,1 ,1000);
        camera.setPosition(0.0f,0f, 30f);
        camera.setUp(0, 1, 0);
        camera.setLookAt(0f, 0f, 0f);

        camera.setMatrices();
        cameraController=new CameraController(camera, glGame.getGLGraphics());


        LightTracing tracer=new LightTracing(width,height,camera.getPosition());
        Bitmap bitmap=tracer.trace();
        textureId=  ShaderUtil.loadTextureWithUtils(bitmap,false);
        bitmap.recycle();

//        textureId=ShaderUtil.loadTextureWithUtils(game.getContext(),"sky/sky.png",false);
        obj.bind();

	}

	@Override
	public void dispose() {

        super.dispose();
	}




}
