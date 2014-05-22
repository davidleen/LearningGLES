uniform mat4 uMVPMatrix; //总变换矩阵
attribute vec3 aPosition;  //顶点位置
attribute vec2 aTexCoor;    //顶点纹理坐标
varying vec2 vTextureCoord;  //用于传递给片元着色器的变量 
uniform float u_startAngle;//旗帜起始角度   0-2PI之间 
uniform float uWidthSpan;
const float uHeightSpan=0.1;
const float PI=3.14;
void main()     
{

    float uStartPosition=-5.0;
     float currentAngle=u_startAngle+ (aPosition.x-uStartPosition) / uWidthSpan *2.0* PI;
     float tz=sin(degrees(currentAngle))*uHeightSpan;
        gl_Position=uMVPMatrix * vec4(aPosition.x,aPosition.y,tz,1);
        //gl_Position=uMVPMatrix * vec4(aPosition.xyz,1);
     vTextureCoord = aTexCoor;//将接收的纹理坐标传递给片元着色器
     
}                          