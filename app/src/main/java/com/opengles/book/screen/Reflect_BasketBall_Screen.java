package com.opengles.book.screen;

import java.util.List;

import android.opengl.GLES20;

import com.giants3.android.openglesframework.framework.Game;
import com.giants3.android.openglesframework.framework.Input;
import com.giants3.android.openglesframework.framework.MatrixState;

import com.giants3.android.openglesframework.framework.gl.FPSCounter;
import com.giants3.android.openglesframework.framework.gl.LookAtCamera;
import com.giants3.android.openglesframework.framework.impl.GLScreen;
import com.opengles.book.galaxy.CameraController;
import com.opengles.book.galaxy.ObjectDrawable;
import com.opengles.book.objects.FlutterFlag;
import com.opengles.book.objects.PoetPanel;
import com.opengles.book.objects.RectangleObject;
import com.opengles.book.objects.SphereObject;

public   class Reflect_BasketBall_Screen extends GLScreen {

	ObjectDrawable basketBall;
	ObjectDrawable floor;
	ObjectDrawable basketBallReflected;
	ObjectDrawable floorTransparent;
	BallController controller;
	
	FlutterFlag flag;
	
	LookAtCamera camera;
	
	CameraController cameraController; 
	
	//诗词背景绘画
	PoetPanel poets;
	FPSCounter counter;
	 
	float  ratio;


	public Reflect_BasketBall_Screen(Game game) {
		super(game);
		 
			

		counter = new FPSCounter();
		 
		 floor=new RectangleObject(game.getContext(), "basketball_reflect/mdb.png",12, 12);
		 basketBall=new SphereObject(game.getContext(), "basketball_reflect/basketball.png", 2)	;
		 floorTransparent=new RectangleObject(game.getContext(), "basketball_reflect/mdbtm.png",12, 12);
		 poets= new PoetPanel(game.getContext(), 5, 5);
		 controller=new BallController(10);
		 
		 flag=new FlutterFlag(game.getContext());
		
	}
	
    private float timeCollopased;
	@Override
	public void update(float deltaTime) {

		List<Input.TouchEvent> events=  glGame.getInput().getTouchEvents();
		  
		cameraController.onTouchEvent(events);
		for (Input.TouchEvent touch : events)
		{
			switch(touch.type)
			{
			case Input.TouchEvent.TOUCH_DOWN:
				controller.addVy(1);
				break;
			 
				 
			}
			

			 
		}
		
		  
		  controller.update(deltaTime);
		  poets.onUpdate(deltaTime);
		  
		  timeCollopased+=deltaTime;
		  if(timeCollopased>0.02)
		  {
			  flag.currStartAngle+=(float) (Math.PI/16);
			  timeCollopased-=0.02;
		  }

	}

	@Override
	public void present(float deltaTime) {
	//	counter.logFrame();
		
		//调用此方法计算产生透视投影矩阵与相机
		camera.setMatrices();
		
//		  
//        MatrixState.setFrustumProject(-ratio, ratio, -1, 1, 1, 100);
//         
//        MatrixState.setCamera(0.0f,7.0f,7.0f,0,0f,0,0,1,0);
		//设置屏幕背景色RGBA
        GLES20.glClearColor(0f,0f,0f,1.0f);  
		// 清除颜色
		GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT
				| GLES20.GL_COLOR_BUFFER_BIT);
		 
		
		
		
		float y=controller.getY(); 
		 MatrixState.pushMatrix();
         MatrixState.translate(0, -2, 0); 
         
        
		  //添加模板测试  镜像体在反射面板外 将被丢弃。
         //warn   no work on some type gpu  need to confirm/
		  GLES20.glClear(GLES20.GL_STENCIL_BUFFER_BIT);
		  GLES20.glEnable(GLES20.GL_STENCIL_TEST);
		  //设置模板测试参数
		  GLES20.glStencilFunc(GLES20.GL_ALWAYS, 1, 1);
		  //设置模板测试后的操作
		  GLES20.glStencilOp(GLES20.GL_KEEP, GLES20.GL_KEEP, GLES20.GL_REPLACE);
		  
		  //绘制反射面地板
		  floor.draw();
		 
		  //设置模板测试参数
		  GLES20.glStencilFunc(GLES20.GL_EQUAL, 1, 1);
		  //设置模板测试后的操作
		  GLES20.glStencilOp(GLES20.GL_KEEP, GLES20.GL_KEEP, GLES20.GL_KEEP);
		  
		//绘制镜像体
		 MatrixState.pushMatrix();
		 MatrixState.translate(0, -y, 0); 
		 basketBall.draw();
		 MatrixState.popMatrix();
		 
		 //关闭模板测试。
		 GLES20.glDisable(GLES20.GL_STENCIL_TEST);
		 //绘制半透明地板
		//开启混合
         GLES20.glEnable(GLES20.GL_BLEND);
         //设置混合因子
         GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
         floorTransparent.draw();
         GLES20.glDisable(GLES20.GL_BLEND);
		
