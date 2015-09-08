package com.opengles.book.enginer;

import android.opengl.GLES20;
import com.bulletphysics.collision.shapes.*;
import com.bulletphysics.extras.gimpact.GImpactMeshShape;
import com.opengles.book.objects.Sphere;

import javax.vecmath.Quat4f;

/**
 * {简单描述用途}
 * Created by davidleen29   qq:67320337
 * on 2014-6-25.
 */
public class TestCode {

    StaticPlaneShape shape;
    SphereShape sphereShape;
    CylinderShape cylinderShape;
    CapsuleShape capsuleShape;
    CompoundShape compoundShape;
    BvhTriangleMeshShape bvhTriangleMeshShape;
    GImpactMeshShape gImpactMeshShape;




    TriangleIndexVertexArray triangleIndexVertexArray;

    public TestCode()
    {



    }
}
