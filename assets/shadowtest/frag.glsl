precision mediump float;
uniform sampler2D sTexture;//纹理内容数据
uniform sampler2D shadowTexture;//阴影纹理映射贴图。


varying vec4 ambient;
varying vec4 diffuse;
varying vec4 specular;
varying vec2 vTextureCoord;
varying  vec4 vPosition;


// This function looks up the shadow texture
// and returns whether the pixel is in shadow or not
bool getShadowFactor(vec4 lightZ)
{
    vec4 packedZValue = texture2D(shadowTexture, lightZ.st);
   // float bias = 0.005*tan(acos(cosTheta)); // cosTheta is dot( n,l ), clamped between 0 and 1
   // bias = clamp(bias, 0,0.01);
    return packedZValue.x<lightZ.z-0.025;

 // unpack the value stored to get the depth.
    const vec4 bitShifts = vec4(1.0 / (256.0 * 256.0 * 256.0),
                        1.0 / (256.0 * 256.0),
                        1.0 / 256.0,
                        1);
    float shadow = dot(packedZValue , bitShifts);
  //  if(shadow>0.9) return true;


    return shadow<lightZ.z;
}

void main()                         
{

        vec4 finalColor=texture2D(sTexture, vTextureCoord);

        if( vPosition.w>0.0)
        {
            vec4 lightZ = vPosition / vPosition.w;
            lightZ = (lightZ + 1.0) / 2.0;
            bool isInShadow=    getShadowFactor(lightZ);


            if( isInShadow)
            {
                 //阴影中 仅适用环境光计算
                  finalColor = finalColor*ambient;


              }else
              {
           //给此片元颜色值
               finalColor = finalColor*ambient+finalColor*specular+finalColor*diffuse;
              }
            }else
            {
              finalColor = finalColor*ambient+finalColor*specular+finalColor*diffuse;
            }

            gl_FragColor =finalColor  ;


}