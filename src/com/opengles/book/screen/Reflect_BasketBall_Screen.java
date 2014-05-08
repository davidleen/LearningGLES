package com.opengles.book.screen;

import java.util.List;

import android.opengl.GLES20;
import android.util.Log;

import com.opengles.book.MatrixState;
import com.opengles.book.framework.Game;
import com.opengles.book.framework.Input.TouchEvent;
import com.opengles.book.framework.gl.FPSCounter;
import com.opengles.book.framework.impl.GLScreen;
import com.opengles.book.galaxy.ObjectDrawable;
import com.opengles.book.objects.RectangleObject;
import com.opengles.book.objects.SphereObject;
import com.opengles.book.utils.FontUtil;

public   class Reflect_BasketBall_Screen extends GLScreen {

	ObjectDrawable basketBall;
	ObjectDrawable floor;
	ObjectDrawable basketBallReflected;
	ObjectDrawable floorTransparent;
	BallController controller;
	 
	//诗词背景绘画
	ObjectDrawable poets;
	FPSCounter counter;
	 



	public Reflect_BasketBall_Screen(Game game) {
		super(game);
		 


		counter = new FPSCounter();
		 
		 floor=new RectangleObject(game.getContext(), "basketball_reflect/mdb.png",12, 12);
		 basketBall=new SphereObject(game.getContext(), "basketball_reflect/basketball.png", 2)	;
		 floorTransparent=new RectangleObject(game.getContext(), "basketball_reflect/mdbtm.png",12, 12);
		 poets= new RectangleObject(game.getContext(), FontUtil.generateWLT(FontUtil.content, 512, 512), 5, 5);
		 controller=new BallController(10);
	}

	@Override
	public void update(float deltaTime) {

		List<TouchEvent> events=  glGame.getInput().getTouchEvents();
		  
		for (TouchEvent touch : events)
		{
			switch(touch.type)
			{
			case TouchEvent.TOUCH_DOWN: 
				controller.addVy(1);
				break;
			 
				 
			}
			

			 
		}
		
		  
		  controller.update(deltaTime);
		  
		 

	}

	@Override
	public void present(float deltaTime) {
	//	counter.logFrame();
		
		// 清除颜色
		GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT
				| GLES20.GL_COLOR_BUFFER_BIT);
		
		float y=controller.getY();
		
		 MatrixState.pushMatrix();
         MatrixState.translate(0, -2, 0); 
         //绘制反射面地板
		  floor.draw();
		//绘制镜像体
		 MatrixState.pushMatrix();
		 MatrixState.translate(0, -y, 0); 
		 basketBall.draw();
		 MatrixState.popMatrix();
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
		
		 MatrixState.popMatrix();
		 

         MatrixState.pushMatrix();
         MatrixState.translate(-12,  0, -5); 
		 MatrixState.rotate(90, 1, 0, 0) ; 
		 
		 poets.draw();
		 MatrixState.popMatrix();
		
		 
		
	}

	@Override
	public void pause() {
		 
		
		floor.unBind();
		basketBall.unBind();
		floorTransparent.unBind();
		poets.unBind();
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
		basketBall.bind();
		floorTransparent.bind();
		poets.bind();
	 
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
				
				Log.d("tag", "vy  tempCurrY:"+tempCurrY);
				if(tempCurrY<=0)
				{//若当前位置低于地面则碰到地面反弹
					//反弹后起始位置为0
					startY=0;		
					//反弹后起始速度
					vy=-(vy-G*timeSum)*RESILIENCE;
					//反弹后此轮运动时间清0
					timeSum=0;
					//若速度小于阈值则停止运动
					Log.d("tag", "vy:"+vy);
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
