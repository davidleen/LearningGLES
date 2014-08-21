precision mediump float;

//接收从顶点着色器过来的参数
varying  vec4 vPosition;

void main()
{

  // the depth
                float normalizedDistance  = vPosition.z / vPosition.w;
                    // scale it from 0-1
                normalizedDistance = (normalizedDistance + 1.0) / 2.0;

                // bias (to remove artifacts)
           // normalizedDistance += 0.0005;

             gl_FragColor = vec4(normalizedDistance,0.0,0.0,1.0);

  }
