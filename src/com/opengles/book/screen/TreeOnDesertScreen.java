package com.opengles.book.screen;

import java.util.Collections;
import java.util.List;

import android.opengl.GLES20;

import com.opengles.book.MatrixState;
import com.opengles.book.framework.Game;
import com.opengles.book.framework.Input.TouchEvent;
import com.opengles.book.framework.impl.GLScreen;
import com.opengles.book.galaxy.ObjectDrawable;

import com.opengles.book.objects.RectangleObject;
import com.opengles.book.screen.treeOnDesert.TreeGroup;

/**
 * 
 * @author davidleen29 
 * @create : 2014-4-22 下午11:25:49
 * @{  沙漠上树木的场景   使用标志板技术。
 * 		2d图片 永远对准camera
 * }
 */
public class TreeOnDesertScreen extends GLScreen{
	
	public static final int UNIT_SIZE=1;
	
	 private TreeGroup group;
	private ObjectDrawable desert;
	private ObjectDrawable maskView;//alpha测试用纹理矩形
	private float lastX;
	private float lastY;
	private static int cameraRadias=20;
	private float cameraXDegree=0;
	private float cameraYDegree=MIN_CAMERA_Y_DEGREE;
	private static float MIN_CAMERA_Y_DEGREE=3;
	private static float MAX_CAMERA_Y_DEGREE=15;
	float ratio;

	public TreeOnDesertScreen(Game game) {
		super(game);
		 group=new TreeGroup(game.getContext());
	 //	desert=new Desert(game.getContext(),100 , 100);
            desert=new RectangleObject(game.getContext(),"tree_on_desert/desert.bmp",100 , 100);
            RectangleObject   object=new RectangleObject(game.getContext(), "tree_on_desert/mask.png", 1, 1);
            object.addAlphaTest(0.6f);
            maskView=object;
	}

	@Override
	public void update(float deltaTime) {
		List<TouchEvent> touchEvents = glGame.getInput().getTouchEvents();
		 
		
		float offsetX=0;
		float offsetY=0;

		for (TouchEvent touch : touchEvents)
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
		     if(group!=null)
		     {
		      //计算所有树的朝向
		       group.calculateBillboardDirection();
		        //给树按照离视点的距离排序 
		         Collections.sort( group.alist); 
		     } 
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
				
				 //调用此方法计算产生透视投影矩阵
				MatrixState.setProject(-ratio, ratio, -1, 1, 1,  100);

				 //调用此方法产生摄像机9参数位置矩阵 
				resetCamera();
				
				
				
				//绘制沙漠
 				 MatrixState.pushMatrix();
 		            MatrixState.translate(0, -2, 0);
 		           if(desert!=null)
		            desert.draw();
 		            MatrixState.popMatrix();	
 		            
 		            
 		            //绘制树木 面对相机
 		            
 		           //开启混合
 		            GLES20.glEnable(GLES20.GL_BLEND);
 		            //设置混合因子。
 		            GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
 		           MatrixState.pushMatrix();
 		            MatrixState.translate(0, -2, 0);
 		           if(group!=null)
 		            group.draw();
 		            MatrixState.popMatrix();
 		            
 		           //关闭混合
 		            GLES20.glDisable(GLES20.GL_BLEND);
 		            
 		            
 		            
 		            
 		            
// 		            //绘制视窗
 		           //清除深度缓冲
 		            GLES20.glClear( GLES20.GL_DEPTH_BUFFER_BIT);
 					MatrixState.pushMatrix();
 					//调用此方法计算产生平行投影矩阵
 					 MatrixState.setProjectOrtho(-1f, 1f, -1f, 1f,1, 100);
 					//调用此方法产生摄像机9参数位置矩阵
 					MatrixState.setCamera(0, 10, 0, 0f, 0f, 0f, 0f, 0.0f,  1.0f);			
 					
	 					 
	 					//MatrixState.translate(3, 0, 0);
	 					//   MatrixState.rotate(90, 1, 0, 0);
	 					 
	 				 maskView.draw();//绘制alpha测试用矩形
	 					 
					MatrixState.popMatrix();
				
	}

	@Override
	public void pause() {
		 
		if(desert!=null)
	 	desert.unBind();
		if(group!=null)
		group.unBind();
		if(maskView!=null)
			maskView.unBind();
	}

	@Override
	public void resume() {
		GLES20.glClearColor(1.0f, 1.0f, 1.0f, 0.5f);

		GLES20.glEnable(GLES20.GL_DEPTH_TEST);
		 
		int width = glGame.getGLGraphics().getWidth();
		int height = glGame.getGLGraphics().getHeight();
		GLES20.glViewport(0, 0, width, height);
		//计算GLSurfaceView的宽高比
		 ratio = (float) width / height;
		
 
		// 初始化变换矩阵
		MatrixState.setInitStack();
		 
		if(desert!=null)
		desert.bind();
		if(group!=null)
		group.bind();
		if(group!=null)
		{
	      group.calculateBillboardDirection();
        //给树按照离视点的距离排序 
        Collections.sort( group.alist); 
		}
		
		if(maskView!=null)
			maskView.bind();
		}
	

	@Override
	public void dispose() {
		 
		
	}
	
}
