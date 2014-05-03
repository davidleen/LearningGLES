package com.opengles.book.objLoader;

import java.util.Arrays;
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


//    public void setParts(List<ObjModelPart> parts)
//    {
//        this.parts=parts;
//        int length=0;
//        for(ObjModelPart part:parts)
//        {
//            length+=part.indexData.length;
//
//
//        }
//        indexData=new short[length];
//        int index=0;
//        for(ObjModelPart part:parts)
//        {
//            for(short value:part.indexData)
//                indexData[index++]=value;
//
//        }
//
//
//    }

//    public short[] getIndexData() {
//
//
//
//        return indexData;
//
//    }
}
