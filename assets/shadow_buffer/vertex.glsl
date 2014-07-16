////专门用于计算阴影缓冲的顶点着色器   只需要 总变换矩阵，顶点位置。变换矩阵。
uniform mat4 uMVPMatrix; //总变换矩阵
uniform mat4 uMMatrix; //变换矩阵
uniform vec3 uLightLocation;//光源位置。
attribute vec3 aPosition;   //顶点位置
varying vec4 vPosition;//传递给片元的顶点位置

void main()     
{ 
   gl_Position = uMVPMatrix * vec4(aPosition,1); //根据总变换矩阵计算此次绘制此顶点位置  
   vPosition=uMMatrix*vec4(aPosition,1);//计算出变换后的顶点位置并传给片元着色器

   

}