precision mediump float;
uniform    vec3 uLightLocation;//光源位置。
//接收从顶点着色器过来的参数
varying vec4 vPosition;

void main()                         
{    


   float dis=distance(vPosition.xyz,uLightLocation);
   //将距离转换成颜色值  转成纹理图
   //整数部分
    float integerPart=floor(dis);
    //小数部分
    float fragPart=fract(dis);


    float r=floor(integerPart/256.0);
    float g=mod(integerPart,256.0);

    //放大小数部分取整
    fragPart=floor(fragPart*1024.0);
    float b=floor(fragPart/256.0);
    float a=mod(fragPart,256.0);

    //压缩至0-1 间值
    r=r/256.0;
    g=g/256.0;
    b=b/256.0;
      a=a/256.0;



   // 片元颜色值
   gl_FragColor = vec4(r,g,b,a);

}   