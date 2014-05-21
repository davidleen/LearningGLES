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
                //操做在按钮面板范围内。
                if(rectangle.include(x,y))
                {

                    float relativeX=x-rectangle.lowerLeft.x;
                    float relativeY=y-rectangle.lowerLeft.y;
                    int xIndex=(int)(relativeX/(rectangle.width/3.0f));
                    int yIndex=(int)(relativeY/(rectangle.height/3.0f));
                    int index=yIndex*3+xIndex;
                    switch (touchEvent.type)
                    {
                        case Input.TouchEvent.TOUCH_DOWN:


                          



                            break;
                        case Input.TouchEvent.TOUCH_DRAGGED:
                        	
                        	 switch ( index)
                             {


                                 case 0: //左平移
                                	 camera.getPosition().sub(1,0,0);
                                 camera.getLookAt().sub(1, 0, 0);
                                 break;  
                                 case 2: //右平移
                                	 camera.getPosition().add(1,0,0);
                                 camera.getLookAt().add(1, 0, 0);
                                 break;  
                                 
                                 case 1: //上平移
                                   camera.getPosition().add(0,1,0);
                                   camera.getLookAt().add(0, 1, 0);
                                 break; 
                                 
                                 case 7: //下平移
                                	 camera.getPosition().sub(0,1,0);
                                 camera.getLookAt().sub(0, 1, 0);
                                 
                                 
                                 break; 
                                 
                                 
                                 case 3: //前进
                                	 camera.getPosition().sub(0,0,1);
                                	 camera.getLookAt().sub(0, 0, 1);
                                	 break;
                                 case 5: //后退
                                	 camera.getPosition().add(0,0,1);
                                	 camera.getLookAt().add(0, 0, 1);
                                 break; 
                                 
                                 
                                 case 6: //右旋转
                                	 
                                	 camera.getPosition().rotate(5,0,1,0);

                                     break;  //左旋转

                                 case 8://左旋转
                                	 camera.getPosition().rotate(-5,0,1,0);

                                     break;  //左旋转
                             }
                            //通知镜头变换
                            if(index>=0&&index<9&&index!=4)
                            {
                                firCameraChanged();
                            }

                            break;
                        case Input.TouchEvent.TOUCH_UP:
                            break;
                    }
                }



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
