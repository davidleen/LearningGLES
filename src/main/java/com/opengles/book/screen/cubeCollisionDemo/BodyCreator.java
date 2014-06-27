package com.opengles.book.screen.cubeCollisionDemo;

import com.bulletphysics.collision.shapes.*;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.RigidBodyConstructionInfo;
import com.bulletphysics.linearmath.DefaultMotionState;
import com.bulletphysics.linearmath.Transform;
import com.opengles.book.math.Vector3;
import com.opengles.book.objects.Sphere;

import javax.vecmath.Tuple3f;
import javax.vecmath.Vector3f;

/**
 * {简单描述用途}
 * Created by davidleen29   qq:67320337
 * on 2014-6-26.
 */
public class BodyCreator {


    /**
     * 创建球体
     * @param collisionShape
     * @param mass
     * @param origin
     * @return
     */
    public static RigidBody createSphere(CollisionShape collisionShape,float mass, Vector3f origin)
    {
     return   create(collisionShape,mass,origin,0.8f,0.6f);
    }

    /**
     *
     * @param collisionShape //图形
     * @param mass           //质量
     * @param origin        //起始位置
     * @param restitution  //弹性系数
     * @param friction   摩擦系数
     * @return
     */
    public static RigidBody create(CollisionShape collisionShape,float mass, Vector3f origin,float restitution,float friction)
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
        RigidBody body= new RigidBody(rbInfo);



        //设置反弹系数
        body.setRestitution(restitution);
        //设置摩擦系数
        body.setFriction(friction);


        return body;


    }



    /**
     * 构造方法
     * @param collisionShape
     * @param mass
     * @param cx
     * @param cy
     * @param cz
     * @return
     */
    public static final  RigidBody createCube(  CollisionShape collisionShape,  float mass, float cx, float cy, float cz)
    {

        return create(collisionShape,mass,new Vector3f(cx,cy,cz),0.6f,0.8f);



    }
    public static final  RigidBody createCube(  CollisionShape collisionShape,  float mass,Vector3f position)
    {

        return create(collisionShape,mass,position,0.6f,0.8f);



    }

    public static RigidBody createComObject( CollisionShape collisionShape,  float mass,Vector3f origin)
    {



        return create(collisionShape,mass,origin,0.3f,0.9f);

    }


    /**
     * 创建复合的对象模型
     */
    public static CompoundShape create(SphereShape  rightSphereShape,BoxShape boxShape, SphereShape leftSphereShape,int size)
    {

        CompoundShape compoundShape=new CompoundShape();


        Transform localTransform=new Transform();
        localTransform.setIdentity();
        //添加立方体
        localTransform.origin.set(new Vector3f(0,0,0));
        compoundShape.addChildShape(localTransform,boxShape);


        //添加球体  右边球
        localTransform=new Transform();
        //localTransform.basis.transform(new  Vector3f(size,0,0));
        localTransform.origin.set(new Vector3f(0,size,0));
        compoundShape.addChildShape(localTransform,rightSphereShape);

        //添加球体  左边求
        localTransform=new Transform();
       // localTransform.basis.transform(new  Vector3f(-size,0,0));
        localTransform.origin.set(new Vector3f(0,-size,0));
        compoundShape.addChildShape(localTransform,leftSphereShape);
         return  compoundShape;



    }




//   public CollisionShape  createGrayMapShape()
//   {
//
//
//       TriangleIndexVertexArray  array=new TriangleIndexVertexArray();
//
//       CollisionShape groundShape=new BvhTriangleMeshShape(,true,true);
//
//   }

}
