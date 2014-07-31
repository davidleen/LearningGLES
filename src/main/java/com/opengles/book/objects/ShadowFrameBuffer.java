package com.opengles.book.objects;

import android.opengl.GLES20;
import com.opengles.book.framework.MVP;
import com.opengles.book.framework.gl.ViewPort;

/**
 * 阴影映射  距离检测 缓冲帮组类。
 * Created by davidleen29   qq:67320337
 * on 2014-7-15.
 */
public class ShadowFrameBuffer {


    //id 索引
    int frameIdIndex=0,renderIdIndex=1,textureIdIndex=2;

    //引用ids
    int[] bufferId=new int[3];


    private ViewPort viewPort;

    public ShadowFrameBuffer(ViewPort viewPort)
    {


        this.viewPort=viewPort;


    }


    public  void bind()
    {


            GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, bufferId[frameIdIndex]);


    }

    public  void unBind()
    {


        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER,0);


    }

    public int getTextureBufferId()
    {
        return bufferId[textureIdIndex];
    }


    /**
     *
     * @return
     */
    public int getDepthBufferId(){
        return bufferId[renderIdIndex];
    }

    public void create()
    {



//获取Renderbuffer 支持的最大的 值  所有纹理宽高必须小于这个值。
        int[] size=new int[1];
        GLES20.glGetIntegerv(GLES20.GL_MAX_RENDERBUFFER_SIZE, size, 0);

        if(viewPort.width>size[0]||viewPort.height>size[0])
        {
            throw new RuntimeException(" texWidth and texHeight must be smaller than  GL_MAX_RENDERBUFFER_SIZE ");
        }


        //id  index  0 frameId, 1 renderId 2 textureId;

        GLES20.glGenFramebuffers(1,bufferId,frameIdIndex);
        GLES20.glGenRenderbuffers(1, bufferId, renderIdIndex);
        GLES20.glGenTextures(1, bufferId, textureIdIndex);


        // bind texture and load the texture mip-level 0
        // texels are RGB565
        // no texels need to be specified as we are going to draw into
        // the texture
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,bufferId[textureIdIndex]);
        GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D,0,GLES20.GL_RGB,viewPort.width,viewPort.height,0,GLES20.GL_RGB,GLES20.GL_UNSIGNED_SHORT_5_6_5,null);

        //      GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D,0,GLES20.GL_RGBA,texWidth,texHeight,0,GLES20.GL_RGBA,GLES20.GL_UNSIGNED_SHORT_4_4_4_4,null);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_REPEAT);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_REPEAT);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);


        // bind renderbuffer and create a 16-bit depth buffer
        // width and height of renderbuffer = width and height of
        // the texture
        GLES20.glBindRenderbuffer(GLES20.GL_RENDERBUFFER, bufferId[renderIdIndex]);
        GLES20.glRenderbufferStorage(GLES20.GL_RENDERBUFFER,GLES20.GL_DEPTH_COMPONENT16,viewPort.width,viewPort.height);

        //bind the frameBuffer;
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER,bufferId[frameIdIndex]);

        //specify texture as color attachment
        GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER,GLES20.GL_COLOR_ATTACHMENT0,GLES20.GL_TEXTURE_2D,bufferId[textureIdIndex],0);


        //specify renderbuffer as depth_attachment
        GLES20.glFramebufferRenderbuffer(GLES20.GL_FRAMEBUFFER,GLES20.GL_DEPTH_ATTACHMENT,GLES20.GL_RENDERBUFFER,bufferId[renderIdIndex]);


        //check for framebuffer complete
        int status= GLES20.glCheckFramebufferStatus(GLES20.GL_FRAMEBUFFER);
        if(status==GLES20.GL_FRAMEBUFFER_COMPLETE)
        {




        }else
        {throw new RuntimeException("status:"+status+", hex:"+Integer.toHexString(status));}
    }



    public void delete()
    {


        // cleanup

        GLES20.glDeleteRenderbuffers(1, bufferId,renderIdIndex);
        GLES20.glDeleteFramebuffers(1, bufferId ,frameIdIndex);
        GLES20.glDeleteTextures(1, bufferId,textureIdIndex);
    }

}
