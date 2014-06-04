uniform float uTime;//已经耗用时间
uniform vec3 centerPosition;   //粒子系统中心位置。
attribute vec3 startPosition;//起始位置
attribute vec3 endPosition;//结束位置
attribute float lifeTime;  //粒子存活时间
varying vec2 vTextureCoord;  //用于传递给片元着色器的变量
varying float v_lifeTime;//传递给片元的时间参数
void main()     
{                            		

    if(uTime<=lifeTime)
    {
        gl_Position.xyz=startPosition+(uTime* endPosition);

        gl_Position.w=1.0;

    }


}