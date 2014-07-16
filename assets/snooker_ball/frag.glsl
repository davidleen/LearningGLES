precision mediump float;
uniform mat4 uLightMVPMatrix; //总变换矩阵(对于光源位置的矩阵。)
uniform mat4 uMMatrix; //变换矩阵
uniform vec3 uLightLocation;	//光源位置
uniform sampler2D sTexture;//纹理内容数据
uniform sampler2D shadowTexture;//阴影纹理映射贴图。
//接收从顶点着色器过来的参数

varying vec4 ambient;
varying vec4 diffuse;
varying vec4 specular;
varying vec2 vTextureCoord;
varying vec4 vPosition;

void main()                         
{

  //获取光源虚拟摄像机上的片元点
     vec4 positionOnLightCamera=uLightMVPMatrix*vec4(vPosition.xyz,1);
     //透视除法
     positionOnLightCamera=positionOnLightCamera/positionOnLightCamera.w;

//将坐标转换为纹理坐标
     float s=(positionOnLightCamera.s+1.0)/2.0;
     float t=(positionOnLightCamera.t+1.0)/2.0;

    vec4 depth4=texture2D(shadowTexture,vec2(s,t));
    float minDis=depth4.r*1024.0+depth4.r*256.0+depth4.b+depth4.a/256.0;

    float currDis=distance(vPosition.xyz,uLightLocation);



//将计算出的颜色给此片元
   vec4 finalColor=texture2D(sTexture, vTextureCoord);


  if(minDis<=(currDis-3.0)) //若实际距离大于最小距离， 则在阴影中。 3.0为修正值  根据具体情况调整， 否则会出现严重自身阴影问题。
    {
        //阴影中 仅适用环境光计算
        gl_FragColor = finalColor*ambient*1.2;
      // gl_FragColor = finalColor*ambient+finalColor*specular+finalColor*diffuse;
    }else
    {
 //给此片元颜色值
     gl_FragColor = finalColor*ambient+finalColor*specular+finalColor*diffuse;
    }
gl_FragColor = finalColor*ambient+finalColor*specular+finalColor*diffuse;

  gl_FragColor = finalColor*ambient+finalColor*specular+finalColor*diffuse;
}   