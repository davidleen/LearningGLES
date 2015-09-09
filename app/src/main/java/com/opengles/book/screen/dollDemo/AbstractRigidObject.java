package com.opengles.book.screen.dollDemo;

import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.RigidBodyConstructionInfo;
import com.bulletphysics.linearmath.MotionState;
import com.bulletphysics.linearmath.Transform;
import com.giants3.android.openglesframework.framework.MatrixState;
import com.opengles.book.galaxy.ObjectDrawable;
import com.giants3.android.openglesframework.framework.math.MathUtils;

import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

/**
 * 物体对象
 * Created by davidleen29   qq:67320337
 * on 2014-7-2.
 */
public class AbstractRigidObject extends  RigidBody{


    public AbstractRigidObject(RigidBodyConstructionInfo constructionInfo) {
        super(constructionInfo);
    }

    public AbstractRigidObject(float mass, MotionState motionState, CollisionShape collisionShape) {
        super(mass, motionState, collisionShape);
    }

    public AbstractRigidObject(float mass, MotionState motionState, CollisionShape collisionShape, Vector3f localInertia) {
        super(mass, motionState, collisionShape, localInertia);
    }

    static Transform tempTransform=new Transform();
    /**
     * 绘制
     * 由物理世界的模型 获取该物体的位置参数
     * 通过矩阵转移
     * 绘制物体。
     */
    public   void draw(ObjectDrawable drawable
    )
    {

        //绘制山地
        MatrixState.pushMatrix();
        //从物理世界中山地的位置。
        Transform transform =  getMotionState().getWorldTransform(tempTransform);
        //平移变换
        MatrixState.translate(transform.origin.x, transform.origin.y, transform.origin.z);


        //旋转变换
        Quat4f quat4f=transform.getRotation(new Quat4f());
        //判断是否有旋转
        if(quat4f.x!=0||quat4f.y!=0||quat4f.z!=0)
        {

            float[] fa= MathUtils.fromSYStoAXYZ(quat4f);
            MatrixState.rotate(fa[0],fa[1],fa[2],fa[3]);
        }


        drawable.draw();

        MatrixState.popMatrix();

    }

}
