package com.opengles.book.framework.objects;

import com.opengles.book.math.Vector3;

/**
 * 聚光灯光源
 * Created by davidleen29   qq:67320337
 * on 2014-6-18.
 */
public class CSpotLight extends  CLightSource {

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
}
