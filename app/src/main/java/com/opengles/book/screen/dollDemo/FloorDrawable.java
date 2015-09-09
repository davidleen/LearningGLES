package com.opengles.book.screen.dollDemo;

import android.content.Context;

import com.giants3.android.openglesframework.framework.MatrixState;
import com.opengles.book.ShaderUtil;
import com.opengles.book.galaxy.ObjectDrawable;
import com.opengles.book.objects.RectangleViewObject;

/**
 *地板绘制类。
 * Created by davidleen29   qq:67320337
 * on 2014-7-4.
 */
public class FloorDrawable implements ObjectDrawable {


    public static final int WIDTH = 10000;
    public static final int HEIGHT = 10000;
    private RectangleViewObject planObject;
          private int floorTextureId;

    @Override
    public void bind() {
        planObject.bind();
    }

    @Override
    public void unBind() {

        planObject.unBind();

    }

    @Override
    public void draw() {


        //绘制地板
         MatrixState.pushMatrix();


        //绕x旋转90度  面板的默认是 朝前面那的。

         MatrixState.rotate(90,1,0,0);
        planObject.draw(floorTextureId);
        MatrixState.popMatrix();
    }

    @Override
    public void update(float deltaTime) {




    }
    public FloorDrawable(Context context)
    {

        this(context,WIDTH,HEIGHT);
    }

    public FloorDrawable(Context context,int width, int height)
    {





        floorTextureId = ShaderUtil.loadTextureWithUtils(context, "sky/sky.png", false);
        planObject= new RectangleViewObject(context, width, height);
    }
}
