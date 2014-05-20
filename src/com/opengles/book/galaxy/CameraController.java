package com.opengles.book.galaxy;

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

    CameraDirectionListener listener;

    public CameraController(LookAtCamera camera,GLGraphics graphics)
    {
        this.camera=camera;
        this.graphics = graphics;
        rectangle=new Rectangle(0,800,300,300);

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
                    float relativeY=x-rectangle.lowerLeft.y;
                    int xIndex=(int)(relativeX/100);
                    int yIndex=(int)(relativeY/100);
                    int index=xIndex*3+yIndex;
                    switch (touchEvent.type)
                    {
                        case Input.TouchEvent.TOUCH_DOWN:


                           switch ( index)
                           {


                               case 0: camera.getPosition().sub(1,0,0);break;  //左平移
                               case 2: camera.getPosition().add(1,0,0);break;  //右平移
                               case 4: camera.getPosition().rotate(5,0,1,0);break;  //左旋转
                               case 6: camera.getPosition().rotate(-5,0,1,0);break;  //左旋转
                           }



                            break;
                        case Input.TouchEvent.TOUCH_DRAGGED:
                            break;
                        case Input.TouchEvent.TOUCH_UP:
                            break;
                    }
                }



            }

    }

    public interface CameraDirectionListener
    {
        public void fireDirectionChanged(int directionId,int step);

        public static final int TURN_LEFT=0;
        public static final int TURN_RIGHT=1;
        public static final int MOVE_LEFT=1;
        public static final int MOVE_RIGHT=1;
        public static final int MOVE_FORWARD=1;
        public static final int MOVE_BACK=1;
        public static final int MOVE_UP=1;
//        public static final int TURN_LEFT=1;
//        public static final int TURN_LEFT=1;
    }
}
