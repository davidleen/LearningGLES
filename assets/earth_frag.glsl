precision mediump  float;  
varying vec2 vNightTextureCoord;  //夜景纹理坐标
varying vec2 vDayTextureCoord;    //日景纹理坐标
uniform sampler2D mMapLoc; 

varying vec4 vAmbient;    //散射光定义
varying vec4 vDiffuse;   //散射光分量 传递给片元
varying vec4 vSpec;   //spec光分量 传递给片元
varying vec3 vPosition;  //传递给片元定点位置。
void main()
{

	 vec4 finalColorDay= texture2D ( mMapLoc , vDayTextureCoord ) ;
	 vec4 finalColorNight= texture2D ( mMapLoc , vNightTextureCoord ) ;
	 
	 finalColorDay=finalColorDay*vAmbient+finalColorDay*vDiffuse+finalColorDay*vSpec;
	 finalColorNight=finalColorNight*vec4(0.5,0.5,0.5,1.0);
	 
	 if(vDiffuse.x>0.21)
	 {
	 	gl_FragColor=finalColorDay;
	 }else
	  if(vDiffuse.x<0.05)
	  {
	  	gl_FragColor=finalColorNight;
	  }else
	  {
	  	float t=(vDiffuse.x-0.05)/(0.21-0.05); 
    	 gl_FragColor=t*finalColorDay+(1.0-t)*finalColorNight;
	  }
	 
	// gl_FragColor =
	 // gl_FragColor =vAmbient * gl_FragColor +vDiffuse * gl_FragColor;
	//  gl_FragColor = vec4(1,0,0,1);
	 
}