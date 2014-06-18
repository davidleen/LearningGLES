package com.opengles.book.math;

import android.opengl.Matrix;
import android.util.FloatMath;
import com.opengles.book.framework.Pool;

/**
 * 添加上对象池  工厂方法 释放方法  对象重用。
 */
public class Vector3 {
	private static final float[] matrix = new float[16];
	private static final float[] inVec = new float[4];
	private static final float[] outVec = new float[4];
	public float x, y, z;

	private Vector3() {
	}

	private Vector3(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	private Vector3(Vector3 other) {
		this.x = other.x;
		this.y = other.y;
		this.z = other.z;
	}

    public static Vector3 create(float x, float y, float z) {
        Vector3 vector3=vector3Pool.newObject();
        vector3.set(x,y,z);
        return vector3;
    }

    public static Vector3 create(Vector3 other) {
        return create(other.x,other.y,other.z);
    }


    public Vector3 cpy() {
		return create(x, y, z);
	}

	public Vector3 set(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
		return this;
	}

	public Vector3 set(Vector3 other) {
		this.x = other.x;
		this.y = other.y;
		this.z = other.z;
		return this;
	}

	public Vector3 add(float x, float y, float z) {
		this.x += x;
		this.y += y;
		this.z += z;
		return this;
	}

	public Vector3 add(Vector3 other) {
		this.x += other.x;
		this.y += other.y;
		this.z += other.z;
		return this;
	}

	public Vector3 sub(float x, float y, float z) {
		this.x -= x;
		this.y -= y;
		this.z -= z;
		return this;
	}

	public Vector3 sub(Vector3 other) {
		this.x -= other.x;
		this.y -= other.y;
		this.z -= other.z;
		return this;
	}

	public Vector3 mul(float scalar) {
		this.x *= scalar;
		this.y *= scalar;
		this.z *= scalar;
		return this;
	}

	public float len() {
		return FloatMath.sqrt(x * x + y * y + z * z);
	}

	public Vector3 nor() {
		float len = len();
		if (len != 0) {
			this.x /= len;
			this.y /= len;
			this.z /= len;
		}
		return this;
	}

	public Vector3 rotate(float angle, float axisX, float axisY, float axisZ) {
		inVec[0] = x;
		inVec[1] = y;
		inVec[2] = z;
		inVec[3] = 1;
		Matrix.setIdentityM(matrix, 0);
		Matrix.rotateM(matrix, 0, angle, axisX, axisY, axisZ);
		Matrix.multiplyMV(outVec, 0, matrix, 0, inVec, 0);
		x = outVec[0];
		y = outVec[1];
		z = outVec[2];
		return this;
	}

	public float dist(Vector3 other) {
		float distX = this.x - other.x;
		float distY = this.y - other.y;
		float distZ = this.z - other.z;
		return FloatMath.sqrt(distX * distX + distY * distY + distZ * distZ);
	}

	public float dist(float x, float y, float z) {
		float distX = this.x - x;
		float distY = this.y - y;
		float distZ = this.z - z;
		return FloatMath.sqrt(distX * distX + distY * distY + distZ * distZ);
	}

	public float distSquared(Vector3 other) {
		float distX = this.x - other.x;
		float distY = this.y - other.y;
		float distZ = this.z - other.z;
		return distX * distX + distY * distY + distZ * distZ;
	}

	public float distSquared(float x, float y, float z) {
		float distX = this.x - x;
		float distY = this.y - y;
		float distZ = this.z - z;
		return distX * distX + distY * distY + distZ * distZ;
	}

    /**
     * 求向量x乘法
     * @param v1
     * @param v2

     * @return
     */
    public static Vector3 mul(Vector3 v1,Vector3 v2 )
    {

     return    Vector3.create().set(v1.x * v2.x, v1.y * v2.y, v1.z * v1.z);
    }


    /**
     * 向量点乘积
     * @param v1
     * @param v2
     * @return
     */
    public static float dotValue(Vector3 v1,Vector3 v2)
    {

        return  v1.x*v2.x+v1.y*v2.y+v1.z*v1.z;
    }


    /**
     * 构造类    提供数据重用
     * @return
     */
    public static Vector3 create()
    {

        return    create(0,0,0);
    }


    /**
     * 释放方法
     * @param recycled
     */
    public static void recycle(Vector3 recycled)
    {

        recycled.set(0,0,0);
        vector3Pool.free(recycled);
    }

    private static Pool<Vector3> vector3Pool=new Pool<Vector3>(new Pool.PoolObjectFactory<Vector3>() {
        @Override
        public Vector3 createObject() {
            return Vector3.create();
        }
    },100);
}