package com.opengles.book.screen;

import java.util.ArrayList;
import java.util.List;

import android.opengl.GLES20;
import android.util.Log;

import com.opengles.book.MatrixState;
import com.opengles.book.framework.Game;
import com.opengles.book.framework.Input.TouchEvent;
import com.opengles.book.framework.impl.GLScreen;
import com.opengles.book.galaxy.ObjectDrawable;
import com.opengles.book.math.Vector2;
import com.opengles.book.objects.MenuObject;
import com.opengles.book.objects.MenuObject.MenuClickListner;
import com.opengles.book.objects.RectangleObject;

public class MainScreen extends FrameBufferScreen{
	
	private List<MenuObject> menus;
	 int width;
	 int height;

	 
	public MainScreen(Game game) {
		super(game);
		menus=new ArrayList<MenuObject>();
		MenuObject 	menu=new MenuObject(game.getContext(),300, 50,0,300,"太阳系");
		 menu.setListener(new MenuClickListner() {
				
				@Override
				public void onClick(MenuObject object) {
					 
					//切换screen
					
					glGame.setScreen(new GalaxyScreen(glGame));
				}
			});	
		menus.add(menu);
		 
		 menu=new MenuObject(game.getContext(),300, 50,0,150,"灰度地图天空穹");
		 menu.setListener(new MenuClickListner() {
				
				@Override
				public void onClick(MenuObject object) {
					 
					//切换screen
					
					glGame.setScreen(new GrayMapScreen(glGame));
				}
			});	
		 menus.add(menu);
		
		 menu=new MenuObject(game.getContext(),300, 50,0,-0,"篮球倒影+动态字幕");
		 menu.setListener(new MenuClickListner() {
				
				@Override
				public void onClick(MenuObject object) {
					 
					//切换screen
					
					glGame.setScreen(new Reflect_BasketBall_Screen(glGame));
				}
			});	
		 menus.add(menu);
			
			menu=new MenuObject(game.getContext(),300, 50,0,-150,"树林（标志板）");
menu.setListener(new MenuClickListner() {
				
				@Override
				public void onClick(MenuObject object) {
					 
					//切换screen
					
					glGame.setScreen(new TreeOnDesertScreen(glGame));
				}
			});
			menus.add(menu);
		
			menu=new MenuObject(game.getContext(),300, 50,0,-300,"兔子（Obj）");
			menu.setListener(new MenuClickListner() {
				
				@Override
				public void onClick(MenuObject object) {
					 
					//切换screen 
					glGame.setScreen(new OjObjectScreen(glGame));
				}
			});
			menus.add(menu);
	}

	@Override
	public void update(float deltaTime) {
        super.update(deltaTime);
		List<TouchEvent> touchs = glGame.getInput().getTouchEvents();
		
		for (TouchEvent touch : touchs)
		{
			
			//Log.d("TEST", "click "+touch);
			switch(touch.type)
			{
			case TouchEvent.TOUCH_DOWN: 
				 
				for(int i=0;i<menus.size();i++)
				{
					MenuObject obj=menus.get(i);
					if(	 obj.location.include(touch.x-width/2, touch.y-height/2 ))
					{
						Log.d("TEST",i+"  click "+touch);
						obj.performMenuClick();
					}
					
					 
					
				}
				
				break;
			 
				 
			}
		}
		
		
	}

	@Override
	public void onPresent(float deltaTime) {
		
		// 清除颜色
		GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT
						| GLES20.GL_COLOR_BUFFER_BIT);

        float ratio = (float) width / height;
        //调用此方法计算产生透视投影矩阵
        MatrixState.setOrthoProject(-width/2, width/2, -height/2, height/2,    1,  1000);

        MatrixState.setCamera(0,  10, 0f, 0f, 0f, 0f, 0f, 0f,   -1f);
        MatrixState.setInitStack();
		
		 
		 for(MenuObject obj:menus)
		 {
			 MatrixState.pushMatrix();
			  MatrixState.translate(0, 0, obj.location.lowerLeft.y+obj.location.height/2) ;
			  obj.draw();
				MatrixState.popMatrix();
		 }
		 
		
	}

	@Override
	public void pause() {
        super.pause();
		 
		 for(MenuObject obj:menus)
		 {
			 obj.unBind();
		 }
		GLES20.glDisable(GLES20.GL_DEPTH_TEST);
		//GLES20.glDisable(GLES20.GL_CULL_FACE);
	}

	@Override
	public void resume() {

        super.resume();
		GLES20.glClearColor(1.0f, 1.0f, 1.0f, 0.5f); 
		GLES20.glEnable(GLES20.GL_DEPTH_TEST);
		//GLES20.glEnable(GLES20.GL_CULL_FACE);
		 
		  width = glGame.getGLGraphics().getWidth();
		  height = glGame.getGLGraphics().getHeight();
		GLES20.glViewport(0, 0, width, height);
		//计算GLSurfaceView的宽高比

		 for(MenuObject obj:menus)
		 {
			 obj.bind();
		 }
		
	}

	@Override
	public void dispose() {

        super.dispose();
		
		
	}
	
	
	

}
