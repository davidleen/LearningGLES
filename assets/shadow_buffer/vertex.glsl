uniform mat4 uMVPMatrix; //总变换矩阵
uniform mat4 uMMatrix; //变换矩阵

uniform  highp vec3 uLightLocation;	//光源位置
attribute vec3 aPosition;  //顶点位置

//用于传递给片元着色器的变量
//varying vec4 ambient;
//varying vec4 diffuse;
//varying vec4 specular;
//varying vec2 vTextureCoord;
varying  vec4 vPosition;


void main()     
{ 
   gl_Position = uMVPMatrix * vec4(aPosition,1); //根据总变换矩阵计算此次绘制此顶点位置


   vPosition=uMMatrix*vec4(aPosition,1);
}                      