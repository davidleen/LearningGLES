package com.opengles.book.objects;

import java.io.IOException;
import java.util.Vector;

import com.opengles.book.ShaderUtil;
import com.opengles.book.math.Rectangle;
import com.opengles.book.math.Vector2;
import com.opengles.book.utils.BitmapUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.view.View.MeasureSpec;
import android.widget.Button;

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

}
