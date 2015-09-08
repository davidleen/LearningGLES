package com.opengles.book.bn;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author davidleen29
 * @create : 2013-12-12 下午11:53:40
 *         贝塞尔曲线
 */
public class BezierUtil {

	// 控制点列表
	public static ArrayList<BNPosition> al = new ArrayList<BNPosition>();

	/**
	 * 生成贝塞尔曲线点序列的方法。 al 即是 贝塞尔曲线的控制点。
	 * 贝塞尔曲线 生成公式
	 * 
	 * @param span
	 *            t 的取值宽度 决定t 的取值密度。
	 * @return
	 */
	public static List<BNPosition> getBezierData(float span)
	{
		List<BNPosition> result = new ArrayList<BNPosition>();
		int n = al.size() - 1;
		if (n < 1) // only tow point no bezier line;
			return result;

		int steps = (int) (1.0f / span);
		long[] factorial = new long[n + 1];// 计算阶乘
		factorial[0] = 1;
		for (int i = 1, count = factorial.length; i < count; i++)
		{

			factorial[i] = factorial[i - 1] * (i + 1);
		}

		float t;
		float x;
		float y;
		// 分段循环
		for (int i = 0; i < steps; i++) {

			t = i * span;
			if (t > 1)
				t = 1; // 系数最大值

			// 贝塞尔曲线上 x y 值
			x = 0;
			y = 0;

			// 计算 1-t 的n 次方 t 的n次方

			float[] tp = new float[n +1];
			float[] otp = new float[n + 1];

			for (int j = 0; j <= n   ; j++)
			{
				tp[j] = (float) Math.pow(t, j);
				otp[j] = (float) Math.pow((1 - t), j);
			}

			float xs;
			// 循环计算贝塞尔曲线上的各点坐标
			for (int k = 0   ,count=n; k < count; k++)
			{
				xs = (factorial[count] / (factorial[k] * factorial[count - k]))
						* tp[k]
						* otp[count - k];

				x = x + al.get(k).x * xs;
				y = y + al.get(k).y * xs;

			}

			result.add(new BNPosition(x, y));

		}

		return result;
	}

}
