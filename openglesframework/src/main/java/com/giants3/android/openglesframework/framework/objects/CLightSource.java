package com.giants3.android.openglesframework.framework.objects;

import com.giants3.android.openglesframework.framework.math.Vector3;

/**
 * 光源对象
 * Created by davidleen29   qq:67320337
 * on 2014-6-18.
 */
public abstract class CLightSource {


    public Vector3 position;
    public Vector3 kAmbient;
    public Vector3 kDiffuse;
    public Vector3 kSpecular;


    /**
     * 计算环境光
     * @param materialAmbient
     * @return
     */
    public abstract     Vector3 evaluateAmbient(Vector3  materialAmbient);

    /**
     * 计算散射光
     * @param light
     * @param normal
     * @param materialDiffuse
     * @return
     */
    public abstract     Vector3 evaluateDiffuse(Vector3 light, Vector3 normal,Vector3 materialDiffuse);

    /**
     * 计算镜面反射光
     * @param normal
     * @param light
     * @param camera
     * @param materialSpecular
     * @param _shininess
     * @return
     */
    public abstract     Vector3 evaluateSpecular( Vector3 normal, Vector3 light,Vector3 camera,
                                                  Vector3 materialSpecular,   float _shininess);

    /**
     * 计算光照效果
     * @param normal
     * @param light
     * @param camera


     * @param ambientOut
     * @param diffuseOut
     * @param specularOut
     */
    public abstract void evaluate( Vector3 normal, Vector3 light,Vector3 camera,
                          CObject material,Vector3 ambientOut,Vector3 diffuseOut,Vector3 specularOut);

}
