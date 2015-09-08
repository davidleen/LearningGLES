package com.opengles.book;

import com.opengles.book.R;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

public class FloatUtils {

	public static final int RATIO_FLOATTOBYTE = Float.SIZE / Byte.SIZE;
	public static final int RATIO_SHORTTOBYTE = Short.SIZE / Byte.SIZE;
	public static final int RATIO_INTTOBYTE = Integer.SIZE / Byte.SIZE;
	
	/**
	 * FloatArrayToNativeBuffer
	 * 
	 * @param arrays
	 * @return
	 */
	public static FloatBuffer FloatArrayToNativeBuffer(float[] arrays)
	{

		FloatBuffer buffer;
		ByteBuffer cbb = ByteBuffer.allocateDirect(arrays.length
				* RATIO_FLOATTOBYTE);
		cbb.order(ByteOrder.nativeOrder()); // 设置字节顺序为本地操作系统顺序
		buffer = cbb.asFloatBuffer();
		buffer.put(arrays);
		buffer.flip();
		// 特别提示：由于不同平台字节顺序不同数据单元不是字节的一定要经过ByteBuffer
		// 转换，关键是要通过ByteOrder设置nativeOrder()，否则有可能会出问题
		return buffer;
	}
	
		
	public  static ShortBuffer ShortArrayToNativeBuffer(short[] arrays)
	{
		
		ShortBuffer buffer;
		ByteBuffer cbb = ByteBuffer.allocateDirect(arrays.length
				* RATIO_SHORTTOBYTE);
		cbb.order(ByteOrder.nativeOrder()); // 设置字节顺序为本地操作系统顺序
		buffer = cbb.asShortBuffer();
		buffer.put(arrays);
		buffer.flip();
		 
		return buffer;
	}
	
	public  static IntBuffer IntArrayToNativeBuffer(int[] arrays)
	{
		
		IntBuffer buffer;
		ByteBuffer cbb = ByteBuffer.allocateDirect(arrays.length
				* RATIO_INTTOBYTE);
		cbb.order(ByteOrder.nativeOrder()); // 设置字节顺序为本地操作系统顺序
		buffer = cbb.asIntBuffer();
		buffer.put(arrays);
		buffer.flip();
		 
		return buffer;
	}

}
