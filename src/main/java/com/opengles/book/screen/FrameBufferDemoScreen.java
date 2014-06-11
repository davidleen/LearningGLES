package com.opengles.book.screen;

import android.opengl.GLES20;
import com.opengles.book.framework.Game;
import com.opengles.book.framework.impl.GLScreen;

/**
 * {简单描述用途}
 * Created by davidleen29   qq:67320337
 * on 2014-6-11.
 */
public class FrameBufferDemoScreen extends GLScreen {

    private static final int texWidth=512;
    private static final int texHeight=512;


    @Override
    public void update(float deltaTime) {

    }

    @Override
    public void present(float deltaTime) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }

    public FrameBufferDemoScreen(Game game) {
        super(game);

        //获取Renderbuffer 支持的最大的 值  所有纹理宽高必须小于这个值。
        int[] size=new int[1];
        GLES20.glGetIntegerv(GLES20.GL_MAX_RENDERBUFFER_SIZE,size,0);

        if(texWidth>size[0]||texHeight>size[0])
        {
            throw new RuntimeException(" texWidth and texHeight must be smaller than  GL_MAX_RENDERBUFFER_SIZE ");
        }


        //id  index  0 frameId, 1 renderId 2 textureId;
        int[] bufferId=new int[3];
        GLES20.glGenFramebuffers(1,bufferId,0);
        GLES20.glGenRenderbuffers(1, bufferId, 1);
        GLES20.glGenTextures(1, bufferId, 2);



        // bind texture and load the texture mip-level 0
        // texels are RGB565
        // no texels need to be specified as we are going to draw into
        // the texture
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,bufferId[2]);
        GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D,0,GLES20.GL_RGB,texWidth,texHeight,0,GLES20.GL_RGB,GLES20.GL_UNSIGNED_SHORT_5_6_5,null);

        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);


        // bind renderbuffer and create a 16-bit depth buffer
        // width and height of renderbuffer = width and height of
        // the texture
        GLES20.glBindRenderbuffer(GLES20.GL_RENDERBUFFER,bufferId[1] );

        GLES20.glRenderbufferStorage(GLES20.GL_RENDERBUFFER,GLES20.GL_DEPTH_COMPONENT16,texWidth,texHeight);

        //bind the frameBuffer;
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER,bufferId[0]);

        //specify texture as color attachment
        GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER,GLES20.GL_COLOR_ATTACHMENT0,GLES20.GL_TEXTURE_2D,bufferId[2],0);

        //


    }
}
