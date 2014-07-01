package com.opengles.book.screen.cubeCollisionDemo;

import android.content.Context;
import android.opengl.GLES20;
import android.util.Log;
import com.bulletphysics.collision.broadphase.AxisSweep3;
import com.bulletphysics.collision.dispatch.*;
import com.bulletphysics.collision.shapes.*;
import com.bulletphysics.dynamics.DiscreteDynamicsWorld;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.constraintsolver.SequentialImpulseConstraintSolver;
import com.bulletphysics.linearmath.DefaultMotionState;
import com.bulletphysics.linearmath.Transform;

import com.opengles.book.MatrixState;
import com.opengles.book.ShaderUtil;
import com.opengles.book.framework.Pool;
import com.opengles.book.framework.gl.CubeTexture;
import com.opengles.book.math.AABB3;
import com.opengles.book.math.MathUtils;
import com.opengles.book.math.Vector3;
import com.opengles.book.objects.CubeDrawer;
import com.opengles.book.objects.RectangleViewObject;
import com.opengles.book.objects.SphereObject;

import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * {简单描述用途}
 * Created by davidleen29   qq:67320337
 * on 2014-6-25.
 */
public class World {

    private static final  String TAG="World";


    public static final int MAX_AABB_LENGTH = 1000;
    private AABB3 boundary;
    //立方体的边长。
    public static final int BOX_SIZE = 4;
    public static final int BULLET_SIZE=1;

    public  static final  int SPHERE_RADIUS=4;


    public static final int D_PLAN=-10;
    //立方体 共用形状
    private BoxShape boxShape;
    //平面形状
    private StaticPlaneShape planeShape;
    private SphereShape sphereShape;
    private DiscreteDynamicsWorld dynamicsWorld;


    private CubeDrawer cubeDrawer;
    private CubeDrawer bulletDrawer;
    //球体绘制对象
    private SphereObject sphereObject;
    private RectangleViewObject planObject;
    //物理模拟频率
    public static final float TIME_STEP = 1 / 60f;
    //最大迭代子步数
    public static final int MAX_SUB_STEPS = 5;


    private List<RigidBody> bodies;
    private List<RigidBody> tempBodies=new ArrayList<RigidBody>();
    private TexFloor floor;

    int floorTextureId;

    //子弹的框架
    private BoxShape bulletShape;


    CubeTexture textureGrass;
    CubeTexture textureRock;


    //山地绘制类
    Mountain mountain;
    //灰度地图顶点索引数据
    GrayMap grayMap ;

    CollisionShape mountainShape;


    public World(Context context) {


        cubeDrawer = new CubeDrawer(context, BOX_SIZE);
        bulletDrawer=new CubeDrawer(context,BULLET_SIZE);
        planObject = new RectangleViewObject(context, MAX_AABB_LENGTH, MAX_AABB_LENGTH);
        sphereObject=new SphereObject(context,"sky/sky.png",SPHERE_RADIUS);

        floorTextureId = ShaderUtil.loadTextureWithUtils(context, "sky/sky.png", false);
        textureGrass = new CubeTexture(context.getResources(), "gray_map/grass.png");
        textureRock = new CubeTexture(context.getResources(), "gray_map/rock.png");


        grayMap=   GrayMap.load(context,"gray_map/land.png",10);


        mountain=new Mountain(context,grayMap);
        init(context);
    }

