package com.opengles.book.screen.cubeCollisionDemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.opengl.GLES20;
import com.opengles.book.FloatUtils;

import java.io.IOException;


/**
 *
 *
 * 灰度地图生成工具类
 * Created by Administrator on 2014/6/26.
 */
public class GrayMap {
    //读取灰度图
    final static float LAND_HIGH_ADJUST=-2f;//陆地的高度调整值
    final static float LAND_HIGHEST=20f;//陆地最大高差
    final static int DEFAULT_UNIT_SIZE=1;

    public int drawType= GLES20.GL_TRIANGLES;

    public static final  int VERTEX_COUNT_PER_TRIANGLE=3;

    public float[]  vertexData;
    public short[]  indexData;

//    public int vertexStrideWidth;
//    public int indexStrideWidth;
    public int triangleCount;
    public int vertexCount;
  //  public int vertexBuffSize;
   // public int indexBuffSize;

    //关闭法向量
    protected static final int VERTEX_POS_SIZE = 3;// xyz
    // protected static final int VERTEX_NORMAL_SIZE = 3;// xyz
    protected static final int VERTEX_TEXTURE_SIZE = 2;// s t
    protected static final int VERTEX_SIZE=VERTEX_POS_SIZE+VERTEX_TEXTURE_SIZE;
    protected static final int VERTEX_STRIP_SIZE_OF_BUFFER = VERTEX_SIZE
            * FloatUtils.RATIO_FLOATTOBYTE;
    public static GrayMap load(Context context,String fileName)
    {



        return load(context,fileName,DEFAULT_UNIT_SIZE);

    }
    public static GrayMap load(Context context,String fileName,int unitSize  )
    {
        return load(context,fileName,unitSize,LAND_HIGHEST,LAND_HIGH_ADJUST);
    }
    public static GrayMap load(Context context,String fileName,int unitSize, float maxHeight, float adjustHeight    )
    {

        GrayMap grayMap=new GrayMap();

        Bitmap bt=null;
        try {
            bt = BitmapFactory.decodeStream(context.getAssets().open(fileName));
        } catch (IOException e) {
            throw new RuntimeException("no found image "+fileName);

        }
        int colsPlusOne=bt.getWidth();
        int rowsPlusOne=bt.getHeight();
        float[][] result=new float[rowsPlusOne][colsPlusOne];
        for(int i=0;i<rowsPlusOne;i++)
        {
            for(int j=0;j<colsPlusOne;j++)
            {
                int color=bt.getPixel(j,i);
                int r= Color.red(color);
                int g=Color.green(color);
                int b=Color.blue(color);
                int h=(r+g+b)/3;//灰度值  灰度值 作为高度比率
                result[i][j]=h/255f*maxHeight+ adjustHeight;
            }
        }



        bt.recycle();
        bt=null;


        int rows=result.length;
        int cols=result[0].length;
        int vCount=cols*rows ;//共有点数  共有方块数
        float[] vertices =new float[vCount* VERTEX_SIZE];//每个顶点xyz三个坐标 st 纹理坐标
        int count=0;//顶点计数器
        float halfRows=rows/2;
        float halfCols=cols/2;

        //16 为纹理图切割块数
        float sizew=16.0f/cols;//列数
        float sizeh=16.0f/rows;//行数
        for(int j=0;j<rows;j++)
        {
            for(int i=0;i<cols;i++)
            {

                //xyz
                vertices[count++]=(i-halfCols)*unitSize;
                vertices[count++]=result[j][i]*unitSize/2;
                vertices[count++]=(j-halfRows)*unitSize;
                //st
                vertices[count++]=i*sizew;
                vertices[count++]=j*sizeh;



            }
        }

        //每个方块由2个三角形组成， 每个三角形3个顶点。
        int indexCount=vCount*2*3;
        short[] indexData=new short[indexCount];
        count=0;
        for(int j=0;j<rows-1;j++)
        {
            for(int i=0;i<cols-1;i++)
            {

                short p1=(short) (j*rows+i);
                short p2=(short) (j*rows+i+1);
                short p3=(short) ((j+1)*rows+i );
                short p4=(short) ((j+1)*rows+i+1 );

                indexData[count++]=p1;
                indexData[count++]=p2;
                indexData[count++]=p3;

                indexData[count++]=p3;
                indexData[count++]=p2;
                indexData[count++]=p4;



            }

        }


        grayMap.vertexData=vertices;
       // grayMap.vertexData.asFloatBuffer().put(vertices).flip();



        grayMap.indexData=indexData;

        grayMap.triangleCount= indexData.length/VERTEX_COUNT_PER_TRIANGLE;
        grayMap.vertexCount= vertices.length/VERTEX_SIZE;
//        grayMap.vertexStrideWidth= VERTEX_STRIP_SIZE_OF_BUFFER;
//        grayMap.indexStrideWidth=VERTEX_COUNT_PER_TRIANGLE*FloatUtils.RATIO_SHORTTOBYTE;
        return  grayMap;




    }
}
