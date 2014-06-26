package com.opengles.book.screen.cubeCollisionDemo;

import android.content.Context;
import android.opengl.GLES20;
import com.bulletphysics.collision.broadphase.AxisSweep3;
import com.bulletphysics.collision.dispatch.CollisionConfiguration;
import com.bulletphysics.collision.dispatch.CollisionDispatcher;
import com.bulletphysics.collision.dispatch.DefaultCollisionConfiguration;
import com.bulletphysics.collision.shapes.BoxShape;
import com.bulletphysics.collision.shapes.StaticPlaneShape;
import com.bulletphysics.dynamics.DiscreteDynamicsWorld;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.constraintsolver.SequentialImpulseConstraintSolver;
import com.bulletphysics.linearmath.Transform;
import com.opengles.book.MatrixState;
import com.opengles.book.ShaderUtil;
import com.opengles.book.math.MathUtils;
import com.opengles.book.math.Rectangle;
import com.opengles.book.math.Vector3;
import com.opengles.book.objects.CubeDrawer;
import com.opengles.book.objects.RectangleObject;
import com.opengles.book.objects.RectangleViewObject;

import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;
import java.sql.SQLClientInfoException;
import java.util.ArrayList;
import java.util.List;

/**
 * {简单描述用途}
 * Created by davidleen29   qq:67320337
 * on 2014-6-25.
 */
public class World {


    public static final int MAX_AABB_LENGTH=10000;
    //立方体的边长。
    public static final int BOX_SIZE=6;
    //立方体 共用形状
    private BoxShape boxShape;
    //平面形状
    private StaticPlaneShape planeShape;
    private DiscreteDynamicsWorld dynamicsWorld;


    private CubeDrawer cubeDrawer;
    private RectangleViewObject object;
    //物理模拟频率
    public static final  float TIME_STEP=1/60f;
    //最大迭代子步数
    public static final int MAX_SUB_STEPS=5;



    private List<RigidBody> bodies;
    private TexFloor floor;

    int floorTextureId;
    int[]  cubeTextureIds;

    public World(Context context)
    {


        cubeDrawer=new CubeDrawer(context,BOX_SIZE);
        object=new RectangleViewObject(context,MAX_AABB_LENGTH,MAX_AABB_LENGTH);

        floorTextureId= ShaderUtil.loadTextureWithUtils(context,"sky/sky.png",false);
        cubeTextureIds=new int[2];
        cubeTextureIds[0]=  ShaderUtil.loadTextureWithUtils(context,"gray_map/grass.png",false);
        cubeTextureIds[1]=  ShaderUtil.loadTextureWithUtils(context,"gray_map/rock.png",false);
        init();
    }

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

        bodies=new ArrayList<RigidBody>();


        //初始化1个刚体

        CubeBody body=  CubeBody.create(boxShape,1,BOX_SIZE,BOX_SIZE,BOX_SIZE );

        bodies.add(body);
        dynamicsWorld.addRigidBody(body);
        body.forceActivationState(RigidBody.WANTS_DEACTIVATION);




        //添加平面

        //创建地面矩形
        floor=  TexFloor.create( 0,planeShape);

        dynamicsWorld.addRigidBody(floor);



    }




    public float timeCollapsed=0;

    public void onUpdate(float deltaTime)
    {



        timeCollapsed+=deltaTime;
        if(timeCollapsed>TIME_STEP) {

            dynamicsWorld.stepSimulation(TIME_STEP, MAX_SUB_STEPS);
            timeCollapsed-=TIME_STEP;
        }


    }


    public void onPresent(float deltaData)
    {

        //清除颜色缓存于深度缓存
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);




        for(RigidBody body:bodies) {


            MatrixState.pushMatrix();
            //从物理世界中获取这个箱子对应刚体的变换信息对象
            Transform transform = body.getMotionState().getWorldTransform(new Transform());
            //平移变换
            MatrixState.translate(transform.origin.x, transform.origin.y, transform.origin.z);
            //获取当前旋转变换信息进入四元数
            Quat4f ro = transform.getRotation(new Quat4f());
            if (ro.x != 0 || ro.y != 0 || ro.z != 0) {
                //将四元数装好成AXYZ形式
                float[] fa = MathUtils.fromSYStoAXYZ(ro);//将四元数转换成AXYZ的形式
                //旋转变换
                MatrixState.rotate(fa[0], fa[1], fa[2], fa[3]);
            }


            int textureId = -1;
            if (body.isActive()) {
                //绘制立方体代码
                textureId = cubeTextureIds[0];
            } else {
                textureId = cubeTextureIds[1];

            }
            //绘制立方体代码
         //  cubeDrawer.draw(textureId);
            MatrixState.popMatrix();
        }
        MatrixState.pushMatrix();

        MatrixState.translate(0,0,-10);

       // cubeDrawer.draw(floorTextureId);
        MatrixState.popMatrix();
            //绘制地板
            MatrixState.pushMatrix();

           MatrixState.translate(0,0,-100);
           MatrixState.rotate(90,1,0,0);
           object.draw(floorTextureId);

            MatrixState.popMatrix();



    }

    public void onResume()
    {



        cubeDrawer.bind();
        object.bind();

        //设置屏幕背景色黑色RGBA
        GLES20.glClearColor(0, 0, 0, 0);
//启用深度测试
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        //设置为打开背面剪裁
   ///     GLES20.glEnable(GLES20.GL_CULL_FACE);
        MatrixState.setInitStack();
    }

    public void onPause()
    {

        cubeDrawer.unBind();
        object.unBind();
    }

    public void onDispose()
    {

    }


    public void add()
    {
        CubeBody body=CubeBody.create(boxShape,1,0,2,4);
        //设置箱子的初始速度
        body.setLinearVelocity(new Vector3f(0,2,-12));//箱子直线运动的速度--Vx,Vy,Vz三个分量
        body.setAngularVelocity(new Vector3f(0,0,0)); //箱子自身旋转的速度--绕箱子自身的x,y,x三轴旋转的速度
        bodies.add(body);
        dynamicsWorld.addRigidBody(body);
    }
}
