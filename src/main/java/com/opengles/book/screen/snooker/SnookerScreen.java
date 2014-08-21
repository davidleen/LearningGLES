package com.opengles.book.screen.snooker;

import android.opengl.GLES20;
import com.bulletphysics.collision.broadphase.AxisSweep3;
import com.bulletphysics.collision.dispatch.*;
import com.bulletphysics.collision.shapes.*;
import com.bulletphysics.dynamics.DiscreteDynamicsWorld;
import com.bulletphysics.dynamics.DynamicsWorld;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.RigidBodyConstructionInfo;
import com.bulletphysics.dynamics.constraintsolver.SequentialImpulseConstraintSolver;
import com.bulletphysics.linearmath.DefaultMotionState;
import com.bulletphysics.linearmath.MotionState;
import com.bulletphysics.linearmath.Transform;
import com.opengles.book.LightSources;
import com.opengles.book.MatrixState;
import com.opengles.book.framework.Game;
import com.opengles.book.framework.Input;
import com.opengles.book.framework.gl.*;
import com.opengles.book.framework.impl.GLScreen;
import com.opengles.book.glsl.FrameBufferManager;
import com.opengles.book.math.AABB3;
import com.opengles.book.math.Vector3;
import com.opengles.book.objects.ShadowFrameBuffer;
import com.opengles.book.screen.cubeCollisionDemo.BodyCreator;
import com.opengles.book.screen.cubeCollisionDemo.ConcreateObject;
import com.opengles.book.screen.dollDemo.FloorDrawable;

import javax.vecmath.Vector3f;
import java.util.List;
import java.util.Random;

/**
 * 桌球游戏界面
 * Created by davidleen29 on 2014/7/8.
 */
public class SnookerScreen  extends GLScreen{



    private Vector3 lightPosition=Vector3.create(0,100,-100);
    private static final float MAX_AABB_LENGTH = 100;
    final static float EYE_X=0;//观察者的位置x
    final static float EYE_Y=  10 ;//观察者的位置y
    final static float EYE_Z= 5;//观察者的位置z

    final static float TARGET_X=0;//目标的位置x
    final static float TARGET_Y=8;//目的位置Y
    final static float TARGET_Z=0;//目标的位置Z
    final static float NEAR=1;
    final static float FAR=500;

    final static float BALL_RADIUS=0.25f;


    final static float ROOM_WIDTH=20;
    final static float ROOM_LONG=30;
    final static float ROOM_HEIGHT=20;




    //桌腿尺寸大小设置。
    Vector3 tableLegSize=Vector3.create(0.5f,6,0.5f);
    //桌面尺寸
    final static Vector3 TABLE_SIZE=Vector3.create(6,0.25f,10);
    //长围栏尺寸
    Vector3 LONG_BAR_SIZE=Vector3.create(0.25f,0.5f,9f);
    //短围栏的尺寸
    Vector3 SHORT_BAR_SIZE=Vector3.create(5f,0.5f,0.25f);

    Vector3 BALL_STICK_SIZE=Vector3.create(6,0.1f,0.1f);

    private ProjectInfo projectInfo;
    private ProjectInfo shadowProject;
    private Camera3D camera;
    private Camera3D shadowCamera;
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

    RigidBody ballStick;

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
    private SnookerObjObject tablePlanDrawable;

    //桌面长边栏绘制
    private CuboidDrawable tableLongBarDrawable;
    //桌面短边栏绘制
    private CuboidDrawable tableShortBarDrawable;

    //木棍绘制类
    private CuboidDrawable ballStickDrawable ;


    //球体绘制类。
    private BallDrawable[] ballDrawables;
    private ShadowDrawable[] ballShadowDrawables;
    private ShadowDrawable tablePlanShadowDrawable;

    private Texture  ballTexture ;


    private ScreenClickHandler handler;


    //阴影缓冲区控制对象。
    private ShadowFrameBuffer buffer;

