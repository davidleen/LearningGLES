package com.opengles.book;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import org.apache.http.protocol.HTTP;

 

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.util.Log;
import com.opengles.book.R;

/**
 * 加载顶点Shader与片元Shader的工具类
 * 
 * @author davidleen29  create : 2013-10-18 下午11:31:56
 * 
 */

public class ShaderUtil {
	private static final String TAG = "ShaderUtil";

	// 加载制定shader的方法
	public static int loadShader(int shaderType, // shader的类型
													// GLES20.GL_VERTEX_SHADER(顶点)
													// GLES20.GL_FRAGMENT_SHADER(片元)
			String source // shader的脚本字符串
	) {
		// 创建一个新shader
		int shader = GLES20.glCreateShader(shaderType);
		// 若创建成功则加载shader
		if (shader != 0) {
			// 加载shader的源代码
			GLES20.glShaderSource(shader, source);
			// 编译shader
			GLES20.glCompileShader(shader);
			// 存放编译成功shader数量的数组
			int[] compiled = new int[1];
			// 获取Shader的编译情况
			GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compiled, 0);
			if (compiled[0] == 0) {// 若编译失败则显示错误日志并删除此shader
				Log.e("ES20_ERROR", "Could not compile shader " + shaderType
						+ ":");
				Log.e("ES20_ERROR", GLES20.glGetShaderInfoLog(shader));
				GLES20.glDeleteShader(shader);
				shader = 0;
			}
		}
		return shader;
	}

	// 创建shader程序的方法
	public static int createProgram(String vertexSource, String fragmentSource) {
		
		Log.d(TAG, "vertexSource:"+vertexSource);
		Log.d(TAG, "fragmentSource:"+fragmentSource);
		
		// 加载顶点着色器
		int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexSource);
		if (vertexShader == 0) {
			return 0;
		}

		// 加载片元着色器
		int pixelShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentSource);
		if (pixelShader == 0) {
			return 0;
		}

		// 创建程序
		int program = GLES20.glCreateProgram();
		// 若程序创建成功则向程序中加入顶点着色器与片元着色器
		if (program != 0) {
			// 向程序中加入顶点着色器
			GLES20.glAttachShader(program, vertexShader);
			checkGlError("glAttachShader");
			// 向程序中加入片元着色器
			GLES20.glAttachShader(program, pixelShader);
			checkGlError("glAttachShader");
			// 链接程序
			GLES20.glLinkProgram(program);
			// 存放链接成功program数量的数组
			int[] linkStatus = new int[1];
			// 获取program的链接情况
			GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS, linkStatus, 0);
			// 若链接失败则报错并删除程序
			if (linkStatus[0] != GLES20.GL_TRUE) {
				Log.e("ES20_ERROR", "Could not link program: ");
				Log.e("ES20_ERROR", GLES20.glGetProgramInfoLog(program));
				GLES20.glDeleteProgram(program);
				program = 0;
			}
		}
		return program;
	}

	// 检查每一步操作是否有错误的方法
	public static void checkGlError(String op) {
		int error;
		while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
			Log.e("ES20_ERROR", op + ": glError " + error);
			throw new RuntimeException(op + ": glError " + error);
		}
	}

	// assets
	public static String loadFromAssetsFile(String fname, Resources r) {

		try {
			return readStringFromInputString(r.getAssets().open(fname));
		} catch (IOException e) {
			 
			e.printStackTrace();
			return null;
		}

	}

	// raw
	public static String loadFromRawResouces(int resoucesId, Resources r) {

		return readStringFromInputString(r.openRawResource(resoucesId));
	}

	private static String readStringFromInputString(InputStream in)
	{
		String result = null;
		try {

			int ch = 0;
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			while ((ch = in.read()) != -1) {
				baos.write(ch);
			}
			byte[] buff = baos.toByteArray();
			baos.close();
			in.close();
			result = new String(buff, HTTP.UTF_8);
			result = result.replaceAll("\\r\\n", "\n");
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (BuildConfig.DEBUG)
			Log.v(TAG, "result:" + result);

		return result;
	}
	
	
	public static int loadTextureWithUtils(InputStream is) 
	{
		return loadTextureWithUtils(is,false);
	}
	// /
		// Load texture from resource
		//
		public static int loadTextureWithUtils(InputStream is,boolean isMipMap)
		{
			int[] textureId = new int[1];

			GLES20.glGenTextures(1, textureId, 0);
			GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId[0]);
			
			
			if(isMipMap)
			{
				//使用MipMap线性纹理采样
				GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,
						GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR_MIPMAP_NEAREST);
			 	//使用MipMap最近点纹理采样 
				GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,
						GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR_MIPMAP_LINEAR);
				
			}else
			{
				//使用MipMap线性纹理采样
				GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,
						GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
			 	//使用MipMap最近点纹理采样 
				GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,
						GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
				
				
				
			}
			GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S,
					GLES20.GL_REPEAT);
			GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T,
					GLES20.GL_REPEAT);
			
			Bitmap bitmap;
			bitmap = BitmapFactory.decodeStream(is);
			GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
			bitmap.recycle();
			 //自动生成Mipmap纹理
			if(isMipMap)
	        GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);
			

			return textureId[0];

		}
		
		
		public static void createVertexBuffer(int target, float[] vertices,
				int bufferId) {
			int size = vertices.length * FloatUtils.RATIO_FLOATTOBYTE;
			FloatBuffer fb = ByteBuffer.allocateDirect(size)
					.order(ByteOrder.nativeOrder()).asFloatBuffer();
			fb.put(vertices);
			fb.flip();

			createBuffer(target, fb, size, bufferId);
			fb.clear();
		}

		public static void createIndexBuffer(int target, short[] indices,
				int bufferId) {
			int size = indices.length * FloatUtils.RATIO_SHORTTOBYTE;
			ShortBuffer sb = ByteBuffer.allocateDirect(size)
					.order(ByteOrder.nativeOrder()).asShortBuffer();
			sb.put(indices);
			sb.flip();

			createBuffer(target, sb, size, bufferId);
			sb.clear();
		}

		public static void createBuffer(int target, Buffer buf, int size,
				int bufferId) {
			GLES20.glBindBuffer(target, bufferId);
			GLES20.glBufferData(target, size, buf, GLES20.GL_STATIC_DRAW);
			GLES20.glBindBuffer(target, 0);
		}
}
