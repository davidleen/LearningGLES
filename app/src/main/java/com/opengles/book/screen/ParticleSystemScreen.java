package com.opengles.book.screen;

 
import android.opengl.GLES20;
import android.util.Log;
import com.opengles.book.LightSources;
import com.opengles.book.MatrixState;
import com.opengles.book.framework.Game;
import com.opengles.book.framework.Input.TouchEvent;
import com.opengles.book.framework.gl.FPSCounter;
import com.opengles.book.framework.gl.LookAtCamera;
import com.opengles.book.framework.impl.GLScreen;
import com.opengles.book.galaxy.CameraController;
import com.opengles.book.galaxy.ObjectDrawable;
import com.opengles.book.math.Vector3;
import com.opengles.book.objLoader.AABB;
import com.opengles.book.objects.ObjObject;
import com.opengles.book.objects.ParticleSystemObject;
import com.opengles.book.objects.TwistCuboid;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 *  a screen for generate various object by  .obj file
 */
public   class ParticleSystemScreen extends FrameBufferScreen {


	 


   
    private float timeCollapsedForSun = 0;
    int sunAng=0;


    private CameraController cameraController;
    LookAtCamera  camera;

    ObjectDrawable object;

  
	public ParticleSystemScreen(Game game) {
		super(game);
         
		object=new ParticleSystemObject(game.getContext());




	}

//    @Override
//    protected void onPresent() {
//
//    }

    @Override
	public void update(float deltaTime) {
        super.update(deltaTime);
		
		List<TouchEvent> touchEvents = glGame.getInput().getTouchEvents();
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


        object.update(deltaTime);   


	}


    
	@Override
	public void onPresent(float deltaTime) {

        camera.setMatrices();
		// 清除颜色
		GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT
				| GLES20.GL_COLOR_BUFFER_BIT);



		MatrixState.pushMatrix();
		MatrixState.translate(0, 0, 0);
        // 倾斜
       // MatrixState.rotate(15, 0, 0, 1);

       object.draw();





		MatrixState.popMatrix();


	}

	@Override
	public void pause() {
        super.pause();
        object.unBind();
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
        camera.setPosition(0.0f,0f, 10f);
        camera.setUp(0,1,0);
        camera.setLookAt(0f,0,0f) ;
        cameraController=new CameraController(camera, glGame.getGLGraphics());

        object.bind();

	}

	@Override
	public void dispose() {
        super.dispose();

	}




}
