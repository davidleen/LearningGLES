package com.opengles.book.screen.dollDemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;
import com.opengles.book.MatrixState;
import com.opengles.book.ShaderUtil;
import com.opengles.book.galaxy.ObjectDrawable;
import com.opengles.book.objects.RectangleViewObject;

import java.lang.ref.SoftReference;
import java.util.HashMap;

/**
 *地板绘制类。
 * Created by davidleen29   qq:67320337
 * on 2014-7-4.
 */
public class FloorDrawable implements ObjectDrawable {


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


         MatrixState.rotate(90,1,0,0);
        planObject.draw(floorTextureId);
        MatrixState.popMatrix();
    }

    @Override
    public void update(float deltaTime) {

    }


    public FloorDrawable(Context context)
    {





        floorTextureId = ShaderUtil.loadTextureWithUtils(context, "sky/sky.png", false);
        planObject= new RectangleViewObject(context, 10000, 10000);
    }
}
