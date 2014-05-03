package com.opengles.book.objLoader;

import java.util.Vector;

/**
 * obj模型数据 基本元素
 * Created by davidleen29   qq:67320337
 * on 14-4-16.
 */
public class ObjModelPart {


   public Material material;
    public int index;
    public int length;

    public ObjModelPart(Material material) {
        this.material = material;
    }


}
