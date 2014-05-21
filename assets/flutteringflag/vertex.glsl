uniform mat4 uMVPMatrix; //总变换矩阵
attribute vec3 aPosition;  //顶点位置
attribute vec2 aTexCoor;    //顶点纹理坐标
varying vec2 vTextureCoord;  //用于传递给片元着色器的变量 
uniform float u_startAngle;//旗帜起始角度   0-2PI之间 
uniform float uWidthSpan;
void main()     
{

    
     float currentAngle=u_startAngle+aPosition.x * 3.1415926;
     float tz=sin(currentAngle);
    gl_Position=uMVPMatrix * vec4(aPosition.x,aPosition.y,tz,1);
//gl_Position=uMVPMatrix * vec4(aPosition.xyz,1);
     vTextureCoord = aTexCoor;//将接收的纹理坐标传递给片元着色器
     
}                          