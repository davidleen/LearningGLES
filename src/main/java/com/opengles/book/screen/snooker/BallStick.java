package com.opengles.book.screen.snooker;

import android.content.Context;
import android.widget.BaseAdapter;
import android.widget.ListView;
import com.opengles.book.MatrixState;
import com.opengles.book.framework.Input;
import com.opengles.book.framework.gl.Texture;
import com.opengles.book.framework.impl.GLGame;
import com.opengles.book.math.Vector3;
import com.opengles.book.objLoader.ObjModel;
import com.opengles.book.objLoader.ObjectParser;
import com.opengles.book.objects.ObjObject;

import java.util.List;
import java.util.ListIterator;

/**
 * 桌球棍类。
 * Created by davidleen29   qq:67320337
 * on 2014-7-14.
 */
public class BallStick implements  Workable
 {



     //木棍绘制类

     Vector3 originPosition=Vector3.create(0,8,0);




     ObjObject ballStick;




     private float WIELD_TIME=1;
    /**
     * 桌球棍挥击事件
     */
     OnStickListener listener;


     public BallStick(Context context)
     {

         ballStick=new ObjObject(context,"ballstick/","ballstick.obj");
     }


    State state;

    public enum State
    {   //桌球棍状态  0 不存在， 1 蓄力  2 发射
        STATE_UN_WORK,STATE_HOLD,STATE_WIELDING;
    }
    //球棍力度
    private  int power;

    float x; float y;

    public float APPROXIMATE_VALUE=5f;


    private float wieldAccumulate;

    /**
     * 构造函数
     * @param listener
     */
    public BallStick(OnStickListener listener)
    {
        this.listener=listener;
    }


    /**
     * 接受点击事件
     * @param event
     */
    public boolean onTouchEvent( Input.TouchEvent event )
    {



            if(event.type== Input.TouchEvent.TOUCH_DOWN)
            {

                state=State.STATE_HOLD;

                x=event.x;
                y=event.y;

            }

            if(state==State.STATE_HOLD&&event.type== Input.TouchEvent.TOUCH_UP)
            {
                 if(   Math.abs(event.x-x)<APPROXIMATE_VALUE&&Math.abs(event.y-y)<APPROXIMATE_VALUE)
                 {

                     //按下抬起的 在指定范围内表示点击。
                     //进入球棍动画阶段。
                     wieldAccumulate=0;
                     state=State.STATE_WIELDING;
                 }else
                state=State.STATE_UN_WORK;

            }






        return  true;

    }

     @Override
     public void resume() {

        ballStick.bind();
     }

     @Override
     public void pause() {

         ballStick.unBind();
     }


     /**
     * 状态更新
     */
    public   void update(float deltaTime)
    {

        //已经点击
        if(  state==State.STATE_HOLD)
        {
           // holdAccumulate+=deltaTime;
        }


    }


    /**
     * 状态展示。
     */
    public void present(float deltaTime) {
        if (state == State.STATE_UN_WORK) return;


        float step = 0;
        if (state == State.STATE_WIELDING)
        {
            wieldAccumulate+=deltaTime;
            step= wieldAccumulate/WIELD_TIME*3;
        }


        MatrixState.pushMatrix();
        MatrixState.translate(originPosition.x,originPosition.y,originPosition.z-step);
        ballStick.draw();
        MatrixState.popMatrix();
    }



     @Override
     public void add(Workable workable) {

     }

     @Override
     public void remove(Workable workable) {

     }

     @Override
     public boolean hasChild() {
         return false;
     }


     /**
     * 球滚完成挥击监听接口
     */
    public interface  OnStickListener
    {

        public void onStick(Input.TouchEvent event);
    }



}
