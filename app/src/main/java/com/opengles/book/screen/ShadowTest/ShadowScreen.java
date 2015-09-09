package com.opengles.book.screen.ShadowTest;


import android.opengl.GLES20;

import com.giants3.android.openglesframework.framework.Game;
import com.giants3.android.openglesframework.framework.Input;
import com.giants3.android.openglesframework.framework.gl.Camera3D;
import com.giants3.android.openglesframework.framework.gl.ProjectInfo;
import com.giants3.android.openglesframework.framework.gl.ViewPort;
import com.giants3.android.openglesframework.framework.impl.GLScreen;
import com.opengles.book.LightSources;
import com.giants3.android.openglesframework.framework.MatrixState;


import com.giants3.android.openglesframework.framework.math.Vector3;
import com.opengles.book.objLoader.ObjModel;
import com.opengles.book.objLoader.ObjectParser;
import com.opengles.book.objects.ObjObject;
import com.opengles.book.objects.ShadowFrameBuffer;

import java.util.List;


/**
 * 阴影案例场景     球体  柱子  平面  墙体。 茶壶
 * Created by davidleen29   qq:67320337
 * on 2014-7-30.
 */
public class ShadowScreen extends GLScreen {


     Vector3 lightPosition=Vector3.create(0,20,3);



    //阴影
    private ShadowFrameBuffer buffer;
    //阴影视窗。
    private ProjectInfo shadowProject;
    private Camera3D shadowCamera;
    private ProjectInfo projectInfo;
    private Camera3D camera3D;
    private ViewPort viewPort;




    ObjModel teapotModel;

    ShadowObjObject teapotShadow;
    ObjObjectWithShadow  teapotObj;
    CameraController controller;

    ObjModel sphereModel;

    ShadowObjObject sphereShadow;
    ObjObjectWithShadow  sphereObj;



    ObjModel cubeModel;
    ShadowObjObject cubeShadow;
    ObjObjectWithShadow  cubeObj;

    ObjObject objObject;


    public ShadowScreen(Game game) {
        super(game);






        createMVP();
        buffer=new ShadowFrameBuffer(viewPort);
        teapotModel= ObjectParser.parse(game.getContext(),"snooker_ball/","desk.obj");
        teapotShadow=new ShadowObjObject(game.getContext(),teapotModel);
        teapotObj=new ObjObjectWithShadow(game.getContext(),teapotModel);


        sphereModel= ObjectParser.parse(game.getContext(),"sphere/","sphere.obj");
        sphereShadow=new ShadowObjObject(game.getContext(),sphereModel);
        sphereObj=new ObjObjectWithShadow(game.getContext(),sphereModel);

       cubeModel= ObjectParser.parse(game.getContext(), "","cube.obj");
        cubeShadow=new ShadowObjObject(game.getContext(), cubeModel);
        cubeObj=new ObjObjectWithShadow(game.getContext(), cubeModel);



        objObject=new ObjObject(game.getContext(), "","cube.obj");

    }



    private static float SUN_STEP=0.2f;
    private float timeCollapsed=0.0f;
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        timeCollapsed+=deltaTime;
        if(timeCollapsed>SUN_STEP)
        {

            timeCollapsed-=SUN_STEP;
            lightPosition.x+=1;
            if(lightPosition.x>30)
            {
                lightPosition.x=-30;
            }


             updateRelateData();
        }




        List<Input.TouchEvent> touchEvents =
                glGame.getInput().getTouchEvents();


