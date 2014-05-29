package com.opengles.book;

import java.nio.ByteBuffer;

import android.content.res.Resources;
import android.opengl.GLES20;
import com.opengles.book.R;
import com.opengles.book.framework.gl.Texture;

public class cube extends Vertices {

	private int mProgramObject;

	// Attribute locations
	private int mPositionLoc;
	private int mTexCoordLoc;
	private Texture texutre;

	public cube(Resources rs) {

		super(24, 36, false, true, false);
		float[] vertices =
			{ -0.5f, -0.5f, 0.5f, 0, 1,
					0.5f, -0.5f, 0.5f, 1, 1,
					0.5f, 0.5f, 0.5f, 1, 0,
					-0.5f, 0.5f, 0.5f, 0, 0,

					0.5f, -0.5f, 0.5f, 0, 1,
					0.5f, -0.5f, -0.5f, 1, 1,
					0.5f, 0.5f, -0.5f, 1, 0,
					0.5f, 0.5f, 0.5f, 0, 0,

					0.5f, -0.5f, -0.5f, 0, 1,
					-0.5f, -0.5f, -0.5f, 1, 1,
					-0.5f, 0.5f, -0.5f, 1, 0,
					0.5f, 0.5f, -0.5f, 0, 0,

					-0.5f, -0.5f, -0.5f, 0, 1,
					-0.5f, -0.5f, 0.5f, 1, 1,
					-0.5f, 0.5f, 0.5f, 1, 0,
					-0.5f, 0.5f, -0.5f, 0, 0,

					-0.5f, 0.5f, 0.5f, 0, 1,
					0.5f, 0.5f, 0.5f, 1, 1,
					0.5f, 0.5f, -0.5f, 1, 0,
					-0.5f, 0.5f, -0.5f, 0, 0,

					-0.5f, -0.5f, 0.5f, 0, 1,
					0.5f, -0.5f, 0.5f, 1, 1,
					0.5f, -0.5f, -0.5f, 1, 0,
					-0.5f, -0.5f, -0.5f, 0, 0
		};

		short[] indices =
			{ 0, 1, 3, 1, 2, 3,
					4, 5, 7, 5, 6, 7,
					8, 9, 11, 9, 10, 11,
					12, 13, 15, 13, 14, 15,
					16, 17, 19, 17, 18, 19,
					20, 21, 23, 21, 22, 23,
		};

		setVertices(vertices, 0, vertices.length);
		setIndices(indices, 0, indices.length);
		doInitiat(rs);
	}

	/**
	 * ִ�г�ʼ��
	 */
	private void doInitiat(Resources rs)
	{

		String vShaderStr =
				"uniform float u_offset;      \n" +
						"attribute vec4 a_position;   \n" +
						"attribute vec2 a_texCoord;   \n" +
						"varying vec2 v_texCoord;     \n" +
						"void main()                  \n" +
						"{                            \n" +
						"   gl_Position = a_position; \n" +
						"   gl_Position.x += u_offset;\n" +
						"   v_texCoord = a_texCoord;  \n" +
						"}                            \n";

		String fShaderStr =
				"precision mediump float;                            \n"
						+
						"varying vec2 v_texCoord;                            \n"
						+
						"uniform sampler2D s_texture;                        \n"
						+
						"void main()                                         \n"
						+
						"{                                                   \n"
						+
						"  gl_FragColor = texture2D(s_texture, v_texCoord);  \n"
						+
						"}                                                   \n";

		// Load the shaders and get a linked program object
		mProgramObject = ESShader.loadProgram(vShaderStr, fShaderStr);

		// Get the attribute locations
		mPositionLoc = GLES20.glGetAttribLocation(mProgramObject, "a_position");
		mTexCoordLoc = GLES20.glGetAttribLocation(mProgramObject, "a_texCoord");

		// // Get the sampler location
		// mSamplerLoc = GLES20.glGetUniformLocation ( mProgramObject,
		// "s_texture" );
		//
		// // Get the offset location
		// mOffsetLoc = GLES20.glGetUniformLocation( mProgramObject, "u_offset"
		// );

		// Load the texture
		texutre = new Texture(rs, "crate");

	}

	public void draw()
	{

		// Use the program object
		GLES20.glUseProgram(mProgramObject);

		// Load the vertex position
		vertices.position(0);
		GLES20.glVertexAttribPointer(mPositionLoc, 4, GLES20.GL_FLOAT,
				false,
				6 * 4, vertices);
		// Load the texture coordinate
		vertices.position(4);
		GLES20.glVertexAttribPointer(mTexCoordLoc, 2, GLES20.GL_FLOAT,
				false,
				6 * 4,
				vertices);

		GLES20.glEnableVertexAttribArray(mPositionLoc);
		GLES20.glEnableVertexAttribArray(mTexCoordLoc);

		texutre.bind();

		GLES20.glDrawElements(GLES20.GL_TRIANGLES, 6, GLES20.GL_UNSIGNED_SHORT,
				indices);

	}

}
