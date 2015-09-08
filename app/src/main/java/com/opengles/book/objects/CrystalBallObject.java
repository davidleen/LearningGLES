package com.opengles.book.objects;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.util.Log;
import android.util.SparseArray;
import com.opengles.book.*;
import com.opengles.book.framework.gl.CubeTexture;
import com.opengles.book.framework.gl.Texture;
import com.opengles.book.galaxy.ObjectDrawable;
import com.opengles.book.objLoader.Material;
import com.opengles.book.objLoader.ObjModel;
import com.opengles.book.objLoader.ObjModelPart;
import com.opengles.book.objLoader.ObjectParser;
import com.opengles.book.utils.TextureMap;

import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author davidleen29 
 * @create : 2014-4-25 下午11:44:25
 * @{  简单对象  shader 都是固定的   抽取公用方法和属性。}
 */
public   class CrystalBallObject implements ObjectDrawable {


	protected String TAG=CrystalBallObject.this.getClass().getName();





	protected static final int VERTEX_POS_SIZE = 3;// xyz
	 protected static final int VERTEX_NORMAL_SIZE = 3;// xyz
	protected static final int VERTEX_TEXTURE_COORD_SIZE = 2;// s t

	protected static final int STRIP_SIZE = (VERTEX_POS_SIZE
			+ VERTEX_NORMAL_SIZE
			+ VERTEX_TEXTURE_COORD_SIZE)
			* FloatUtils.RATIO_FLOATTOBYTE;

	int mProgram;//自定义渲染管线程序id
    int muMVPMatrixHandle;//总变换矩阵引用id
    int maPositionHandle; //顶点位置属性引用id
    int maNormalHandle;//向量属性索引
    int maTexCoorHandle; //顶点纹理坐标属性引用id
    String mVertexShader;//顶点着色器
    String mFragmentShader;//片元着色器


    int alphaThreadHoldHandler;


    int  cubeMapHandler;//立体纹理引用id
    //材质纹理id
    int  mMapLocHandler;

    int uCameraHandler;//相机引用id
    int uMMatrixHandler;//物体变换矩阵id


    private CubeTexture cubeTexture;



    int[] vboIds;

    int indexLength;

    ObjModel model;

    //光源位置
    int muLightLocationSun;
    int mabientLightHandler;// 环境光
    int mDiffuseLightHandler;// 散射光
    int mSpecLightHandler;// 反射光

    private Context context;

    

    // Sphere sphere;

        public CrystalBallObject(Context context)
        {

        this.context=context;
         //  model  = ObjectParser.parse(context, "tz/", "tz.obj");
        //   model  = ObjectParser.parse(context, "twistcuboid/", "cuboid.obj");
           model  = ObjectParser.parse(context, "sphere/", "sphere.obj");
         //   sphere=new Sphere(5,true);
            String path="crystalball/";
          //  cubeTexture=new CubeTexture(context.getResources(),new String[]{path+"basketball.png"});
            //  cubeTexture=new CubeTexture(context.getResources(),new String[]{path+"blue.png",path+"sky.png",path+"mask.png",path+"tree.png",path+"tree.png",path+"tree.png"});
           cubeTexture=new CubeTexture(context.getResources(),new String[]{path+"basketball.png",path+"blue.png",path+"sky.png",path+"mask.png",path+"tree.png",path+"water.png"});
            

            //初始化shader
            initShader(context);
        }





    @Override
		public void bind() {


			vboIds = new int[2];

			GLES20.glGenBuffers(2, vboIds, 0);

			ShaderUtil.createVertexBuffer(GLES20.GL_ARRAY_BUFFER,
                    model.vertexData,
					vboIds[0]);

			short[] indexData=model.indexData;
			indexLength=indexData.length;
			ShaderUtil.createIndexBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER,
					indexData,
					vboIds[1]);

			Log.d(TAG, "vboIds:" + vboIds[0] + "," + vboIds[1]);


