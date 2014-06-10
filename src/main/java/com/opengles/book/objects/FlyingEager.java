package com.opengles.book.objects;

import android.content.Context;
import com.opengles.book.galaxy.ObjectDrawable;
import com.opengles.book.objLoader.ObjModel;
import com.opengles.book.objLoader.ObjectParser;

/**
 *  flying eager that  use mix vertex technique;
 * Created by Administrator on 2014/6/9.
 */
public class FlyingEager implements ObjectDrawable {

    public FlyingEager(Context context) {
        super();
        ObjModel eager= ObjectParser.parse(context,"eager/","eager.obj");
        ObjModel eager1= ObjectParser.parse(context,"eager/","eager1.obj");
        ObjModel eager2= ObjectParser.parse(context,"eager/","eager2.obj");


    }

    @Override
    public void bind() {

    }

    @Override
    public void unBind() {

    }

    @Override
    public void draw() {

    }

    @Override
    public void update(float deltaTime) {

    }
}
