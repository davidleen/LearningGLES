precision mediump  float;  
varying vec2 vNightTextureCoord;  //夜景纹理坐标
varying vec2 vDayTextureCoord;    //日景纹理坐标
uniform sampler2D mMapLoc; 

varying vec4 vAmbient;    //散射光定义
varying vec4 vDiffuse;   //散射光分量 传递给片元
varying vec4 vSpec;   //spec光分量 传递给片元
varying vec3 vPosition;  //传递给片元定点位置。
varying float vFogFactor; //雾因子
void main()
{

	
	  //使用雾 
	  vec4 fogColor = vec4(0.97,0.76,0.03,1.0);//雾的颜色	
 	if(vFogFactor != 0.0){//如果雾因子为0，不必计算光照
 	
	 	 vec4 finalColorDay= texture2D ( mMapLoc , vDayTextureCoord ) ;
		 vec4 finalColorNight= texture2D ( mMapLoc , vNightTextureCoord ) ;
		 
		 finalColorDay=finalColorDay*vAmbient+finalColorDay*vDiffuse+finalColorDay*vSpec;
		 finalColorNight=finalColorNight*vec4(0.5,0.5,0.5,1.0);
		  vec4 finalColor;
		 if(vDiffuse.x>0.21)
		 {
		 	finalColor=finalColorDay;
		 }else
		  if(vDiffuse.x<0.05)
		  {
		  	finalColor=finalColorNight;
		  }else
		  {
		  	float t=(vDiffuse.x-0.05)/(0.21-0.05); 
	    	 finalColor=t*finalColorDay+(1.0-t)*finalColorNight;
		  } 
			 
			gl_FragColor = finalColor*vFogFactor + fogColor*(1.0-vFogFactor);//物体颜色和雾颜色插值计算最终颜色
	}else{
 	    gl_FragColor=fogColor;
 	}
 	
 	
	 
	// gl_FragColor =
	 // gl_FragColor =vAmbient * gl_FragColor +vDiffuse * gl_FragColor;
	//  gl_FragColor = vec4(1,0,0,1);
	 
}