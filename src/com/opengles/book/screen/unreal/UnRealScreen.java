package com.opengles.book.screen.unreal;

import android.content.Context;
import android.opengl.GLES20;
import com.opengles.book.LightSources;
import com.opengles.book.MatrixState;
import com.opengles.book.framework.Game;
import com.opengles.book.framework.Input.TouchEvent;
import com.opengles.book.framework.gl.FPSCounter;
import com.opengles.book.framework.impl.GLScreen;
import com.opengles.book.galaxy.ObjObject;
import com.opengles.book.galaxy.ObjectDrawable;

import java.util.List;

/**
 * 产生香蕉的界面
 */
public   class UnRealScreen extends GLScreen {


	ObjectDrawable center;

	FPSCounter counter;
	private float timeCollapsed = 0;;





	private float mPreviousX;//
    private float mPreviousY;

	float yAngle;
    float xAngle;
    int sunAng=0;

	public UnRealScreen(Game game) {
		super(game);

		center = getCenterDrawable(game.getContext());


		counter = new FPSCounter();
	}

	@Override
	public void update(float deltaTime) {

		List<TouchEvent> touchs = glGame.getInput().getTouchEvents();
		// float dx;

		for (TouchEvent touch : touchs)
		{
			if (touch.type == TouchEvent.TOUCH_DRAGGED)
			{
				// dx = touch.x - mPreviousX;

				yAngle += (touch.x - mPreviousX);
                xAngle += (touch.y - mPreviousY);
			}

			mPreviousX = touch.x;
            mPreviousY=touch.y;
		}


        timeCollapsed += deltaTime;
        if (timeCollapsed >= 0.1f)
        {
            timeCollapsed -= 0.1f;


            sunAng +=3;
            if (sunAng > 360)
                sunAng -= 360;
            double radias = Math.toRadians(sunAng);
            LightSources.setSunLightPosition(
                    (int) (1000 * Math.cos(radias)), 1,
                    (int) (1000 * Math.sin(radias)));
        }

	}

	@Override
	public void present(float deltaTime) {
	//	counter.logFrame();

		// 清除颜色
		GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT
				| GLES20.GL_COLOR_BUFFER_BIT);

		MatrixState.pushMatrix();
		MatrixState.translate(0, 0, 0);
        // 倾斜
       // MatrixState.rotate(15, 0, 0, 1);
		MatrixState.rotate(xAngle, 1, 0, 0);
		MatrixState.rotate(yAngle, 0, 1, 0);




		center.draw();





		MatrixState.popMatrix();


	}

	@Override
	public void pause() {

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
		 
		MatrixState.setFrustumProject(-ratio, ratio, -1, 1, 3,  110);

	 

		MatrixState.setCamera(0, 0, 40f, 0f, 0f, 1f, 0f, 1.0f, 40f);
		 
		LightSources.setSunLightPosition(1000, 1, 0);
		// 设置 三种光线
		LightSources.setAmbient(0.15f, 0.15f, 0.15f, 1f);
		LightSources.setDiffuse(0.5f, 0.5f, 0.25f, 1f);
		LightSources.setSpecLight(0.3f, 0.3f, 0.15f, 1f);
		// 初始化变换矩阵
		MatrixState.setInitStack();


		center.bind();


	}

	@Override
	public void dispose() {

	}

	protected   ObjectDrawable getCenterDrawable(Context context)
    {

        return  new UnRealObject(context );
        
       //return  new ObjObject(context,"","banana.obj");
    }

}
