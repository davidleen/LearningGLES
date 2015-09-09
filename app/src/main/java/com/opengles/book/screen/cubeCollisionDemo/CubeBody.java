//package com.opengles.book.screen.cubeCollisionDemo;
//
//import android.content.Context;
//import com.bulletphysics.collision.shapes.CollisionShape;
//import com.bulletphysics.dynamics.DynamicsWorld;
//import com.bulletphysics.dynamics.RigidBody;
//import com.bulletphysics.dynamics.RigidBodyConstructionInfo;
//import com.bulletphysics.linearmath.DefaultMotionState;
//import com.bulletphysics.linearmath.Transform;
//import com.giants3.android.openglesframework.framework.MatrixState;
//import com.giants3.android.openglesframework.framework.math.MathUtils;
//import com.opengles.book.objects.CubeDrawer;
//
//import javax.vecmath.Quat4f;
//import javax.vecmath.Vector3f;
//
///**
// * 立方体
// * Created by davidleen29   qq:67320337
// * on 2014-6-25.
// */
//public class CubeBody  extends  RigidBody{
//
//
//
//    private CubeDrawer cubeDrawer;
//
//
//    /**
//     * 构造方法
//     * @param collisionShape
//     * @param mass
//     * @param cx
//     * @param cy
//     * @param cz
//     * @return
//     */
//    public static final  CubeBody create(  CollisionShape collisionShape,  float mass, float cx, float cy, float cz)
//    {
//        boolean  isDynamic=(mass>0);
//        Vector3f localInertia=new Vector3f(0,0,0);
//        if(isDynamic)
//        {
//            //计算惯性
//            collisionShape.calculateLocalInertia(mass,localInertia);
//        }
//
//        //创建刚体初始变换对象
//        Transform startTransform=new Transform();
//        //变换初始化
//        startTransform.setIdentity();
//        //设置刚体初始位置
//        startTransform.origin.set(cx,cy,cz);
//
//        //创建刚体的运动状态对象
//        DefaultMotionState motionState=new DefaultMotionState(startTransform);
//
//        //创建刚体描述信息对象
//        RigidBodyConstructionInfo rbInfo=new RigidBodyConstructionInfo(mass,motionState,collisionShape,localInertia);
//
//
//
//
//
//        //创建刚体
//            CubeBody body= new CubeBody(rbInfo);
//
//
//
//        //设置反弹系数
//        body.setRestitution(0.6f);
//        //设置摩擦系数
//        body.setFriction(0.8f);
//
//
//        return body;
//
//
//    }
//
//    private  CubeBody(RigidBodyConstructionInfo constructionInfo) {
//        super(constructionInfo);
//
//    }
//
//
//
//
//}
