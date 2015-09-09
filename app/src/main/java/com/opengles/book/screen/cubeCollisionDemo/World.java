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
import com.bulletphysics.extras.gimpact.GImpactCollisionAlgorithm;
import com.bulletphysics.extras.gimpact.GImpactMeshShape;
import com.bulletphysics.linearmath.DefaultMotionState;
import com.bulletphysics.linearmath.Transform;

import com.giants3.android.openglesframework.framework.Pool;
import com.giants3.android.openglesframework.framework.gl.CubeTexture;
import com.giants3.android.openglesframework.framework.utils.FloatUtils;
import com.giants3.android.openglesframework.framework.MatrixState;
import com.opengles.book.ShaderUtil;

import com.giants3.android.openglesframework.framework.math.AABB3;
import com.giants3.android.openglesframework.framework.math.MathUtils;
import com.giants3.android.openglesframework.framework.math.Vector3;
import com.opengles.book.objects.CubeDrawer;
import com.opengles.book.objects.RectangleViewObject;
import com.opengles.book.objects.SphereObject;

import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
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
    public static final int BULLET_SIZE=2;

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

    //子弹纹理
    CubeTexture bulletTexture;
    CubeTexture textureGrass;
    CubeTexture textureRock;


    //山地绘制类
    Mountain mountain;
    //灰度地图顶点索引数据
    GrayMap grayMap ;

    CollisionShape mountainShape;

    RigidBody mountainBody;

    Random random = new Random();


    private TeapotObject teapotObject;



    public World(Context context) {


        cubeDrawer = new CubeDrawer(context, BOX_SIZE);
        bulletDrawer=new CubeDrawer(context,BULLET_SIZE);
        planObject = new RectangleViewObject(context, MAX_AABB_LENGTH, MAX_AABB_LENGTH);
        sphereObject=new SphereObject(context,"sky/sky.png",SPHERE_RADIUS);

        floorTextureId = ShaderUtil.loadTextureWithUtils(context, "sky/sky.png", false);
        textureGrass = new CubeTexture(context.getResources(), "gray_map/grass.png");
        textureRock = new CubeTexture(context.getResources(), "gray_map/rock.png");
        bulletTexture = new CubeTexture(context.getResources(), "teapot.png");

        grayMap=   GrayMap.load(context,"gray_map/land.png",10);
        teapotObject=new TeapotObject(context);

        mountain=new Mountain(context,grayMap);
        init(context);
    }

    public void init(Context context) {
        //检测配置信息对象
        CollisionConfiguration collisionConfiguration = new DefaultCollisionConfiguration();
        //算法分配对象
          CollisionDispatcher  collisionDispatcher = new CollisionDispatcher(collisionConfiguration);
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
        planeShape = new StaticPlaneShape(new Vector3f(0,0  , 1), -MAX_AABB_LENGTH);


        //创建集合

        bodies = new ArrayList<RigidBody>();


        //初始化1个刚体

        RigidBody body = BodyCreator.createCube(boxShape, 1, BOX_SIZE, BOX_SIZE, -BOX_SIZE * 5);

        bodies.add(body);
        dynamicsWorld.addRigidBody(body);
         body.forceActivationState(RigidBody.WANTS_DEACTIVATION);

        addTeapot(new Vector3f(0,6,-20),new Vector3f(0,0,0));





        //添加平面

        //创建地面矩形
        floor = TexFloor.create(0, planeShape);

        dynamicsWorld.addRigidBody(floor);



        //注册算法    GImpactCollisionShape 添加到world中 必须注册该算法。
        CollisionDispatcher dispatcher = (CollisionDispatcher) dynamicsWorld.getDispatcher();
        GImpactCollisionAlgorithm.registerAlgorithm(dispatcher);


//
        //创建山地纹理碰撞体
        //三角形顶点数组

      TriangleIndexVertexArray indexVertexArray=new TriangleIndexVertexArray();

        IndexedMesh indexedMesh=new IndexedMesh();

         indexedMesh.numTriangles= grayMap.triangleCount;

        indexedMesh.numVertices= grayMap.vertexCount;

        int indexSize=grayMap.indexData.length;
        ByteBuffer indexBuff= ByteBuffer.allocateDirect(indexSize * FloatUtils.RATIO_SHORTTOBYTE).order(ByteOrder.nativeOrder());

        for(int i=0;i<indexSize;i++)
        {
            indexBuff.putShort(grayMap.indexData[i]);
        }
        indexBuff.flip();
       indexedMesh.triangleIndexBase=indexBuff;



        int  vertexSize=grayMap.vertexData.length;
        ByteBuffer vertexBuff= ByteBuffer.allocateDirect(vertexSize* FloatUtils.RATIO_FLOATTOBYTE).order(ByteOrder.nativeOrder());

        for(int i=0;i<vertexSize;i++)
        {
            vertexBuff.putFloat(grayMap.vertexData[i]);
        }
        vertexBuff.flip();
        indexedMesh.vertexBase=vertexBuff;
        indexedMesh.vertexStride=GrayMap.VERTEX_STRIP_SIZE_OF_BUFFER;
       indexedMesh.triangleIndexStride=3*FloatUtils.RATIO_SHORTTOBYTE;
//
//
        //  indexVertexArray.addIndexedMesh(indexedMesh );
       indexVertexArray.addIndexedMesh(indexedMesh,ScalarType.SHORT);
//        //创建地形对应的碰撞形状
//
        mountainShape=new BvhTriangleMeshShape(indexVertexArray,true,true);

          mountainBody=BodyCreator.createMountain(mountainShape, new Vector3f(0, -20, -20));
        //设置非运动
        mountainBody.setCollisionFlags(CollisionFlags.STATIC_OBJECT);
        //  mountainBody.setCollisionFlags(mountainBody.getCollisionFlags()&~CollisionFlags.KINEMATIC_OBJECT);
        mountainBody.forceActivationState(CollisionObject.ACTIVE_TAG);
     //   mountainBody.setGravity(new Vector3f(0,9.8f,0));
        dynamicsWorld.addRigidBody(mountainBody);


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
            Transform transform = body.getMotionState().getWorldTransform(tempTransform);
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
            CollisionShape collisionShape=body.getCollisionShape();
            if( collisionShape instanceof GImpactMeshShape)
            {// 绘制茶壶


                teapotObject.draw(body);




            }else {

                drawShape(collisionShape, body.isActive());
            }

            MatrixState.popMatrix();
        }

        //绘制地板
        MatrixState.pushMatrix();


       MatrixState.translate(0,planeShape.getPlaneConstant()   ,0 );
      //      MatrixState.rotate(90, 1, 0, 0);
       planObject.draw(floorTextureId);

        MatrixState.popMatrix();



        ConcreateObject.draw(mountain,mountainBody);

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

                bulletDrawer.draw(bulletTexture);
            }else
            {
                //绘制立方体代码
                cubeDrawer.draw(texture);
            }



        }else


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

    public void onResume() {

        teapotObject.bind();
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
        teapotObject.unBind();
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
        velocity.scale(300);
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


    /**
     * 往屏幕丢掷茶壶
     * @param newPosition
     * @param newDirection
     */
    public void addTeapot(Vector3f newPosition,Vector3f newDirection)
    {

        RigidBody body=null;
        body = bulletPool.newObject();




        //设置子弹的初始速度
        Vector3f velocity=new Vector3f(newDirection);
        velocity.scale(10);
      velocity.add(new Vector3f(0, random.nextInt(30), 0));
        //创建刚体初始变换对象


        //变换初始化
        tempTransform.setIdentity();
        //设置刚体初始位置
        tempTransform.origin.set(newPosition.x, newPosition.y, newPosition.z);


        body.setWorldTransform(tempTransform);
        body.setCollisionShape(teapotObject.teapotShape);


//        //设置箱子的初始速度
        body.setLinearVelocity(velocity);//箱子直线运动的速度--Vx,Vy,Vz三个分量
       //  body.setAngularVelocity(new Vector3f(1,1,1)); //箱子自身旋转的速度--绕箱子自身的x,y,x三轴旋转的速度
        bodies.add(body);

        if(!body.isInWorld()) {
            dynamicsWorld.addRigidBody(body);


        }
    }




    private Pool<RigidBody> bulletPool=new Pool<RigidBody>(new Pool.PoolObjectFactory<RigidBody>() {
        @Override
        public RigidBody createObject() {
            return new RigidBody(1, new DefaultMotionState() ,null  );
        }
    },20);



     Transform tempTransform=new Transform();



}
