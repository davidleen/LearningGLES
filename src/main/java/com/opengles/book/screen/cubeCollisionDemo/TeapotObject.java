package com.opengles.book.screen.cubeCollisionDemo;

import android.content.Context;
import com.bulletphysics.collision.shapes.BvhTriangleMeshShape;
import com.bulletphysics.collision.shapes.IndexedMesh;
import com.bulletphysics.collision.shapes.ScalarType;
import com.bulletphysics.collision.shapes.TriangleIndexVertexArray;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.extras.gimpact.GImpactMeshShape;
import com.bulletphysics.linearmath.Transform;
import com.opengles.book.FloatUtils;
import com.opengles.book.MatrixState;
import com.opengles.book.math.MathUtils;
import com.opengles.book.objLoader.ObjModel;
import com.opengles.book.objLoader.ObjectParser;
import com.opengles.book.objects.ObjObject;

import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * 立体世界的茶壶
 * Created by davidleen29   qq:67320337
 * on 2014-7-2.
 */
public class TeapotObject {


    //。obj 模型数据
    public ObjModel model;

    public ObjObject objObject;
    GImpactMeshShape teapotShape;

    static Transform tempTransform=new Transform();

    public TeapotObject(Context context)
    {
        model= ObjectParser.parse(context,"","teapot.obj");
        objObject=new ObjObject(context, model);



        //创建山地纹理碰撞体
        //三角形顶点数组

        TriangleIndexVertexArray indexVertexArray=new TriangleIndexVertexArray();

        IndexedMesh indexedMesh=new IndexedMesh();


        //使用三角形绘制  每3个顶点绘制一个三角形
        int triangleCount=model.indexData.length/3;
        indexedMesh.numTriangles=triangleCount;

        indexedMesh.numVertices=model.vertexData.length/model.vertexStripSize;

        int indexSize=model.indexData.length;
        ByteBuffer indexBuff= ByteBuffer.allocateDirect(indexSize * FloatUtils.RATIO_SHORTTOBYTE).order(ByteOrder.nativeOrder());

        for(int i=0;i<indexSize;i++)
        {
            indexBuff.putShort(model.indexData[i]);
        }
        indexBuff.flip();

        indexedMesh.triangleIndexBase=indexBuff;

        int  vertexSize=model.vertexData.length;
        ByteBuffer vertexBuff= ByteBuffer.allocateDirect(vertexSize* FloatUtils.RATIO_FLOATTOBYTE).order(ByteOrder.nativeOrder());

        for(int i=0;i<vertexSize;i++)
        {
            vertexBuff.putFloat(model.vertexData[i]);
        }
        vertexBuff.flip();
        indexedMesh.vertexBase=vertexBuff;

        indexedMesh.vertexStride=model.vertexBuffStripSize;
        indexedMesh.triangleIndexStride=3*FloatUtils.RATIO_SHORTTOBYTE;
//
//
        //  indexVertexArray.addIndexedMesh(indexedMesh );
        indexVertexArray.addIndexedMesh(indexedMesh, ScalarType.SHORT);
//        //创建地形对应的碰撞形状
//
        teapotShape =new GImpactMeshShape(indexVertexArray);
        teapotShape.updateBound();



    }


    public void configCollisionShape(RigidBody body)
    {




        body.setCollisionShape(teapotShape);

    }


    /**
     * 数据初始化
     */
    public   void bind(){


        objObject.bind();
    }


    /**
     * 数据释放
     */
        public void dispose()
        {


        }

        public void unBind()
        {
            objObject.unBind();
        }


    public void draw(RigidBody body)
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


        objObject.draw();

        MatrixState.popMatrix();
    }


}
