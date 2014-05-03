precision mediump  float;  
 
varying vec2 vTextureCoord;    //纹理坐标
uniform sampler2D mMapLoc; 

varying vec4 vAmbient;    //散射光定义
varying vec4 vDiffuse;   //散射光分量 传递给片元
varying vec4 vSpec;   //spec光分量 传递给片元
 
void main()
{

	 vec4 finalColor= texture2D ( mMapLoc , vTextureCoord ) ;
	 finalColor.a=(finalColor.r+finalColor.g+finalColor.b)/3.0;// //根据颜色值计算透明度 
	 finalColor=finalColor*vAmbient+finalColor*vDiffuse+finalColor*vSpec;
	   
	 	gl_FragColor=finalColor;
	 
	 
	 
}