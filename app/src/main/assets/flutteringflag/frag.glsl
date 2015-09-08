precision mediump float;
varying vec2 vTextureCoord;//接收从顶点着色器过来的参数
uniform sampler2D sTexture;//纹理内容数据


const float blurStep=1.0;
void main()                         
{           
   //给此片元从纹理中采样出颜色值          
  // gl_FragColor = texture2D(sTexture, vTextureCoord); 
  
  vec4 sample0;
  vec4 sample1;
  vec4 sample2;
  vec4 sample3;
  
  float step=blurStep/100.0;
  
  
  sample0  = texture2D(sTexture, vec2(vTextureCoord.x-step,vTextureCoord.y-step));
   sample1  = texture2D(sTexture, vec2(vTextureCoord.x+step,vTextureCoord.y-step));
   sample2  = texture2D(sTexture, vec2(vTextureCoord.x-step,vTextureCoord.y+step));
  
   sample3  = texture2D(sTexture, vec2(vTextureCoord.x+step,vTextureCoord.y+step));
  
   vec4 bcolor=(sample0+sample1+sample2+sample3)/4.0;
  
  // vec4 bcolor = texture2D(sTexture, vTextureCoord);//给此片元从纹理中采样出颜色值 
	
	
	
	
	
      gl_FragColor=bcolor ;


}              