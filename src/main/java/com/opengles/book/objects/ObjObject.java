package com.opengles.book.objects;

import android.content.Context;
import android.opengl.GLES20;
import android.util.Log;
import com.opengles.book.*;
import com.opengles.book.framework.gl.CubeTexture;
import com.opengles.book.framework.gl.Texture;
import com.opengles.book.galaxy.ObjectDrawable;
import com.opengles.book.glsl.Uniform;
import com.opengles.book.glsl.Uniform3fv;
import com.opengles.book.glsl.Uniform4fv;
import com.opengles.book.glsl.UniformMatrix4F;
import com.opengles.book.math.Vector3;
import com.opengles.book.objLoader.*;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author davidleen29
 * @create : 2014-1-7 下午9:22:33
 *         .obj 文件对象 绘制
 */
public class ObjObject implements ObjectDrawable {



	protected static final int VERTEX_POS_SIZE = 3;// xyz
	protected static final int VERTEX_NORMAL_SIZE = 3;// xyz
	protected static final int VERTEX_TEXTURE_CORD_SIZE = 2;// s t

//	protected static final int STRIP_SIZE = (VERTEX_POS_SIZE
//			+ VERTEX_NORMAL_SIZE
//			+ VERTEX_TEXTURE_CORD_SIZE)
//			* FloatUtils.RATIO_FLOATTOBYTE;
	String TAG = ObjObject.this.getClass().getName();

	int mProgram;// 着色器id
	//int muMVPMatrixHandle;//总变换矩阵handler
	//int mUMatrixHandle; // 物体变换矩阵句柄



    Uniform4fv ambientLightUniform;// 环境光
    Uniform4fv diffuseLightUniform;// 散射光
    Uniform4fv specLightUniform;// 反射光

   // 总变换矩阵属性
    private UniformMatrix4F finalMatrix;
    //物体变换矩阵属性
    private UniformMatrix4F objectMatrix;

    //相机位置属性
    private Uniform3fv cameraUniform;
    //太阳光位置属性  //光源位置。
    private Uniform3fv sunLocationUniform;


	// int textureId;
	protected ObjModel model;

	private Context context;

	Map<String, Integer> textureMap = new HashMap<String, Integer>();



    //顶点绘制类
    private Vertices attributeWrap;

    public Texture texture;

	public ObjObject(Context context)
	{
		this(context, "tz/", "tz.obj");
	}

	public ObjObject(Context context, String path, String fileName)
	{

		this.context = context;

		model = ObjectParser.parse(context, path, fileName);



		// 创建program
		String mVertexShader = ShaderUtil.loadFromAssetsFile(
				getVertexFileName(),
				context.getResources());
		String mFragmentShader = ShaderUtil.loadFromAssetsFile(
				getFragmentFileName(),
				context.getResources());
		mProgram = ShaderUtil.createProgram(mVertexShader, mFragmentShader);


        finalMatrix=new UniformMatrix4F(mProgram,"uMVPMatrix",new Uniform.UniformBinder<float[]>() {
            @Override
            public float[] getBindValue() {
                return MatrixState.getFinalMatrix();
            }
        });

        objectMatrix=new UniformMatrix4F(mProgram,"uMMatrix",new Uniform.UniformBinder<float[]>() {
            @Override
            public float[] getBindValue() {
                return MatrixState.getMMatrix();
            }
        });
        cameraUniform=new Uniform3fv(mProgram,"cameraPosition",new Uniform.UniformBinder<FloatBuffer>() {
            @Override
            public FloatBuffer getBindValue() {
                return MatrixState.cameraFB;
            }
        });


        sunLocationUniform=new Uniform3fv(mProgram,"uLightLocation",new Uniform.UniformBinder<FloatBuffer>() {
            @Override
            public FloatBuffer getBindValue() {
                return LightSources.lightPositionFBSun;
            }
        });
        // 材料光属性
        ambientLightUniform=new Uniform4fv(mProgram,"abientLight",new Uniform.UniformBinder<FloatBuffer>() {
            @Override
            public FloatBuffer getBindValue() {
                return LightSources.ambientBuffer;
            }
        });

        specLightUniform=new Uniform4fv(mProgram,"lightSpecular",new Uniform.UniformBinder<FloatBuffer>() {
            @Override
            public FloatBuffer getBindValue() {
                return LightSources.specLightBuffer;
            }
        });
        diffuseLightUniform=new Uniform4fv(mProgram,"lightDiffuse",new Uniform.UniformBinder<FloatBuffer>() {
            @Override
            public FloatBuffer getBindValue() {
                return LightSources.diffuseBuffer;
            }
        });


        
        attributeWrap=new Vertices(new String[]{"aPosition","aNormal","aTexCoor"},new int[]{VERTEX_POS_SIZE,VERTEX_NORMAL_SIZE,VERTEX_TEXTURE_CORD_SIZE},mProgram);










		
		
		onCreate(mProgram);
	}

	

