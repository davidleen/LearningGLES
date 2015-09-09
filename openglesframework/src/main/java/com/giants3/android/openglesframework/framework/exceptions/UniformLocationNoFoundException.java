package com.giants3.android.openglesframework.framework.exceptions;

/**
 * shader  attribute 属性未找到异常
 * Created by davidleen29   qq:67320337
 * on 2014-7-22.
 */
public class UniformLocationNoFoundException extends RuntimeException {



    public UniformLocationNoFoundException(String program, String uniform)
    {


        super(" uniform :"+ uniform+" was no found in program:"+program);
    }

}