    //FPS framebuffer 应用。
    FrameBufferManager.FrameBuffer frameBuffer;

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


        //投影映射的视图。
        //   shadowProject=new ProjectInfo(projectInfo.left, projectInfo.right, projectInfo.bottom, projectInfo.top, projectInfo.near,  projectInfo.far);
        int baseHeight=10;
        shadowProject=new ProjectInfo(-baseHeight*ratio, baseHeight*ratio, -baseHeight, baseHeight, 1,  200);
        //映射的camera
        shadowCamera=new Camera3D( lightPosition.x,   //人眼位置的X
                lightPosition.y, 	//人眼位置的Y
                lightPosition.z,   //人眼位置的Z
                TARGET_X, 	//人眼球看的点X
                TARGET_Y,   //人眼球看的点Y
                TARGET_Z,   //人眼球看的点Z
                0,
                1,
                0);



        dynamicsWorld= generateDynamicsWorld();



        //桌球纹理
        ballTexture=new Texture(game.getContext().getResources(),"balls.png",false);




        ballShadowDrawables=new ShadowDrawable[16];
        //桌球绘制对象。
        ballDrawables=new BallDrawable[16];


        CubeBallShader shader=new CubeBallShader(game.getContext());
        BallShader ballShader=new BallShader(game.getContext());
        BallShadowShader ballShadowShader=new BallShadowShader(game.getContext());
        for(int i=0;i<16;i++)
        {

            int x=i%4*(ballTexture.width/4);
            int y=i/4*(ballTexture.height/4);

            TextureRegion region=new TextureRegion(ballTexture,x,y,ballTexture.width/4,ballTexture.height/4);
            SphereWithLimitTexture sphere = new SphereWithLimitTexture(BALL_RADIUS,  region);
            ballShadowDrawables[i]=new ShadowDrawable(sphere.attributes,sphere.indics,ballShadowShader);
            ballDrawables[i]=new BallDrawable( BALL_RADIUS,region,ballShader);
        }
        BallShadowShader cubeShadowShader=new BallShadowShader(game.getContext(),SphereWithLimitTexture.VERTEX_POS_SIZE+SphereWithLimitTexture.VERTEX_NORMAL_SIZE);
        //桌面对象3D纹理
        CuboidWithCubeTexture      data=new CuboidWithCubeTexture(TABLE_SIZE.x,TABLE_SIZE.y,TABLE_SIZE.z);
        tablePlanShadowDrawable=new ShadowDrawable(data.vertexData,data.indexData,cubeShadowShader);


        buffer=new ShadowFrameBuffer(viewPort);

        frameBuffer=new FrameBufferManager.FrameBuffer(game.getContext(),viewPort.width,viewPort.height);


        //地板绘制。
        floorDrawable=new FloorDrawable(game.getContext(),80,60);

        //立方体纹理定义
        legCuboidTexture=new CubeTexture(game.getContext().getResources(),"crystalball/blue.png");
        barCuboidTexture=new CubeTexture(game.getContext().getResources(),"crystalball/water.png");
        planeCuboidTexture=new CubeTexture(game.getContext().getResources(),"teapot.png");



        tablePlanDrawable=new SnookerObjObject( game.getContext());
        //桌腿绘制对象
            data=new CuboidWithCubeTexture(tableLegSize.x,tableLegSize.y,tableLegSize.z);
        legDrawer =new CuboidDrawable( data.vertexData,data.indexData,legCuboidTexture,shader);

