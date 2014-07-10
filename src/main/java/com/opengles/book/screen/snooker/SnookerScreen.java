package com.opengles.book.screen.snooker;

import android.opengl.GLES20;
import com.bulletphysics.collision.broadphase.AxisSweep3;
import com.bulletphysics.collision.dispatch.*;
import com.bulletphysics.collision.shapes.BoxShape;
import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.collision.shapes.SphereShape;
import com.bulletphysics.collision.shapes.StaticPlaneShape;
import com.bulletphysics.dynamics.DiscreteDynamicsWorld;
import com.bulletphysics.dynamics.DynamicsWorld;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.RigidBodyConstructionInfo;
import com.bulletphysics.dynamics.constraintsolver.SequentialImpulseConstraintSolver;
import com.bulletphysics.linearmath.DefaultMotionState;
import com.bulletphysics.linearmath.MotionState;
import com.bulletphysics.linearmath.Transform;
import com.opengles.book.MatrixState;
import com.opengles.book.framework.Game;
import com.opengles.book.framework.Input;
import com.opengles.book.framework.gl.Camera3D;
import com.opengles.book.framework.gl.CubeTexture;
import com.opengles.book.framework.gl.ProjectInfo;
import com.opengles.book.framework.gl.ViewPort;
import com.opengles.book.math.AABB3;
import com.opengles.book.math.Vector3;
import com.opengles.book.objects.SphereObject;
import com.opengles.book.screen.FrameBufferScreen;
import com.opengles.book.screen.cubeCollisionDemo.BodyCreator;
import com.opengles.book.screen.cubeCollisionDemo.ConcreateObject;
import com.opengles.book.screen.dollDemo.FloorDrawable;

import javax.vecmath.Vector3f;
import java.util.List;

/**
 * 桌球游戏界面
 * Created by davidleen29 on 2014/7/8.
 */
public class SnookerScreen  extends FrameBufferScreen{
    private static final float MAX_AABB_LENGTH = 100;
    final static float EYE_X=0;//观察者的位置x
    final static float EYE_Y=  10 ;//观察者的位置y
    final static float EYE_Z= 5;//观察者的位置z

    final static float TARGET_X=0;//目标的位置x
    final static float TARGET_Y=8;//目的位置Y
    final static float TARGET_Z=0;//目标的位置Z
    final static float NEAR=1;
    final static float FAR=100;

    final static float BALL_RADIUS=0.25f;


    final static float ROOM_WIDTH=20;
    final static float ROOM_LONG=30;
    final static float ROOM_HEIGHT=20;


    //桌面尺寸
    final static Vector3 TABLE_SIZE=Vector3.create(6,0.25f,10);

    //桌腿尺寸大小设置。
    Vector3 tableLegSize=Vector3.create(0.5f,6,0.5f);

    //长围栏尺寸
    Vector3 LONG_BAR_SIZE=Vector3.create(0.5f,0.25f,10);
    //短围栏的尺寸
    Vector3 SHORT_BAR_SIZE=Vector3.create(5,0.25f,0.5f);

    private ProjectInfo projectInfo;
    private Camera3D camera;
    private ViewPort viewPort;
    //地板类绘制。
    private FloorDrawable floorDrawable;
    private CuboidDrawable legDrawer;
    //地板刚体对象
    private RigidBody floor;
    //桌子腿 4个
    private RigidBody[] legs;
    //桌面
    private RigidBody tablePlane;
    //桌子长边栏
    RigidBody[] longBars;
    //桌子短边栏
    RigidBody[] shortBars;
    //球体模型
    RigidBody[] balls;

    //物理世界模型
    private DynamicsWorld dynamicsWorld;


    //边框的立体贴图
    private CubeTexture barCuboidTexture;
    //桌面的立体贴图。
    private CubeTexture planeCuboidTexture;
    //桌脚的立体贴图
    private CubeTexture legCuboidTexture;



    //摄像机 帮助 控制类。
    private CameraHelper cameraHelper;



    //桌面类绘制。
    private CuboidDrawable tablePlanDrawable;

