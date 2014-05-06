package com.opengles.book.screen;

import android.opengl.GLES20;

import com.opengles.book.MatrixState;
import com.opengles.book.framework.Game;
import com.opengles.book.framework.gl.FPSCounter;
import com.opengles.book.framework.impl.GLScreen;
import com.opengles.book.galaxy.ObjectDrawable;
import com.opengles.book.objects.RectangleObject;

public   class Reflect_BasketBall_Screen extends GLScreen {

	ObjectDrawable basketBall;
	ObjectDrawable floor;
	ObjectDrawable basketBallReflected;
	ObjectDrawable floorTransparent;
	 
 
	FPSCounter counter;
	 



	public Reflect_BasketBall_Screen(Game game) {
		super(game);
		 


		counter = new FPSCounter();

		 floor=new RectangleObject(game.getContext(), "basketball_reflect/mdb.png",8, 8);
	}

	@Override
	public void update(float deltaTime) {

		  glGame.getInput().getTouchEvents();
		 

	}

	@Override
	public void present(float deltaTime) {
	//	counter.logFrame();

		// 清除颜色
		GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT
				| GLES20.GL_COLOR_BUFFER_BIT);
		 MatrixState.pushMatrix();
         MatrixState.translate(0, -2, 0);
		 
		floor.draw();
		  MatrixState.popMatrix();
	}

	@Override
	public void pause() {
		 
		
		floor.unBind();
		//bezierObject.unBind();
		GLES20.glDisable(GLES20.GL_DEPTH_TEST);
		GLES20.glDisable(GLES20.GL_CULL_FACE);

	}

	@Override
	public void resume() {

		GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

		GLES20.glEnable(GLES20.GL_DEPTH_TEST);
//		GLES20.glEnable(GLES20.GL_CULL_FACE);
		int width = glGame.getGLGraphics().getWidth();
		int height = glGame.getGLGraphics().getHeight();
		GLES20.glViewport(0, 0, width, height);

		float ratio = (float) width / height;
		 //调用此方法计算产生透视投影矩阵
        MatrixState.setProject(-ratio, ratio, -1, 1, 1, 100);
        //调用此方法产生摄像机9参数位置矩阵
        MatrixState.setCamera(0.0f,7.0f,7.0f,0,0f,0,0,1,0);


//		LightSources.setSunLightPosition(100, 1, 0);
//		// 设置 三种光线
//		LightSources.setAmbient(0.15f, 0.15f, 0.15f, 1f);
//		LightSources.setDiffuse(0.5f, 0.5f, 0.25f, 1f);
//		LightSources.setSpecLight(0.3f, 0.3f, 0.15f, 1f);
		// 初始化变换矩阵
		MatrixState.setInitStack();
		floor.bind();
	 
	 
	}

	@Override
	public void dispose() {

	}

	 

}
