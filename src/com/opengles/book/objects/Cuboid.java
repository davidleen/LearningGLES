//package com.opengles.book.objects;
//
//import com.opengles.book.math.Vector3;
//
///**
// * 长方体 三维构造器
// * Created by davidleen29   qq:67320337
// * on 14-5-23.
// */
//public class Cuboid {
//
//    int halfXLength; int halfYLength; int halfZLength;
//    Vector3 center;
//
//
//    public Cuboid(int halfXLength, int halfYLength, int halfZLength)
//    {
//            this(halfXLength, halfYLength, halfZLength,new Vector3(0,0,0));
//
//    }
//
//    public Cuboid(int halfXLength, int halfYLength, int halfZLength, Vector3 center) {
//        this.halfXLength = halfXLength;
//        this.halfYLength = halfYLength;
//        this.halfZLength = halfZLength;
//        this.center = center;
//
//
//        //create();
//    }
//
//
//
//    public void create(boolean fixedXAxis,boolean fixedYAxis,boolean fixedZAxis)
//    {
//
//        int rowCount=(int)WIDTH_SPAN;
//        int columnCount=(int)WIDTH_SPAN;
//        int totalCount = rowCount * columnCount;
//        int triangleCount = totalCount*2 ; // 一个方形// 2个三角形
//        int indicesCount = triangleCount * 3;// 一个三角形3个点
//
//        int 	stride =STRIP_SIZE;
//
//        float[]	attributes = new float[totalCount
//                * stride];
//        short[]	indics = new short[indicesCount];
//
//
//
//        float Sx  =1.0f/ (halfXLength*2);
//        float Sy  =1.0f/ (halfYLength*2);
//        float Sz  =1.0f/ (halfZLength*2);
//        int position = 0, indexPosition = 0;
//        for (int x = -halfXLength; x <= halfXLength; x++)
//        {
//
//
//            if(fixedXAxis&&Math.abs(x)==halfXLength)
//                continue;
//
//            for (int y = -halfYLength; y <= halfYLength; y++)
//            {
//
//                if(fixedYAxis&&Math.abs(y)==halfYLength)
//                    continue;
//
//                for (int z= -halfZLength; z <= halfZLength; z++)
//                {
//
//                    if(fixedZAxis&&Math.abs(z)==halfZLength)
//                        continue;
//
//                    attributes[position++] = x ;
//                    attributes[position++] = y  ;
//                    attributes[position++] = z ;
//                    // // 法向量值
//                    attributes[position++] =x;
//                    attributes[position++] =y;
//                    attributes[position++] =z;
//
//
//                    if(!fixedXAxis )
//                        attributes[position++] =Sx;
//                    if(!fixedYAxis )
//                        attributes[position++] =Sy;
//                    if(!fixedZAxis )
//                        attributes[position++] =Sx;
//
//
//
//
//            }
//             }
//        }
//
//        for (int i = 0; i < rowCount -1 ; i++)
//        {
//
//            for (int j = 0; j < columnCount  -1; j++)
//            {
//
//                // 划分四边形 变成2个三角形
//                // v1_____v3
//                // /| |
//                // v0|_____|v2
//                short v0 = (short) (i * columnCount + j);
//                short v1 = (short) ((i + 1) * columnCount + j);
//                short v2 = (short) (i * columnCount + j + 1);
//                short v3 = (short) ((i + 1) * columnCount + j + 1);
//                indics[indexPosition++] = v0;
//                indics[indexPosition++] = v1;
//                indics[indexPosition++] = v3;
//
//                indics[indexPosition++] = v0;
//                indics[indexPosition++] = v3;
//                indics[indexPosition++] = v2;
//
//
//            }
//        }
//
//
//        vertexData=attributes;
//        indexData=indics;
//
//
//    }
//
//    float[] vertexData;
//    short[] indexData;
//}
