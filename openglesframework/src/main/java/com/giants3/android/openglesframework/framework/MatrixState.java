package com.giants3.android.openglesframework.framework;

import java.nio.FloatBuffer;
import java.util.Stack;


 

import android.opengl.Matrix;

import com.giants3.android.openglesframework.framework.utils.FloatUtils;

/**
 * 矩阵相关功能类
 * 
 * @author davidleen29
 * @create : 2013-11-28 下午9:28:51
 * @{ 简单的描述这个类用途}
 */
public class MatrixState
{

	public static final int MATRIX_SIZE = 16;
	static Pool.PoolObjectFactory<float[]> factory = new Pool.PoolObjectFactory<float[]>() {
		@Override
		public float[] createObject() {

			return new float[MATRIX_SIZE];
		}
	};
	public static Pool<float[]> pool = new Pool<float[]>(factory, 100);
	private static float[] mProjMatrix = getNewMatrix();// //4x4矩阵 投影用
	private static float[] mVMatrix = getNewMatrix();////摄像机位置朝向9参数矩阵   
	private static float[] mMVPMatrix = getNewMatrix();//获取具体物体的总变换矩阵
	static float[] mMMatrix = getNewMatrix();
	  //保护变换矩阵的栈
	public static Stack<float[]> mStack = new Stack<float[]>(); 

	private static float[] currMatrix;//当前变换矩阵

	public static void setInitStack()// 设置初始化矩阵。
	{
        if(currMatrix==null)
		    currMatrix = pool.newObject();
		Matrix.setIdentityM(currMatrix, 0);
		Matrix.setRotateM(currMatrix, 0, 0, 1, 0, 0);
	}

	public static void pushMatrix()// �����任����
	{
		float[] currentClone = pool.newObject();
		copyMatrix(currMatrix, currentClone);
		mStack.push(currentClone);
	}

	public static void copyMatrix(float[] src, float[] dest)
	{

		for (int i = 0; i < MATRIX_SIZE; i++)
		{
			dest[i] = src[i];
		}

	}


    /**
     * 保存当前现场
     */
    public void save()
    {

    }

    /**
     * 恢复现场
     */
    public void restore()
    {

    }

	/**
	 * 生成新的矩阵 并且初始化 即设0
	 * 
	 * @return
	 */
	public static float[] getNewMatrix()
	{
		float[] result = pool.newObject();
		Matrix.setIdentityM(result, 0);
		return result;

	}

    /**
     * 生成新的矩阵 并且初始化 即设0
     *
     * @return
     */
    public static void freeMatrix(  float[] matrix)
    {
        pool.free(matrix);

    }

	public static void popMatrix()// 弹出矩阵
	{

		pool.free(currMatrix);
		currMatrix = mStack.pop();
	}

	public static void translate(float x, float y, float z)// ������xyz���ƶ�
	{
		Matrix.translateM(currMatrix, 0, x, y, z);
	}

	public static void rotate(float angle, float x, float y, float z)// ������xyz��ת��
	{
		Matrix.rotateM(currMatrix, 0, angle, x, y, z);
	}


    public static void scaleM(  float x, float y, float z )
    {
        Matrix.scaleM(currMatrix, 0,  x, y, z);
    }

	// 设置摄像机

	public static float[] cameraLocation = new float[3];// 摄像机位置
	public static FloatBuffer cameraFB = FloatUtils.FloatArrayToNativeBuffer(cameraLocation);

	public static void setCamera
			(
					float cx, // 摄像机位置x
					float cy, // 摄像机位置y
					float cz, // 摄像机位置z
					float tx, // 摄像机目标点x
					float ty, // 摄像机目标点y
					float tz, // 摄像机目标点z
					float upx, // 摄像机UP向量X分量
					float upy, // 摄像机UP向量Y分量
					float upz // 摄像机UP向量Z分量
			)
	{
		Matrix.setLookAtM
				(
						mVMatrix,
						0,
						cx,
						cy,
						cz,
						tx,
						ty,
						tz,
						upx,
						upy,
						upz
				);
		cameraLocation[0] = cx;
		cameraLocation[1] = cy;
		cameraLocation[2] = cz;
		 
		cameraFB.put(cameraLocation);
		cameraFB.flip();
	}

	  //设置透视投影参数
	public static void setFrustumProject
			(
					float left,		//near面的left
			    	float right,    //near面的right
			    	float bottom,   //near面的bottom
			    	float top,      //near面的top
			    	float near,		//near面距离
			    	float far       //far面距离
			)
	{
		Matrix.frustumM(mProjMatrix, 0, left, right, bottom, top, near, far);
	}
	
	 //设置正交投影参数
    public static void setOrthoProject
    (
    	float left,		//near面的left
    	float right,    //near面的right
    	float bottom,   //near面的bottom
    	float top,      //near面的top
    	float near,		//near面距离
    	float far       //far面距离
    )
    {    	
    	Matrix.orthoM(mProjMatrix, 0, left, right, bottom, top, near, far);
    }  

	//获取具体物体的总变换矩阵   投影矩阵 X 摄像机矩阵 X 物体变换矩阵
	public static float[] getFinalMatrix()
	{
		// mMVPMatrix ;
		Matrix.multiplyMM(mMVPMatrix, 0, mVMatrix, 0, currMatrix, 0);
		Matrix.multiplyMM(mMVPMatrix, 0, mProjMatrix, 0, mMVPMatrix, 0);
		return mMVPMatrix;
	}


    //获取具体物体的总变换矩阵   投影矩阵 X 摄像机矩阵 X 物体变换矩阵
    public static void getFinalMatrix(float[] matrix)
    {

        float[]  mvp=getFinalMatrix();
        copyMatrix(mvp,matrix);


    }

	   //获取具体物体的变换矩阵
	public static float[] getMMatrix()
	{
		return currMatrix;
	}




    //获取摄像机矩阵的你矩阵方法

    public static void getInvertVMatrix(float[] result)
    {

        Matrix.invertM(result,0,mVMatrix,0);

    }

    /**
     * 获取摄像机投影组合矩阵。
     */
    public static void
    getViewProjMatrix(float[] newViewProj )
    {

        Matrix.multiplyMM(newViewProj, 0, mProjMatrix, 0, mVMatrix, 0);

    }

    /**
     * 将摄像机坐标系中的坐标变换为世界坐标系中的坐标
     * @param p
     * @return
     */
    public static float[] fromCameraToWorld(float[] p)
    {

        float[] invM=getNewMatrix();
        getInvertVMatrix(invM);
        float[] worldPosition=new float[4];
        Matrix.multiplyMV(worldPosition,0,invM,0,new float[]{p[0],p[1],p[2],1},0);
        pool.free(invM);

        return new float[]{worldPosition[0],worldPosition[1],worldPosition[2]};

    }
}
