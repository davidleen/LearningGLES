package com.opengles.book.glsl;

import android.content.Context;
import android.graphics.*;
import android.opengl.GLES20;
import com.opengles.book.MatrixState;
import com.opengles.book.ShaderUtil;
import com.opengles.book.framework.gl.FPSCounter;
import com.opengles.book.math.Vector2;
import com.opengles.book.objects.RectangleViewObject;

/**
 * FBO 对象管理器
 * Created by davidleen29   qq:67320337
 * on 2014-6-16.
 */
public class FrameBufferManager {


    public  FrameBuffer create()
    {
        return null;
    }

    public void delete(FrameBuffer buffer)
    {

    }


    public static final class FrameBuffer
    {
        RectangleViewObject plan;
        RectangleViewObject fpsPlan;
        int frameIdIndex=0,renderIdIndex=1,textureIdIndex=2;
        int[] bufferId=new int[3];
        Canvas canvas;
        Bitmap bitmap;
        int width;
        int height;
        int testTextId;
        FPSCounter counter;
        Paint pain;
        public FrameBuffer(Context context,int width,int height)
        {
            this.width=width;
            this.height=height;
            counter = new FPSCounter();
            plan=new RectangleViewObject(context,width,height);
            fpsPlan=new RectangleViewObject(context,width/10,height/10,1f,new Vector2(0,-height/2+height/10));

            bitmap= Bitmap.createBitmap(width/10, height/10, Bitmap.Config.ARGB_8888);
            canvas=new Canvas(bitmap);
            pain=new Paint();pain.setColor(Color.RED);
            pain.setTextSize(30);


        }




        public  void create( )
        {





//获取Renderbuffer 支持的最大的 值  所有纹理宽高必须小于这个值。
            int[] size=new int[1];
            GLES20.glGetIntegerv(GLES20.GL_MAX_RENDERBUFFER_SIZE, size, 0);

            if(width>size[0]||height>size[0])
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
            GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D,0,GLES20.GL_RGB,width,height,0,GLES20.GL_RGB,GLES20.GL_UNSIGNED_SHORT_5_6_5,null);

            //      GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D,0,GLES20.GL_RGBA,texWidth,texHeight,0,GLES20.GL_RGBA,GLES20.GL_UNSIGNED_SHORT_4_4_4_4,null);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_REPEAT);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_REPEAT);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);


            // bind renderbuffer and create a 16-bit depth buffer
            // width and height of renderbuffer = width and height of
            // the texture
            GLES20.glBindRenderbuffer(GLES20.GL_RENDERBUFFER, bufferId[renderIdIndex]);
            GLES20.glRenderbufferStorage(GLES20.GL_RENDERBUFFER,GLES20.GL_DEPTH_COMPONENT16,width,height);

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


            GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER,0);


            plan.bind();
            fpsPlan.bind();
        }


        public void delete()
        {

            plan.unBind();
            fpsPlan.unBind();
            // cleanup
            //   GLES20.glDeleteRenderbuffers(1, depthRenderbuffer);
            GLES20.glDeleteRenderbuffers(1, bufferId,renderIdIndex);
            GLES20.glDeleteFramebuffers(1, bufferId ,frameIdIndex);
            //   GLES20.glDeleteFramebuffers(1, framebuffer);
            // GLES20.glDeleteTextures(1, texture);
            GLES20.glDeleteTextures(1, bufferId,textureIdIndex);
        }


        public void bind()
        {
            GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, bufferId[frameIdIndex]);
        }

        /**
         * 显示当前缓冲区内容。
         * 使用平行透视   四边形 显示frame 缓冲内容
         *
         */
        public void  show()
        {


            GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);

            GLES20.glDisable(GLES20.GL_CULL_FACE);
            //绘制视窗
            //清除深度缓冲
           // GLES20.glClearColor(0,0,0,1.0f);
            GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

            //调用此方法计算产生平行投影矩阵
            MatrixState.pushMatrix();

            MatrixState.setOrthoProject(-width/2f, width/2f, -height/2f, height/2f,1, 100);

            //缓冲区的绘制 需要调整镜头   在缓冲区图像 与正常的图像是倒转的。

                //调用此方法产生摄像机9参数位置矩阵
            //需要调整镜头
            MatrixState.setCamera(0,0 , -10, 0f, 0f, 0f, 0f, -1f,  0.0f);

            MatrixState.setInitStack();
            //fps 提示 旋转  fist apply  last effect
            MatrixState.pushMatrix();
          // MatrixState.rotate(180,0,0,1);
          //  MatrixState.rotate(180,0,1,0);
            plan.draw(bufferId[textureIdIndex]);


            MatrixState.popMatrix();



            //调用此方法产生摄像机9参数位置矩阵
            //绘制fps
            MatrixState.setCamera(0,0 , 10, 0f, 0f, 0f, 0f, 1f,  0.0f);
            if(counter.countFrame())
            {

                if(testTextId!=0)   //remove old texture
                    GLES20.glDeleteTextures(1,new int[]{testTextId},0);
                //update   redraw image  rebind to data;
                //   canvas.drawColor(Color.BLACK);
                canvas.drawColor(Color.TRANSPARENT , PorterDuff.Mode.CLEAR);
                canvas.drawText("fps:"+counter.getFps(),10,50,pain);
                testTextId= ShaderUtil.loadTextureWithUtils(bitmap, false);

            }
            //开启混合
            GLES20.glEnable(GLES20.GL_BLEND);
            //设置混合因子
            GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
//
            if(testTextId!=0)
            fpsPlan.draw(testTextId);


            GLES20.glDisable(GLES20.GL_BLEND);
            MatrixState.popMatrix();



        }

        }



}



