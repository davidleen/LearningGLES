package com.opengles.book.objects;

import android.content.Context;
import android.opengl.GLES20;

import com.giants3.android.openglesframework.framework.MatrixState;
import com.giants3.android.openglesframework.framework.gl.Texture;
import com.giants3.android.openglesframework.framework.utils.FloatUtils;
import com.opengles.book.*;

import com.opengles.book.galaxy.ObjectDrawable;
import com.opengles.book.glsl.*;
import com.opengles.book.objLoader.*;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.util.*;

/**
 *  flying eager that  use mix vertex technique;
 *
 *  关键帧动画。
 * Created by Administrator on 2014/6/9.
 */
public class FlyingEager implements ObjectDrawable {





    float timeCollapsed=0;
    float TIME_FOR_RADIO_CHANGED=0.05f;

    float direction=1;
    @Override
    public void update(float deltaTime) {

        timeCollapsed+=deltaTime;
        if(timeCollapsed>TIME_FOR_RADIO_CHANGED)
        {
            timeCollapsed=timeCollapsed-TIME_FOR_RADIO_CHANGED;
            //更新融合比例值。
            combineRadio[0]+=(direction*0.05f);
            if( combineRadio[0]>2.0) {
                combineRadio[0] = 2.0f;
                direction*=-1; //调整方向
            }else
            if( combineRadio[0]<0)
            {
                combineRadio[0]=0;
                direction*=-1; //调整方向

            }
        }



    }

    String TAG = FlyingEager.this.getClass().getName();

    int mProgram;// 着色器id

    Uniform4fv ambientLightUniform;// 环境光
    Uniform4fv diffuseLightUniform;// 散射光
    Uniform4fv specLightUniform;// 反射光
    Uniform1fv combineRadioUniform;// 融合比例

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


    private  float[] combineRadio=new float[]{1.0f};




    public FlyingEager(Context context  )
    {

        this.context = context;


        ObjModel eager= ObjectParser.parse(context,"eager/","eager.obj");
        ObjModel eager1= ObjectParser.parse(context,"eager/","eager1.obj");
        ObjModel eager2= ObjectParser.parse(context,"eager/","eager2.obj");


        model=new ObjModel();
        model.indexData=eager.indexData;
        model.parts=eager.parts;
        model.path=eager.path;
        //3个顶点 1个法向量 1个纹理
        model.vertexStripSize=ObjModel.VERTEX_POS_SIZE*3+ObjModel.VERTEX_NORMAL_SIZE+ObjModel.VERTEX_TEXTURE_SIZE;
        model.vertexBuffStripSize= model.vertexStripSize* FloatUtils.RATIO_FLOATTOBYTE;
        //拼接顶点顶点。  3个obj 文件  法向量  纹理 完全相同，  仅顶点不同。

        int vertexCount=eager.vertexData.length/eager.vertexStripSize;
        float[] vertexData=new float[vertexCount*model.vertexStripSize];
        for(int i=0;i<vertexCount;i++)
        {

            for(int j=0;j<ObjModel.VERTEX_POS_SIZE;j++) {
                //复制第一个顶点
                vertexData[i * model.vertexStripSize + j] = eager.vertexData[i * eager.vertexStripSize + j];
                //复制第二个顶点
                vertexData[i * model.vertexStripSize+ObjModel.VERTEX_POS_SIZE + j] = eager1.vertexData[i * eager1.vertexStripSize + j];
                //复制第三个顶点
                vertexData[i * model.vertexStripSize+ObjModel.VERTEX_POS_SIZE*2 + j] = eager2.vertexData[i * eager2.vertexStripSize + j];
                //复制法向量
                vertexData[i * model.vertexStripSize +ObjModel.VERTEX_POS_SIZE*3 +j] = eager.vertexData[i * eager.vertexStripSize +ObjModel.VERTEX_POS_SIZE+ j];

            }
            //复制纹理。
            for(int j=0;j<ObjModel.VERTEX_TEXTURE_SIZE;j++)
            vertexData[i * model.vertexStripSize +ObjModel.VERTEX_POS_SIZE*4 +j] = eager.vertexData[i * eager.vertexStripSize +ObjModel.VERTEX_POS_SIZE+ObjModel.VERTEX_NORMAL_SIZE+ j];




        }

        //set vertexData;
        model.vertexData=vertexData;



        // 创建program
        String mVertexShader = ShaderUtil.loadFromAssetsFile(
                getVertexFileName(),
                context.getResources());
        String mFragmentShader = ShaderUtil.loadFromAssetsFile(
                getFragmentFileName(),
                context.getResources());
        mProgram = ShaderUtil.createProgram(mVertexShader, mFragmentShader);


        //uniform 属性绑定
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
        cameraUniform=new Uniform3fv(mProgram,"uCamera",new Uniform.UniformBinder<FloatBuffer>() {
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


        //配置顶点属性
        attributeWrap=new Vertices(new String[]{"aPosition","bPosition","cPosition","aNormal","aTexCoor"},new int[]{ObjModel.VERTEX_POS_SIZE,ObjModel.VERTEX_POS_SIZE,ObjModel.VERTEX_POS_SIZE,ObjModel.VERTEX_NORMAL_SIZE,ObjModel.VERTEX_TEXTURE_SIZE},mProgram);



        //配置融合比例参数 。
        combineRadioUniform=new Uniform1fv(mProgram,"combineRadio",new Uniform.UniformBinder<float[]>() {
            @Override
            public float[] getBindValue() {
                return combineRadio;
            }
        });








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

        GLES20.glUseProgram(0);




    }

    @Override
    public void unBind() {
        attributeWrap.dispose();
        deleteTexture();

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


        //注入融合比例
        combineRadioUniform.bind();




        List<ObjModelPart> partList = model.parts;

        int size = partList.size();
        for (int i = 0; i < size; i++) {
            ObjModelPart part = partList.get(i);
            if(part.length<=0) continue;
            Material material = part.material;
            LightSources.setAmbient(material.ambientColor[0],material.ambientColor[1],material.ambientColor[2],material.alpha);
            LightSources.setDiffuse(material.diffuseColor[0],material.diffuseColor[1],material.diffuseColor[2],material.alpha);
            LightSources.setSpecLight(material.specularColor[0],material.specularColor[1],material.specularColor[2],material.alpha);
            //specify texture as color attachment
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






    /**
     * @return
     */
    protected String getFragmentFileName() {
        return "objAnim/frag.glsl";
    }

    /**
     * @return
     */
    protected String getVertexFileName() {
        return "objAnim/vertex.glsl";
    }





}
