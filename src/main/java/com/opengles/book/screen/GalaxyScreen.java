package com.opengles.book.screen;

import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.opengl.GLES20;

import com.opengles.book.LightSources;
import com.opengles.book.MatrixState;
import com.opengles.book.framework.Game;
import com.opengles.book.framework.Input.TouchEvent;
import com.opengles.book.framework.gl.FPSCounter;
import com.opengles.book.framework.gl.LookAtCamera;
import com.opengles.book.framework.impl.GLScreen;
import com.opengles.book.galaxy.CameraController;
import com.opengles.book.galaxy.Celestial;
import com.opengles.book.galaxy.CloudWithVbo;
import com.opengles.book.galaxy.EarthWithVbo;
import com.opengles.book.galaxy.MoonWithVbo;
import com.opengles.book.galaxy.ObjectDrawable;

public   class GalaxyScreen extends GLScreen {

	ObjectDrawable moon;
	ObjectDrawable center;
	ObjectDrawable celestial;
	ObjectDrawable bigCelestial;
	ObjectDrawable cloud;
//	ObjectDrawable bezierObject;;
	FPSCounter counter;
	private float timeCollapsed = 0;;
	private int moonSelfRotateDegress = 0;

	private int sunAng = 0;
	private int moonRotateDegress = 0;

	 
	
	private LookAtCamera camera;
	private CameraController cameraController;

	 float yAngle=10;

	public GalaxyScreen(Game game) {
		super(game);
		moon = new MoonWithVbo(game.getContext());
		center = getCenterDrawable(game.getContext());
		celestial = new Celestial(game.getContext());
		cloud=new CloudWithVbo(game.getContext());
		bigCelestial = new Celestial(game.getContext(), 400, 3);

	//	bezierObject = new BezierObject(game.getContext());
		counter = new FPSCounter();
	}

	@Override
	public void update(float deltaTime) {

		List<TouchEvent> touchs = glGame.getInput().getTouchEvents();

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
		celestial.draw();
		bigCelestial.draw();
		MatrixState.pushMatrix();
		MatrixState.translate(0, 0, 0);

		
		// 倾斜
		MatrixState.rotate(-15, 0, 0, 1);

		MatrixState.rotate(yAngle, 0, 1, 0);

		MatrixState.pushMatrix();

		MatrixState.rotate(moonRotateDegress, 0, 1, 0);
		//绘制地球
		center.draw();
		
		
		   //开启混合
        GLES20.glEnable(GLES20.GL_BLEND);  
        //设置混合因子
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		
		cloud.draw();

		  //关闭混合
        GLES20.glDisable(GLES20.GL_BLEND);

		MatrixState.popMatrix();

		// MatrixState.transtate(3,0, 0);
		// earth1.bind();
		// earth1.drawSelf();

		// 绘制月球
		MatrixState.pushMatrix();
		MatrixState.rotate(moonRotateDegress, 0, 1, 0); // 公转

		MatrixState.translate(10, 0, 0);

		MatrixState.rotate(moonSelfRotateDegress, 0, 1, 0); // 自转

		moon.draw();

		MatrixState.popMatrix();

		MatrixState.popMatrix();

		MatrixState.pushMatrix();
		MatrixState.translate(10, 0, 0);
	//	bezierObject.draw();
		MatrixState.popMatrix();

	}

	@Override
	public void pause() {
		moon.unBind();
		cloud.unBind();
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


		moon.bind();
		center.bind();
		celestial.bind();
		bigCelestial.bind();
		cloud.bind();
	 
	}

	@Override
	public void dispose() {

	}

	protected   ObjectDrawable getCenterDrawable(Context context)
	{
		return new EarthWithVbo(context);
	}

}
