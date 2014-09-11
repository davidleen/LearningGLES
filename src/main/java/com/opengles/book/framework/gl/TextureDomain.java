package com.opengles.book.framework.gl;

import android.content.Context;

import java.util.Map;

/**
 * 纹理集合控件
 * Created by davidleen29   qq:67320337
 * on 2014-9-11.
 */
public class TextureDomain {


    private Context context;

    public Map<String,Integer> textureMap;
    static TextureDomain domain;
    public void add(String filePath)
    {



    }

    /**
     * 在系统恢复时候调用接口
     * 重新生成纹理
     */
    public void  onResume()
    {



    }

    /**
     * 在系统暂停时候
     * 删除纹理。
     */
    public void onPause()
    {


    }


    public void clear()
    {}




    public static TextureDomain getInstance() {

        if (domain == null)
        {domain=new TextureDomain();}
        return domain;

    }



    public void setContext(Context context)
    {
        this.context=context;
    }

}
