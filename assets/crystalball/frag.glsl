precision mediump float;
varying vec2 vTextureCoord;//接收从顶点着色器过来的参数
uniform samplerCube sTexture;//环境立体内容数据
uniform sampler2D mMapLoc;//纹理内容数据

varying vec3 v_normal; //法向量
varying vec3 v_incident;//入射向量

varying vec4 ambient;
varying vec4 diffuse;
varying vec4 specular;
const float refractFactor=1.5;


  const float fresnelBias=0.01;

              const float fresnelScale=0.8;

              const float fresnelPower=10.0;

           //   const vec3 etaRatio=vec3(1.1,1.2,1.3);

 const vec3 etaRatio=vec3(0.9,0.8,0.7);





void main()                         
{           

   
       //计算折射光分量   反射系数
 //  float  reflectionFactor =clamp(  fresnelBias +

//                     fresnelScale * pow(1.0 + dot(v_incident, v_normal),

 //                                       fresnelPower) ,0.0,1.0);
      
      //使用简单方式                                  
         float dotValue=      dot(-normalize(v_incident), normalize(v_normal)) ;  
      float  reflectionFactor;    
      const float maxCos=0.7;
      const float minCos=0.2;
      
         if(dotValue>maxCos)
         {
          	 reflectionFactor=0.0;
          } else
           if(dotValue<minCos)
           {
           		reflectionFactor= 1.0;
           }
           else
           {
           reflectionFactor= (maxCos-dotValue)/(maxCos-minCos);
           }       		             
                                        

                //  reflectionFactor=0.0;
                                        //reflectionFactor

                                //计算不同的折射偏光
                                   vec3      TRed   = refract(v_incident, v_normal, etaRatio.x);

                                     vec3      TGreen = refract(v_incident, v_normal, etaRatio.y);

                                     vec3      TBlue  = refract(v_incident, v_normal, etaRatio.z);




      vec3 v_reflect=reflect(v_incident,v_normal);

     vec4  finalReflectColor = textureCube(sTexture, v_reflect);
   //   vec4  finalRefractColor =textureCube(sTexture, TRed);
     vec4  finalRefractColor =vec4( textureCube(sTexture, TRed).r,textureCube(sTexture, TGreen).g,textureCube(sTexture, TBlue).b,1.0);
      //将计算出的颜色给此片元
        vec4 finalColor=texture2D(mMapLoc, vTextureCoord);
        finalColor=finalRefractColor*(1.0-reflectionFactor) +finalReflectColor *reflectionFactor;
        finalColor=finalColor*ambient+finalColor*specular+finalColor*diffuse;
  		gl_FragColor = finalColor;
   
}              