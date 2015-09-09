package com.giants3.android.openglesframework.framework.math;


public class Sphere {
    public final Vector3 center = Vector3.create();
    public float radius;
    
    public Sphere(float x, float y, float z, float radius) {
        this.center.set(x,y,z);
        this.radius = radius;
    }
}