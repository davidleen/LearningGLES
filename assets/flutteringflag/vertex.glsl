uniform mat4 uMVPMatrix; //总变换矩阵
attribute vec3 aPosition;  //顶点位置
attribute vec2 aTexCoor;    //顶点纹理坐标
varying vec2 vTextureCoord;  //用于传递给片元着色器的变量 
uniform float u_startAngle;//旗帜起始角度   0-2PI之间 
uniform float uWidthSpan;

const float PI=3.14;
const float uHeightSpan=4.0*PI;
void main()     
{

    float uStartPosition=0.0;
    
    //计算X向波浪          	
     float currentAngle=u_startAngle+ (aPosition.x-uStartPosition) / uWidthSpan *uHeightSpan;
     float tx=sin( currentAngle ) *0.1;
     
     
      //计算Y向波浪
      
        currentAngle=u_startAngle+ (aPosition.y-uStartPosition) / uWidthSpan *uHeightSpan;
     float ty=sin( currentAngle  ) *0.1;
     
        gl_Position=uMVPMatrix * vec4(aPosition.x,aPosition.y,tx+ty,1);
     //   gl_Position=uMVPMatrix * vec4(aPosition.xyz,1);
     vTextureCoord = aTexCoor;//将接收的纹理坐标传递给片元着色器
     
}                          