	@Override
	public void bind() {

		try {
			createTexture(context, model);
		} catch (IOException e) {
			 
			e.printStackTrace();
		}


        attributeWrap.create(model.vertexData,model.indexData);


		// 预先绑定固定值 不需要在绘制时候重新绑定。
		GLES20.glUseProgram(mProgram);
		// 绑定固定值， 会变换的数值 在draw中绑定。
        onBind(mProgram);
		GLES20.glUseProgram(0);
		
		


	}

	@Override
	public void unBind() {
        attributeWrap.dispose();
		deleteTexture();
		onUnBind(mProgram);
	}

	@Override
	public void draw() {
		GLES20.glUseProgram(mProgram);



        //属性绑定
        attributeWrap.bind();
        //最终矩阵绑定
        finalMatrix.bind();
        //物体移动矩阵绑定
        objectMatrix.bind();
        //相机位置绑定
        cameraUniform.bind();
        //重新注入太阳光位置 光线会转动
        sunLocationUniform.bind();




		
		
		onDraw(mProgram);

		List<ObjModelPart> partList = model.parts;

		int size = partList.size();
		for (int i = 0; i < size; i++) {
			ObjModelPart part = partList.get(i);
			if(part.length<=0) continue;
			Material material = part.material;
 			 LightSources.setAmbient(material.ambientColor[0],material.ambientColor[1],material.ambientColor[2],material.alpha);
 			 LightSources.setDiffuse(material.diffuseColor[0],material.diffuseColor[1],material.diffuseColor[2],material.alpha);
 			 LightSources.setSpecLight(material.specularColor[0],material.specularColor[1],material.specularColor[2],material.alpha);

			 

			GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, getTextureId(model.path,material.textureFile));


              ambientLightUniform.bind();// 环境光
              diffuseLightUniform.bind();// 散射光
              specLightUniform.bind();// 反射光

            attributeWrap.draw(GLES20.GL_TRIANGLES,
                    part.index, part.length
            );

        }
        attributeWrap.unbind();


	}

	/**
	 * 加载纹理
	 * 
	 *
	 * @param context
	 * @param model
	 * @throws IOException
	 */
	public void createTexture(Context context, ObjModel model)
			throws IOException
	{

		for (ObjModelPart part : model.parts)
		{
			
			String totalFileName = model.path + part.material.textureFile;
			int textureId;
			if(part.material.textureFile.equals(Material.DEFAULT_PNG)) 
			{
				 textureId = ShaderUtil.loadTextureWithUtils(context.getAssets()
							.open("objObject/default.png"));
			}
			else
				
			  textureId = ShaderUtil.loadTextureWithUtils(context, totalFileName,false );
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

	
	
	protected void onBind(int mProgram)
	{
		//let sub class do any more  for bind;
	}
	
	protected void onUnBind(int mProgram)
	{
		
		//let sub class do any more  for unbind;
		
	}
	
	
	protected void onDraw(int mProgram)
	{
		//let sub class do any more  for bind to the value;
	}
	
	protected void onCreate(int mProgram)
	{
		//let sub class do any more  for get handler ;
	}
	
	 
	
	/**
	 * @return
	 */
	protected String getFragmentFileName() {
		return "objObject/frag.glsl";
	}

	/**
	 * @return
	 */
	protected String getVertexFileName() {
		return "objObject/vertex.glsl";
	}


    @Override
    public void update(float deltaTime) {

    }

    public AABB getBoundary() {
        return model.boundary;
    }
}
