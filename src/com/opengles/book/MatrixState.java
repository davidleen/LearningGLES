package com.opengles.book;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.Stack;

import com.opengles.book.R;
import com.opengles.book.framework.Pool;
import com.opengles.book.framework.Pool.PoolObjectFactory;

 

import android.opengl.Matrix;

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
	static PoolObjectFactory<float[]> factory = new PoolObjectFactory<float[]>() {

		@Override
		public float[] createObject() {

			return new float[MATRIX_SIZE];
		}
	};
	public static Pool<float[]> pool = new Pool<float[]>(factory, 100);
	private static float[] mProjMatrix = getNewMatrix();// 4x4���� ͶӰ��
	private static float[] mVMatrix = getNewMatrix();// �����λ�ó���9�������
	private static float[] mMVPMatrix = getNewMatrix();// ��������õ��ܱ任����
	static float[] mMMatrix = getNewMatrix();// ����������ƶ���ת����

	public static Stack<float[]> mStack = new Stack<float[]>();// �����任�����ջ

	private static float[] currMatrix;// ��ǰ�任����

	public static void setInitStack()// ��ȡ���任��ʼ����
	{
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
	 * 生成新的矩阵 并且初始化 即设0
	 * 
	 * @return
	 */
	private static float[] getNewMatrix()
	{
		float[] result = pool.newObject();
		Matrix.setIdentityM(result, 0);
		return result;

	}

	public static void popMatrix()// �����任����
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

	// 设置摄像机

	public static float[] cameraLocation = new float[3];// 摄像机位置
	public static FloatBuffer cameraFB = FloatUtils
			.FloatArrayToNativeBuffer(cameraLocation);

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
	public static void setProject
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
    public static void setProjectOrtho
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

	//获取具体物体的总变换矩阵
	public static float[] getFinalMatrix()
	{
		// mMVPMatrix ;
		Matrix.multiplyMM(mMVPMatrix, 0, mVMatrix, 0, currMatrix, 0);
		Matrix.multiplyMM(mMVPMatrix, 0, mProjMatrix, 0, mMVPMatrix, 0);
		return mMVPMatrix;
	}

	   //获取具体物体的变换矩阵
	public static float[] getMMatrix()
	{
		return currMatrix;
	}

}
