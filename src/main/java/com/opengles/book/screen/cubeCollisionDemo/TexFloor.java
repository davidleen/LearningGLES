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
public class TexFloor  extends  RigidBody{


    private TexFloor(RigidBodyConstructionInfo constructionInfo) {
        super(constructionInfo);
    }

    public static final TexFloor create(float  yOffset,CollisionShape groundShape )
    {

        //创建刚体初始变换对象
        Transform groundTransform=new Transform();
        groundTransform.setIdentity();
        //设置初始 的平移变换
        groundTransform.origin.set(new Vector3f(0.0f,yOffset,0f));
        //存储惯性向量
        Vector3f localIneria=new Vector3f(0,0,0);

        MotionState motionState=new DefaultMotionState(groundTransform);
        // o 质量的物体 表示静止的物体
        RigidBodyConstructionInfo rbInfo=new RigidBodyConstructionInfo(0,motionState,groundShape,localIneria);
        TexFloor body=new TexFloor(rbInfo);
        body.setRestitution(0.4f);
        body.setFriction(0.8f);
        return body;



    }



}
