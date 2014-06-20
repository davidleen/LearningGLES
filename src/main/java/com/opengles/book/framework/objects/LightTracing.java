package com.opengles.book.framework.objects;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Log;
import com.opengles.book.math.Vector3;

import java.util.ArrayList;
import java.util.List;

/**
 * {简单描述用途}
 * Created by davidleen29   qq:67320337
 * on 2014-6-19.
 */
public class LightTracing {

    //默认背景黑色
    Vector3 background=Vector3.create().set(1,1,1) ;
    int width; int height;
    int z=0;
    Vector3 camera;//相机位置

    private int totalTraceDepth=1;

    public CLightSource[] sources;
    public List<CObject> objects=new ArrayList<CObject>();
    public LightTracing(int width, int height,Vector3 camera)
    {
        this.width=width;
        this.height=height;
        this.camera=Vector3.create().set(camera);

        CLightSource cLightSource=new CDirectionalLight();
        cLightSource.position=Vector3.create().set(0,1000,0);
        cLightSource.kAmbient=Vector3.create().set(1,1,1);
        cLightSource.kDiffuse=Vector3.create().set(1,1,1);
        cLightSource.kSpecular=Vector3.create().set(1,1,1);

        sources=new CLightSource[]{cLightSource};


        //空间构建
        CSphere object=new CSphere();
        //球体  红色
        object.center=Vector3.create().set(0,0,-100);
        object.radius=300;
        object.color=Vector3.create().set(1,0,0);

        object.kAmbient=Vector3.create().set(0.15f,0.15f,0.15f);
        object.kDiffuse=Vector3.create().set(0.5f,0.5f,0.23f);
        object.kSpecular=Vector3.create().set(0.3f,0.3f,0.15f);
        object.shininess=10;

         objects.add(object);
//        //球体  黄色
//        object=new CSphere();
//        object.center=Vector3.create().set(0,100,-200);
//        object.radius=90;
//        object.color=Vector3.create().set(1,1,0);
//
//        object.kAmbient=Vector3.create().set(0.15f,0.15f,0.15f);
//        object.kDiffuse=Vector3.create().set(0.5f,0.5f,0.23f);
//        object.kSpecular=Vector3.create().set(0.3f,0.3f,0.15f);
//
//        //球体 绿色
//        object=new CSphere();
//        object.center=Vector3.create().set(100,0,-80);
//        object.radius=80;
//        object.color=Vector3.create().set(0,1,0);
//
//        object.kAmbient=Vector3.create().set(0.15f,0.15f,0.15f);
//        object.kDiffuse=Vector3.create().set(0.5f,0.5f,0.23f);
//        object.kSpecular=Vector3.create().set(0.3f,0.3f,0.15f);
//
//        objects.add(object);


        //开始光线追踪
        trace();
    }

    public Bitmap trace( )
    {
          CRay ray=new CRay();
        Bitmap  bitmap= Bitmap.createBitmap(width,height, Bitmap.Config.RGB_565);

        int halfWidth=width/2;
        int halfHeight=height/2;


        for(int x=-halfWidth;x<halfWidth;x++) {
            for (int y = -halfHeight; y < halfHeight; y++) {


                calculatePrimaryRay(x, y, ray); // Ray or iginat ing from the

                if(x==0&& y==0)
                {
                   Log.e("TAG","x");
                }

                 Vector3    color = rayTrace(ray, 1); // Start Recursion for this
                writePixel(bitmap,x+halfWidth, y+halfHeight, color); // write Pixel to Output Buffer
                Vector3.recycle(color);
            }
        }

        return bitmap;
    }

    public Vector3 rayTrace(CRay ray,int depth)
    {


        Vector3 color=Vector3.create().set(background);
        Vector3 intersectPoint=Vector3.create();
        for(CObject object:objects)
        {

            if(object.isIntersected(ray,intersectPoint)!=IntersectType.MISS)
            {
                Vector3 p =intersectPoint;
                Vector3 N = object.getNormal(p).nor();

                //视线向量
                Vector3 viewVector =Vector3.create().set(camera).sub( p).nor();
                for(CLightSource source:sources)
                {


                    Vector3 L=Vector3.create().set(source.position).sub(intersectPoint).nor();
                    Vector3 ambient=Vector3.create();
                    Vector3 diffuse=Vector3.create();
                    Vector3 specular=Vector3.create();

                    source.evaluate(N,L,camera,object,ambient,diffuse,specular);


                    //颜色值累加
//                    color.add(ambient).add(diffuse).add(specular);
                    Vector3 tempAmbient=Vector3.mul(object.color, ambient);

                    Vector3 tempDiffuse=Vector3.mul(object.color, diffuse);

                    Vector3 tempSpecular=Vector3.mul(object.color, specular);


                    Vector3.recycle(ambient);

                    Vector3.recycle(diffuse);

                    Vector3.recycle(specular);


                    color.set(tempAmbient).add(tempDiffuse).add(tempSpecular);
                    Vector3.recycle(tempAmbient);

                    Vector3.recycle(tempDiffuse);

                    Vector3.recycle(tempSpecular);

                   if(totalTraceDepth > depth)
                   {
                        //计算射线和物体交点处的反射射线 Reflect;
                        CRay reflect=new CRay();
                        Vector3 c = rayTrace(reflect, ++depth);
                        color.add(Vector3.mul(color, c));

                    }



                    Vector3.recycle(L);
                }
                Vector3.recycle(viewVector);
                Vector3.recycle(p);
                Vector3.recycle(N);
            }



        }
        Vector3.recycle(intersectPoint);
        return color;
    }
    private void writePixel(Bitmap bitmap,int x, int y, Vector3 color) {






            bitmap.setPixel(x,y,Color.rgb((int) (color.x * 255), (int) (color.y * 255), (int) (color.z * 255)));
    }

    private void calculatePrimaryRay(int x, int y, CRay ray) {



        ray.origin=camera;
        ray.direction=Vector3.create(x,y,0).sub(camera).nor();

    }


}
