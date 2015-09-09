package com.opengles.book.screen;

import java.util.List;

import android.opengl.GLES20;

import com.bulletphysics.dynamics.DiscreteDynamicsWorld;
import com.giants3.android.openglesframework.framework.Game;
import com.giants3.android.openglesframework.framework.Input;
import com.giants3.android.openglesframework.framework.gl.FPSCounter;
import com.giants3.android.openglesframework.framework.gl.LookAtCamera;
import com.giants3.android.openglesframework.framework.impl.GLScreen;
import com.opengles.book.LightSources;

import com.opengles.book.galaxy.CameraController;

/**
 * 沙滩场景  有椰子树 摇摆 海水浮动,天空
 * @author davidleen29
 *
 */
public   class BeachScreen extends GLScreen {


    DiscreteDynamicsWorld world;
	FPSCounter counter;
	
	
	private float timeCollapsed = 0;;
	private int moonSelfRotateDegress = 0;

	private int sunAng = 0;
	private int moonRotateDegress = 0;

	 
	
	private LookAtCamera camera;
	private CameraController cameraController;

	 float yAngle=10;

	public BeachScreen(Game game) {
		super(game);
		 

	 
		counter = new FPSCounter();
	}

	@Override
	public void update(float deltaTime) {

		List<Input.TouchEvent> touchs = glGame.getInput().getTouchEvents();

			cameraController.onTouchEvent(touchs);

	 

		timeCollapsed += deltaTime;
		if (timeCollapsed >= 0.1f)
		{
			timeCollapsed -= 0.1f;

			moonSelfRotateDegress += 3;
			if (moonSelfRotateDegress > 360)
				moonSelfRotateDegress -= 360;

			moonRotateDegress += 1;
			if (moonRotateDegress > 360)
				moonRotateDegress -= 360;
			sunAng += 1;
			if (sunAng > 360)
				sunAng -= 360;
			double radias = Math.toRadians(sunAng);
			LightSources.setSunLightPosition(
					(int) (100 * Math.cos(radias)), 1,
					(int) (100 * Math.sin(radias)));

			// MatrixState.setCamera((float) (40 * Math.cos(radias)), 0,
			// (float) (40 * Math.sin(radias)), 0f, 0f, -0f,
			// (float) (40 * Math.cos(radias)), 1.0f,
			// (float) (40 * Math.sin(radias)));

		}

	}

	@Override
	public void present(float deltaTime) {
	//	counter.logFrame();

		
		camera.setMatrices();
		// 清除颜色
		GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT
				| GLES20.GL_COLOR_BUFFER_BIT);
		 
		 

	}

	@Override
	public void pause() {
		 
		//bezierObject.unBind();
		GLES20.glDisable(GLES20.GL_DEPTH_TEST);
		GLES20.glDisable(GLES20.GL_CULL_FACE);

	}

	@Override
	public void resume() {

		GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

		GLES20.glEnable(GLES20.GL_DEPTH_TEST);
		GLES20.glEnable(GLES20.GL_CULL_FACE);
		int width = glGame.getGLGraphics().getWidth();
		int height = glGame.getGLGraphics().getHeight();
		GLES20.glViewport(0, 0, width, height);
		 
		float ratio = (float) width / height;
		
		camera=new LookAtCamera(2,1/ratio,4,300);
		 camera.setPosition(0.0f,0.0f,40f);
		  camera.setUp(0,1,0);
		  camera.setLookAt(0f,0,0f) ;
		cameraController=new CameraController(camera, glGame.getGLGraphics());
		
		 
		LightSources.setSunLightPosition(100, 1, 0);
		// 设置 三种光线
		LightSources.setAmbient(0.15f, 0.15f, 0.15f, 1f);
		LightSources.setDiffuse(0.5f, 0.5f, 0.25f, 1f);
		LightSources.setSpecLight(0.3f, 0.3f, 0.15f, 1f);


	 
	 
	}

	@Override
	public void dispose() {

	}

	 

}
