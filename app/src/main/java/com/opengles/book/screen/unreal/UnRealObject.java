package com.opengles.book.screen.unreal;

import android.content.Context;
import android.opengl.GLES20;
import android.util.Log;

import com.giants3.android.openglesframework.framework.MatrixState;
import com.giants3.android.openglesframework.framework.utils.FloatUtils;
import com.opengles.book.*;
import com.opengles.book.galaxy.ObjectDrawable;
import com.opengles.book.objLoader.Material;
import com.opengles.book.objLoader.ObjModel;
import com.opengles.book.objLoader.ObjModelPart;
import com.opengles.book.objLoader.ObjectParser;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author 无纹理数据的对象绘制。非真实感绘制。
 * @create : 2014-1-7 下午9:22:33
 *         .obj 文件对象 绘制
 */
public class UnRealObject implements ObjectDrawable {

	protected static final int VERTEX_POS_INDEX = 0;// xyz
	protected static final int VERTEX_NORMAL_INDEX = 1;// xyz
// 	protected static final int VERTEX_TEXTURE_CORD_INDEX = 2;// s t

	protected static final int VERTEX_POS_SIZE = 3;// xyz
	protected static final int VERTEX_NORMAL_SIZE = 3;// xyz
 	protected static final int VERTEX_TEXTURE_CORD_SIZE = 2;// s t

	protected static final int STRIP_SIZE = (VERTEX_POS_SIZE
			+ VERTEX_NORMAL_SIZE
			 + VERTEX_TEXTURE_CORD_SIZE
			)
			* FloatUtils.RATIO_FLOATTOBYTE;
	String TAG = "Teapot";

	int mProgram;// 着色器id
	int muMVPMatrixHandle;// �ܱ任��������id
	int mUMatrixHandle; // 物体变换矩阵句柄

	int muLightLocationSun;
	int mabientLightHandler;// 环境光
	int mDiffuseLightHandler;// 散射光
	int mSpecLightHandler;// 反射光
	// int mMapLoc;

	int mCameraPositionHandler;// 相机位置

	// int textureId;
	ObjModel model;
	int[] vboIds;

	private Context context;

	Map<String, Integer> textureMap = new HashMap<String, Integer>();

	public UnRealObject(Context context)
	{
		this(context, "unrealobjects/", "tz.obj");
	}

	public UnRealObject(Context context, String path, String fileName)
	{

		this.context = context;
		model = ObjectParser.parse(context, path, fileName);

		// 创建program
		String mVertexShader = ShaderUtil.loadFromAssetsFile(
			path+	"vertex.glsl",
				context.getResources());
		String mFragmentShader = ShaderUtil.loadFromAssetsFile(
				path+	"frag.glsl",
				context.getResources());
		mProgram = ShaderUtil.createProgram(mVertexShader, mFragmentShader);

		GLES20.glBindAttribLocation(mProgram, VERTEX_POS_INDEX, "aPosition");
		GLES20.glBindAttribLocation(mProgram, VERTEX_NORMAL_INDEX,
				"aNormal");
//		GLES20.glBindAttribLocation(mProgram, VERTEX_TEXTURE_CORD_INDEX,
//				"aTexCoor");

		GLES20.glLinkProgram(mProgram);

		Log.d(TAG, "mProgram" + mProgram);

		// 总变换阵 id
		muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
		// 物体运动针id
		mUMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMMatrix");

		muLightLocationSun = GLES20.glGetUniformLocation(mProgram,
				"uLightLocation");

		mCameraPositionHandler = GLES20.glGetUniformLocation(mProgram,
				"cameraPosition");

		// 材料光属性
		mDiffuseLightHandler = GLES20.glGetUniformLocation(mProgram,
				"lightDiffuse");

		mSpecLightHandler = GLES20.glGetUniformLocation(mProgram,
				"lightSpecular");
		mabientLightHandler = GLES20.glGetUniformLocation(mProgram,
				"abientLight");
	}

	@Override
	public void bind() {

		try {
			createTexture(context, model);
		} catch (IOException e) {

			e.printStackTrace();
		}

		vboIds = new int[2];
		GLES20.glGenBuffers(2, vboIds, 0);
		ShaderUtil.createVertexBuffer(GLES20.GL_ARRAY_BUFFER,
				model.vertexData,
				vboIds[0]);
		ShaderUtil.createIndexBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER,
				model.indexData,
				vboIds[1]);

		Log.d(TAG, "vboIds:" + vboIds[0] + "," + vboIds[1]);

		// 预先绑定固定值 不需要在绘制时候重新绑定。
		GLES20.glUseProgram(mProgram);
		// 绑定固定值， 会变换的数值 在draw中绑定。

		// 注入相机位置数据
		GLES20.glUniform3fv(mCameraPositionHandler, 1,
				MatrixState.cameraFB);

