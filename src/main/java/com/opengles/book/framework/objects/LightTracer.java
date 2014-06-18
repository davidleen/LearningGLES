package com.opengles.book.framework.objects;

import com.opengles.book.LightSources;
import com.opengles.book.math.Vector3;

/**
 * 光线追踪方法类
 * Created by davidleen29   qq:67320337
 * on 2014-6-18.
 */
public class LightTracer {



    public LightTracer()
    {

    }

    public CLightSource[] sources;
    public CObject[] objects;
    float distance = 1000000; // 初始化无限大距离

    //光线递归层次
    private int TotalTraceDepth=2;

    public Vector3 cameraPosition;

    public Vector3 trace(CRay ray,int depth)
    {


        Vector3 color=null;
        for(CObject object:objects)
        {
            if(object.isIntersected(ray,distance)!=IntersectType.MISS)
            {
                Vector3 p = ray.getPoint(distance);
                Vector3 N = object.getNormal(p);
                N.nor();
                for(CLightSource source:sources)
                {

                    Vector3 ambient = source.evaluateAmbient(object.kAmbient);
                    Vector3 L = Vector3.create().set(source.position).sub(p);
                    L.nor();
                    Vector3 diffuse =source.evaluateDiffuse(N, L,object.kDiffuse);
                    Vector3 V =Vector3.create().set(cameraPosition).sub( p);
                    V.nor();
                    Vector3 specular = source.evaluateSpecular(N, L, V, object.kSpecular,object.shininess);

                    color =Vector3.create().add(ambient).add(diffuse).add(specular);

//                    // if(TotalTraceDepth == depth)
//                    return color；
//                    else
//                    {
//                        //计算射线和物体交点处的反射射线 Reflect;
//
//                        GVector3 c = Tracer(Reflect, ++depth);
//                        color += GVector3(color[0]*c[0],color[1]*c[1],color[2]*c[2]);
//                        return color；
//                    }


                    Vector3.recycle(ambient);
                    Vector3.recycle(L);
                    Vector3.recycle(diffuse);
                    Vector3.recycle(V);
                    Vector3.recycle(specular);
                }
                Vector3.recycle(p);
                Vector3.recycle(N);
            }
        }
        return color;
    }
}
