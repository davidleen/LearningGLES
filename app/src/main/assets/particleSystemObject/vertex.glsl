uniform float uTime;//已经耗用时间
uniform vec3 centerPosition;   //粒子系统中心位置。
attribute vec3 startPosition;//起始位置
attribute vec3 endPosition;//结束位置
attribute float lifeTime;  //粒子存活时间 
varying float v_lifeTime;//传递给片元的时间参数
void main()     
{                            		

    if(uTime<=lifeTime)
    {
        gl_Position.xyz=startPosition+ (uTime * endPosition);
		gl_Position.xyz += centerPosition ;
        gl_Position.w=1.0;

    }else 
      gl_Position = vec4(-1000.0,-1000.0,0.0,0.0);
    	
    	v_lifeTime = 1.0- (uTime/lifeTime);
    	v_lifeTime = clamp(v_lifeTime,0.0,1.0);
    	gl_PointSize = v_lifeTime * v_lifeTime * 40.0;
    


}