         MatrixState.pushMatrix();
		 MatrixState.translate(0,  y, 0); 
		  basketBall.draw();
		 MatrixState.popMatrix();
		
		 
		 

         MatrixState.pushMatrix();
         MatrixState.translate(-12,  0, -5); 
		 MatrixState.rotate(60, 1, 0, 0) ; 
		 
		 
 		 GLES20.glEnable(GLES20.GL_BLEND); 
 		//设置混合因子
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        poets.draw();
 		 GLES20.glDisable(GLES20.GL_BLEND);
		 MatrixState.popMatrix();



        flag.draw();

		 
		 //下述代码 为此视角场景服务
		 GLES20.glEnable(GLES20.GL_SCISSOR_TEST);

		//设置区域
     	 GLES20.glScissor(1280-200,480-200,200,200);
     	//设置屏幕背景色RGBA
         GLES20.glClearColor(1f,1f,1f,1.0f);
         //清除颜色缓存于深度缓存
         GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
		 float scissorRatio=1;

         //调用此方法计算产生透视投影矩阵
         MatrixState.setFrustumProject(-ratio, ratio, -1, 1, 3, 100);
         //调用此方法产生摄像机9参数位置矩阵
         MatrixState.setCamera(0  ,0f,10.0f,0,y,0,0,1,0);

         MatrixState.pushMatrix();
		  MatrixState.translate(6,  y, 0);
		  basketBall.draw();
		 MatrixState.popMatrix();


		 GLES20.glDisable(GLES20.GL_SCISSOR_TEST);
		
		 MatrixState.popMatrix();
		

	}

	@Override
	public void pause() {
		 
		
		floor.unBind();
		basketBall.unBind();
		floorTransparent.unBind();
		poets.unBind();
		flag.unBind();
		//bezierObject.unBind();
		GLES20.glDisable(GLES20.GL_DEPTH_TEST);
//		GLES20.glDisable(GLES20.GL_CULL_FACE);

	}

	@Override
	public void resume() {

		GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

		GLES20.glDisable(GLES20.GL_DEPTH_TEST);
 
		int width = glGame.getGLGraphics().getWidth();
		int height = glGame.getGLGraphics().getHeight();
		GLES20.glViewport(0, 0, width, height);

		  ratio = (float) width / height;
		  camera=new LookAtCamera(2, 1/ratio,1, 100);
		  camera.setPosition(0.0f,10.0f,10f);
		  camera.setUp(0,1,0);
		  camera.setLookAt(0f,1,0f) ;
		  cameraController=new CameraController(camera, glGame.getGLGraphics());
		  


//		LightSources.setSunLightPosition(100, 1, 0);
//		// 设置 三种光线
//		LightSources.setAmbient(0.15f, 0.15f, 0.15f, 1f);
//		LightSources.setDiffuse(0.5f, 0.5f, 0.25f, 1f);
//		LightSources.setSpecLight(0.3f, 0.3f, 0.15f, 1f);
		// 初始化变换矩阵
		MatrixState.setInitStack();
		floor.bind();
		basketBall.bind();
		floorTransparent.bind();
		poets.bind();
		flag.bind();
	 
	}

	@Override
	public void dispose() {

	}

	 class BallController
	 {
		 private static final float G=9.8f;
		 private static final float RESILIENCE=0.8f;
		 private float  y;
		 public BallController(float y)
		 {
			 this.y=y;
			 startY=y;
			 
		 }
		 
		 public void addVy(float i) {
			 vy+=i;
			
		}

		private float timeSum;
		 private float vy=10;
		 private float startY;
		 public void update(float timeCollapsed)
		 {
			 if(y==0 ) return ;
			//此轮运动时间增加
			 timeSum+=timeCollapsed;
				//根据此轮起始Y坐标、此轮运动时间、此轮起始速度计算当前位置
				float tempCurrY=startY-0.5f*G*timeSum*timeSum+vy*timeSum;
				
			//	Log.d("tag", "vy  tempCurrY:"+tempCurrY);
				if(tempCurrY<=0)
				{//若当前位置低于地面则碰到地面反弹
					//反弹后起始位置为0
					startY=0;		
					//反弹后起始速度
					vy=-(vy-G*timeSum)*RESILIENCE;
					//反弹后此轮运动时间清0
					timeSum=0;
					//若速度小于阈值则停止运动
					//Log.d("tag", "vy:"+vy);
					if(vy<0.35f)
					{
						y=0;
						 
					}
				}
				else
				{//若没有碰到地面则正常运动
					y=tempCurrY;
				}
				 
				 
				 
				 
			  
		 }
		 
		 public float getY()
		 {
			 return y;
		 }
		 
		 
	 }

}
