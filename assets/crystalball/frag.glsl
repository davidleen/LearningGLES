precision mediump float;
varying vec2 vTextureCoord;//接收从顶点着色器过来的参数
uniform samplerCube sTexture;//环境立体内容数据
uniform sampler2D mMapLoc;//纹理内容数据

varying vec3 v_normal; //法向量
varying vec3 v_incident;//入射向量

varying vec4 ambient;
varying vec4 diffuse;
varying vec4 specular;

void main()                         
{           
   //给此片元从纹理中采样出颜色值          
  // gl_FragColor = texture2D(sTexture, vTextureCoord); 
 //  vec4 bcolor = texture2D(sTexture, vTextureCoord);//给此片元从纹理中采样出颜色值

    //  gl_FragColor=bcolor ;
       //   gl_FragColor = textureCube(sTexture, vec3(0.5,0.5,0.5));

  //    vec3 v_reflect=reflect(v_incident,v_normal);
   //   gl_FragColor = textureCube(sTexture, v_reflect);
      //将计算出的颜色给此片元
         vec4 finalColor=texture2D(mMapLoc, vTextureCoord);
 gl_FragColor = finalColor*ambient+finalColor*specular+finalColor*diffuse;
    //gl_FragColor=gl_FragColor*alphaThreadHold;
}              