package com.opengles.book.screen.snooker;

import android.content.Context;
import com.opengles.book.framework.gl.CubeTexture;
import com.opengles.book.galaxy.ObjectDrawable;
import com.opengles.book.objects.CubeDrawer;

/**
 * 长方体对象绘制类。
 * Created by davidleen29   qq:67320337
 * on 2014-7-9.
 */
public class CuboidDrawable extends CubeDrawer implements ObjectDrawable {


    CubeTexture cubeTexture;
    public CuboidDrawable(Context context, float xLength, float yLength, float zLength,CubeTexture texture) {
        super(context, xLength, yLength, zLength);
        this.cubeTexture=texture;

    }


    @Override
    public void bind() {
        super.bind();
        cubeTexture.reload();
    }

    @Override
    public void unBind() {
        super.unBind();
        cubeTexture.dispose();
    }


    public void draw( ) {
        super.draw(cubeTexture);
    }

    @Override
    public void update(float deltaTime) {

    }
}
