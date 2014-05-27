package com.opengles.book.objects;

import android.content.Context;

import android.opengl.GLES20;
import com.opengles.book.galaxy.ObjObject;


/**
 * a cuboid   twisting
 * @author davidleen29
 *
 */
public class TwistCubiod  extends ObjObject{

    private  int ySpanHandler;
    private int angleSpanHandler;
    private  int startYHandler;

    private float angleSpan;
	public TwistCubiod(Context context ) {
		super(context, "twistcuboid/", "cuboid.obj");
		 
	}

	@Override
	protected void onBind(int mProgram) {




        GLES20.glUniform1f(angleSpanHandler,angleSpan);
        GLES20.glUniform1f(ySpanHandler,60);
        GLES20.glUniform1f(startYHandler,-9);

    }

	@Override
	protected void onUnBind(int mProgram) { 
		super.onUnBind(mProgram);
	}

	@Override
	protected void onDraw(int mProgram) {
		 
		super.onDraw(mProgram);
        GLES20.glUniform1f(angleSpanHandler,angleSpan);
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

            ySpanHandler= GLES20.glGetUniformLocation(mProgram,"ySpan");
            angleSpanHandler= GLES20.glGetUniformLocation(mProgram,"angleSpan");
            startYHandler= GLES20.glGetUniformLocation(mProgram,"startY");

    }


    private float timeCollapsed=0;
    float step=0.1f;
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        timeCollapsed+=deltaTime;
        if(timeCollapsed>0.05)
        {


            if(angleSpan>=3)
                step=-0.1f;
            if(angleSpan<=-3)
                step=0.1f;
            angleSpan+=step;
            timeCollapsed-=0.05f;
        }

    }
}
