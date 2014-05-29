precision mediump float;
varying vec3 vPosition;//接收从顶点着色器过来的参数
uniform samplerCube sTexture;//纹理内容数据
 
void main()                         
{           
   //给此片元从纹理中采样出颜色值          
  
   vec4 bcolor = textureCube(sTexture, vPosition);//给此片元从纹理中采样出颜色值 
    
      gl_FragColor=bcolor ;
   
    //gl_FragColor=gl_FragColor*alphaThreadHold;
}              