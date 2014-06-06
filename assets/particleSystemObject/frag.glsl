precision mediump float;
uniform vec4 uColor;
uniform sampler2D sTexture;//纹理内容数据
varying float v_lifeTime;//传递给片元的时间参数
void main()                         
{           
   //给此片元从纹理中采样出颜色值          
  // gl_FragColor = texture2D(sTexture, gl_PointCoord); 
   vec4 textureColor = texture2D(sTexture, gl_PointCoord );//给此片元从纹理中采样出颜色值 
	
	//do color attenuate;
       gl_FragColor = uColor  * textureColor       ;
     // gl_FragColor.a=1.0;
      //
      gl_FragColor.a  *=   v_lifeTime;
     
    // gl_FragColor=textureColor;
}              