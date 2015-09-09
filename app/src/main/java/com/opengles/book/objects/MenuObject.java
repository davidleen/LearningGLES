package com.opengles.book.objects;

import com.opengles.book.ShaderUtil;
import com.giants3.android.openglesframework.framework.math.Rectangle;
import com.giants3.android.openglesframework.framework.math.Vector2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class MenuObject extends RectangleObject{

	int textureId;
	private int width;
	private int height;
	 public Rectangle location;
	public MenuObject(Context context, int width, int height, int x,int y,String text  ) {
		super(context, width, height);
		location=new Rectangle(x, y, width, height);
		 
		  
		 
		   Bitmap bitmap=null;
		 
			bitmap = Bitmap.createBitmap(width,height,Config.ARGB_8888);
		 
		 
		Canvas canvas=new Canvas(bitmap);
		canvas.drawColor(Color.BLUE);
		Paint paint=new Paint();
		paint.setColor(Color.GREEN);
		int textSize=18;
	//	paint.setStrokeWidth(18);
		paint.setTextSize(textSize);
		canvas.drawText(text, width/3, height/3*2,paint);
		 
		 
		   
		textureId=ShaderUtil.loadTextureWithUtils(bitmap, false);
		
		 
	}
	@Override
	protected int getTextureId() {
		 
		return textureId;
	}
	
	
	
	/**
	 * 菜单点击事件
	 * @author davidleen29
	 *
	 */
	public interface MenuClickListner
	{
		public void onClick(MenuObject object);
	}
	
	 private MenuClickListner listener;
	public MenuClickListner getListener() {
		return listener;
	}
	public void setListener(MenuClickListner listener) {
		this.listener = listener;
	}
	 
	public void performMenuClick()
	{
		if(listener!=null)
		{
			listener.onClick(this);
		}
	}

    @Override
    public void update(float deltaTime) {

    }
}
