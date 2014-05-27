package com.opengles.book.objects;
 

import com.opengles.book.ShaderUtil;

import android.content.Context;
 

/**
 * 平面的通用对象
 * @author Administrator
 *
 */
public class SphereObject extends AbstractSimpleObject{

	
	private static final int UNIT_SIZE=1;
	
	private String textureFileName;
	float[] vertexData;
	short[] indexData;
	int textureId=-1;
	public SphereObject(Context context, String textureFileName,int radius) {
		super(context);
		 this.textureFileName=textureFileName;
		 initData(radius);
		 textureId=ShaderUtil.loadTextureWithUtils(context, textureFileName, false);
		 
		 
	}
	
	private void initData(int radius )
	{
		
		Sphere sphere=new Sphere(radius);
		vertexData=sphere.attributes; 
		
		indexData= sphere.indics;
		
		
	}
	@Override
	protected float[] getVertexData() {
		 
		return vertexData;
	}
	@Override
	protected short[] getIndexData() {
		 
		return indexData;
	}
	 

	@Override
	protected int getTextureId() {
		 
		return textureId;
	}

    @Override
    public void update(float deltaTime) {

    }
}
