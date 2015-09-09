package com.giants3.android.openglesframework.framework.objects;

import com.giants3.android.openglesframework.framework.math.Vector3;

/**
 * 点光源
 * Created by davidleen29   qq:67320337
 * on 2014-6-18.
 */
public class CPointLight  extends  CLightSource {

    @Override
    public Vector3 evaluateAmbient(Vector3 materialAmbient) {
        return null;
    }

    @Override
    public Vector3 evaluateDiffuse(Vector3 light, Vector3 normal, Vector3 materialDiffuse) {
        return null;
    }

    @Override
    public Vector3 evaluateSpecular(Vector3 normal, Vector3 light, Vector3 camera, Vector3 materialSpecular, float _shininess) {
        return null;
    }

    @Override
    public void evaluate(Vector3 normal, Vector3 light, Vector3 camera, CObject material, Vector3 ambientOut, Vector3 diffuseOut, Vector3 specularOut) {

    }
}
