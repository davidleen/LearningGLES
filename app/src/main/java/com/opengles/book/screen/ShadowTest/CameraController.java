package com.opengles.book.screen.ShadowTest;


import com.giants3.android.openglesframework.framework.Input;
import com.giants3.android.openglesframework.framework.gl.Camera3D;
import com.giants3.android.openglesframework.framework.math.Vector3;

/**
 *    简单摄像机控制类。
 * Created by davidleen29   qq:67320337
 * on 2014-7-30.
 */
public class CameraController {


    Camera3D camera3D;
    boolean  hasStart=false;

    public CameraController(Camera3D camera3D)
    {
        this.camera3D=camera3D;
    }



private float  mPreviousX  ;
    private float   mPreviousY ;
    public void onTouchEvent(Input.TouchEvent event)
    {

        switch ( event.type)
        {
            case  Input.TouchEvent.TOUCH_DRAGGED:


                float deltaX=event.x-mPreviousX;
                float deltaY=event.y-mPreviousY;

                if(hasStart&& Math.abs(deltaX)>1&&Math.abs(deltaY)>1)
                {
                    Vector3 vector3=Vector3.create(camera3D.eyeX,camera3D.eyeY,camera3D.eyeZ);
                    vector3.y+=deltaY/10.0;
                    vector3.rotate(-deltaX,0,1,0);
                    camera3D.eyeX=vector3.x;
                    camera3D.eyeY=Math.max(Math.min(vector3.y,6),1);
                    camera3D.eyeZ=vector3.z;
                    Vector3.recycle(vector3);



                }


                break;


            case  Input.TouchEvent.TOUCH_DOWN:

                hasStart=true;


                break;

            case  Input.TouchEvent.TOUCH_UP:

                hasStart=false;


                break;
        }

        mPreviousX = event.x;
        mPreviousY=event.y;

    }


}
