precision mediump float;


//接收从顶点着色器过来的参数
varying  vec4 vPosition;
// taken from Fabien Sangalard's DEngine - this is used to pack the depth into a 32-bit texture (GL_RGBA)
vec4 pack (float depth)
{
    const vec4 bitSh = vec4(256.0 * 256.0 * 256.0,
                       256.0 * 256.0,
                       256.0,
                      1.0);
    const vec4 bitMsk = vec4(0,
                         1.0 / 256.0,
                         1.0 / 256.0,
                             1.0 / 256.0);
    vec4 comp = fract(depth * bitSh);
    comp -= comp.xxyz * bitMsk;
    return comp;
}
void main()
{
             // the depth
                float normalizedDistance  = vPosition.z / vPosition.w;
                    // scale it from 0-1
                normalizedDistance = (normalizedDistance + 1.0) / 2.0;

                // bias (to remove artifacts)
           // normalizedDistance += 0.0005;

             gl_FragColor = vec4(normalizedDistance);


                // pack value into 32-bit RGBA texture
            //   gl_FragColor = pack(normalizedDistance);







  }
