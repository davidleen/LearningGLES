uniform mat4 uMVPMatrix; //总变换矩阵
attribute vec3 aPosition;  //顶点位置
attribute vec2 aTexCoor;    //顶点纹理坐标
varying vec2 vTextureCoord;  //用于传递给片元着色器的变量
varying float currY;				//用于传递给片元着色器的Y坐标
uniform vec4  u_clipPlane;    //裁剪平面方程参数。
varying float u_clipDist;     //传递给片元的裁剪信息。
void main()     
{                            		
   gl_Position = uMVPMatrix * vec4(aPosition,1); //根据总变换矩阵计算此次绘制此顶点位置
   vTextureCoord = aTexCoor;//将接收的纹理坐标传递给片元着色器
   currY=aPosition.y;	//将顶点的Y坐标传递给片元着色器
   //计算裁剪信息  aPosition 与 gl_Position 不同的裁剪 效果是不一样的。
   u_clipDist=dot(aPosition.xyz,u_clipPlane.xyz)+u_clipPlane.w;
   
 //u_clipDist=dot(gl_Position.xyz,u_clipPlane.xyz)+u_clipPlane.w;
   
}                          