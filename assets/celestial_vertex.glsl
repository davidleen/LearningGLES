 uniform mat4 uMVPMatrix; 
 uniform float mStarSize; 
 attribute vec3 mPositionLoc;
 
 

 void main()
{

	gl_Position =  uMVPMatrix *  vec4(mPositionLoc,1);
	 gl_PointSize = mStarSize;
	

}

