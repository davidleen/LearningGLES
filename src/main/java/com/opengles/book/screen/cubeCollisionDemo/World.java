package com.opengles.book.screen.cubeCollisionDemo;

import com.bulletphysics.collision.broadphase.AxisSweep3;
import com.bulletphysics.collision.dispatch.CollisionConfiguration;
import com.bulletphysics.collision.dispatch.CollisionDispatcher;
import com.bulletphysics.collision.dispatch.DefaultCollisionConfiguration;
import com.bulletphysics.collision.shapes.BoxShape;
import com.bulletphysics.collision.shapes.StaticPlaneShape;
import com.bulletphysics.dynamics.DiscreteDynamicsWorld;
import com.bulletphysics.dynamics.constraintsolver.SequentialImpulseConstraintSolver;
import com.opengles.book.math.Vector3;

import javax.vecmath.Vector3f;
import java.sql.SQLClientInfoException;

/**
 * {简单描述用途}
 * Created by davidleen29   qq:67320337
 * on 2014-6-25.
 */
public class World {


    public static final int MAX_AABB_LENGTH=10000;
    public static final int BOX_SIZE=6;
    private BoxShape boxShape;
    private StaticPlaneShape planeShape;
    private DiscreteDynamicsWorld dynamicsWorld;
    //物理模拟频率
    public static final  float TIME_STEP=1/60f;
    //最大迭代子步数
    public static final int MAX_SUB_STEPS=5;

    public void init()
    {
        //检测配置信息对象
        CollisionConfiguration collisionConfiguration=new DefaultCollisionConfiguration();
        //算法分配对象
        CollisionDispatcher collisionDispatcher=new CollisionDispatcher(collisionConfiguration);
        //构建物理世界边框
        Vector3f worlddAabbMin=new Vector3f(-MAX_AABB_LENGTH,-MAX_AABB_LENGTH,-MAX_AABB_LENGTH);
        Vector3f worldAabbMax=new Vector3f(MAX_AABB_LENGTH,MAX_AABB_LENGTH,MAX_AABB_LENGTH);
        //最大代理数量
        int maxProxies=1024;
        //创建碰撞检测粗测阶段的加速算法对象
        AxisSweep3 overlappingPairCache=new AxisSweep3(worlddAabbMin,worldAabbMax,maxProxies);
        //创建推动约束解决这对象
        SequentialImpulseConstraintSolver solver=new SequentialImpulseConstraintSolver();

        //创建物理世界对象。
        dynamicsWorld = new DiscreteDynamicsWorld(collisionDispatcher,overlappingPairCache,solver,collisionConfiguration);

        //设置重力加速度
        Vector3f gravity=new Vector3f(0,-9.8f,0);
        dynamicsWorld.setGravity(gravity);
        //创建公用的立方体碰撞形状
        boxShape = new BoxShape(new Vector3f(BOX_SIZE,BOX_SIZE,BOX_SIZE));
        //创建共用的平面碰撞形状。
        planeShape = new StaticPlaneShape(new Vector3f(0,1,0),0);




        //创建集合




    }


    public float timeCollpase=0;

    public void onUpdate(float deltaTime)
    {
        timeCollpase+=deltaTime;
        if(timeCollpase>TIME_STEP) {

            dynamicsWorld.stepSimulation(TIME_STEP, MAX_SUB_STEPS);
            timeCollpase-=TIME_STEP;
        }
    }
}
