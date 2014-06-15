package com.opengles.book.screen;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.util.DisplayMetrics;
import com.opengles.book.LightSources;
import com.opengles.book.MatrixState;
import com.opengles.book.R;

import com.opengles.book.framework.Game;
import com.opengles.book.framework.Input;
import com.opengles.book.framework.gl.LookAtCamera;
import com.opengles.book.framework.impl.GLScreen;
import com.opengles.book.galaxy.CameraController;
import com.opengles.book.objects.NewFlutterFlag;
import com.opengles.book.objects.NewSky;
import com.opengles.book.objects.ObjObject;
import com.opengles.book.objects.TwistCuboid;

import java.util.List;

/**
 * {简单描述用途}
 * Created by davidleen29   qq:67320337
 * on 2014-6-11.
 */
public class FrameBufferDemoScreen extends GLScreen {

    private static   int texWidth=512;
    private static   int texHeight=512;
    int frameIdIndex=0,renderIdIndex=1,textureIdIndex=2;
    int[] bufferId=new int[3];
   // TwistCuboid cuboid;


    NewSky obj;
    private LookAtCamera camera;
    CameraController cameraController;

    private int textureId;
    NewFlutterFlag flag;


    @Override
    public void update(float deltaTime) {
        List<Input.TouchEvent> touchEvents = glGame.getInput().getTouchEvents();
        cameraController.onTouchEvent(touchEvents);
    }

    @Override
    public void present(float deltaTime) {
        camera.setMatrices();

        //   GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER,0);
        //clear color and  depth buffer;

        //获取Renderbuffer 支持的最大的 值  所有纹理宽高必须小于这个值。
        int[] size=new int[1];
        GLES20.glGetIntegerv(GLES20.GL_MAX_RENDERBUFFER_SIZE,size,0);

        if(texWidth>size[0]||texHeight>size[0])
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
      GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D,0,GLES20.GL_RGB,texWidth,texHeight,0,GLES20.GL_RGB,GLES20.GL_UNSIGNED_SHORT_5_6_5,null);

        //      GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D,0,GLES20.GL_RGBA,texWidth,texHeight,0,GLES20.GL_RGBA,GLES20.GL_UNSIGNED_SHORT_4_4_4_4,null);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_REPEAT);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_REPEAT);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);




        // bind renderbuffer and create a 16-bit depth buffer
        // width and height of renderbuffer = width and height of
        // the texture
        GLES20.glBindRenderbuffer(GLES20.GL_RENDERBUFFER, bufferId[renderIdIndex]);
        GLES20.glRenderbufferStorage(GLES20.GL_RENDERBUFFER,GLES20.GL_DEPTH_COMPONENT16,texWidth,texHeight);

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

            //render to texture using fbo;



        }else
        {throw new RuntimeException("status:"+status+", hex:"+Integer.toHexString(status));}


      //  GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER,bufferId[frameIdIndex]);

        GLES20.glClearColor(0,0,0,1.0f);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        // Set the active texture unit to texture unit 0.


       obj.draw( textureId);

        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER,0);
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

      // GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,bufferId[textureIdIndex]);

        MatrixState.pushMatrix();
     //   MatrixState.translate(5,0,0);
        //     obj.draw( textureId);
     // obj.draw( bufferId[textureIdIndex]);

        flag.draw( bufferId[textureIdIndex]);
        MatrixState.popMatrix();



        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
        // cleanup
        //   GLES20.glDeleteRenderbuffers(1, depthRenderbuffer);
        GLES20.glDeleteRenderbuffers(1, bufferId,renderIdIndex);
        GLES20.glDeleteFramebuffers(1, bufferId ,frameIdIndex);
        //   GLES20.glDeleteFramebuffers(1, framebuffer);
        // GLES20.glDeleteTextures(1, texture);
        GLES20.glDeleteTextures(1, bufferId,textureIdIndex);
    }

    @Override
    public void pause() {

        obj.unBind();
        flag.unBind();

    }


    @Override
    public void resume() {




//
        int width = texWidth;
        int height = texHeight                ;
        GLES20.glViewport(0, 0, width, height);
        float ratio = (float) width / height;







        //  textureId=  ShaderUtil.loadTextureWithUtils(game.getContext(),"sky/sky.png",false);

        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        GLES20.glEnable(GLES20.GL_CULL_FACE);



        // 设置光线位置
        LightSources.setSunLightPosition(1000, 1, 0);
        // 设置 三种光线
        LightSources.setAmbient(0.15f, 0.15f, 0.15f, 1f);
        LightSources.setDiffuse(0.5f, 0.5f, 0.25f, 1f);
        LightSources.setSpecLight(0.3f, 0.3f, 0.15f, 1f);




        camera=new LookAtCamera(2,1/ratio,1 ,1000);
        camera.setPosition(0.0f,0f, 20f);
        camera.setUp(0,1,0);
        camera.setLookAt(0f,0f,0f) ;
        cameraController=new CameraController(camera, glGame.getGLGraphics());



        obj.bind();
        flag.bind();
     //   cuboid.bind();

        // Enable texture mapping
        GLES20.glEnable(GLES20.GL_TEXTURE_2D);

    }

    @Override
    public void dispose() {

    }

    public FrameBufferDemoScreen(Game game) {
        super(game);

         DisplayMetrics metrics= game.getContext().getResources().getDisplayMetrics();
         texWidth= metrics.widthPixels;
         texHeight=metrics.heightPixels;
        obj=new NewSky(game.getContext());
       // cuboid=new TwistCuboid(game.getContext());

        Bitmap bitmap=null;
//                bitmap=Bitmap.createBitmap(texWidth,texHeight, Bitmap.Config.RGB_565);
//        Canvas canvas=new Canvas(bitmap);
//        canvas.drawColor(Color.RED);
//        canvas.drawBitmap(BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.icon),texWidth/2,texHeight/2,null);

        bitmap=BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.icon);
        textureId=    loadTextureWithUtils(bitmap);
        flag=new NewFlutterFlag(game.getContext());

    }



    public static int loadTextureWithUtils(Bitmap bitmap )
    {
        int[] textureId = new int[1];

        GLES20.glGenTextures(1, textureId, 0);


        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId[0]);

        //使用MipMap线性纹理采样
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,
                GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
        //使用MipMap最近点纹理采样
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,
                GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);



        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S,
                GLES20.GL_REPEAT);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T,
                GLES20.GL_REPEAT);


        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);



        return textureId[0];

    }
}
