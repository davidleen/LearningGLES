package com.opengles.book.math;

import javax.vecmath.Quat4f;

/**
 *     算术功能类
 */
public class MathUtils {


    //将四元数转换为角度及转轴向量
    public static float[] fromSYStoAXYZ(Quat4f q4)
    {
        double sitaHalf=Math.acos(q4.w);
        float nx=(float) (q4.x/Math.sin(sitaHalf));
        float ny=(float) (q4.y/Math.sin(sitaHalf));
        float nz=(float) (q4.z/Math.sin(sitaHalf));

//        float nx=(float)Math.acos (q4.x/Math.sin(sitaHalf)));
//        float ny=(float) (q4.y  );
//        float nz=(float) (q4.z  );

        return new float[]{(float) Math.toDegrees(sitaHalf*2),nx,ny,nz};
    }
}
