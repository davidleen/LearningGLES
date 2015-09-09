package com.opengles.book.screen.dollDemo;

import android.content.Context;
import android.opengl.GLES20;
import android.widget.ListView;
import com.bulletphysics.collision.broadphase.AxisSweep3;
import com.bulletphysics.collision.dispatch.CollisionConfiguration;
import com.bulletphysics.collision.dispatch.CollisionDispatcher;
import com.bulletphysics.collision.dispatch.DefaultCollisionConfiguration;
import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.collision.shapes.StaticPlaneShape;
import com.bulletphysics.dynamics.DiscreteDynamicsWorld;
import com.bulletphysics.dynamics.DynamicsWorld;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.RigidBodyConstructionInfo;
import com.bulletphysics.dynamics.constraintsolver.SequentialImpulseConstraintSolver;
import com.bulletphysics.linearmath.DefaultMotionState;
import com.bulletphysics.linearmath.MotionState;
import com.bulletphysics.linearmath.Transform;
import com.giants3.android.openglesframework.framework.Game;
import com.giants3.android.openglesframework.framework.Input;
import com.giants3.android.openglesframework.framework.MatrixState;

import com.giants3.android.openglesframework.framework.gl.Camera3D;
import com.giants3.android.openglesframework.framework.gl.ProjectInfo;
import com.giants3.android.openglesframework.framework.math.AABB3;
import com.giants3.android.openglesframework.framework.math.Vector3;
import com.opengles.book.objLoader.ObjModel;
import com.opengles.book.objLoader.ObjectParser;
import com.opengles.book.objects.ObjObject;
import com.opengles.book.screen.FrameBufferScreen;
import com.opengles.book.screen.cubeCollisionDemo.ConcreateObject;

import javax.vecmath.Vector3f;
import java.util.List;

/**
 *  人偶场景
 * Created by davidleen29   qq:67320337
 * on 2014-7-3.
 */
public class DollDemoScreen extends FrameBufferScreen{

    private static final float MAX_AABB_LENGTH = 100;
    final static float EYE_X=0;//观察者的位置x
    final static float EYE_Y=  5 ;//观察者的位置y
    final static float EYE_Z= 3;//观察者的位置z

    final static float TARGET_X=0;//目标的位置x
    final static float TARGET_Y=0;//目的位置Y
    final static float TARGET_Z=0;//目标的位置Z
    final static float NEAR=1;
    final static float FAR=100;
    private Doll doll;
    private DollPartObjObject[]  rigidObjObjects;
    private DynamicsWorld dynamicsWorld;
    private RigidBody floor;

    private ProjectInfo projectInfo;
    private Camera3D camera;

    private FloorDrawable  floorDrawable;

    //物理模拟频率
    public static final float TIME_STEP = 1 / 60f;
    //最大迭代子步数
    public static final int MAX_SUB_STEPS = 5;
    public float timeCollapsed = 0;




