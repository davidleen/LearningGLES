uniform mat4 uMVPMatrix; //总变换矩阵
attribute vec3 aPosition;  //顶点位置
attribute vec2 aTexCoor;    //顶点纹理坐标
varying vec2 vTextureCoord;  //用于传递给片元着色器的变量
attribute vec3 u_startPosition;//旗帜飘动起始位置
attribute vec3 u_endPosition;//旗帜飘动结束位置
attribute float u_startAngle;//旗帜起始角度   0-2PI之间
constant float PI=3.1415926f;

void main()     
{

    vec4 newPosition=uMVPMatrix * vec4(aPosition,1);
    vec4 newStartPosition=uMVPMatrix * vec4(u_startPosition,1);
    vec4 newEndPosition=uMVPMatrix * vec4(u_endPosition,1);

     vTextureCoord = aTexCoor;//将接收的纹理坐标传递给片元着色器
     gl_Position =  newPosition;
}                          