		GLES20.glUseProgram(0);

	}

	@Override
	public void unBind() {
		GLES20.glDeleteBuffers(2, vboIds, 0);
		deleteTexture();

	}

	@Override
	public void draw() {
		GLES20.glUseProgram(mProgram);

		// 启用位置向量数据
		GLES20.glEnableVertexAttribArray(VERTEX_POS_INDEX);
		// 启用法向量数据
		GLES20.glEnableVertexAttribArray(VERTEX_NORMAL_INDEX);
//		GLES20.glEnableVertexAttribArray(VERTEX_TEXTURE_CORD_INDEX);

		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vboIds[0]);
		GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, vboIds[1]);

		int offset = 0;
		int stride = STRIP_SIZE;
		GLES20.glVertexAttribPointer
				(
						VERTEX_POS_INDEX,
						VERTEX_POS_SIZE,
						GLES20.GL_FLOAT,
						false,
						stride,
						offset
				);

		// 启用法向量
		offset += VERTEX_POS_SIZE * FloatUtils.RATIO_FLOATTOBYTE;

		GLES20.glVertexAttribPointer
				(
						VERTEX_NORMAL_INDEX,
						VERTEX_NORMAL_SIZE,
						GLES20.GL_FLOAT,
						false,
						stride,
						offset
				);

//		offset += VERTEX_NORMAL_SIZE * FloatUtils.RATIO_FLOATTOBYTE;
//
//		GLES20.glVertexAttribPointer
//				(
//						VERTEX_TEXTURE_CORD_INDEX,
//						VERTEX_TEXTURE_CORD_SIZE,
//						GLES20.GL_FLOAT,
//						false,
//						stride,
//						offset
//				);

		// 总变换矩阵
		GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false,
				MatrixState.getFinalMatrix(), 0);

		// 物体移动变换矩阵。
		GLES20.glUniformMatrix4fv(mUMatrixHandle, 1, false,
				MatrixState.getMMatrix(), 0);

		// 注入太阳光位置 光线会转动
		GLES20.glUniform3fv(muLightLocationSun, 1,
				LightSources.lightPositionFBSun);

		List<ObjModelPart> partList = model.parts;

		int size = partList.size();
		for (int i = 0; i < size; i++) {
			ObjModelPart part = partList.get(i);
			Material material = part.material;
			// LightSources.setAmbient(material.ambientColor[0],material.ambientColor[1],material.ambientColor[2],material.alpha);
			// LightSources.setDiffuse(material.diffuseColor[0],material.diffuseColor[1],material.diffuseColor[2],material.alpha);
			// LightSources.setSpecLight(material.specularColor[0],material.specularColor[1],material.specularColor[2],material.alpha);

			// 注入环境光光数据
			// GLES20.glUniform4fv(mabientLightHandler, 1,
			// part.material.getAmbientBuffer());
			//
			// 注入散射光数据
			// GLES20.glUniform4fv(mDiffuseLightHandler, 1,
			// part.material.getDiffuseBuffer());
			//
			// // 注入镜面光数据
			// GLES20.glUniform4fv(mSpecLightHandler, 1,
			// part.material.getSpecularBuffer());

			GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, getTextureId(model.path,material.textureFile));
			// 注入环境光光数据
			GLES20.glUniform4fv(mabientLightHandler, 1,
					LightSources.ambientBuffer);

			// 注入散射光数据
			GLES20.glUniform4fv(mDiffuseLightHandler, 1,
					LightSources.diffuseBuffer);

			// 注入镜面光数据
			GLES20.glUniform4fv(mSpecLightHandler, 1,
					LightSources.specLightBuffer);

			GLES20.glDrawElements(GLES20.GL_TRIANGLES, part.length,
					GLES20.GL_UNSIGNED_SHORT, part.index
							* FloatUtils.RATIO_SHORTTOBYTE);

		}

		// 解除绑定
		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
		GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, 0);
		// 关闭位置向量数据
		GLES20.glDisableVertexAttribArray(VERTEX_POS_INDEX);
		// 关闭法向量数据
		GLES20.glDisableVertexAttribArray(VERTEX_NORMAL_INDEX);
//		// 关闭纹理数据
//		GLES20.glDisableVertexAttribArray(VERTEX_TEXTURE_CORD_INDEX);

	}

	/**
	 * 加载纹理
	 * 
	 * @param context
	 * @throws IOException
	 */
	public void createTexture(Context context, ObjModel model)
			throws IOException
	{

		for (ObjModelPart part : model.parts)
		{
			if(part.material.textureFile==null) continue;
			String totalFileName = model.path + part.material.textureFile;
			int textureId = ShaderUtil.loadTextureWithUtils(context.getAssets()
					.open(totalFileName));
			textureMap.put(totalFileName, textureId);

		}

	}

	public void deleteTexture()
	{
		Collection<Integer> textureIds = textureMap.values();

		int size = textureIds.size();
		int[] textureArray = new int[size];
		Iterator<Integer> iterator = textureIds.iterator();
		int i = 0;
		while (iterator.hasNext())
		{
			textureArray[i++] = iterator.next();
		}

		GLES20.glDeleteTextures(textureIds.size(), textureArray, 0);
	}

	private int getTextureId(String path,String fileName) {
		 
		Integer result= textureMap.get(path+fileName);
		if(null==result)
			return 0;
			
		return result;
	}


    @Override
    public void update(float deltaTime) {

    }
}
