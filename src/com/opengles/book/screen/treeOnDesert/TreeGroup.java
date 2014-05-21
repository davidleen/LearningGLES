package com.opengles.book.screen.treeOnDesert;

import static com.opengles.book.screen.TreeOnDesertScreen.*;

import java.util.ArrayList;
import java.util.List;

import com.opengles.book.framework.gl.LookAtCamera;
import com.opengles.book.galaxy.ObjectDrawable;

import android.content.Context;

public class TreeGroup implements ObjectDrawable
{
	TreeForDraw tfd;
	public List<SingleTree> alist=new ArrayList<SingleTree>();
	
	public TreeGroup(Context context)
	{
		tfd=new TreeForDraw(context);
		alist.add(new SingleTree(0,0,0,tfd));
		alist.add(new SingleTree(8*UNIT_SIZE,0,0,tfd));
		alist.add(new SingleTree(5.7f*UNIT_SIZE,5.7f*UNIT_SIZE,0,tfd));
		alist.add(new SingleTree(0,-8*UNIT_SIZE,0,tfd));
		alist.add(new SingleTree(-5.7f*UNIT_SIZE,5.7f*UNIT_SIZE,0,tfd));
		alist.add(new SingleTree(-8*UNIT_SIZE,0,0,tfd));
		alist.add(new SingleTree(-5.7f*UNIT_SIZE,-5.7f*UNIT_SIZE,0,tfd));
		alist.add(new SingleTree(0,8*UNIT_SIZE,0,tfd));
		alist.add(new SingleTree(5.7f*UNIT_SIZE,-5.7f*UNIT_SIZE,0,tfd));
	}
	public void calculateBillboardDirection(LookAtCamera camera)
    {
    	//计算列表中每个树木的朝向
    	for(int i=0;i<alist.size();i++)
    	{
    		alist.get(i).calculateBillboardDirection(camera);
    	}
    }
    
    public void drawSelf(int texId)
    {
    }
	@Override
	public void bind() {
		// 的每个树木
		tfd.bind();
    	 
	}
	@Override
	public void unBind() {
		tfd.unBind();
		 
		
	}
	@Override
	public void draw() {
		//绘制列表中的每个树木
    	for(int i=0;i<alist.size();i++)
    	{
    		alist.get(i).draw( );
    	}
		
	}
    
}