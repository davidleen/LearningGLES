package com.opengles.book.screen.cubeCollisionDemo;

import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.dynamics.DynamicsWorld;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.RigidBodyConstructionInfo;
import com.bulletphysics.linearmath.DefaultMotionState;
import com.bulletphysics.linearmath.Transform;
import com.opengles.book.MatrixState;

import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

/**
 * 立方体
 * Created by davidleen29   qq:67320337
 * on 2014-6-25.
 */
public class TexCube {


    private final RigidBody body;

    public TexCube(float halfSize,CollisionShape collisionShape,DynamicsWorld dynamicsWorld,float mass, float cx,float cy, float cz )
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
        startTransform.origin.set(cx,cy,cz);

        //创建刚体的运动状态对象
        DefaultMotionState motionState=new DefaultMotionState(startTransform);

        //创建刚体描述信息对象
        RigidBodyConstructionInfo rbInfo=new RigidBodyConstructionInfo(mass,motionState,collisionShape,localInertia);

        //创建刚体

        body = new RigidBody(rbInfo);
        //设置反弹系数
        body.setRestitution(0.6f);
        //设置摩擦系数
        body.setFriction(0.8f);
        //将刚体添加进物理世界
        dynamicsWorld.addRigidBody(body);

        //create texture;

    }


    public void draw(int textureId)
    {

        if(body.isActive())
        {

        }


        //从物理世界中获取这个箱子对应刚体的变换信息对象
        Transform transform=body.getMotionState().getWorldTransform(new Transform());
        //平移变换
        MatrixState.translate(transform.origin.x,transform.origin.y,transform.origin.z);
        //获取当前旋转变换信息进入四元数
        Quat4f ro=transform.getRotation(new Quat4f());
        if(ro.x!=0||ro.y!=0||ro.z!=0)
        {
            //将四元数装好成AXYZ形式
            float[] fa=new float[4];//SysUtil.fromSystoAxyz()
            //旋转变换
            MatrixState.rotate(fa[0],fa[1],fa[2],fa[3]);
        }

        //绘制立方体代码


    }
}
