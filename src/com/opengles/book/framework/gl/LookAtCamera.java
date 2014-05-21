package com.opengles.book.framework.gl;

 

import com.opengles.book.MatrixState;
import com.opengles.book.math.Vector3;

import android.opengl.GLU;

 

public class LookAtCamera  extends Camera3D{
	final Vector3 position;
	final Vector3 up;
	final Vector3 lookAt;
	float fieldOfView;
	float aspectRatio;
	float near;
	float far;

	public LookAtCamera(float fieldOfView, float aspectRatio, float near, float far) {
		this.fieldOfView = fieldOfView;
		this.aspectRatio = aspectRatio;
		this.near = near;
		this.far = far;
		
		position = new Vector3(0,0,1);
		up = new Vector3(0, 1, 0);
		lookAt = new Vector3(0,0,0);
	}
	
	public Vector3 getPosition() {
		return position;
	}
	
	public Vector3 getUp() {
		return up;
	}
	
	public Vector3 getLookAt() {
		return lookAt;
	}
	public void setPosition(Vector3 newPosition)
	{
		position.set(newPosition);
	}
	public void setPosition(float x, float y, float z)
	{
		position.set(x,y,z);


	}
	
	public void setUp(float x, float y, float z)
	{
		up.set(x,y,z);
	}
	
	
	public void setLookAt(float x, float y, float z)
	{
		lookAt.set(x,y,z);
	}
	
	public void setMatrices( ) { 
		MatrixState.setFrustumProject( -fieldOfView/2,fieldOfView/2, -fieldOfView*aspectRatio/2, fieldOfView*aspectRatio/2, near, far); 
		MatrixState.setCamera(  position.x, position.y, position.z, lookAt.x, lookAt.y, lookAt.z, up.x, up.y, up.z);
		MatrixState.setInitStack();
	}
}
