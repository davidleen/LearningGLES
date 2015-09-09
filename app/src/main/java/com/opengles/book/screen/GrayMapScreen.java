package com.opengles.book.screen;

import java.util.List;

import android.opengl.GLES20;

import com.giants3.android.openglesframework.framework.Game;
import com.giants3.android.openglesframework.framework.Input;
import com.giants3.android.openglesframework.framework.MatrixState;

import com.giants3.android.openglesframework.framework.gl.LookAtCamera;
import com.giants3.android.openglesframework.framework.impl.GLScreen;
import com.opengles.book.galaxy.CameraController;
import com.opengles.book.galaxy.ObjectDrawable;
 
import com.opengles.book.objects.Mountion;
import com.opengles.book.objects.Sky;

/**
 * 
 * @author davidleen29 
 * @create : 2014-4-22 下午11:25:49
 * @{ 灰色纹理地图案例
 * }
 */
public class GrayMapScreen extends GLScreen {
	
	public static final int UNIT_SIZE=1;
	
	private ObjectDrawable sky;
	private ObjectDrawable mountain;

	private static int cameraRadio =20;


    private LookAtCamera camera;
    private CameraController cameraController;

	public GrayMapScreen(Game game) {
		super(game);
		 
		 mountain =new Mountion(game.getContext() );
		 sky=new Sky(game.getContext());
		//desert=new AbstractSimpleObject1(game.getContext(), 100, 100 );
	}

	@Override
	public void update(float deltaTime) {
		List<Input.TouchEvent> touchEvents = glGame.getInput().getTouchEvents();
		 
		cameraController.onTouchEvent(touchEvents);


		     
	}


	@Override
	public void present(float deltaTime) {


		// 清除颜色
				GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT
						| GLES20.GL_COLOR_BUFFER_BIT);

                camera.setMatrices();
				//绘制沙漠
 				 MatrixState.pushMatrix();
 		            MatrixState.translate(0, -2, 0);
 		           if(mountain !=null)
 		        	  mountain.draw();
 		      	if(sky!=null)
 		      		sky.draw();
 		           MatrixState.popMatrix();
 		            
	}

	@Override
	public void pause() {
		 
		if(mountain !=null)
			mountain.unBind();
		
		if(sky!=null)
			sky.unBind();
	}

	@Override
	public void resume() {
		GLES20.glClearColor(1.0f, 1.0f, 1.0f, 0.5f);

		GLES20.glEnable(GLES20.GL_DEPTH_TEST);
		 
		int width = glGame.getGLGraphics().getWidth();
		int height = glGame.getGLGraphics().getHeight();
		GLES20.glViewport(0, 0, width, height);
		//计算GLSurfaceView的宽高比
		float ratio = (float) width / height;

        camera=new LookAtCamera(2, 1/ratio,1, 1000);
        camera.setPosition(0.0f,0f, cameraRadio);
        camera.setUp(0,1,0);
        camera.setLookAt(0f,0,0f) ;
        cameraController=new CameraController(camera, glGame.getGLGraphics());


		// 初始化变换矩阵
		MatrixState.setInitStack();
		 
		if(mountain !=null)
			mountain.bind();
		if(sky!=null)
			sky.bind();
		 
		}
	

	@Override
	public void dispose() {
		 
		
	}
	
}
