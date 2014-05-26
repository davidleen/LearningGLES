package com.opengles.book.galaxy;

import android.graphics.Camera;
import com.opengles.book.framework.Input;
import com.opengles.book.framework.gl.Camera3D;
import com.opengles.book.framework.gl.LookAtCamera;
import com.opengles.book.framework.impl.GLGraphics;
import com.opengles.book.math.Rectangle;

import java.util.List;

/**
 * a class to control the camera direction base on the touchEvent
 * Created by davidleen29   qq:67320337
 * on 14-5-20.
 */
public class CameraController {


     private GLGraphics graphics;
    private LookAtCamera camera;

    private Rectangle rectangle;

    CameraUpdateListener listener;
    float buttonWidth;

    Rectangle flippingArea;
    Rectangle forwardArea;
    Rectangle backwardArea;
     Rectangle upwardArea;
    Rectangle downwardArea;
    Rectangle leftwardArea;
    Rectangle rightwardArea;
    float mPreviousX;
    float mPreviousY;
    float xAngle;
    float yAngle;
    private static final float  MOVE_STEP=0.5f;

    public void setListener(CameraUpdateListener listener) {
        this.listener = listener;
    }

    public CameraController(GLGraphics graphics)
    {
    this(null,graphics);

    }

    public CameraController(LookAtCamera camera,GLGraphics graphics)
    {
        this.camera=camera;
        this.graphics = graphics;
        rectangle=new Rectangle(0,0,graphics.getWidth(),graphics.getHeight());
        buttonWidth=rectangle.width/8;

        //中间区域 面板划动
        flippingArea=new Rectangle(buttonWidth,buttonWidth,rectangle.width-2*buttonWidth,rectangle.height-2*buttonWidth);
        //右上角 前进
        forwardArea=new Rectangle(rectangle.width-buttonWidth,0, buttonWidth, buttonWidth);
        //右下角  后退
       backwardArea=new Rectangle(rectangle.width-buttonWidth,rectangle.height-buttonWidth, buttonWidth, buttonWidth);

        //中间左 平移
        leftwardArea=new Rectangle(0,(rectangle.height-buttonWidth)/2,buttonWidth,buttonWidth);
        //中间右 平移
        rightwardArea=new Rectangle(rectangle.width-buttonWidth,(rectangle.height-buttonWidth)/2,buttonWidth,buttonWidth);
        //左上角 镜头抬高
        upwardArea=new Rectangle(0,0, buttonWidth, buttonWidth);
        //左下角 镜头放低
        downwardArea=new Rectangle(0,rectangle.height-buttonWidth, buttonWidth,buttonWidth);
    }

    public void setCamera(LookAtCamera camera) {
        this.camera = camera;
        firCameraChanged();
    }

    public void onTouchEvent(List<Input.TouchEvent> touchEvents)
    {
            for(Input.TouchEvent touchEvent:touchEvents)
            {
                int x=touchEvent.x ;
                int y=touchEvent.y;

                boolean  cameraChanged=false;
                if(flippingArea.include(x,y))
                {
                    //在手势滑动范围类， 执行旋转。

                        if (touchEvent.type == Input.TouchEvent.TOUCH_DRAGGED)
                        {
                            // dx = touch.x - mPreviousX;

                            yAngle = (touchEvent.x - mPreviousX);
                            xAngle  = (touchEvent.y - mPreviousY);
                            //对camera 进行负向旋转  等于物体正向旋转
                            camera.getPosition().rotate(-xAngle,1,0,0);
                            camera.getPosition().rotate(-yAngle,0,1,0);
                            cameraChanged=true;
                        }

                        mPreviousX = touchEvent.x;
                        mPreviousY=touchEvent.y;



                }
                else
                    if(upwardArea.include(x,y))
                    {
                        camera.getPosition().sub(0,MOVE_STEP,0);
                        camera.getLookAt().sub(0, MOVE_STEP, 0);
                        cameraChanged=true;

                    }

                    else
                    if(downwardArea.include(x,y))
                    {
                        camera.getPosition().add(0,MOVE_STEP,0);
                        camera.getLookAt().add(0, MOVE_STEP, 0);

                    }
                    else
                    if(leftwardArea.include(x,y))
                    {

                        camera.getPosition().add(MOVE_STEP,0,0);
                        camera.getLookAt().add(MOVE_STEP, 0, 0);
                        cameraChanged=true;
                    }
                    else
                    if(rightwardArea.include(x,y))
                    {
                        camera.getPosition().sub(MOVE_STEP,0,0);
                        camera.getLookAt().sub(MOVE_STEP, 0, 0);
                        cameraChanged=true;

                    }
                    else
                    if(forwardArea.include(x,y))
                    {

                        camera.getPosition().sub(0,0,MOVE_STEP);
                        camera.getLookAt().sub(0, 0, MOVE_STEP);
                        cameraChanged=true;
                    }
                    else
                    if(backwardArea.include(x,y))
                    {
                        camera.getPosition().add(0,0,MOVE_STEP);
                        camera.getLookAt().add(0, 0, MOVE_STEP);
                        cameraChanged=true;

                    }

                  if(cameraChanged)
                  {
                      firCameraChanged();
                  }

//                else
//                {
//
//                    float relativeX=x-rectangle.lowerLeft.x;
//                    float relativeY=y-rectangle.lowerLeft.y;
//                    int xIndex=(int)(relativeX/(rectangle.width/3.0f));
//                    int yIndex=(int)(relativeY/(rectangle.height/3.0f));
//                    int index=yIndex*3+xIndex;
//                    switch (touchEvent.type)
//                    {
//                        case Input.TouchEvent.TOUCH_DOWN:
//
//
//
//
//
//
//                            break;
//                        case Input.TouchEvent.TOUCH_DRAGGED:
//
//                        	 switch ( index)
//                             {
//
//
//                                 case 0: //左平移
//                                	 camera.getPosition().sub(1,0,0);
//                                 camera.getLookAt().sub(1, 0, 0);
//                                 break;
//                                 case 2: //右平移
//                                	 camera.getPosition().add(1,0,0);
//                                 camera.getLookAt().add(1, 0, 0);
//                                 break;
//
//                                 case 1: //上平移
//                                   camera.getPosition().add(0,1,0);
//                                   camera.getLookAt().add(0, 1, 0);
//                                 break;
//
//                                 case 7: //下平移
//                                	 camera.getPosition().sub(0,1,0);
//                                 camera.getLookAt().sub(0, 1, 0);
//
//
//                                 break;
//
//
//                                 case 3: //前进
//                                	 camera.getPosition().sub(0,0,1);
//                                	 camera.getLookAt().sub(0, 0, 1);
//                                	 break;
//                                 case 5: //后退
//                                	 camera.getPosition().add(0,0,1);
//                                	 camera.getLookAt().add(0, 0, 1);
//                                 break;
//
//
//                                 case 6: //右旋转
//
//                                	 camera.getPosition().rotate(5,0,1,0);
//
//                                     break;  //左旋转
//
//                                 case 8://左旋转
//                                	 camera.getPosition().rotate(-5,0,1,0);
//
//                                     break;  //左旋转
//                             }
//                            //通知镜头变换
//                            if(index>=0&&index<9&&index!=4)
//                            {
//                                firCameraChanged();
//                            }
//
//                            break;
//                        case Input.TouchEvent.TOUCH_UP:
//                            break;
//                    }
//                }



            }

    }

    private void firCameraChanged()
    {
        if(listener!=null)
            listener.onCameraUpdate(camera);
    }

    public interface CameraUpdateListener
    {
        public void onCameraUpdate(LookAtCamera camera);


    }
}
