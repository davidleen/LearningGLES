package com.opengles.book.screen;

import java.util.List;

import android.opengl.GLES20;

import com.opengles.book.MatrixState;
import com.opengles.book.framework.Game;
import com.opengles.book.framework.Input.TouchEvent;
import com.opengles.book.framework.impl.GLScreen;
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
public class GrayMapScreen extends GLScreen{
	
	public static final int UNIT_SIZE=1;
	
	private ObjectDrawable sky;
	private ObjectDrawable mountion;
	private float lastX;
	private float lastY;
	private static int cameraRadias=20;
	private float cameraXDegree=0;
	private float cameraYDegree=MIN_CAMERA_Y_DEGREE;
	private static float MIN_CAMERA_Y_DEGREE=3;
	private static float MAX_CAMERA_Y_DEGREE=15;

	public GrayMapScreen(Game game) {
		super(game);
		 
		 mountion=new Mountion(game.getContext() );
		 sky=new Sky(game.getContext());
		//desert=new AbstractSimpleObject1(game.getContext(), 100, 100 );
	}

	@Override
	public void update(float deltaTime) {
		List<TouchEvent> touchs = glGame.getInput().getTouchEvents();
		 
		
		float offsetX=0;
		float offsetY=0;

		for (TouchEvent touch : touchs)
		{
			switch(touch.type)
			{
			case TouchEvent.TOUCH_DOWN: 
				lastX=touch.x;
				lastY=touch.y; 
				offsetX=0;
				offsetY=0;
				break;
			case TouchEvent.TOUCH_DRAGGED:
				if (touch.type == TouchEvent.TOUCH_DRAGGED)
				{
					// dx = touch.x - mPreviousX;
					offsetX=touch.x-lastX;
					offsetY=touch.y-lastY;
					lastX=touch.x;
					lastY=touch.y;

				}
				
				break;
			}
			

			 
		}
		 
		//设置新的观察目标点XZ坐标
				float cx=(float)( offsetX);//观察目标点x坐标  
//		     cameraYDegree+=offsetY; 
//		     
//		     cameraYDegree=Math.max(cameraYDegree, MIN_CAMERA_Y_DEGREE);
//		     cameraYDegree=Math.min(MAX_CAMERA_Y_DEGREE,cameraYDegree );
		     cameraXDegree-=cx;
		     cameraXDegree%=360; 
		     resetCamera();
		     
	}
	private void resetCamera()
	{
		 double radian=Math.toRadians(cameraXDegree);
		  MatrixState.setCamera((float)(cameraRadias*Math.sin(radian)),cameraYDegree,(float)(cameraRadias*Math.cos(radian)),0,0,0,0,1,0);
		     
	}

	@Override
	public void present(float deltaTime) {
		// 清除颜色
				GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT
						| GLES20.GL_COLOR_BUFFER_BIT);

				//绘制沙漠
 				 MatrixState.pushMatrix();
 		            MatrixState.translate(0, -2, 0);
 		           if(mountion!=null)
 		        	  mountion.draw();
 		      	if(sky!=null)
 		      		sky.draw();
 		           MatrixState.popMatrix();
 		            
	}

	@Override
	public void pause() {
		 
		if(mountion!=null)
			mountion.unBind();
		
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
		 //调用此方法计算产生透视投影矩阵
		MatrixState.setFrustumProject(-ratio, ratio, -1, 1, 1,  1000);

		 //调用此方法产生摄像机9参数位置矩阵

		resetCamera();
//		// ����̫���ƹ�ĳ�ʼλ��
//		LightSources.setSunLightPosition(1000, 1, 0);
//		// 设置 三种光线
//		LightSources.setAmbient(0.15f, 0.15f, 0.15f, 1f);
//		LightSources.setDiffuse(0.5f, 0.5f, 0.25f, 1f);
//		LightSources.setSpecLight(0.3f, 0.3f, 0.15f, 1f);
		// 初始化变换矩阵
		MatrixState.setInitStack();
		 
		if(mountion!=null)
			mountion.bind();
		if(sky!=null)
			sky.bind();
		 
		}
	

	@Override
	public void dispose() {
		 
		
	}
	
}