        data=new CuboidWithCubeTexture(LONG_BAR_SIZE.x,LONG_BAR_SIZE.y,LONG_BAR_SIZE.z);
        tableLongBarDrawable=new CuboidDrawable(data.vertexData,data.indexData  ,barCuboidTexture,shader);
        data=new CuboidWithCubeTexture(SHORT_BAR_SIZE.x,SHORT_BAR_SIZE.y,SHORT_BAR_SIZE.z);
        tableShortBarDrawable=new CuboidDrawable(data.vertexData,data.indexData ,barCuboidTexture,shader);
        data=new CuboidWithCubeTexture(BALL_STICK_SIZE.x,BALL_STICK_SIZE.y,BALL_STICK_SIZE.z);
        ballStickDrawable=new CuboidDrawable(data.vertexData,data.indexData ,legCuboidTexture,shader);
//




        floor=generateFloor(dynamicsWorld);
       legs=generateTableLeg(dynamicsWorld);
        tablePlane=generateTablePlane
                (dynamicsWorld);
        longBars=generateTableLongBar(dynamicsWorld);
        shortBars=generateTableShortBar(dynamicsWorld);
        ballStick=generateBallStick(dynamicsWorld);

        balls=generateSnookerBalls(dynamicsWorld);

        //初始化屏幕点击事件
        handler=new ScreenClickHandler( new ScreenClickHandler.OnClickListener() {
            @Override
            public void onClick(Input.TouchEvent event) {
                    //执行代码
                //推动白球

                if(balls[15].isActive())
                { return;}






                //获取当前白球位置
                //从物理世界中山地的位置。
                Transform transform =  balls[15].getMotionState().getWorldTransform(new Transform());
                Vector3f ballPosition=transform.origin;

                //计算球前行方向
               Vector3f direction=new Vector3f(ballPosition.x-camera.eyeX,0,ballPosition.z-camera.eyeZ);

                direction.normalize();

//                //求出棍子近白球的起点
//                Vector3 stickStart=Vector3.create(direction.x,direction.y,direction.z).mul(BALL_RADIUS).add(ballPosition.x,ballPosition.y,ballPosition.z);
//
//                //计算3个轴的旋转方向。
//               double  rx= Math.acos( direction.x);
//                double ry=Math.acos(direction.y);
//                double  rz= Math.acos( direction.z);
//
//
//                //获取球棍当前位置
//                  transform =  ballStick.getMotionState().getWorldTransform(transform);
//                Vector3 position=Vector3.create(direction.x,direction.y,direction.z).mul(BALL_STICK_SIZE.x/2).add(stickStart);
//                transform.origin.set(position.x,position.y,position.z);
//                transform.basis.rotX(30);
//                ballStick.setWorldTransform(transform);




//                 Vector3.
//
             Vector3f  newValue=   balls[15].getLinearVelocity(new Vector3f( ));
                //计算球速度
              direction.scale(random.nextInt(10)+10);
             newValue.add(direction);
              balls[15].setLinearVelocity( newValue);
                if(!balls[15].isActive())
              balls[15].activate();



            }
        });

