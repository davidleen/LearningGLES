package com.opengles.book.screen;

import android.graphics.Camera;
import android.opengl.GLES20;
import android.util.Log;
import com.opengles.book.LightSources;
import com.opengles.book.MatrixState;
import com.opengles.book.framework.Game;
import com.opengles.book.framework.Input.TouchEvent;
import com.opengles.book.framework.gl.FPSCounter;
import com.opengles.book.framework.gl.LookAtCamera;
import com.opengles.book.framework.impl.GLScreen;
import com.opengles.book.galaxy.CameraController;
import com.opengles.book.math.Vector3;
import com.opengles.book.objLoader.AABB;
import com.opengles.book.objects.ObjObject;
import com.opengles.book.objects.TwistCuboid;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 *  a screen for generate various object by  .obj file
 */
public   class OjObjectScreen extends GLScreen {


	private int currentObjectIndex;

	FPSCounter counter;
    private float timeForObjectChange=1;
	
    private float timeCollapsedForObject = 0;
    private float timeCollapsedForSun = 0;
    int sunAng=0;


    private CameraController cameraController;
    LookAtCamera  camera;



    private List<ObjObject> objects;

	public OjObjectScreen(Game game) {
		super(game);



        objects=new ArrayList<ObjObject>();
        //                      objects.add(new ObjObject(game.getContext(),"","teapot.obj"));
       objects.add(new ObjObject(game.getContext(), "eager/","eager.obj"));
        objects.add(new ObjObject(game.getContext(), "eager/","eager1.obj"));
        objects.add(new ObjObject(game.getContext(), "eager/","eager2.obj"));
//
//     objects.add(new ObjObject(game.getContext(), "tz/","tz.obj"));
//            objects.add(new ObjObject(game.getContext(),"cuboid"+ File.separator,"cuboid.obj"));
//                      objects.add(new TwistCuboid(game.getContext() ));
//         objects.add(new ObjObject(game.getContext(),"","shot.obj"));
//             objects.add(new ObjObject(game.getContext(),"","cube.obj"));
//         objects.add(new ObjObject(game.getContext(),"","invader.obj"));
       currentObjectIndex=0;




        counter = new FPSCounter();
	}

	@Override
	public void update(float deltaTime) {

        objects.get(currentObjectIndex).update(deltaTime);
		List<TouchEvent> touchEvents = glGame.getInput().getTouchEvents();
        cameraController.onTouchEvent(touchEvents);
        timeCollapsedForSun += deltaTime;
        if (timeCollapsedForSun >= 0.1f)
        {
            timeCollapsedForSun -= 0.1f;
            sunAng +=3;
            if (sunAng > 360)
                sunAng -= 360;
            double radias = Math.toRadians(sunAng);
            LightSources.setSunLightPosition(
                    (int) (1000 * Math.cos(radias)), 1,
                    (int) (1000 * Math.sin(radias)));
        }


            timeCollapsedForObject+=deltaTime;
            if (objects.size()>1&&timeCollapsedForObject >=timeForObjectChange)
            {
                long switchTime=System.nanoTime();
                objects.get(currentObjectIndex).unBind();
                currentObjectIndex++;
                currentObjectIndex%=objects.size();
                objects.get(currentObjectIndex).bind();
                float timeUsdInBind=(System.nanoTime()-switchTime)/1000000000.0f;
                Log.d(TAG,"timeUsdInBind:"+timeUsdInBind);


                updateCameraBaseOnObjectBoundary(camera,objects.get(currentObjectIndex).getBoundary());


                timeCollapsedForObject  -=( timeUsdInBind+timeForObjectChange);

            }


	}


    /**
     * 根据当前对象更新camera 位置。
     * @param camera
     * @param boundary
     */
    private  void updateCameraBaseOnObjectBoundary(LookAtCamera camera,AABB boundary)
    {
          boundary= objects.get(currentObjectIndex).getBoundary();
        Vector3 center=boundary.getCenter();
        camera.setPosition(0.0f,center.y,boundary.getMaxSpan()+boundary.max.z );
        camera.setUp(0,1,0);
        camera.setLookAt(center) ;
    }
	@Override
	public void present(float deltaTime) {
	 	counter.logFrame();
        camera.setMatrices();
		// 清除颜色
		GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT
				| GLES20.GL_COLOR_BUFFER_BIT);



		MatrixState.pushMatrix();
		MatrixState.translate(0, 0, 0);
        // 倾斜
       // MatrixState.rotate(15, 0, 0, 1);

        objects.get(currentObjectIndex).draw();





		MatrixState.popMatrix();


	}

	@Override
	public void pause() {

        objects.get(currentObjectIndex).unBind();
		GLES20.glDisable(GLES20.GL_DEPTH_TEST);
		 GLES20.glDisable(GLES20.GL_CULL_FACE);

	}

	@Override
	public void resume() {

		GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

		GLES20.glEnable(GLES20.GL_DEPTH_TEST);
		 GLES20.glEnable(GLES20.GL_CULL_FACE);
		int width = glGame.getGLGraphics().getWidth();
		int height = glGame.getGLGraphics().getHeight();
		GLES20.glViewport(0, 0, width, height);
		float ratio = (float) width / height;


		// 设置光线位置
		LightSources.setSunLightPosition(1000, 1, 0);
		// 设置 三种光线
		LightSources.setAmbient(0.15f, 0.15f, 0.15f, 1f);
		LightSources.setDiffuse(0.5f, 0.5f, 0.25f, 1f);
		LightSources.setSpecLight(0.3f, 0.3f, 0.15f, 1f);




        camera=new LookAtCamera(2,1/ratio,1 ,1000);
        ObjObject objObject=objects.get(currentObjectIndex);
        updateCameraBaseOnObjectBoundary(camera,objObject.getBoundary());

        cameraController=new CameraController(camera, glGame.getGLGraphics());

        objObject.bind();

	}

	@Override
	public void dispose() {

	}




}
