package com.opengles.book.screen.cubeCollisionDemo;

import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.dynamics.DiscreteDynamicsWorld;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.RigidBodyConstructionInfo;
import com.bulletphysics.linearmath.DefaultMotionState;
import com.bulletphysics.linearmath.MotionState;
import com.bulletphysics.linearmath.Transform;

import javax.vecmath.Vector3f;

/**
 * 地面
 * Created by davidleen29   qq:67320337
 * on 2014-6-25.
 */
public class TexFloor {


    public TexFloor(float size,float  yOffset,CollisionShape groundShape,DiscreteDynamicsWorld dynamicsWorld)
    {

        //创建刚体初始变换对象
        Transform groundTransform=new Transform();
        groundTransform.setIdentity();
        //设置初始 的平移变换
        groundTransform.origin.set(new Vector3f(0.0f,yOffset,0f));
        //存储惯性向量
        Vector3f localIneria=new Vector3f(0,0,0);

        MotionState motionState=new DefaultMotionState(groundTransform);
        RigidBodyConstructionInfo rbInfo=new RigidBodyConstructionInfo(0,motionState,groundShape,localIneria);
        RigidBody body=new RigidBody(rbInfo);
        body.setRestitution(0.4f);
        body.setFriction(0.8f);
        dynamicsWorld.addRigidBody(body);

        //初始化shader相关数据

    }



    public void draw(int textureId)
    {

    }
}
