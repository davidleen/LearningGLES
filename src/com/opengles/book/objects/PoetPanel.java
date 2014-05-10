package com.opengles.book.objects;

import com.opengles.book.ShaderUtil;
import com.opengles.book.utils.FontUtil;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.opengl.GLES20;

/**
 * 诗词面板
 * @author davidleen29
 *
 */
public class PoetPanel extends RectangleObject{

	
	int textureId=-1;
	private float timeSum=0;
	private Bitmap bitmap ;
	private  Canvas canvas;
	public PoetPanel(Context context, int width,
			int height) {
		super(context, width, height);
		  bitmap=Bitmap.createBitmap(512,512,Bitmap.Config.ARGB_8888);
		  canvas=new Canvas(bitmap);
	}

	@Override
	protected int getTextureId() {
		 
		if(textureId!=-1)
		{
			GLES20.glDeleteTextures(1, new int[]{textureId}, 0);
			textureId=-1;
		}
		
		canvas.drawColor(Color.TRANSPARENT ,Mode.CLEAR);
		// canvas.drawColor(Color.TRANSPARENT);
		 
		 FontUtil.drawText(canvas,FontUtil.getContent(FontUtil.content) );
		 textureId= ShaderUtil.loadTextureWithUtils(  bitmap,false);
		  
		 return textureId;
	}
	
	/**
	 *  
	 * @param time
	 */
	public void onUpdate(float time)
	{
		timeSum+=time;
		if(timeSum>1)
		{
			timeSum-=1;
			FontUtil.updateRGB();
			FontUtil.increaseIndex();
			 
		}
	}
	
	
	
	
}
