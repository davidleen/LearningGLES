package com.opengles.book.screen.springDemo;


import com.giants3.android.openglesframework.framework.math.Vector2;
import com.giants3.android.openglesframework.framework.math.Vector3;

/*
 * 质点类
 *
 * 根据质点受到力的情况，计算质点当前的位置与速度
 *
 */
public class Mass {
    //质点质量
    public   float m;
    //质点的位置

     Vector3 pos;
    //质点的速度
    Vector3 vel;
    //质点的受力
    Vector3 force;

    public Mass(float m)
    {

       this(m,null,null);

    }

    public Mass(float m,Vector3 pos,Vector3 vel){
        this.m=m;		//指定质量

        this.pos=Vector3.create();
        if(pos!=null)
        this.pos.set(pos);	//指定位置
        this.vel=Vector3.create();
        if(vel!=null)
        this.vel.set(vel);	//指定速度

//该方法初始化质点受到的力，初始时受到的力为0
        force = Vector3.create();
    }
    public void dispose( )
    {
         Vector3.recycle( pos);
        Vector3.recycle(vel);
    }
//调用该方法，对质点施加力（包括重力、空气阻力等）
public void applyForce(Vector3 force){	//施加力的方法
     force.add(force);
}


    //计算质点当前位置与速度的方法
    public void calculateCurrPosAndVel(float dt){
        Vector3 a = force.cpy().mul(1/m);	// 计算加速度 a = F/m
          a.mul(dt); 	//计算速度增量 deltaV = at
          a.add(vel);// 计算速度v = v + at
          vel.set(a);//保存最新速度
       	  a.mul(dt).add(pos); // 计算位置 pos = pos + vt
          pos.set(a);    //设置新位置。

        Vector3.recycle(a);
    }
}
