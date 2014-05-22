package com.opengles.book.objects;

/**
 * {简单描述用途}
 * Created by davidleen29   qq:67320337
 * on 14-5-22.
 */
public class Shader {

    public String vertexShader;
    public String fragShader;
    public Attribute aPosition;
    public Attribute aNormal;
    public Uniform camera;

    public class Uniform
    {
        String name;
        int handler;

    }
    public class Attribute
    {

    }


    public void init()
    {
        camera.handler=-1;


    }
}
