package com.opengles.book.screen.snooker;

import android.opengl.GLES20;

import com.giants3.android.openglesframework.framework.exceptions.AttributeLocationNoFoundException;
import com.giants3.android.openglesframework.framework.utils.FloatUtils;
import com.opengles.book.ShaderUtil;


/**
 * a class for binding  drawing attribute in a program;
 *
 * @author davidleen29
 */
public class Vertices {


    //每个顶点属性大小。
    final int vertexSize;
    final int[] attributeHandlers;
    final int[] attributeSizes;
    final int stride;



    public Vertices(String[] attributeNames, int[] attributeSizes, int mProgram) {

        this(attributeNames, attributeSizes, 0, mProgram);

    }

    /**
     *
     * @param attributeNames
     * @param attributeSizes  各属性大小
     * @param totalAttributeSize    总共属性大小  如果<=0 则累加attributeSize;
     * @param mProgram
     */
    public Vertices(String[] attributeNames, int[] attributeSizes, int totalAttributeSize, int mProgram) {


        //计算单顶点所有属性大小。

        if (totalAttributeSize <= 0) {


            int totalSize = 0;
            for (int size : attributeSizes) {
                totalSize += size;
            }
            this.vertexSize = totalSize;
        } else {
            vertexSize = totalAttributeSize;
        }
        this.attributeSizes = attributeSizes;

        stride = vertexSize * FloatUtils.RATIO_FLOATTOBYTE;


        //获取属性的id
        int attributeCount = attributeNames.length;
        attributeHandlers = new int[attributeCount];


        for (int i = 0; i < attributeCount; i++) {
            attributeHandlers[i] = GLES20.glGetAttribLocation(mProgram,
                    attributeNames[i]);
            if( attributeHandlers[i] <0)
            {
                throw new AttributeLocationNoFoundException(String.valueOf(mProgram),attributeNames[i]);
            }
        }


    }


    public int[] create(float[] vertexData, short[] indicesData) {

        int[] bufferIds=new int[2];
        GLES20.glGenBuffers(2, bufferIds, 0);
        ShaderUtil.createVertexBuffer(GLES20.GL_ARRAY_BUFFER,
                vertexData,
                bufferIds[0]);
        ShaderUtil.createIndexBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER,
                indicesData,
                bufferIds[1]);

        return bufferIds;

    }





    /**
     * s释放资源
     */
    public void dispose(int[] bufferIds) {
        GLES20.glDeleteBuffers(2, bufferIds, 0);
    }

    public void draw(   int[] bufferIds,int numVertices) {
        draw(GLES20.GL_TRIANGLES,0,bufferIds,numVertices);
    }

    /**
     * @param primitiveType
     * @param offset
     * @param numVertices
     */
    public void draw(int primitiveType, int offset, int[] bufferIds,int numVertices) {

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, bufferIds[0]);
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, bufferIds[1]);

        // //bind attribute
        int attributeCount = attributeHandlers.length;
        int attributeOffset = 0;
        for (int i = 0; i < attributeCount; i++) {
            GLES20.glEnableVertexAttribArray(attributeHandlers[i]);
            GLES20.glVertexAttribPointer(attributeHandlers[i], attributeSizes[i], GLES20.GL_FLOAT,
                    false,
                    stride, attributeOffset);

            attributeOffset += attributeSizes[i] * FloatUtils.RATIO_FLOATTOBYTE;

        }


        GLES20.glDrawElements(primitiveType, numVertices,
                GLES20.GL_UNSIGNED_SHORT, offset * FloatUtils.RATIO_SHORTTOBYTE);

        //unbind attribute
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, bufferIds[0]);
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, bufferIds[1]);


        for (int i = 0; i < attributeCount; i++) {
            GLES20.glDisableVertexAttribArray(attributeHandlers[i]);
        }

    }







}
