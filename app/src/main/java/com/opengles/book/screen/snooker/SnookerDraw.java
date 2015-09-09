package com.opengles.book.screen.snooker;

import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.linearmath.Transform;
import com.giants3.android.openglesframework.framework.MatrixState;
import com.giants3.android.openglesframework.framework.math.MathUtils;

import javax.vecmath.Quat4f;

/**
 * {简单描述用途}
 * Created by davidleen29   qq:67320337
 * on 2014-7-15.
 */
public class SnookerDraw {



    public static Transform tempTransform=new Transform();

    /**
     * 绘制
     * 由物理世界的模型 获取该物体的位置参数
     * 通过矩阵转移
     * 绘制物体。
     */
    public static void draw(BallDrawable drawable,
                            RigidBody body//物理世界模型
            ,int textureId,int shadowId,float[] cameraViewProj
    )
    {

        //绘制山地
        MatrixState.pushMatrix();
        //从物理世界中山地的位置。
        Transform transform =  body.getMotionState().getWorldTransform(tempTransform);
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


        drawable.draw(textureId,shadowId,cameraViewProj);

        MatrixState.popMatrix();

    }


    /**
     * 绘制
     * 由物理世界的模型 获取该物体的位置参数
     * 通过矩阵转移
     * 绘制物体。
     */
    public static void draw(ShadowDrawable drawable,
                            RigidBody body//物理世界模型

    )
    {

        //绘制山地
        MatrixState.pushMatrix();
        //从物理世界中山地的位置。
        Transform transform =  body.getMotionState().getWorldTransform(tempTransform);
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


    /**
     * 绘制
     * 由物理世界的模型 获取该物体的位置参数
     * 通过矩阵转移
     * 绘制物体。
     */
    public static void draw(CuboidDrawable  drawable,
                            RigidBody body//物理世界模型
           ,int shadowId,float[] cameraViewProj
    )
    {

        //绘制山地
        MatrixState.pushMatrix();
        //从物理世界中山地的位置。
        Transform transform =  body.getMotionState().getWorldTransform(tempTransform);
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


        drawable.draw(shadowId,cameraViewProj);

        MatrixState.popMatrix();

    }


    /**
     * 绘制
     * 由物理世界的模型 获取该物体的位置参数
     * 通过矩阵转移
     * 绘制物体。
     */
    public static void draw(SnookerObjObject  drawable,
                            RigidBody body//物理世界模型
            ,int shadowId,float[] cameraViewProj
    )
    {

        //绘制山地
        MatrixState.pushMatrix();
        //从物理世界中山地的位置。
        Transform transform =  body.getMotionState().getWorldTransform(tempTransform);
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


        drawable.draw(shadowId,cameraViewProj);

        MatrixState.popMatrix();

    }
}