    public void init(Context context) {
        //检测配置信息对象
        CollisionConfiguration collisionConfiguration = new DefaultCollisionConfiguration();
        //算法分配对象
        CollisionDispatcher collisionDispatcher = new CollisionDispatcher(collisionConfiguration);
        //构建物理世界边框
        Vector3f worlddAabbMin = new Vector3f(-MAX_AABB_LENGTH, -MAX_AABB_LENGTH, -MAX_AABB_LENGTH);
        Vector3f worldAabbMax = new Vector3f(MAX_AABB_LENGTH, MAX_AABB_LENGTH, MAX_AABB_LENGTH);
        //最大代理数量
        int maxProxies = 1024;
        //创建碰撞检测粗测阶段的加速算法对象
        AxisSweep3 overlappingPairCache = new AxisSweep3(worlddAabbMin, worldAabbMax, maxProxies);
        boundary=new AABB3(  Vector3.create(-MAX_AABB_LENGTH, -MAX_AABB_LENGTH, -MAX_AABB_LENGTH),  Vector3.create(MAX_AABB_LENGTH, MAX_AABB_LENGTH, MAX_AABB_LENGTH));

        //创建推动约束解决这对象
        SequentialImpulseConstraintSolver solver = new SequentialImpulseConstraintSolver();

        //创建物理世界对象。
        dynamicsWorld = new DiscreteDynamicsWorld(collisionDispatcher, overlappingPairCache, solver, collisionConfiguration);

        //设置重力加速度
        Vector3f gravity = new Vector3f(0, -9.8f, 0);
        dynamicsWorld.setGravity(gravity);
        //创建公用的立方体碰撞形状
        boxShape = new BoxShape(new Vector3f(BOX_SIZE, BOX_SIZE, BOX_SIZE));
        bulletShape= new BoxShape(new Vector3f(BULLET_SIZE,BULLET_SIZE,BULLET_SIZE));
        sphereShape = new SphereShape(SPHERE_RADIUS);


        //创建共用的平面碰撞形状。
        planeShape = new StaticPlaneShape(new Vector3f(0, 0, 1), -MAX_AABB_LENGTH);


        //创建集合

        bodies = new ArrayList<RigidBody>();


        //初始化1个刚体

        RigidBody body = BodyCreator.createCube(boxShape, 1, BOX_SIZE, BOX_SIZE, -BOX_SIZE * 5);

        bodies.add(body);
        dynamicsWorld.addRigidBody(body);
        //body.forceActivationState(RigidBody.WANTS_DEACTIVATION);


        //添加平面

        //创建地面矩形
        floor = TexFloor.create(0, planeShape);

        dynamicsWorld.addRigidBody(floor);



        //创建山地纹理碰撞体
        //三角形顶点数组
//
//        TriangleIndexVertexArray indexVertexArray=new TriangleIndexVertexArray();
//
//       IndexedMesh indexedMesh=new IndexedMesh();
//
//        indexedMesh.numTriangles= grayMap.triangleCount;
//
//        indexedMesh.numVertices= grayMap.vertexCount;
//        indexedMesh.triangleIndexBase=grayMap.indexData;
//        indexedMesh.vertexBase=grayMap.vertexData;
//        indexedMesh.vertexStride=grayMap.vertexStrideWidth;
//        indexedMesh.triangleIndexStride=grayMap.indexStrideWidth;
//
//
//        indexVertexArray.addIndexedMesh(indexedMesh,ScalarType.SHORT);
//        //创建地形对应的碰撞形状
//
//        mountainShape=new BvhTriangleMeshShape(indexVertexArray,true,true);
//
//        RigidBody mountainBody=BodyCreator.createMountain(mountainShape, new Vector3f(0, 0, 0));
//        //设置非运动提
//        mountainBody.setCollisionFlags(mountainBody.getCollisionFlags()&~CollisionFlags.KINEMATIC_OBJECT);
//        mountainBody.forceActivationState(CollisionObject.ACTIVE_TAG);
//        dynamicsWorld.addRigidBody(mountainBody);


    }


    public float timeCollapsed = 0;



    public void onUpdate(float deltaTime) {




        timeCollapsed += deltaTime;
        if (timeCollapsed > TIME_STEP) {

            dynamicsWorld.stepSimulation(TIME_STEP, MAX_SUB_STEPS);
            timeCollapsed -= TIME_STEP;
        }


        tempBodies.clear();
        for (RigidBody body : bodies) {




            //从物理世界中获取这个箱子对应刚体的变换信息对象
              body.getMotionState().getWorldTransform(tempTransform);
            if(!boundary.contains(tempTransform.origin.x,tempTransform.origin.y,tempTransform.origin.z))
            {
            //该物体超出边界～
                dynamicsWorld.removeRigidBody(body);
                tempBodies.add(body);
            }


//           tempTransform.origin.x, tempTransform.origin.y, tempTransform.origin.z ;
//
//            if( tempTransform.origin.z)


        }
        bodies.removeAll(tempBodies);
        for(RigidBody body:tempBodies)
        {
            bulletPool.free(body );
        }
        tempBodies.clear();

    }


    public void onPresent(float deltaData) {
        //清除颜色缓存于深度缓存
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        for (RigidBody body : bodies) {
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


            drawShape(body.getCollisionShape(),body.isActive());


            MatrixState.popMatrix();
        }

        //绘制地板
        MatrixState.pushMatrix();


       MatrixState.translate(0, 0   , planeShape.getPlaneConstant());
    //     MatrixState.rotate(90, 1, 0, 0);
      planObject.draw(floorTextureId);

        MatrixState.popMatrix();




        //绘制山地
        MatrixState.pushMatrix();

        MatrixState.translate(0, -20   , -32);
        //     MatrixState.rotate(90, 1, 0, 0);
        mountain.draw();

        MatrixState.popMatrix();
    }


