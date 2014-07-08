precision mediump float;
uniform sampler2D mMapLoc;//纹理内容数据
//接收从顶点着色器过来的参数
varying vec4 ambient;
varying vec4 diffuse;
varying vec4 specular;
varying vec2 vTextureCoord;

 uniform vec4 pickColor;

void main()                         
{    
   //将计算出的颜色给此片元
   vec4 finalColor=texture2D(mMapLoc, vTextureCoord);    
   //给此片元颜色值
   gl_FragColor = finalColor*ambient+finalColor*specular+finalColor*diffuse;


   gl_FragColor= gl_FragColor * pickColor;

}   