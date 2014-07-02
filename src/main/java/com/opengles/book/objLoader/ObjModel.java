package com.opengles.book.objLoader;

import com.opengles.book.FloatUtils;

import java.util.List;

/**
 * 3D obj 模型数据
 * Created by davidleen29   qq:67320337
 * on 14-4-16.
 */
public class ObjModel {

    //关闭法向量
    protected static final int VERTEX_POS_SIZE = 3;// xyz
     protected static final int VERTEX_NORMAL_SIZE = 3;// xyz
    protected static final int VERTEX_TEXTURE_SIZE = 2;// s t
    protected static final int VERTEX_STRIP_SIZE=VERTEX_POS_SIZE+VERTEX_NORMAL_SIZE+VERTEX_TEXTURE_SIZE;
    protected static final int VERTEX_STRIP_SIZE_OF_BUFFER = VERTEX_STRIP_SIZE
            * FloatUtils.RATIO_FLOATTOBYTE;

	public String path; 

    public float[] vertexData;
    public List<ObjModelPart> parts;
    public short[] indexData;

    public AABB boundary;

    //每个顶点对应的buff 大小
    public int vertexBuffStripSize=VERTEX_STRIP_SIZE_OF_BUFFER;

    public int vertexStripSize=VERTEX_STRIP_SIZE;
}
