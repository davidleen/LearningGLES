package com.opengles.book.screen;

import android.opengl.GLES20;

import com.opengles.book.LightSources;
import com.opengles.book.MatrixState;

import com.opengles.book.framework.Game;
import com.opengles.book.framework.Input.TouchEvent;
import com.opengles.book.framework.gl.CubeTexture;
import com.opengles.book.framework.gl.LookAtCamera;
import com.opengles.book.framework.gl.Texture;
import com.opengles.book.framework.impl.GLScreen;
import com.opengles.book.galaxy.CameraController;
import com.opengles.book.galaxy.ObjectDrawable;
import com.opengles.book.objLoader.ObjModel;
import com.opengles.book.objLoader.ObjectParser;
import com.opengles.book.objects.CrystalBallObject;
import com.opengles.book.objects.ObjObject;
import com.opengles.book.objects.RectangleObject;
import com.opengles.book.objects.Sphere;
import com.opengles.book.screen.treeOnDesert.TreeGroup;

import java.io.File;
import java.util.Collections;
import java.util.List;

/**
 *  水晶球 展示  透视 反射 效果集合
  * @author davidleen29
 * @create : 2014-4-22 下午11:25:49
 *
 */
public class CrystalBallScreen extends GLScreen{

	public static final int UNIT_SIZE=1;



	 private float timeCollapsedForSun = 0;
	    int sunAng=0;

	private static int cameraRadias=70;

	float ratio;

    private LookAtCamera camera;
    private CameraController cameraController;

     private CrystalBallObject  object;


     
      


	public CrystalBallScreen(Game game) {
		super(game);

      object=new CrystalBallObject(game.getContext());
	}

	@Override
	public void update(float deltaTime) {
		List<TouchEvent> touchEvents = glGame.getInput().getTouchEvents();
		 

        cameraController.onTouchEvent(touchEvents);

        timeCollapsedForSun += deltaTime;
        if (timeCollapsedForSun >= 0.1f)
        {
            timeCollapsedForSun -= 0.1f;
            sunAng +=3 ;
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
		// 清除颜色
				GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT
						| GLES20.GL_COLOR_BUFFER_BIT);
				


                //设置观察点。
                camera.setMatrices();
              object.draw();


    }

	@Override
	public void pause() {
		 
        object.unBind();;
	}

	@Override
	public void resume() {
		GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.5f);

		GLES20.glEnable(GLES20.GL_DEPTH_TEST);
		 
		int width = glGame.getGLGraphics().getWidth();
		int height = glGame.getGLGraphics().getHeight();
		GLES20.glViewport(0, 0, width, height);
		//计算GLSurfaceView的宽高比
		 ratio = (float) width / height;
		setCamera();




        object.bind();
    }
	

	@Override
	public void dispose() {
		 
		object.dispose();
	}


    private void setCamera()
    {
        camera=new LookAtCamera(2, 1/ratio,1, 100);
        cameraController=new CameraController( glGame.getGLGraphics());
        cameraController.setListener(new CameraController.CameraUpdateListener() {
            @Override
            public void onCameraUpdate(LookAtCamera camera) {

            }
        });
        camera.setPosition(0.0f,0,cameraRadias);
        camera.setUp(0,1,0);
        camera.setLookAt(0f,0,0f) ;
        cameraController.setCamera(camera);

    }


}
