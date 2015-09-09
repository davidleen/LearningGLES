package com.giants3.android.openglesframework.framework.exceptions;

/**
 * shader  attribute 属性未找到异常
 * Created by davidleen29   qq:67320337
 * on 2014-7-22.
 */
public class AttributeLocationNoFoundException extends RuntimeException {



    public AttributeLocationNoFoundException(String program, String attribute)
    {


        super(attribute+" was no found in program:"+program);
    }

}