        for (Input.TouchEvent event:touchEvents) {

            controller.onTouchEvent(event);


        }





    }

    @Override
    public void present(float deltaTime) {
        super.present(deltaTime);
//        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
//        GLES20.glClearDepthf(1.0f);
//        GLES20.glDepthFunc( GLES20.GL_LEQUAL );  // Passes if the incoming depth value is less than or equal to the stored depth value.
//        GLES20.glDepthMask( true ); // enable writing into the depth buffer
        //启动阴影绘制。
       buffer.bind();
        //调用此方法计算产生透视投影矩阵
        shadowProject.setOrtho();
        shadowCamera.setCamera();
        GLES20.glClearColor(1,1,1,1);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        //绘制阴影物体。

        MatrixState.pushMatrix();
        MatrixState.scaleM(5,1,5);
        teapotShadow.draw();
        MatrixState.popMatrix();






        MatrixState.pushMatrix();
        MatrixState.translate(0,4,2);
        MatrixState.scaleM(0.1f,0.1f,0.1f);
        MatrixState.translate(0,-15,0);
         sphereShadow.draw();
         MatrixState.popMatrix();

        MatrixState.pushMatrix();
        MatrixState.translate(3,5,3);
        MatrixState.scaleM(0.1f,0.1f,0.1f);
        MatrixState.translate(0,-15,0);
        sphereShadow.draw();
        MatrixState.popMatrix();


        MatrixState.pushMatrix();
        MatrixState.translate(0,3,0);
          teapotShadow.draw();
        MatrixState.popMatrix();




        MatrixState.pushMatrix();
        MatrixState.translate(0,5,0);
        cubeShadow.draw();
        MatrixState.popMatrix();


        //侧面 面板
        MatrixState.pushMatrix();
        MatrixState.translate(0,0,-6);
        MatrixState.rotate(90,1,0,0);

        teapotShadow.draw();
        MatrixState.popMatrix();




        int shadowTextureId=buffer.getTextureBufferId();
        //获取以光线为摄像点的 虚拟矩阵。
        //申请矩阵数据
        float[]cameraViewProj= MatrixState.getNewMatrix();
        MatrixState.getViewProjMatrix(cameraViewProj);


       buffer.unBind();

                //绘制实际物体。
                camera3D.setCamera();
                //调用此方法计算产生透视投影矩阵
                projectInfo.setFrustum();
                //清除颜色缓存于深度缓存
                GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);


            MatrixState.pushMatrix();
            MatrixState.scaleM(5,1,5);
            teapotObj.draw(shadowTextureId,cameraViewProj);
            MatrixState.popMatrix();


         MatrixState.pushMatrix();
        MatrixState.translate(0,4,2);
        MatrixState.scaleM(0.1f,0.1f,0.1f);
        MatrixState.translate(0,-15,0);
        sphereObj.draw(shadowTextureId,cameraViewProj);
        MatrixState.popMatrix();


        MatrixState.pushMatrix();
        MatrixState.translate(3,5,3);
        MatrixState.scaleM(0.1f,0.1f,0.1f);
        MatrixState.translate(0,-15,0);
        sphereObj.draw(shadowTextureId,cameraViewProj);
        MatrixState.popMatrix();


                MatrixState.pushMatrix();
                MatrixState.translate(0,3,0);
                teapotObj.draw(shadowTextureId,cameraViewProj);
                MatrixState.popMatrix();



                MatrixState.pushMatrix();
                MatrixState.translate(0,5,0);
                cubeObj.draw(shadowTextureId,cameraViewProj);
                MatrixState.popMatrix();


        //侧面 面板
        MatrixState.pushMatrix();
        MatrixState.translate(0,0,-6);
        MatrixState.rotate(90,1,0,0);

        teapotObj.draw();
        MatrixState.popMatrix();



                objObject.draw();

      //  释放矩阵数据；
        MatrixState.freeMatrix(cameraViewProj);


    }

    @Override
    public void pause() {
        super.pause();
        if(teapotShadow!=null)
        teapotShadow.unBind();
        teapotObj.unBind();
        sphereShadow.unBind();
        sphereObj.unBind();
        objObject.unBind();

        cubeShadow.unBind();
        cubeObj.unBind();

        buffer.delete();
    }

    @Override
    public void resume() {
        super.resume();
        viewPort.apply();
        initLight();
        buffer.create();
        if(teapotShadow!=null)
        teapotShadow.bind();
        teapotObj.bind();

        sphereShadow.bind();
        sphereObj.bind();

        cubeShadow.bind();
        cubeObj.bind();

        objObject.bind();

        MatrixState.setInitStack();
        GLES20.glClearColor(1,1,1,1);
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
    }

    @Override
    public void dispose() {
        super.dispose();
    }


    /**
     * 绘制茶壶
     */
    private  void  drawTeapot()
    {




    }


    /**
     * 设置mvp 参数
     */
    private void  createMVP()
    {
        //设置视窗大小及位置
        int width=glGraphics.getWidth();
        int height=glGraphics.getHeight();
        viewPort=new ViewPort(0,0,width,height);


        //计算透视投影的比例
        float  ratio = (float) width / height;
        projectInfo=new ProjectInfo(-ratio, ratio, -1, 1, 1, 50);
        camera3D=new Camera3D( 0,   //人眼位置的X
                5, 	//人眼位置的Y
               10,   //人眼位置的Z
                0, 	//人眼球看的点X
                0,   //人眼球看的点Y
                0,   //人眼球看的点Z
                0,
                20,
                0);

        //投影映射的视图。
        int baseHeight=20;
        shadowProject=new ProjectInfo(-baseHeight*ratio, baseHeight*ratio, -baseHeight, baseHeight,1,  30);
        //映射的camera
        shadowCamera=new Camera3D( lightPosition.x,   //人眼位置的X
                lightPosition.y, 	//人眼位置的Y
                lightPosition.z,   //人眼位置的Z
                0, 	//人眼球看的点X
                0,   //人眼球看的点Y
                0,   //人眼球看的点Z
                0,
                1,
                0);


        controller=new CameraController(camera3D);


    }




   private void initLight()
   {
       LightSources.setSunLightPosition(lightPosition.x,lightPosition.y,lightPosition.z);
       // 设置 三种光线
       LightSources.setAmbient(0.15f, 0.15f, 0.15f, 1f);
       LightSources.setDiffuse(0.5f, 0.5f, 0.25f, 1f);
       LightSources.setSpecLight(0.3f, 0.3f, 0.15f, 1f);
   }

    private void updateRelateData()
    {
        LightSources.setSunLightPosition(lightPosition.x,lightPosition.y,lightPosition.z);
        shadowCamera.eyeX=lightPosition.x;
        shadowCamera.eyeY=lightPosition.y;
        shadowCamera.eyeZ=lightPosition.z;

        float length=lightPosition.len()+10;
        shadowProject.far=shadowProject.near+length;
    }
}
