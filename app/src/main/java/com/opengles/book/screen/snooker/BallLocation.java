package com.opengles.book.screen.snooker;

import com.giants3.android.openglesframework.framework.math.Vector3;

import java.util.ArrayList;
import java.util.List;

/**
 * 计算15个球摆放位置
 * Created by davidleen29   qq:67320337
 * on 2014-7-10.
 */
public class BallLocation {

    static Vector3 Direct_Left = Vector3.create(1,0,0);
    static Vector3 Direct_Right = Vector3.create(1,0,0);
    static {
        Direct_Left.rotate(60,0,1,0);
        Direct_Right.rotate(120,0,1,0);
    }

    static List<Vector3> result=new ArrayList<Vector3>();


    /**
     * 计算桌球摆放位置
     * @return
     */
    public static  List<Vector3>    calculate(Vector3 origin)
    {


        float step=0.5f;

        for(Vector3 vector3:result)
        {
            Vector3.recycle(vector3);
        }
        result.clear();

        result.add(origin);

        doCalculate(0.5f, origin, 1, 5);

        return result;



    }



    private static  void doCalculate(float step,Vector3 position, int depth,int maxDepth)
    {

        if(depth==maxDepth)
        {
            return ;
        }


        //left add all
        for(int i=depth+1;i<=maxDepth;i++)
        {
            Vector3 newPosition=Vector3.create();
            newPosition.set(Direct_Left).mul(step*(i-depth)).add(position);
            result.add(newPosition);
        }


        //add right
        Vector3 newPosition=Vector3.create();
        newPosition.set(Direct_Right).mul(step).add(position);
        result.add(newPosition);

        doCalculate(step,newPosition,depth+1,maxDepth);

    }
}
