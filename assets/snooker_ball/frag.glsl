precision mediump float;
uniform sampler2D sTexture;//纹理内容数据
uniform sampler2D shadowTexture;//阴影纹理映射贴图。
//uniform sampler2DShadow shadowTexture;//阴影纹理映射贴图。

varying vec4 ambient;
varying vec4 diffuse;
varying vec4 specular;
varying vec2 vTextureCoord;
varying  vec4 vPosition;


void main()                         
{

        vec4 finalColor=texture2D(sTexture, vTextureCoord);


            vec4 lightZ = vPosition / vPosition.w;
                    lightZ = (lightZ + 1.0) / 2.0;
                      vec4 packedZValue = texture2D(shadowTexture, lightZ.st);
                         float bias =0.005 ;
                    bool isInShadow=   packedZValue.x<lightZ.z-bias;


                    if( isInShadow)
                    {
                         //阴影中 仅适用环境光计算
                          finalColor = finalColor*ambient;


                      }else
                      {
                   //给此片元颜色值
                       finalColor = finalColor*ambient+finalColor*specular+finalColor*diffuse;
                      }


            gl_FragColor =finalColor  ;


}