    float previousX;
    float previousY;

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);


        timeCollapsed += deltaTime;
        if (timeCollapsed > TIME_STEP) {

            dynamicsWorld.stepSimulation(TIME_STEP, MAX_SUB_STEPS);
            timeCollapsed -= TIME_STEP;
        }

        ListView listView;
        List<Input.TouchEvent> touchEvents = glGame.getInput().getTouchEvents();



        for (Input.TouchEvent event:touchEvents)
        {




            switch (event.type)
            {
                case Input.TouchEvent.TOUCH_DOWN:


                    if(!doll.isPicking()) {

                        float[][] nearFar = projectInfo.calculateNearFarPosition(event.x, event.y, glGraphics.getWidth(), glGraphics.getHeight());
                        Vector3 near=Vector3.create(nearFar[0][0],nearFar[0][1],nearFar[0][2]);
                        Vector3 far=Vector3.create(nearFar[1][0],nearFar[1][1],nearFar[1][2]);
                        doll.intersectSegment(near, far);
                        Vector3.recycle(near);
                        Vector3.recycle(far);

                    }
                    previousX=event.x;
                    previousY=event.y;
                    break;
                case
                        Input.TouchEvent.TOUCH_DRAGGED:

                    boolean isMoveFlag=false;
                    //移动大于指定阀值 才认为是移动；
                    if(Math.abs(event.x-previousX)>10)
                        isMoveFlag=true;

                    if(doll.isPicking()&&isMoveFlag)
                    {

                        //计算上新触控点在世界坐标系中的位置
                        float[][] newXY = projectInfo.calculateNearFarPosition(event.x, event.y, glGraphics.getWidth(), glGraphics.getHeight());
                        //计算上一触控点在世界坐标系中的位置
                        float[][] oldXY = projectInfo.calculateNearFarPosition(previousX, previousY, glGraphics.getWidth(), glGraphics.getHeight());
                    Vector3 moveDir=Vector3.create().set(newXY[0][0]-oldXY[0][0],newXY[0][1]-oldXY[0][1],newXY[0][2]-oldXY[0][2]);
                        float moveFactory=0.5f;
                        moveDir.mul(moveFactory);
                        doll.addMove(moveDir);


                    }
                    previousX=event.x;
                    previousY=event.y;
                    break;

                case    Input.TouchEvent.TOUCH_UP:
                    if(doll.isPicking())
                    {
                        doll.releasePick();
                    }

                    break;
            }
        }






    }

    @Override
    public void pause() {
        super.pause();
        if(rigidObjObjects!=null)
        for (ObjObject objObject:rigidObjObjects)
        {
            objObject.unBind();
        }
        floorDrawable.unBind();
    }

    @Override
    public void resume() {
        super.resume();

        //设置视窗大小及位置
        int width=glGraphics.getWidth();
        int height=glGraphics.getHeight();
        GLES20.glViewport(0, 0, width, height);
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

        if(rigidObjObjects!=null)
            for (ObjObject objObject:rigidObjObjects)
            {
                objObject.bind();
            }
        floorDrawable.bind();


        MatrixState.setInitStack();
    }

    @Override
    protected void onPresent(float deltaData) {

        //调用此方法计算产生透视投影矩阵
        projectInfo.setFrustum();
        camera.setCamera();

        //清除颜色缓存于深度缓存
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);


        ConcreateObject.draw(floorDrawable,floor);

        doll.drawSelf(doll.pickIndex);
    }


    @Override
    public void dispose() {
        super.dispose();
    }

    public DollDemoScreen(Game game) {
        super(game);
        dynamicsWorld=generateDynamicsWorld();
        rigidObjObjects=new DollPartObjObject[Doll.BodyPartIndex.BODYPART_COUNT.ordinal()] ;
       initBodyForDraws();
         doll=new Doll(dynamicsWorld,rigidObjObjects);
        //创建地板绘制对象
        floorDrawable=new FloorDrawable(game.getContext());
        floor=generateFloor(dynamicsWorld);




    }


    public void initBodyForDraws(){

        Context context=game.getContext();
        ObjModel headModel= ObjectParser.parse(context,"doll/","head.obj");
        ObjModel lowerArmModel= ObjectParser.parse(context,"doll/","lower_arm.obj");
        ObjModel lowerLegModel= ObjectParser.parse(context,"doll/","lower_leg.obj");
        ObjModel pelvisModel= ObjectParser.parse(context,"doll/","pelvis.obj");
        ObjModel spineModel= ObjectParser.parse(context,"doll/","spine.obj");
        ObjModel upperArmModel= ObjectParser.parse(context,"doll/","upper_arm.obj");
        ObjModel upperLegModel= ObjectParser.parse(context,"doll/","upper_leg.obj");




        rigidObjObjects[Doll.BodyPartIndex.BODYPART_HEAD.ordinal()]=new DollPartObjObject(context,headModel);
        rigidObjObjects[Doll.BodyPartIndex.BODYPART_SPINE.ordinal()]=new DollPartObjObject(context,spineModel);
        rigidObjObjects[Doll.BodyPartIndex.BODYPART_PELVIS.ordinal()]=new DollPartObjObject(context,pelvisModel);
        rigidObjObjects[Doll.BodyPartIndex.BODYPART_RIGHT_UPPER_ARM.ordinal()]=new DollPartObjObject(context,upperArmModel);
        rigidObjObjects[Doll.BodyPartIndex.BODYPART_LEFT_UPPER_ARM.ordinal()]=new DollPartObjObject(context,upperArmModel);
        rigidObjObjects[Doll.BodyPartIndex.BODYPART_LEFT_LOWER_ARM.ordinal()]=new DollPartObjObject(context,lowerArmModel);
        rigidObjObjects[Doll.BodyPartIndex.BODYPART_RIGHT_LOWER_ARM.ordinal()]=new DollPartObjObject(context,lowerArmModel);
        rigidObjObjects[Doll.BodyPartIndex.BODYPART_RIGHT_UPPER_LEG.ordinal()]=new DollPartObjObject(context,upperLegModel);
        rigidObjObjects[Doll.BodyPartIndex.BODYPART_LEFT_UPPER_LEG.ordinal()]=new DollPartObjObject(context,upperLegModel);
        rigidObjObjects[Doll.BodyPartIndex.BODYPART_RIGHT_LOWER_LEG.ordinal()]=new DollPartObjObject(context,lowerLegModel);
        rigidObjObjects[Doll.BodyPartIndex.BODYPART_LEFT_LOWER_LEG.ordinal()]=new DollPartObjObject(context,lowerLegModel);
//        for(int i=0;i<rigidObjObjects.length;i++){
//            lovnList.add(bodyForDraws[i]);
//        }
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
        AABB3   boundary=new AABB3(  Vector3.create(-MAX_AABB_LENGTH, -MAX_AABB_LENGTH, -MAX_AABB_LENGTH),  Vector3.create(MAX_AABB_LENGTH, MAX_AABB_LENGTH, MAX_AABB_LENGTH));

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
}
