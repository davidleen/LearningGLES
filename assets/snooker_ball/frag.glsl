precision mediump float;
uniform  highp mat4 uLightMVPMatrix; //总变换矩阵(对于光源位置的矩阵。)
uniform sampler2D sTexture;//纹理内容数据
uniform sampler2D shadowTexture;//阴影纹理映射贴图。
//接收从顶点着色器过来的参数
uniform highp vec3 uLightLocation;	//光源位置
varying vec4 ambient;
varying vec4 diffuse;
varying vec4 specular;
varying vec2 vTextureCoord;
varying  vec4 vPosition;

void main()                         
{

  //获取光源虚拟摄像机上的片元点
     vec4 positionOnLightCamera=uLightMVPMatrix*vec4(vPosition.xyz,1);
     //透视除法
     positionOnLightCamera=positionOnLightCamera/positionOnLightCamera.w;

//将坐标转换为纹理坐标
     float s=(positionOnLightCamera.s+1.0)/2.0;
     float t=(positionOnLightCamera.t+1.0)/2.0;
     vec4 finalColor=texture2D(sTexture, vTextureCoord);
     if( s>=0.0 && s<=1.0 && t>=0.0 && t<=1.0)
     {

    vec4 depth4=texture2D(shadowTexture,vec2(s,t));
    float minDis= depth4.r*256.0*256.0+depth4.g*256.0+depth4.b+depth4.a/32.0;
    float dis=distance(vPosition.xyz,uLightLocation);
    //将计算出的颜色给此片元

    //若实际距离大于最小距离， 则在阴影中。  为修正值  根据具体情况调整， 否则会出现严重自身阴影问题。
    bool isInshadow=    minDis <= dis-0.5;

            if( isInshadow)
            {
                 //阴影中 仅适用环境光计算
                  finalColor = finalColor*ambient;


              }else
              {
           //给此片元颜色值
               finalColor = finalColor*ambient+finalColor*specular+finalColor*diffuse;
              }
      }
    else
    {


     finalColor = finalColor*ambient+finalColor*specular+finalColor*diffuse;
    }
    gl_FragColor =finalColor  ;


}