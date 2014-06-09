package com.opengles.book.objLoader;

import java.util.List;

/**
 * 3D obj 模型数据
 * Created by davidleen29   qq:67320337
 * on 14-4-16.
 */
public class ObjModel {

	public String path; 

    public float[] vertexData;
    public List<ObjModelPart> parts;
    public short[] indexData;

    public AABB boundary;


}
