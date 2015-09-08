package com.opengles.book.screen.dollDemo;

import android.content.Context;
import com.opengles.book.FloatUtils;
import com.opengles.book.glsl.Uniform;
import com.opengles.book.glsl.Uniform4fv;
import com.opengles.book.objLoader.ObjModel;
import com.opengles.book.objects.ObjObject;

import java.nio.FloatBuffer;

/**
 * 人偶各部件绘制。
 * Created by davidleen29   qq:67320337
 * on 2014-7-8.
 */
public class DollPartObjObject  extends ObjObject{




    private static float[] UN_PICK_COLOR=new float[]{1.0f,1.0f,1.0f,1.0f};
    private  static float[] PICK_COLOR=new float[]{1.0f,0f,0.0f,1f};
    private float[] pickedColor=UN_PICK_COLOR;


    Uniform4fv pickColorUniform;// 选中状态下 颜色调整值

    public DollPartObjObject(Context context, ObjModel objModel) {
        super(context, objModel);
    }

    public  void setPick(boolean pick)
    {

        if(pick)
        {
            pickedColor= PICK_COLOR;
        }else
        {
            pickedColor= UN_PICK_COLOR;
        }

    }

    @Override
    protected void onBind(int mProgram) {
        super.onBind(mProgram);
    }

    @Override
    protected void onUnBind(int mProgram) {
        super.onUnBind(mProgram);
    }

    @Override
    protected void onDraw(int mProgram) {
        super.onDraw(mProgram);
       pickColorUniform.bind();
    }

    @Override
    protected void onCreate(int mProgram) {
        super.onCreate(mProgram);
        pickColorUniform=new Uniform4fv(mProgram,"pickColor",new Uniform.UniformBinder<FloatBuffer>() {
            @Override
            public FloatBuffer getBindValue() {
                return FloatUtils.FloatArrayToNativeBuffer(pickedColor);
            }
        });
    }


    /**
     * @return
     */
    protected String getFragmentFileName() {
        return "doll/frag.glsl";
    }

    /**
     * @return
     */
    protected String getVertexFileName() {
        return "doll/vertex.glsl";
    }

}
