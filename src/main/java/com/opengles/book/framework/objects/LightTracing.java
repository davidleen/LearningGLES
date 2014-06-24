package com.opengles.book.framework.objects;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;
import com.opengles.book.framework.gl.LookAtCamera;
import com.opengles.book.math.Vector2;
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
    Vector3 background=Vector3.create().set(0,0,0) ;
    int width; int height;
    int z=0;
    LookAtCamera camera;//相机位置

    float  planShines=100;


    private int totalTraceDepth=2;

    public CLightSource[] sources;
    public List<CObject> objects=new ArrayList<CObject>();
    public LightTracing(int width, int height,LookAtCamera camera)
    {
        this.width=width;
        this.height=height;
this.camera=camera;


        CLightSource aLightSource=new CDirectionalLight();
        aLightSource.position=Vector3.create().set(10000,10000,10000);
        aLightSource.kAmbient=Vector3.create().set(1,1,1);
        aLightSource.kDiffuse=Vector3.create().set(1,1,1);
        aLightSource.kSpecular=Vector3.create().set(1,1,1);


        CLightSource    bLightSource=new CDirectionalLight();
        bLightSource.position=Vector3.create().set(0,-10000,0);
        bLightSource.kAmbient=Vector3.create().set(1,1,1);
        bLightSource.kDiffuse=Vector3.create().set(1,1,1);
        bLightSource.kSpecular=Vector3.create().set(1,1,1);
       // ,bLightSource
        sources=new CLightSource[]{aLightSource };


        //空间构建
        CSphere object=null;

        object=new CSphere();
        //球体  红色
        object.center=Vector3.create().set(0,0,0f);
        object.radius=100f;
        object.color=Vector3.create().set(1,0,0);

        object.kAmbient=Vector3.create().set(0.15f,0.15f,0.15f);
        object.kDiffuse=Vector3.create().set(0.5f,0.5f,0.35f);
        object.kSpecular=Vector3.create().set(0.3f,0.3f,0.15f);
        object.shininess=1;

          objects.add(object);
        //球体  绿色
        object=new CSphere();
        object.center=Vector3.create().set(200,0,5);
        object.radius=50;
        object.color=Vector3.create().set(0,1,0);

        object.kAmbient=Vector3.create().set(0.15f,0.15f,0.15f);
        object.kDiffuse=Vector3.create().set(0.5f,0.5f,0.23f);
        object.kSpecular=Vector3.create().set(0.3f,0.3f,0.15f);
        object.shininess=1;
          objects.add(object);

        //球体 蓝色
        object=new CSphere();
        object.center=Vector3.create().set(-300,0,0);
        object.radius=80;
        object.color=Vector3.create().set(0,0,1);

        object.kAmbient=Vector3.create().set(0.15f,0.15f,0.15f);
        object.kDiffuse=Vector3.create().set(0.5f,0.5f,0.23f);
        object.kSpecular=Vector3.create().set(0.3f,0.3f,0.15f);
        object.shininess=1;

     objects.add(object);









        CSquare  square=null;

        square=new CSquare();
        //添加后面板
        square.normal=Vector3.create(0,0,1);
        square.d= - 400;
        square.min=Vector3.create(-400,-400,-400);
        square.max=Vector3.create(400,400,-400);
        square.color=Vector3.create().set(0,0.4f,0.4f);
        configPlanLightFactor(square);
        objects.add(square);
//
//
//        //添加左边面板
           square=new CSquare();
        square.normal=Vector3.create( 1,0,0);
        square.d=  -400;
        square.min=Vector3.create(-400,-400,-400);
        square.max=Vector3.create(-400,400,400);
        square.color=Vector3.create().set(1f,0f,0f);
        configPlanLightFactor(square);

           objects.add(square);

        //添加右边面板
        square=new CSquare();
        square.normal=Vector3.create( 1,0,0);
        square.d=400;
        square.min=Vector3.create(400,-400,-400);
        square.max=Vector3.create(400,400,400);
        square.color=Vector3.create().set(1f,1f,1f);
        configPlanLightFactor(square);

        objects.add(square);


        //添加上方面板
        square=new CSquare();
        square.normal=Vector3.create(0, 1,0);
        square.d=200;
        square.min=Vector3.create(-400,200,-400);
        square.max=Vector3.create(400,200,400);
        square.color=Vector3.create().set(1f,0.6f,0.6f);
        configPlanLightFactor(square);
         objects.add(square);


        //添加下方面板
        square=new CSquare();
        square.normal=Vector3.create(0, 1,0);
        square.d=-200;
        square.min=Vector3.create(-400,-200,-400);
        square.max=Vector3.create(400,-200,400);
        square.color=Vector3.create().set(1f,0f,1f);
        configPlanLightFactor(square);

        objects.add(square);





        //开始光线追踪
        trace();
    }

    public Bitmap trace( )
    {
          CRay ray= CRay.createCRay();
        Bitmap  bitmap= Bitmap.createBitmap(width,height, Bitmap.Config.ARGB_8888);

        int halfWidth=width/2;
        int halfHeight=height/2;


        for(int x=-halfWidth;x<halfWidth;x++) {
            for (int y = -halfHeight; y < halfHeight; y++) {



                calculatePrimaryRay(x, y, ray); // Ray or iginat ing from the


                 Vector3    color = rayTrace(ray, 1); // Start Recursion for this
                writePixel(bitmap,x+halfWidth, y+halfHeight, color); // write Pixel to Output Buffer
                Vector3.recycle(color);
            }
        }

        CRay.recycle(ray);

        return bitmap;
    }

    public Vector3 rayTrace(CRay ray,int depth)
    {


        Vector3 color=Vector3.create().set(0,0,0);

        IntersectInfo info=null;


        for(CObject object:objects)
        {
//            boolean replaced=false;
            Vector3 intersectPoint=Vector3.create();
            if(object.isIntersected(ray,intersectPoint)!=IntersectType.MISS) {
//                replaced=true;
                if(info==null)
                {

                    info=new IntersectInfo();

                }



                if(info.intersectPoint==null)
                {
                    info.object=object;
                    info.intersectPoint=Vector3.create().set(intersectPoint);


                }else {
                    //calculate  the closest  only the closest point will draw

                    float oldLength = info.intersectPoint.distSquared(ray.origin);
                    float newLength = intersectPoint.distSquared(ray.origin);
                    if (newLength <= oldLength) {

                        info.intersectPoint.set(intersectPoint);
                        info.object = object;
                    }
//                    else
//                    {
//                        replaced=false;
//                    }
                    //否则 丢弃

                }





            }






            Vector3.recycle(intersectPoint);
        }



    if(info!=null)
    {


        //calculate  this point is inShadow;
        Vector3 N = info.object.getNormal(info.intersectPoint).nor();

        //视线向量
        Vector3 viewVector = Vector3.create().set(camera.getPosition()).sub(info.intersectPoint).nor();



        for (CLightSource source : sources) {



            CRay lightRay=CRay.createCRay();
            lightRay.origin.set(info.intersectPoint).sub(ray.direction);
            lightRay.direction.set(source.position).sub(info.intersectPoint);
            //遍历所有物体 检测 该点 是否被其他物体遮挡。
            boolean    isInShow=false;
            for(CObject showTestObject :objects)
            {

                isInShow  =showTestObject.isInShadow(lightRay);
                if(isInShow) break;
            }


            CRay.recycle(lightRay);


            //  计算其是否在该光线阴影中

            Vector3 L = Vector3.create().set(source.position).sub(info.intersectPoint).nor();
            //如果是在阴影中  仅使用环境光亮计算。 也不计算反射光线
            if(isInShow) {

                Vector3 ambient = Vector3.create().set(info.object.kAmbient);
                Vector3.mul(info.object.color, ambient, ambient);
                color.add(ambient) ;
                Vector3.recycle(ambient);
            }else {




                Vector3 finalColor=Vector3.create().set(info.object.color);

                //颜色累加  反射的颜色。
                Vector3 reflectColor=null;
                if (totalTraceDepth > depth) {
                    //计算射线和物体交点处的反射射线 Reflect;
                    CRay reflect = CRay.createCRay();

                    reflect.origin.set(info.intersectPoint);//.sub(ray.direction);
                    //   Vector3 inDirection=Vector3.create().set(info.intersectPoint).sub(ray.origin).nor();
                    if (calculateReflectVector(ray.direction, N, reflect.direction)) {
                        //   reflect.direction.set(N).mul(2).add(ray.direction).nor();
                          reflectColor = rayTrace(reflect, ++depth);



                    }
                    CRay.recycle(reflect);
                }






                //反射光的通道计算
                if(reflectColor!=null) {
//                    Vector3.mul(reflectColor, ambient, tempAmbient);
//                    Vector3.mul(reflectColor, diffuse, tempDiffuse);
//                    Vector3.mul(reflectColor, specular, tempSpecular);
//
//                    reflectColor.set(0, 0, 0).add(tempAmbient).add(tempDiffuse).add(tempSpecular);


                    //合并颜色

                    Vector3.mul(reflectColor, finalColor, reflectColor);

                    finalColor.add(reflectColor);



                    Vector3.recycle(reflectColor);

                    restrictColor(finalColor);

                }


                Vector3 ambient = Vector3.create();
                Vector3 diffuse = Vector3.create();
                Vector3 specular = Vector3.create();

                source.evaluate(N, L, camera.getPosition(), info.object, ambient, diffuse, specular);


                //颜色值累加
//                    color.add(ambient).add(diffuse).add(specular);
                Vector3 tempAmbient = Vector3.create();
                Vector3.mul(finalColor, ambient, tempAmbient);

                Vector3 tempDiffuse = Vector3.create();
                Vector3.mul(finalColor, diffuse, tempDiffuse);

                Vector3 tempSpecular = Vector3.create();
                Vector3.mul(finalColor, specular, tempSpecular);







                Vector3.recycle(ambient);

                Vector3.recycle(diffuse);

                Vector3.recycle(specular);

                //本色通道计算
                color.add(tempAmbient).add(tempDiffuse).add(tempSpecular);


                Vector3.recycle(tempAmbient);

                Vector3.recycle(tempDiffuse);

                Vector3.recycle(tempSpecular);





                Vector3.recycle(finalColor);




             }






            Vector3.recycle(L);
        }
        Vector3.recycle(viewVector);

        Vector3.recycle(N);
        if(info.intersectPoint!=null)
        Vector3.recycle(info.intersectPoint);
    }
        //adjust the color  for 1 as the max value
        restrictColor(color);

        return color;
    }
    private void writePixel(Bitmap bitmap,int x, int y, Vector3 color) {






            bitmap.setPixel(x,y,Color.rgb((int) (color.x * 255), (int) (color.y * 255), (int) (color.z * 255)));
    }

    private void calculatePrimaryRay(int x, int y , CRay ray) {



        ray.origin.set(camera.getPosition());
        ray.direction.set(camera.getLookAt()).sub(camera.getPosition()).nor().mul(camera.near).add(x,y,0).nor();

    }


    /**
     *  R=I-2(I*N)N)
     * @param in
     * @param normal
     * @param reflect
     */
    private boolean   calculateReflectVector(Vector3 in, Vector3 normal, Vector3 reflect)
    {


       float value= Vector3.dotValue(in, normal);
      //  if(value>0) return false;
        Vector3 temp
                =Vector3.create();
        temp.set(normal).mul(2).mul(value);
        reflect.set(in).sub(temp);
        Vector3.recycle(temp);

        return true;

    }



    private boolean  isInShow(CRay ray,Vector3 lightPosition)
    {



        return false;
    }



    private void configPlanLightFactor(CPlan plan)
    {
        //  square.color=Vector3.create().set(0,0.4f,0.4f);
        plan.kAmbient=Vector3.create() .set(0.2f,0.2f,0.1f);
        plan.kDiffuse=Vector3.create() .set(0.3f,0.3f,0.15f);
        plan.kSpecular=Vector3.create() .set(0.5f,0.5f,0.35f);
        plan.shininess=planShines;
    }


    /**
     * 限制颜色  不至于溢出
     */
    private  void restrictColor(Vector3 color)
    {
        color.set(Math.min(color.x, 1), Math.min(color.y, 1), Math.min(color.z, 1));
    }


}