            cubeTexture.reload();

            
            List<ObjModelPart> partList = model.parts;
            int size = partList.size();
            for (int i = 0; i < size; i++) {
                ObjModelPart part = partList.get(i);

                TextureMap.create(context, model.path + part.material.textureFile);
            }

		}




		@Override
		public void unBind() {
			//移除纹理





		}


        public void dispose()
        {


            GLES20.glDeleteBuffers(2, vboIds, 0);
            cubeTexture.dispose();

        }

		@Override
		public void draw() {
			 //指定使用某套shader程序
	   	 GLES20.glUseProgram(mProgram);


	   	 

	   	 
            GLES20.glEnable(GLES20.GL_TEXTURE_CUBE_MAP);
 
           cubeTexture.bind();
           GLES20.glUniform1f(cubeMapHandler,0); 
           
           
            //注入总变换矩阵
	        GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(), 0);
            //注入物体变换矩阵
            GLES20.glUniformMatrix4fv(uMMatrixHandler, 1, false, MatrixState.getMMatrix(), 0);

            // 注入太阳光位置 光线会转动
            GLES20.glUniform3fv(muLightLocationSun, 1,
                    LightSources.lightPositionFBSun);


            GLES20.glUniform3fv(uCameraHandler,1,MatrixState.cameraFB);

	         

	        // 启用位置向量数据
			GLES20.glEnableVertexAttribArray(maPositionHandle);
			// 启用法向量数据
			 GLES20.glEnableVertexAttribArray(maNormalHandle );
			//启用纹理
			GLES20.glEnableVertexAttribArray(maTexCoorHandle);

	        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vboIds[0]);
			GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, vboIds[1]);

			int offset = 0;
			int stride = STRIP_SIZE;
			GLES20.glVertexAttribPointer
                    (
                            maPositionHandle,
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
                            maNormalHandle,
							VERTEX_NORMAL_SIZE,
							GLES20.GL_FLOAT,
							false,
							stride,
							offset
					);

			offset += VERTEX_NORMAL_SIZE * FloatUtils.RATIO_FLOATTOBYTE;

			GLES20.glVertexAttribPointer
					(
							maTexCoorHandle,
                            VERTEX_TEXTURE_COORD_SIZE,
							GLES20.GL_FLOAT,
							false,
							stride,
							offset
					);


            List<ObjModelPart> partList = model.parts;
            //绑定纹理  GL_TEXTURE1 对应 1
            GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
            int size = partList.size();
            for (int i = 0; i < size; i++) {
                ObjModelPart part = partList.get(i);
                if(part.length<=0) continue;
                Material material = part.material;
                 LightSources.setAmbient(material.ambientColor[0],material.ambientColor[1],material.ambientColor[2],material.alpha);
                 LightSources.setDiffuse(material.diffuseColor[0],material.diffuseColor[1],material.diffuseColor[2],material.alpha);
                LightSources.setSpecLight(material.specularColor[0],material.specularColor[1],material.specularColor[2],material.alpha);




                TextureMap.instance.get(model.path+material.textureFile).bind();
                GLES20.glUniform1f(mMapLocHandler,0);

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
			// 启用位置向量数据
			GLES20.glDisableVertexAttribArray(maPositionHandle);
			// 禁用法向量数据
		   GLES20.glDisableVertexAttribArray(maNormalHandle);
			//禁用纹理向量数据
			GLES20.glDisableVertexAttribArray(maTexCoorHandle);

		}





	    
	    
	    
	    //初始化shader
	    public void initShader(Context mv)
	    {
	    	//加载顶点着色器的脚本内容
	        mVertexShader=ShaderUtil.loadFromAssetsFile("crystalball/vertex.glsl", mv.getResources());
	        //加载片元着色器的脚本内容
	        mFragmentShader=ShaderUtil.loadFromAssetsFile("crystalball/frag.glsl", mv.getResources());
	        //基于顶点着色器与片元着色器创建程序
	        mProgram = ShaderUtil.createProgram(mVertexShader, mFragmentShader);
	        //获取程序中顶点位置属性引用id  
	        maPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");
            //向量属性索引
            maNormalHandle = GLES20.glGetAttribLocation(mProgram, "aNormal");
	        //获取程序中顶点纹理坐标属性引用id  
	        maTexCoorHandle= GLES20.glGetAttribLocation(mProgram, "aTexCoor");
	        //获取程序中总变换矩阵引用id
	        muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");  
	    


            //立体纹理对应的id
	        cubeMapHandler= GLES20.glGetUniformLocation(mProgram, "sTexture");
            //相机位置id
            uCameraHandler= GLES20.glGetUniformLocation(mProgram, "uCamera");

            //物体变换矩阵id
            uMMatrixHandler= GLES20.glGetUniformLocation(mProgram, "uMMatrix");

            //光源位置
            muLightLocationSun = GLES20.glGetUniformLocation(mProgram,
                    "uLightLocation");

            // 材料光属性
            mDiffuseLightHandler = GLES20.glGetUniformLocation(mProgram,
                    "lightDiffuse");

            mSpecLightHandler = GLES20.glGetUniformLocation(mProgram,
                    "lightSpecular");
            mabientLightHandler = GLES20.glGetUniformLocation(mProgram,
                    "abientLight");

            //材质纹理id
            mMapLocHandler=  GLES20.glGetUniformLocation(mProgram,
                "mMapLoc");

        }
	    
	    
	   
	    

	    
	    
	    
	    



        @Override
        public void update(float deltaTime) {

        }




}
