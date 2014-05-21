package com.opengles.book.screen.treeOnDesert;

import com.opengles.book.MatrixState;
import com.opengles.book.framework.gl.LookAtCamera;
import com.opengles.book.galaxy.ObjectDrawable;

 

//单个的树类
public class SingleTree implements Comparable<SingleTree> 
{
	
	private static final float NEAR_ZERO=0.00001f ;
	public static  int textureId=0;
	float x;
	float z;
	float yAngle;
	TreeForDraw tfd;

    //与镜头的距离
    private float squareDistanceToCamera;
	public SingleTree(float x,float z,float yAngle,TreeForDraw tfd  )
	{
		this.x=x;
		this.z=z;
		this.yAngle=yAngle;
		this.tfd=tfd;
	}
	public void drawSelf(int texId)
	{
		
	}
	public void calculateBillboardDirection(LookAtCamera camera)
	{//根据摄像机位置计算树木面朝向
		
		float xspan=x-camera.getPosition().x;
		float zspan=z-camera.getPosition().z;
		
		if(zspan<=0)
		{
			yAngle=(float)Math.toDegrees(Math.atan(xspan/zspan));	
		}
		else
		{
			yAngle=180+(float)Math.toDegrees(Math.atan(xspan/zspan));
		}
        squareDistanceToCamera=(float)(Math.pow(xspan,2)+Math.pow(zspan,2) );

	}
	@Override
	public int compareTo(SingleTree another)
	{
		//重写的比较两个树木离摄像机距离的方法

		float  result=squareDistanceToCamera-another.squareDistanceToCamera;


		return (result<-NEAR_ZERO)?1:(result>NEAR_ZERO)?-1:0;
	}
	 
	public void draw() {
		MatrixState.pushMatrix();		
		MatrixState.translate(x, 0, z);
		MatrixState.rotate(yAngle, 0, 1, 0);
		 tfd.draw();
		MatrixState.popMatrix();
		
	}
	
}