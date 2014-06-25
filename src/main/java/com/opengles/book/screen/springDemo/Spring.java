package com.opengles.book.screen.springDemo;

import com.opengles.book.math.Vector3;

/*
 * 弹簧类
 */
public class Spring {

     Mass mass1;
    Mass mass2;
    //弹性系数
    float springFactor=500;
    //弹簧长度
    float springLength=0.2f;
    //摩擦系数
    float frictionFactor=0.2f;

    public Spring(Mass mass1,Mass mass2)
    {
        this.mass1=mass1;
        this.mass2=mass2;
    }


    public void calculateSpringForce(){	//计算各个物体受力的方法
        Vector3 springVector =Vector3.create().set( mass1.pos).sub(mass2.pos );//弹簧的伸长方向
        float distance = springVector.len();//两个质点间的距离
        Vector3 force;//作用力
        if(distance!=0){
            float deltaX = distance-this.springLength;//弹簧偏离平衡位置的距离

            Vector3 normalV =springVector.nor().mul(-1);//将弹簧的方向向量规格化;

            force = normalV.mul(deltaX).mul(springFactor);//弹簧的拉力 = k*deltaX*dir


            Vector3 frictionForce=Vector3.create() ;
            //计算摩擦力 相对速度×摩擦系数。
            frictionForce.set(mass1.vel).sub(mass2.vel).mul(-frictionFactor);
            //添加摩擦力
            force = force.add(frictionForce);//
            Vector3.recycle(frictionForce);

             mass1.applyForce(force);//对第一个质点施加力
             mass2.applyForce(force.mul(-1));//对第二个质点施加力 第二个质点受力 是相反的
        }

        Vector3.recycle(springVector);


    }
}
