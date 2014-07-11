package com.opengles.book.screen.snooker;

import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.RigidBodyConstructionInfo;
import com.bulletphysics.linearmath.DefaultMotionState;
import com.bulletphysics.linearmath.MotionState;
import com.bulletphysics.linearmath.Transform;

import javax.vecmath.Vector3f;

/**
 * 球    15个号码球
 * 0号 为白色
 *
 * Created by davidleen29   qq:67320337
 * on 2014-7-9.
 */
public class SnookerBall extends RigidBody {



    //球号
    public int number;


    private SnookerBall(RigidBodyConstructionInfo constructionInfo,int number) {
        super(constructionInfo);
        this.number=number;
    }




    public static SnookerBall create(CollisionShape collisionShape,float mass, Vector3f origin,int index)
    {
        boolean  isDynamic=(mass>0);
        Vector3f localInertia=new Vector3f(0,0,0);
        if(isDynamic)
        {
            //计算惯性
            collisionShape.calculateLocalInertia(mass,localInertia);
        }


        //创建刚体初始变换对象
        Transform startTransform=new Transform();
        //变换初始化
        startTransform.setIdentity();
        //设置刚体初始位置
        startTransform.origin.set(origin.x,origin.y,origin.z);

        //创建刚体的运动状态对象
        DefaultMotionState motionState=new DefaultMotionState(startTransform);

        //创建刚体描述信息对象
        RigidBodyConstructionInfo rbInfo=new RigidBodyConstructionInfo(mass,motionState,collisionShape,localInertia);





        //创建刚体
        SnookerBall body= new SnookerBall(rbInfo,index);




        //设置反弹系数
        body.setRestitution(0.1f);
        //设置摩擦系数
        body.setFriction(0.8f);


        return body;

    }
}
