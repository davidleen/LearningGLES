package com.opengles.book.screen.snooker;

import android.util.Log;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.Random;

import android.util.Log;

import com.opengles.book.BuildConfig;
import com.opengles.book.FloatUtils;
import com.opengles.book.framework.gl.TextureRegion;
import com.opengles.book.math.Rectangle;

/**
 * Created by Administrator on 2014/7/13.
 */
public class SphereWithLimitTexture {






    /**
     * 球体构造
     *
     * @author davidleen29
     *
     */


        private static final String TAG = "Sphere";

        public float[] attributes;
        public short[] indics;
        protected static final int VERTEX_POS_SIZE = 3;// xyz
        protected static final int VERTEX_NORMAL_SIZE = 3;// xyz
        protected static final int VERTEX_TEXCOORD_SIZE = 2;// s t


        ;

        int angleSpanIndegree = 10;
        int rowCount = 180 / angleSpanIndegree ;
        int columnCount = 360 / angleSpanIndegree ;
        int totalCount = (rowCount+1) * (columnCount+1);
        int triangleCount = totalCount*2 ; // 一个方形// 2个三角形

        int indicesCount = triangleCount * 3;// 一个三角形3个点


        public SphereWithLimitTexture()
        {
            this(5);
        }

        public SphereWithLimitTexture(float r)
        {
            this(r, false,null
            );
        }

        public SphereWithLimitTexture(float r,boolean hasNormal,TextureRegion textureRegion)
        {
            createData(r,hasNormal,textureRegion);
        }

        public void createData(float r,boolean  hasNormal ,TextureRegion textureRegion)
        {

            int 	stride =
                    VERTEX_POS_SIZE+
                            (hasNormal? VERTEX_NORMAL_SIZE:0)+
                            + VERTEX_TEXCOORD_SIZE;


            float angleSpanInRadian = (float) Math.toRadians(angleSpanIndegree);
            attributes = new float[totalCount
                    * stride];
            indics = new short[indicesCount];

            float x, y, z;

            float width=1.0f;
            float height=1.0f;
            float startU=0;
            float startV=0;

            if(textureRegion!=null) {
                width=textureRegion.u2-textureRegion.u1;
                height=textureRegion.v2-textureRegion.v1;
                startU=textureRegion.u1;
                startV=textureRegion.v1;
            }
            float pieceofImageS = width / (columnCount+1);
            float pieceofImageT =height/ (rowCount+1);
           // Log.d(TAG, "totalCount:" + totalCount);
            int position = 0, indexPosition = 0;
            for (int i = 0; i <=rowCount; i++)
            {
                float rowAngle = (float) (i * angleSpanInRadian - Math.PI / 2);
                float sinRow = (float) Math.sin(rowAngle);
                float cosRow = (float) Math.cos(rowAngle);


                y = sinRow ;
                for (int j = 0; j <= columnCount; j++)
                {
                    float columnAngle = j * angleSpanInRadian;
                    x = (float) (cosRow
                            * Math.cos(columnAngle));

                    z = (float) (cosRow
                            * Math.sin(columnAngle));

                    attributes[position++] = x * r;
                    attributes[position++] = y * r;
                    attributes[position++] = z * r;

                    // attributes[position++] = random.nextInt((int) r);
                    // attributes[position++] = random.nextInt((int) r);
                    // attributes[position++] = random.nextInt((int) r);
                    // // 法向量值
                    if(hasNormal)
                    {
                        attributes[position++] = x ;
                        attributes[position++] = y ;
                        attributes[position++] =z ;
                    }
                    //纹理
                    //+0.5 偏移量 使其纹理不至于在边沿上。
                    float s =   (j) * pieceofImageS+startU;
                    float t = ( i) * pieceofImageT +startV;
                    attributes[position++] = s;
                    attributes[position++] = t;



                }
            }

            for (int i = 0; i < rowCount  ; i++)
            {

                for (int j = 0; j < columnCount  ; j++)
                {

                    // 划分四边形 变成2个三角形
                    // v1_____v3
                    // /| |
                    // v0|_____|v2
                    short v0 = (short) (i * columnCount + j); //
                    short v1 = (short) ((i + 1) * columnCount + j);
                    short v2 = (short) (i * columnCount + j + 1);
                    short v3 = (short) ((i + 1) * columnCount + j + 1);
                    indics[indexPosition++] = v0;
                    indics[indexPosition++] = v1;
                    indics[indexPosition++] = v3;

                    indics[indexPosition++] = v0;
                    indics[indexPosition++] = v3;
                    indics[indexPosition++] = v2;


                }
            }



        }






}
