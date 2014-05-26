package com.opengles.book.objects;

import android.content.Context;

import com.opengles.book.galaxy.ObjObject;


/**
 * a cuboid   twisting
 * @author davidleen29
 *
 */
public class TwistCubiod  extends ObjObject{

	public TwistCubiod(Context context ) {
		super(context, "twistcuboid/", "cuboid.obj");
		 
	}

	@Override
	protected void onBind(int mProgram) {
		 
		super.onBind(mProgram);
	}

	@Override
	protected void onUnBind(int mProgram) { 
		super.onUnBind(mProgram);
	}

	@Override
	protected void onDraw(int mProgram) {
		 
		super.onDraw(mProgram);
	}

	@Override
	protected String getFragmentFileName() {
		 
		return "twistcuboid/frag.glsl";
	}

	@Override
	protected String getVertexFileName() {
		 
		return "twistcuboid/vertex.glsl";
	}

	@Override
	protected void onCreate(int mProgram) {
		 
		super.onCreate(mProgram);
	}

	
	
	 

}
