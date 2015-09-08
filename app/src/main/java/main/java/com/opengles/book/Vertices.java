package com.opengles.book;

import android.opengl.GLES20;

/**
 * a class for binding  drawing attribute in a program;
 *
 * @author davidleen29
 */
public class Vertices {


    //每个顶点属性大小。
    final int vertexSize;
    final int[] attributeHandlers;
    int[] vboIds;
    final int[] attributeSizes;
    final int stride;


    int indicesCount = 0;

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
        }


    }


    public void create(float[] vertexData, short[] indicesData) {


        indicesCount = indicesData.length;
        vboIds = new int[2];
        GLES20.glGenBuffers(2, vboIds, 0);
        ShaderUtil.createVertexBuffer(GLES20.GL_ARRAY_BUFFER,
                vertexData,
                vboIds[0]);
        ShaderUtil.createIndexBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER,
                indicesData,
                vboIds[1]);

    }


    /**
     * 绑定顶点属性
     */
    public void bind() {


        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vboIds[0]);
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, vboIds[1]);
        int attributeCount = attributeHandlers.length;
        int attributeOffset = 0;
        for (int i = 0; i < attributeCount; i++) {
            GLES20.glEnableVertexAttribArray(attributeHandlers[i]);


            GLES20.glVertexAttribPointer(attributeHandlers[i], attributeSizes[i], GLES20.GL_FLOAT,
                    false,
                    stride, attributeOffset);

            attributeOffset += attributeSizes[i] * FloatUtils.RATIO_FLOATTOBYTE;

        }


    }


    /**
     * s释放资源
     */
    public void dispose() {
        GLES20.glDeleteBuffers(2, vboIds, 0);
    }

    /**
     * @param primitiveType
     * @param offset
     * @param numVertices
     */
    public void draw(int primitiveType, int offset, int numVertices) {

        GLES20.glDrawElements(primitiveType, numVertices,
                GLES20.GL_UNSIGNED_SHORT, offset * FloatUtils.RATIO_SHORTTOBYTE);


    }

    /**
     * draw TRIANGLES ，all vertex for default
     */
    public void draw(int drawType) {
        draw(drawType, 0, indicesCount);
    }

    /**
     * draw TRIANGLES ，all vertex for default
     */
    public void draw() {
        draw(GLES20.GL_TRIANGLES);
    }

    /**
     * unbind the attributes
     */
    public void unbind() {


        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vboIds[0]);
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, vboIds[1]);
        int attributeCount = attributeHandlers.length;

        for (int i = 0; i < attributeCount; i++) {
            GLES20.glDisableVertexAttribArray(attributeHandlers[i]);
        }
    }

}
