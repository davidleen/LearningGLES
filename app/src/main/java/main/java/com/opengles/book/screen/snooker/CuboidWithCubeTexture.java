package com.opengles.book.screen.snooker;

import android.content.Context;

/**
 * 长方体 顶点构造类。
 * Created by davidleen29   qq:67320337
 * on 2014-7-17.
 */
public class CuboidWithCubeTexture {

    //顶点数据
    public float[] vertexData;
    //索引数据
    public short[] indexData;

    public    CuboidWithCubeTexture( float xLength,float yLength,float zLength)
    {

        vertexData=new float[]{
                //vertex          //normal
                1,1,1,   	       1,1,1,




                1,1,-1,         	1,1,-1,
                1,-1,1,            1,-1,1,
                1,-1,-1,           1,-1,-1,

                -1,1,1,	           -1,1,1,
                -1,1,-1,           -1,1,-1,
                -1,-1,1,           -1,-1,1,
                -1,-1,-1,         -1,-1,-1


        };



        /**
         * 重置3个边的长度
         */
        for(int i=0;i<8;i++)
        {
            vertexData[i*6+0] *=xLength/2;
            vertexData[i*6+1] *=yLength/2;
            vertexData[i*6+2] *=zLength/2;
        }

        indexData=new short[]{

                //右侧面
                0,1,2,
                1,3,2


                //前侧面
                ,  4,0,6,
                6,0,2

                //左侧面
                , 5,4,7,
                7,4,6

                //下侧面
                ,    2,3,6,
                6,3,7

                //上侧面
                , 4,5,1,
                4,1,0

                //后侧面
                , 1,5,3,
                3,5,7

        };






    }
}
