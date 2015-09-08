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


    /**
     * 计算向量的旋转角度。
     *
     * @param desVector
     * @param xyzRotation
     */
    public void getRotation(Vector3 desVector,float[] xyzRotation)
    {
        //计算向量的点乘值。
        float dotValue=Vector3.dotValue(this,desVector);




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
     * 求向量x乘法  /点乘法
     * @param v1
     * @param v2
     * @param result the vector for result
     * @return
     */
    public static void mul(Vector3 v1,Vector3 v2 ,Vector3 result)
    {

          result.set(v1.x * v2.x, v1.y * v2.y, v1.z * v2.z);
    }


    /**
     * 判断点是否在范围值之间
     */
    public  boolean between(Vector3 min,Vector3 max)
    {

        float offset=0.01f;
        //比较大小 使用接近值 偏移量
        if(x<min.x-offset) return false;
        if(y<min.y-offset) return false;
        if(z<min.z-offset) return false;
        if(x>max.x+offset) return false;
        if(y>max.y+offset) return false;
        if(z>max.z+offset) return false;

         return  true;

    }

    /**
     * 向量点乘积
     * @param v1
     * @param v2
     * @return
     */
    public static float dotValue(Vector3 v1,Vector3 v2)
    {

        return  v1.x*v2.x+v1.y*v2.y+v1.z*v2.z;
    }


    /**
     * 向量X乘法
     * u × v = (u2v3 − u3v2,−(u1v3 − u3v1), u1v2 − u2v1).
     * u × v = n ||u|| || v|| sin θ,
     */
    public static void crossValue(Vector3 v1, Vector3 v2 ,Vector3 result)
    {



        result.set(v1.y*v2.z-v1.z*v2.y,-(v1.x*v2.z-v1.z*v2.x),v1.x*v2.y-v1.y-v2.x);


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
            return new Vector3();
        }
    },1000);
}