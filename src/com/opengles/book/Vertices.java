package com.opengles.book;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLES20;
import com.opengles.book.R;

/**
 * ��λ�ռ�Ķ������
 * 
 * @author 138191
 * 
 */
public class Vertices {
	// ������
	// ���ֽڵı���
	public static final int RATE_FLOATTOBYTE = Float.SIZE / Byte.SIZE;
	public static final int RATE_SHORTTOBYTE = Short.SIZE / Byte.SIZE;
	public static final int DIMENSION_SPACE = 3;// �ռ�ά��
	public static final int DIMENSION_COLOR = 4;// ��ɫά��
	public static final int DIMENSION_TEXTURE = 2;// ����ά��

	final boolean hasColor;
	final boolean hasTexCoords;
	final int vertexSize;
	protected final IntBuffer vertices;
	final int[] tmpBuffer;
	protected final ShortBuffer indices;

	/**
	 * �����������
	 * 
	 * @param maxVertics
	 *            ��󶥵���
	 * @param maxIndices
	 *            ��󶥵�������
	 * @param hasColor
	 *            �Ƿ��������ɫ����
	 * @param hasTexCoords
	 *            �Ƿ������������
	 */
	public Vertices(int maxVertics, int maxIndices, boolean hasColor,
			boolean hasTexCoords, boolean hasNormals) {
		this.hasColor = hasColor;
		this.hasTexCoords = hasTexCoords;
		this.vertexSize = (DIMENSION_SPACE + (hasColor ? DIMENSION_COLOR : 0)
				+ (hasTexCoords ? DIMENSION_TEXTURE
						: 0) + (hasNormals ? DIMENSION_SPACE : 0))
				* RATE_FLOATTOBYTE;
		this.tmpBuffer = new int[maxVertics * vertexSize / RATE_FLOATTOBYTE];

		ByteBuffer buffer = ByteBuffer.allocateDirect(maxVertics * vertexSize);
		buffer.order(ByteOrder.nativeOrder());
		vertices = buffer.asIntBuffer();
		if (maxIndices > 0) {
			buffer = ByteBuffer.allocateDirect(maxIndices * RATE_SHORTTOBYTE);
			buffer.order(ByteOrder.nativeOrder());
			indices = buffer.asShortBuffer();
		} else {
			indices = null;
		}

	}

	/**
	 * ���ö������
	 * 
	 * @param vertices
	 * @param offset
	 * @param length
	 */
	public void setVertices(float[] vertices, int offset, int length) {
		this.vertices.clear();
		int len = offset + length;
		for (int i = offset, j = 0; i < len; i++, j++) {
			tmpBuffer[j] = Float.floatToRawIntBits(vertices[i]);
			this.vertices.put(tmpBuffer, 0, length);
			this.vertices.flip();
		}
	}

	/**
	 * ���ö����������
	 * 
	 * @param indices
	 * @param offset
	 * @param length
	 */
	public void setIndices(short[] indices, int offset, int length) {
		this.indices.clear();
		this.indices.put(indices, offset, length);
		this.indices.flip();
	}

	/**
	 * ���ж�����Դ��ϰ�
	 */
	public void bind() {

		// Load the shaders and get a linked program object
		int mProgramObject = ESShader.loadProgram("", "");

		// Get the attribute locations
		int mPositionLoc = GLES20.glGetAttribLocation(mProgramObject,
				"a_position");
		int mTexCoordLoc = GLES20.glGetAttribLocation(mProgramObject,
				"a_texCoord");
		
		int mnormalLoc = GLES20.glGetAttribLocation(mProgramObject,
				"a_normal_position");

		// Get the sampler location
		int mSamplerLoc = GLES20.glGetUniformLocation(mProgramObject,
				"s_texture");

		// Get the offset location
		int mOffsetLoc = GLES20
				.glGetUniformLocation(mProgramObject, "u_offset");

		vertices.position(0);
		GLES20.glVertexAttribPointer(mPositionLoc, DIMENSION_SPACE, GLES20.GL_FLOAT,
				false,
				vertexSize, vertices);
		// Load the texture coordinate
		vertices.position(3);
		GLES20.glVertexAttribPointer(mTexCoordLoc, DIMENSION_TEXTURE, GLES20.GL_FLOAT,
				false,
				vertexSize,
				vertices);
		
		vertices.position(5);
		GLES20.glVertexAttribPointer(mnormalLoc, DIMENSION_SPACE, GLES20.GL_FLOAT,
				false,
				vertexSize,
				vertices);

		GLES20.glEnableVertexAttribArray(mPositionLoc);
		GLES20.glEnableVertexAttribArray(mTexCoordLoc);
		GLES20.glEnableVertexAttribArray(mnormalLoc);
		
		
		
		
		// Bind the texture
		// GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
		// GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, );
		// GLES20.glEnableVertexAttribArray(position);
		// vertices.position(0);
		// GLES20.glVertexAttribPointer(indx, size, type, normalized, stride,
		// ptr);
		// .glVertexPointer(3, GL10.GL_FLOAT, vertexSize, vertices);
		//
		// if (hasColor) {
		// gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
		// vertices.position(3);
		// gl.glColorPointer(4, GL10.GL_FLOAT, vertexSize, vertices);
		// }
		//
		// if (hasTexCoords) {
		// gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		// vertices.position(hasColor ? 7 : 3);
		// gl.glTexCoordPointer(2, GL10.GL_FLOAT, vertexSize, vertices);
		// }

	}

	/**
	 * ���ƶ������
	 * 
	 * @param primitiveType
	 *            ��������
	 * @param offset
	 *            ��ʼƫ��λ��
	 * @param numVertices
	 *            ��������
	 */
	public void draw(int primitiveType, int offset, int numVertices) {

		if (indices != null) {
			indices.position(offset);
			GLES20.glDrawElements(primitiveType, numVertices,
					GLES20.GL_UNSIGNED_SHORT, indices);
		} else {
			GLES20.glDrawArrays(primitiveType, offset, numVertices);
		}

	}

	/**
	 * ����
	 * 
	 */
	public void unbind() {
		// GL10 gl = glGraphics.getGL();
		// if (hasTexCoords)
		// gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		//
		// if (hasColor)
		// gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
	}

}
