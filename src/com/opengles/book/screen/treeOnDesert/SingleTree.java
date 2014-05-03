package com.opengles.book.screen.treeOnDesert;

import com.opengles.book.MatrixState;
import com.opengles.book.galaxy.ObjectDrawable;

 

//单个的树类
public class SingleTree implements Comparable<SingleTree> 
{
	
	 
	public static  int textureId=0;
	float x;
	float z;
	float yAngle;
	TreeForDraw tfd;
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
	public void calculateBillboardDirection()
	{//根据摄像机位置计算树木面朝向
		
		float xspan=x-MatrixState.cameraLocation[0];
		float zspan=z-MatrixState.cameraLocation[2];
		
		if(zspan<=0)
		{
			yAngle=(float)Math.toDegrees(Math.atan(xspan/zspan));	
		}
		else
		{
			yAngle=180+(float)Math.toDegrees(Math.atan(xspan/zspan));
		}
	}
	@Override
	public int compareTo(SingleTree another)
	{
		//重写的比较两个树木离摄像机距离的方法
		float cx=MatrixState.cameraLocation[0];
		float cz=MatrixState.cameraLocation[2];
		float xs=x-cx;
		float zs=z-cz;
		
		float xo=another.x-cx;
		float zo=another.z-cz;
		
		float disA=(float)Math.sqrt(xs*xs+zs*zs);
		float disB=(float)Math.sqrt(xo*xo+zo*zo);
		
		return ((disA-disB)==0)?0:((disA-disB)>0)?-1:1;  
	}
	 
	public void draw() {
		MatrixState.pushMatrix();		
		MatrixState.translate(x, 0, z);
		MatrixState.rotate(yAngle, 0, 1, 0);
		 tfd.draw();
		MatrixState.popMatrix();
		
	}
	
}