    /**
     * 绘制动态刚体
     * @param collisionShape
     * @param isActive
     */
    private void drawShape( CollisionShape collisionShape,boolean  isActive)
    {

        if (collisionShape instanceof SphereShape) {
            //绘制圆球
            sphereObject.draw();

        }else if( collisionShape instanceof BoxShape)
        {//绘制立方体

            CubeTexture texture;
            if (isActive) {
                //绘制立方体代码
                texture = textureGrass;
            } else {
                texture = textureRock;

            }

            if(collisionShape==bulletShape)
            {
                //绘制子弹

                bulletDrawer.draw(texture);
            }else
            {
                //绘制立方体代码
                cubeDrawer.draw(texture);
            }



        }else
        {

           if(collisionShape instanceof  CompoundShape)
           {
               CompoundShape compoundShape=(CompoundShape) collisionShape;
            List<CompoundShapeChild>   childShapes= compoundShape.getChildList();



               Transform localTransform=new Transform();
               for(int i=0,size=childShapes.size();i<size;i++) {

                   MatrixState.pushMatrix();
                   compoundShape.getChildTransform(i,localTransform);
                   MatrixState.translate(localTransform.origin.x,localTransform.origin.y,localTransform.origin.z);
                   drawShape(childShapes.get(i).childShape, isActive);
                   MatrixState.popMatrix();
               }






           }



        }
    }

    public void onResume() {


        cubeDrawer.bind();
        bulletDrawer.bind();
        planObject.bind();
        sphereObject.bind();
        mountain.bind();

        //设置屏幕背景色黑色RGBA
        GLES20.glClearColor(0, 0, 0, 0);
//启用深度测试
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        //设置为打开背面剪裁
        ///     GLES20.glEnable(GLES20.GL_CULL_FACE);
        MatrixState.setInitStack();
    }

    public void onPause() {

        cubeDrawer.unBind();
        bulletDrawer.unBind();
        planObject.unBind();
        sphereObject.unBind();
        mountain.unBind();
    }

    public void onDispose() {

    }


    public void add(Vector3f newPosition,Vector3f newDirection) {

        Log.d(TAG,"newPosition:"+newPosition.toString()+", newDirection:"+newDirection);

        Random random = new Random();
        RigidBody body;
        int value=random.nextInt(3);

        switch (value)
        {
            case 0:
                body = BodyCreator.createCube(boxShape, 1, newPosition);
                break;
            case 1:

                CompoundShape  shape=BodyCreator.create(sphereShape,boxShape,sphereShape,BOX_SIZE*2);
                body = BodyCreator.createComObject(shape, 1, newPosition);


            break;

            default:
                body = BodyCreator.createSphere(sphereShape, 1, newPosition);
        }


        //设置箱子的初始速度
        Vector3f velocity=new Vector3f(newDirection);
        velocity.scale(10);
        velocity.add(new Vector3f(0,random.nextInt(30),0));


//        //设置箱子的初始速度
        body.setLinearVelocity(velocity);//箱子直线运动的速度--Vx,Vy,Vz三个分量
        body.setAngularVelocity(new Vector3f(1,1,1)); //箱子自身旋转的速度--绕箱子自身的x,y,x三轴旋转的速度
        bodies.add(body);
        dynamicsWorld.addRigidBody(body);
    }


    public void addBullet(Vector3f newPosition,Vector3f newDirection)
    {

        RigidBody body=null;
        body =// BodyCreator.createCube(bulletShape, 1, newPosition);

        bulletPool.newObject();

        //设置子弹的初始速度
        Vector3f velocity=new Vector3f(newDirection);
        velocity.scale(500);
        //创建刚体初始变换对象

        //变换初始化
        tempTransform.setIdentity();
        //设置刚体初始位置
        tempTransform.origin.set(newPosition.x,newPosition.y,newPosition.z);


          body.setWorldTransform(tempTransform);


        body.setCollisionShape(bulletShape);




//        //设置箱子的初始速度
        body.setLinearVelocity(velocity);//箱子直线运动的速度--Vx,Vy,Vz三个分量
        body.setAngularVelocity(new Vector3f(1,1,1)); //箱子自身旋转的速度--绕箱子自身的x,y,x三轴旋转的速度
        bodies.add(body);
        dynamicsWorld.addRigidBody(body);
    }




    private Pool<RigidBody> bulletPool=new Pool<RigidBody>(new Pool.PoolObjectFactory<RigidBody>() {
        @Override
        public RigidBody createObject() {
            return new RigidBody(1, new DefaultMotionState() ,null  );
        }
    },20);



     Transform tempTransform=new Transform();



}