    //桌面长边栏绘制
    private CuboidDrawable tableLongBarDrawable;
    //桌面短边栏绘制
    private CuboidDrawable tableShortBarDrawable;



    //球体绘制类。
    private SphereObject ballDrawable;



    public SnookerScreen(Game game) {
        super(game);

        //设置视窗大小及位置
        int width=glGraphics.getWidth();
        int height=glGraphics.getHeight();
        viewPort=new ViewPort(0,0,width,height);

        //计算透视投影的比例
        float  ratio = (float) width / height;
        projectInfo=new ProjectInfo(-ratio, ratio, -1, 1, NEAR, FAR);
        camera=new Camera3D( EYE_X,   //人眼位置的X
                EYE_Y, 	//人眼位置的Y
                EYE_Z,   //人眼位置的Z
                TARGET_X, 	//人眼球看的点X
                TARGET_Y,   //人眼球看的点Y
                TARGET_Z,   //人眼球看的点Z
                0,
                20,
                0);
        cameraHelper=new CameraHelper(camera,new AABB3(Vector3.create(-TABLE_SIZE.x/2-2, tableLegSize.y*1.5f,-TABLE_SIZE.z/2-2),Vector3.create(TABLE_SIZE.x/2+2,tableLegSize.y*2,TABLE_SIZE.z/2+2)));
        dynamicsWorld= generateDynamicsWorld();
        //地板绘制。
        floorDrawable=new FloorDrawable(game.getContext(),80,60);

        //立方体纹理定义
        legCuboidTexture=new CubeTexture(game.getContext().getResources(),"crystalball/blue.png");
        barCuboidTexture=new CubeTexture(game.getContext().getResources(),"crystalball/water.png");
        planeCuboidTexture=new CubeTexture(game.getContext().getResources(),"teapot.png");

        //桌腿绘制对象
        legDrawer =new CuboidDrawable(game.getContext(),tableLegSize.x,tableLegSize.y,tableLegSize.z,legCuboidTexture);
        //桌面对象3D纹理
        tablePlanDrawable=new CuboidDrawable(game.getContext(),TABLE_SIZE.x,TABLE_SIZE.y,TABLE_SIZE.z,planeCuboidTexture);

        tableLongBarDrawable=new CuboidDrawable(game.getContext(),LONG_BAR_SIZE.x,LONG_BAR_SIZE.y,LONG_BAR_SIZE.z,barCuboidTexture);
        tableShortBarDrawable=new CuboidDrawable(game.getContext(),SHORT_BAR_SIZE.x,SHORT_BAR_SIZE.y,SHORT_BAR_SIZE.z,barCuboidTexture);
        ballDrawable=new SphereObject(game.getContext(),"crystalball/blue.png",BALL_RADIUS);

        floor=generateFloor(dynamicsWorld);
       legs=generateTableLeg(dynamicsWorld);
        tablePlane=generateTablePlane
                (dynamicsWorld);
        longBars=generateTableLongBar(dynamicsWorld);
        shortBars=generateTableShortBar(dynamicsWorld);

        balls=generateSnookerBalls(dynamicsWorld);

        MatrixState.setInitStack();
    }
    //物理模拟频率
    public static final float TIME_STEP = 1 / 60f;
    //最大迭代子步数
    public static final int MAX_SUB_STEPS = 5;
    public float timeCollapsed = 0;
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);



        timeCollapsed += deltaTime;
        if (timeCollapsed > TIME_STEP) {

            dynamicsWorld.stepSimulation(TIME_STEP, MAX_SUB_STEPS);
            timeCollapsed -= TIME_STEP;
        }

         List<Input.TouchEvent> touchEvents =
                glGame.getInput().getTouchEvents();
        for (Input.TouchEvent event:touchEvents) {

            cameraHelper.onEvent(event);


        }
        for (Input.TouchEvent event:touchEvents) {

            if(event.type== Input.TouchEvent.TOUCH_DOWN)
            {
             Vector3f  newValue=   balls[15].getLinearVelocity(new Vector3f( ));
                newValue.add(new Vector3f(2,0,-10));
             balls[15].setLinearVelocity( newValue);
                //    balls[15].applyForce(new Vector3f(0,0,10),new Vector3f());
            }


        }


        dynamicsWorld.stepSimulation(deltaTime, MAX_SUB_STEPS);
    }

    @Override
    public void pause() {
        super.pause();
        floorDrawable.unBind();
        legDrawer.unBind();
        tablePlanDrawable.unBind();

        tableLongBarDrawable.unBind();
        tableShortBarDrawable.unBind();
        ballDrawable.unBind();

        legCuboidTexture.dispose();
        planeCuboidTexture.dispose();
        barCuboidTexture.dispose();
    }

    @Override
    public void resume() {
        super.resume();

        viewPort.apply();
        projectInfo.setFrustum();
        camera.setCamera();

        floorDrawable.bind();
        legDrawer.bind();
        tablePlanDrawable.bind();
        ballDrawable.bind();

        tableLongBarDrawable.bind();
        tableShortBarDrawable.bind();

        legCuboidTexture.reload();
        planeCuboidTexture.reload();
        barCuboidTexture.reload();
    }

    @Override
    public void dispose() {
        super.dispose();
    }



    @Override
    protected void onPresent(float deltaData) {

        //调用此方法计算产生透视投影矩阵
        projectInfo.setFrustum();
        camera.setCamera();

        //清除颜色缓存于深度缓存
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        //绘制地板
        ConcreateObject.draw(floorDrawable, floor);
        //绘制桌腿
        if(legs!=null)
        for(RigidBody leg:legs)
            ConcreateObject.draw(legDrawer, leg);

        //绘制桌面
        ConcreateObject.draw(tablePlanDrawable, tablePlane);
        for(RigidBody bar:longBars)
            ConcreateObject.draw(tableLongBarDrawable, bar);

        for(RigidBody bar:shortBars)
            ConcreateObject.draw(tableShortBarDrawable, bar);


        for(RigidBody ball:balls)
            ConcreateObject.draw(ballDrawable, ball);
    }




    /**
     * 生成地板刚体
     * @param dynamicsWorld
     * @return
     */
    private RigidBody generateFloor(DynamicsWorld dynamicsWorld)
    {

        int yOffset=-1 ;
        //创建共用的平面碰撞形状。
        CollisionShape planeShape = new StaticPlaneShape(new Vector3f(0,1  , 0), yOffset);

        //创建刚体初始变换对象
        Transform groundTransform=new Transform();
        groundTransform.setIdentity();
        //设置初始 的平移变换
        groundTransform.origin.set(new Vector3f(0.0f,yOffset,0));
//       groundTransform.basis.rotX(90);
        //存储惯性向量
        Vector3f localIneria=new Vector3f(0,0,0);

        MotionState motionState=new DefaultMotionState(groundTransform);
        // o 质量的物体 表示静止的物体
        RigidBodyConstructionInfo rbInfo=new RigidBodyConstructionInfo(0,motionState,planeShape,localIneria);
        RigidBody body=new RigidBody(rbInfo);
        body.setRestitution(0.4f);
        body.setFriction(0.8f);
        dynamicsWorld.addRigidBody(body);

        return body;
    }
    /**
     * 生成动态世界
     * @return
     */
    public DynamicsWorld generateDynamicsWorld()
    {




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
        AABB3 boundary=new AABB3(  Vector3.create(-MAX_AABB_LENGTH, -MAX_AABB_LENGTH, -MAX_AABB_LENGTH),  Vector3.create(MAX_AABB_LENGTH, MAX_AABB_LENGTH, MAX_AABB_LENGTH));

        //创建推动约束解决这对象
        SequentialImpulseConstraintSolver solver = new SequentialImpulseConstraintSolver();

        //创建物理世界对象。
        DynamicsWorld dynamicsWorld = new DiscreteDynamicsWorld(collisionDispatcher, overlappingPairCache, solver, collisionConfiguration);
        //设置重力加速度
        Vector3f gravity = new Vector3f(0, -9.8f, 0);
        dynamicsWorld.setGravity(gravity);
        return dynamicsWorld;

    }


    /**
     * 添加桌子 腿  4个  位置
     * @param dynamicsWorld
     * @return
     */

    public RigidBody[] generateTableLeg(DynamicsWorld dynamicsWorld)
    {
        Vector3f halfExtend=new Vector3f(tableLegSize.x/2,tableLegSize.y/2,tableLegSize.z/2);

        //创建box形状。
        CollisionShape legShape = new BoxShape(halfExtend);

        //创建刚体初始变换对象
        Transform groundTransform=new Transform();

        int legsCount=4;

        Vector3f[] legPosition=new Vector3f[legsCount];
        legPosition[0]=new Vector3f(-2,tableLegSize.y/2,-4);
        legPosition[1]=new Vector3f(-2,tableLegSize.y/2, 4);
        legPosition[2]=new Vector3f( 2,tableLegSize.y/2,-4);
        legPosition[3]=new Vector3f( 2,tableLegSize.y/2, 4);
        RigidBody[] legs=new RigidBody[legsCount];
        for(int i=0;i<legsCount;i++) {
            groundTransform.setIdentity();
            //设置初始 的平移变换
            groundTransform.origin.set(legPosition[i]);
//       groundTransform.basis.rotX(90);
            //存储惯性向量
            Vector3f localIneria = new Vector3f(0, 0, 0);

            MotionState motionState = new DefaultMotionState(groundTransform);
            // o 质量的物体 表示静止的物体
            RigidBodyConstructionInfo rbInfo = new RigidBodyConstructionInfo(0, motionState, legShape, localIneria);
            RigidBody body = new RigidBody(rbInfo);
            body.setRestitution(0.4f);
            body.setFriction(0.8f);
            dynamicsWorld.addRigidBody(body);
            legs[i]=body;
        }
        return legs;

    }


    /**
     * 添加桌子面 刚体对象
     * @param dynamicsWorld
     * @return
     */

    public RigidBody generateTablePlane(DynamicsWorld dynamicsWorld)
    {
        Vector3f halfExtend=new Vector3f(TABLE_SIZE.x/2 ,TABLE_SIZE.y/2 ,TABLE_SIZE.z/2 );

        //创建box形状。
        CollisionShape planeShape = new BoxShape(halfExtend);

     //  RigidBody body=  BodyCreator.createCube(planeShape, 100, 0, tableLegSize.y + TABLE_SIZE.y / 2, 0);
        Vector3f localInertia=new Vector3f(0,0,0);

            //计算惯性

     //   planeShape.calculateLocalInertia(0,localInertia);



        //创建刚体初始变换对象
        Transform startTransform=new Transform();
        //变换初始化
        startTransform.setIdentity();
        //设置刚体初始位置
        startTransform.origin.set( 0, tableLegSize.y + TABLE_SIZE.y / 2, 0);

        //创建刚体的运动状态对象
        DefaultMotionState motionState=new DefaultMotionState(startTransform);

        //创建刚体描述信息对象
        RigidBodyConstructionInfo rbInfo=new RigidBodyConstructionInfo(0,motionState,planeShape,localInertia);





        //创建刚体
        RigidBody body= new RigidBody(rbInfo);



//        //设置反弹系数
//        body.setRestitution(0.2f);
//        //设置摩擦系数
//        body.setFriction(0.8f);



//        //设置非运动
//        body.setCollisionFlags( CollisionFlags.STATIC_OBJECT);
//       body.forceActivationState(CollisionObject.DISABLE_DEACTIVATION);

        dynamicsWorld.addRigidBody(body);

       // body.setLinearVelocity(new Vector3f(0,0,0));//箱子直线运动的速度--Vx,Vy,Vz三个分量

        return body;


    }


    /**
     * 添加桌子长边栏
     * @param dynamicsWorld
     * @return
     */

    public RigidBody[] generateTableLongBar(DynamicsWorld dynamicsWorld)
    {
        Vector3f halfExtend=new Vector3f(LONG_BAR_SIZE.x/2  ,LONG_BAR_SIZE.y /2 ,LONG_BAR_SIZE.z  /2);

        //创建box形状。
        CollisionShape planeShape = new BoxShape(halfExtend);


        RigidBody[] bodies=new RigidBody[2];

        RigidBody body=  BodyCreator.createCube(planeShape,100, TABLE_SIZE.x/2
                ,tableLegSize.y+TABLE_SIZE.y+LONG_BAR_SIZE.y/2 ,0 );
        //设置非运动
        body.setCollisionFlags( CollisionFlags.STATIC_OBJECT);
        body.forceActivationState(CollisionObject.ACTIVE_TAG);
        dynamicsWorld.addRigidBody(body);

        bodies[0]=body;

        body=  BodyCreator.createCube(planeShape,100, -TABLE_SIZE.x/2,tableLegSize.y+TABLE_SIZE.y+LONG_BAR_SIZE.y/2 ,0);
        //设置非运动
        body.setCollisionFlags( CollisionFlags.STATIC_OBJECT);
        body.forceActivationState(CollisionObject.ACTIVE_TAG);
        dynamicsWorld.addRigidBody(body);

        bodies[1]=body;
        return bodies;


    }

    /**
     * 生成短边栏刚体模型
     * @param dynamicsWorld
     * @return
     */
    public RigidBody[] generateTableShortBar(DynamicsWorld dynamicsWorld)
    {
        Vector3f halfExtend=new Vector3f(SHORT_BAR_SIZE.x/2 ,SHORT_BAR_SIZE.y/2 ,SHORT_BAR_SIZE.z/2 );

        //创建box形状。
        CollisionShape planeShape = new BoxShape(halfExtend);

        RigidBody[] bodies=new RigidBody[2];

        RigidBody body=  BodyCreator.createCube(planeShape,100, 0,tableLegSize.y+TABLE_SIZE.y+SHORT_BAR_SIZE.y/2 ,TABLE_SIZE.z/2  );
        //设置非运动
       // body.setCollisionFlags( CollisionFlags.STATIC_OBJECT);
    //    body.forceActivationState(CollisionObject.ACTIVE_TAG);
        dynamicsWorld.addRigidBody(body);

        bodies[0]=body;

        body=  BodyCreator.createCube(planeShape,100, 0,tableLegSize.y+TABLE_SIZE.y+SHORT_BAR_SIZE.y/2 , -TABLE_SIZE.z/2 );
        //设置非运动
      //  body.setCollisionFlags( CollisionFlags.STATIC_OBJECT);
     //   body.forceActivationState(CollisionObject.ACTIVE_TAG);
        dynamicsWorld.addRigidBody(body);

        bodies[1]=body;
        return bodies;


    }

    /**
     * 生成球模型。
     * @param dynamicsWorld
     * @return
     */
    public RigidBody[] generateSnookerBalls(DynamicsWorld dynamicsWorld)
    {


        //创建box形状。
        CollisionShape planeShape = new SphereShape(BALL_RADIUS);

        RigidBody[] bodies=new RigidBody[16];



        Vector3 origin=Vector3.create( 0,tableLegSize.y + TABLE_SIZE.y + BALL_RADIUS,0);

        List<Vector3> locations=BallLocation.calculate(origin);

        for(int i=0;i<15;i++) {

            Vector3 location=locations.get(i);
            RigidBody body = BodyCreator.create(planeShape, 1, new Vector3f(location.x, location.y  ,location.z), 0.8f, 0.2f);
            dynamicsWorld.addRigidBody(body);
            body.setLinearVelocity(new Vector3f(0,0,0));
            body.forceActivationState(CollisionObject.WANTS_DEACTIVATION);

            bodies[i] = body;

        }


        //白色球体
        RigidBody body = BodyCreator.create(planeShape, 1,new Vector3f(0  , tableLegSize.y + TABLE_SIZE.y + BALL_RADIUS*5 ,2 ), 0.9f, 0.8f);
        dynamicsWorld.addRigidBody(body);
        body.setLinearVelocity(new Vector3f(0,0,0));
       // body.forceActivationState(CollisionObject.WANTS_DEACTIVATION);
        bodies[15]=body;

        return bodies;


    }





}
