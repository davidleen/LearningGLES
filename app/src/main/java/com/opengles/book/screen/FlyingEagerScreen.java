package com.opengles.book.screen;

import android.opengl.GLES20;

import com.giants3.android.openglesframework.framework.Game;
import com.giants3.android.openglesframework.framework.Input;
import com.giants3.android.openglesframework.framework.gl.LookAtCamera;
import com.opengles.book.LightSources;
import com.giants3.android.openglesframework.framework.MatrixState;

import com.opengles.book.galaxy.CameraController;
import com.opengles.book.objects.FlyingEager;

import java.util.List;

/**
 *  a screen for generate various object by  .obj file
 */
public   class FlyingEagerScreen extends FrameBufferScreen {




    private FlyingEager eager;






    private float timeCollapsedForSun = 0;
    int sunAng=0;

    private CameraController cameraController;
    LookAtCamera camera;




	public FlyingEagerScreen(Game game) {
		super(game);


       eager=new FlyingEager(game.getContext());




	}

	@Override
	public void update(float deltaTime) {
        super.update(deltaTime);

		List<Input.TouchEvent> touchEvents = glGame.getInput().getTouchEvents();
        cameraController.onTouchEvent(touchEvents);
        timeCollapsedForSun += deltaTime;
        if (timeCollapsedForSun >= 0.1f)
        {
            timeCollapsedForSun -= 0.1f;
            sunAng +=3;
            if (sunAng > 360)
                sunAng -= 360;
            double radias = Math.toRadians(sunAng);
            LightSources.setSunLightPosition(
                    (int) (1000 * Math.cos(radias)), 1,
                    (int) (1000 * Math.sin(radias)));
        }


        eager.update(deltaTime);


	}



	@Override
	public void onPresent(float deltaTime) {
        // 清除颜色
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT
                | GLES20.GL_COLOR_BUFFER_BIT);

        camera.setMatrices();




		MatrixState.pushMatrix();
		MatrixState.translate(0, 0, 0);


        eager.draw();



		MatrixState.popMatrix();


	}

	@Override
	public void pause() {
        super.pause();
        eager.unBind();
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
        camera.setPosition(0.0f,1,50 );
        camera.setUp(0,1,0);
        camera.setLookAt(0, 0, 0) ;
        cameraController=new CameraController(camera, glGame.getGLGraphics());
        camera.setMatrices();

        eager.bind();

	}

	@Override
	public void dispose() {
        super.dispose();

	}




}
