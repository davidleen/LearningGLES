package com.opengles.book.framework.objects;

import com.opengles.book.math.Vector3;

/**
 * 平行光源
 * Created by davidleen29   qq:67320337
 * on 2014-6-18.
 */
public class CDirectionalLight extends CLightSource {


    @Override
    public Vector3 evaluateAmbient(Vector3 materialAmbient) {
       Vector3 temp= Vector3.create();
          Vector3.mul(kAmbient,materialAmbient,temp);
        return temp;
    }

//    漫反射的计算稍微比环境光复杂，漫反射的计算公式为
//
//            diffuse = Id•Kd• (N•L)
//
//    其中，Id是光源的漫反射成分，Kd是物体的漫反射系数，N是法线，L是入射光向量。
    //diffuse =
    public Vector3 evaluateDiffuse(Vector3 light, Vector3 normal,Vector3 materialDiffuse) {

        Vector3 IdKd= Vector3.create();
        Vector3.mul(kDiffuse,materialDiffuse,IdKd);

        float NdotL =Math.max (Vector3.dotValue(normal,light), 0.0f);
        return IdKd.mul(NdotL);



    }
//镜面反射的计算又比环境光要复杂，镜面反射的计算公式为
//
//    specular = Is•Ks• (V·R)n
//            其中
//    R = 2(L•N) •N-L
//
//    Is是光源镜面反射成分，Ks是物体的镜面反射系数，V是相机方向向量，R是反射向量，­n­就反射强度Shininess。为了提高计算效率，也可以利用HalfVector H来计算镜面反射。
//
//    specular = Is•Ks• (N•H)n
//            其中
//    H=(L+V)/2
//
//    计算H要比计算反射向量R要快得多。
    @Override
    public Vector3 evaluateSpecular(  Vector3 normal, Vector3 light,Vector3 camera,
                                      Vector3 materialSpecular,   float _shininess) {
        Vector3 IsKs = Vector3.create();
        Vector3.mul(kSpecular, materialSpecular,IsKs);

        Vector3 HVector=Vector3.create().set(light).add(camera).mul(0.5f).nor();

        float NdotL = Math.max(Vector3.dotValue(normal, light), 0.0f);
        float NdotH;
        if(NdotL<=0.0f)
            NdotH = 0.0f;
        else
           NdotH = (float)Math.pow(Math.max(Vector3.dotValue(normal,HVector), 0.0f) , _shininess);

        //回收
        Vector3.recycle(HVector);

        return IsKs.mul(NdotH);
    }


    /**
     * 计算光照效果
     * @param normal
     * @param light
     * @param camera


     * @param ambientOut   环境光 返回
     * @param diffuseOut   散射光返回
     * @param specularOut  镜面光返回
     */
    @Override
    public void evaluate( Vector3 normal, Vector3 light,Vector3 camera,
                         CObject material,Vector3 ambientOut,Vector3 diffuseOut,Vector3 specularOut)
    {


        //计算环境光


        Vector3.mul(kAmbient,material.kAmbient,ambientOut);




        //计算漫射光


        Vector3.mul(kDiffuse,material.kDiffuse,diffuseOut);

        float NdotL =Math.max (Vector3.dotValue(normal,light), 0.0f);
        diffuseOut.mul(NdotL) ;




        //计算反射光

        Vector3.mul(kSpecular, material.kSpecular,specularOut);

        //半法向量
        Vector3 HVector=Vector3.create().set(light).add(camera).nor();
        float powerFactor=Math.max(0.0f,(float)Math.pow(Vector3.dotValue(normal,HVector),material.shininess)); 	//镜面反射光强度因子
        //回收
        Vector3.recycle(HVector);
        specularOut.mul(powerFactor);

    }
}
