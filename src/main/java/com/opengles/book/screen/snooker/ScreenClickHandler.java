package com.opengles.book.screen.snooker;

import com.opengles.book.framework.Input;

import java.util.List;

/**
 * 屏幕点击事件处理类
 * Created by davidleen29   qq:67320337
 * on 2014-7-14.
 */
public class ScreenClickHandler {


    float x; float y; boolean hasTouchDown=false;

    public float APPROXIMATE_VALUE=5f;

    private  OnClickListener listener;
    public ScreenClickHandler( OnClickListener listener)
    {
        this.listener=listener;
    }


    public void handleTouchEvents(List<Input.TouchEvent> touchEvents)
    {
        if(listener==null) return ;

        for(Input.TouchEvent event:touchEvents)
        {
            if(event.type== Input.TouchEvent.TOUCH_DOWN)
            {
                hasTouchDown=true;
                x=event.x;
                y=event.y;
                continue;
            }

            if(hasTouchDown&&event.type== Input.TouchEvent.TOUCH_UP)
            {
                 if(   Math.abs(event.x-x)<APPROXIMATE_VALUE&&Math.abs(event.y-y)<APPROXIMATE_VALUE)
                 {

                     //按下抬起的 在指定范围内表示点击。
                     listener.onClick(event);
                     hasTouchDown=false;

                 }


            }


        }



    }


    public interface  OnClickListener
    {

        public void onClick(Input.TouchEvent event);
    }
}