        MatrixState.setInitStack();
    }
    //物理模拟频率
    public static final float TIME_STEP = 1 / 60f;
    //最大迭代子步数
    public static final int MAX_SUB_STEPS = 5;
    public float timeCollapsed = 0;
    Random random=new Random();
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


        handler.handleTouchEvents(touchEvents);


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
        for(BallDrawable ballDrawable:ballDrawables)
         ballDrawable.unBind();
        for(ShadowDrawable ballDrawable:ballShadowDrawables)
            ballDrawable.unBind();

        tablePlanShadowDrawable.unBind();
        ballStickDrawable.unBind();

        legCuboidTexture.dispose();
        planeCuboidTexture.dispose();
        barCuboidTexture.dispose();

        ballTexture.dispose();


        buffer.delete();
        frameBuffer.delete();
    }

    @Override
    public void resume() {
        super.resume();
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        GLES20.glClearColor(1,1,1,1.0f);




        //设置光源位置。
        LightSources.setSunLightPosition(lightPosition.x,lightPosition.y,lightPosition.z);
        // 设置 三种光线
        LightSources.setAmbient(0.3f, 0.3f, 0.3f, 1f);
        LightSources.setDiffuse(0.3f, 0.3f, 0.3f, 1f);
        LightSources.setSpecLight(0.6f, 0.6f, 0.6f, 1f);







        viewPort.apply();
        projectInfo.setFrustum();
        camera.setCamera();




        buffer.create();
        frameBuffer.create();


        floorDrawable.bind();
        legDrawer.bind();
        tablePlanDrawable.bind();
        ballStickDrawable.bind();
        for(BallDrawable ballDrawable:ballDrawables)
        ballDrawable.bind();
        for(ShadowDrawable ballDrawable:ballShadowDrawables)
            ballDrawable.bind();
        tablePlanShadowDrawable.bind();

        tableLongBarDrawable.bind();
        tableShortBarDrawable.bind();

        legCuboidTexture.reload();
        planeCuboidTexture.reload();
        barCuboidTexture.reload();

        ballTexture.reload();
    }

    @Override
    public void dispose() {
        super.dispose();
    }



    @Override
    public void present(float deltaData) {


       // GLES20.glEnable(GLES20.GL_CULL_FACE);


       // viewPort.apply();



        //绘制阴影。
       // GLES20.glDisable(GLES20.GL_BLEND);
        //绑定绘制阴影映射的fbo
       buffer.bind();
        //调用此方法计算产生透视投影矩阵
        shadowProject.setOrtho();
        shadowCamera.setCamera();

        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        //将摄像机移动到光源位置。
       //绘制各物体阴影深度绘制。


        for(int i=0;i<16;i++) {

          SnookerDraw.draw(ballShadowDrawables[i], balls[i] );
        }

//        //绘制桌面
        MatrixState.pushMatrix();
        MatrixState.translate(0,0.25f,0);
         SnookerDraw.draw(tablePlanShadowDrawable, tablePlane );
        MatrixState.popMatrix();
       int  shadowBufferId=buffer.getTextureBufferId();
        //获取以光线为摄像点的 虚拟矩阵。
        //申请矩阵数据
        float[]cameraViewProj=MatrixState.getNewMatrix();
        MatrixState.getViewProjMatrix(cameraViewProj);

        buffer.unBind();



         if(frameBuffer!=null) {

             frameBuffer.bind();
            camera.setCamera();
             //调用此方法计算产生透视投影矩阵
             projectInfo.setFrustum();


            //清除颜色缓存于深度缓存
            GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

            //绘制地板
            ConcreateObject.draw(floorDrawable, floor);
            //绘制桌腿
            if (legs != null)
                for (RigidBody leg : legs)
                    SnookerDraw.draw(legDrawer, leg,shadowBufferId, cameraViewProj);

            //绘制桌面
             MatrixState.pushMatrix();
             MatrixState.translate(0,0.25f,0);
             SnookerDraw.draw(tablePlanDrawable, tablePlane,shadowBufferId, cameraViewProj);
             MatrixState.popMatrix();




            //绘制球棍
            SnookerDraw.draw(ballStickDrawable, ballStick,shadowBufferId, cameraViewProj);
            for (RigidBody bar : longBars)
                SnookerDraw.draw(tableLongBarDrawable, bar,shadowBufferId, cameraViewProj);

            for (RigidBody bar : shortBars)
                SnookerDraw.draw(tableShortBarDrawable, bar,shadowBufferId, cameraViewProj);


            //绑定球纹理
            ballTexture.bind();

            for (int i = 0; i < 16; i++) {

                SnookerDraw.draw(ballDrawables[i], balls[i], ballTexture.textureId, shadowBufferId, cameraViewProj);
            }

             frameBuffer.show();


       }
        //释放矩阵数据；
        MatrixState.freeMatrix(cameraViewProj);
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
        //DbvtBroadphase
      //  BroadphaseInterface overlappingPairCache = new DbvtBroadphase();

        AABB3 boundary=new AABB3(  Vector3.create(-MAX_AABB_LENGTH, -MAX_AABB_LENGTH, -MAX_AABB_LENGTH),  Vector3.create(MAX_AABB_LENGTH, MAX_AABB_LENGTH, MAX_AABB_LENGTH));

        //创建推动约束解决这对象
        SequentialImpulseConstraintSolver solver = new SequentialImpulseConstraintSolver();

        //创建物理世界对象。
        DynamicsWorld dynamicsWorld = new DiscreteDynamicsWorld(collisionDispatcher, overlappingPairCache, solver, collisionConfiguration);
        dynamicsWorld.getDispatchInfo().allowedCcdPenetration=0.00f;
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

       planeShape.calculateLocalInertia(0,localInertia);



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
        body.setRestitution(0f);
        //设置摩擦系数
        body.setFriction(1f);




        dynamicsWorld.addRigidBody(body);

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

        RigidBody body=  BodyCreator.createCube(planeShape,0, TABLE_SIZE.x/2
                ,tableLegSize.y+TABLE_SIZE.y+LONG_BAR_SIZE.y/2 ,0 );

        dynamicsWorld.addRigidBody(body);

        bodies[0]=body;

        body=  BodyCreator.createCube(planeShape,0, -TABLE_SIZE.x/2,tableLegSize.y+TABLE_SIZE.y+LONG_BAR_SIZE.y/2 ,0);

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

        RigidBody body=  BodyCreator.createCube(planeShape,0, 0,tableLegSize.y+TABLE_SIZE.y+SHORT_BAR_SIZE.y/2 ,TABLE_SIZE.z/2  );
        //设置非运动
       // body.setCollisionFlags( CollisionFlags.STATIC_OBJECT);
    //    body.forceActivationState(CollisionObject.ACTIVE_TAG);
        dynamicsWorld.addRigidBody(body);

        bodies[0]=body;

        body=  BodyCreator.createCube(planeShape,0, 0,tableLegSize.y+TABLE_SIZE.y+SHORT_BAR_SIZE.y/2 , -TABLE_SIZE.z/2 );
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
    public SnookerBall[] generateSnookerBalls(DynamicsWorld dynamicsWorld)
    {


        //创建box形状。
        CollisionShape planeShape = new SphereShape(BALL_RADIUS);

        SnookerBall[] bodies=new SnookerBall[16];



        Vector3 origin=Vector3.create( 0,tableLegSize.y + TABLE_SIZE.y + BALL_RADIUS,0);

        List<Vector3> locations=BallLocation.calculate(origin);

        for(int i=0;i<15;i++) {

            Vector3 location=locations.get(i);
            SnookerBall body = SnookerBall.create(planeShape, 1, new Vector3f(location.x, location.y  ,location.z),i+1);
            dynamicsWorld.addRigidBody(body);
            body.forceActivationState(CollisionObject.WANTS_DEACTIVATION);

            bodies[i] = body;

        }


        //白色球体
        SnookerBall body =SnookerBall.create(planeShape, 1,new Vector3f(0  , tableLegSize.y + TABLE_SIZE.y + BALL_RADIUS ,2 ), 0);
        dynamicsWorld.addRigidBody(body);
        body.forceActivationState(CollisionObject.WANTS_DEACTIVATION);
        bodies[15]=body;



        return bodies;


    }


    /**
     * 生成桌球棍。
     */
    public RigidBody generateBallStick(DynamicsWorld dynamicsWorld )
    {

        //创建box形状。
        CollisionShape stickShape = new CapsuleShape(0.1f,4);
        RigidBody body=  BodyCreator.create(stickShape, 1, new Vector3f(0,10,0), 0.2f, 0.5f);
        dynamicsWorld.addRigidBody(body);
        body.forceActivationState(CollisionObject.WANTS_DEACTIVATION);
        return body;


    }


    /**
     * 绘制阴影贴图的距离缓冲。
     */
    public  void drawShadowBuffer()
    {

    }


}
