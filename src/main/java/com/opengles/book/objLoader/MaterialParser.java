package com.opengles.book.objLoader;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * 解析材料库文件
 * Created by davidleen29   qq:67320337
 * on 14-4-16.
 */
public class MaterialParser {

    public static final String SPLIT_EXPRESSION = "[ ]+";

    public static Material.MaterialList loadFile(Context context,String path, String fileName)  {

    	
    	 
        Material.MaterialList materials = new Material.MaterialList();
        String line;
        Material currentMtl = null;
        try
        {
         InputStream in = null;


             in = context.getResources().getAssets().open(path+fileName);


        InputStreamReader isr = new InputStreamReader(in);

        BufferedReader reader = new BufferedReader(isr);


        while ((line = reader.readLine() ) != null) {
        	line=line.trim();
            if (line.startsWith("newmtl")) {
                if (currentMtl != null)
                    materials.add(currentMtl);
                String mtName = line.split(SPLIT_EXPRESSION, 2)[1];
                currentMtl = new Material(mtName);
            } else if (line.startsWith("Ka")) {
                String[] str = line.split(SPLIT_EXPRESSION);
                currentMtl.setAmbientColor(Float.parseFloat(str[1]), Float.parseFloat(str[2]), Float.parseFloat(str[3]));
            } else if (line.startsWith("Kd")) {
                String[] str = line.split(SPLIT_EXPRESSION);
                currentMtl.setDiffuseColor(Float.parseFloat(str[1]), Float.parseFloat(str[2]), Float.parseFloat(str[3]));
            } else if (line.startsWith("Ks")) {
                String[] str = line.split(SPLIT_EXPRESSION);
                currentMtl.setSpecularColor(Float.parseFloat(str[1]), Float.parseFloat(str[2]), Float.parseFloat(str[3]));
            } else if (line.startsWith("Tr")  ) {
                String[] str = line.split(SPLIT_EXPRESSION);
                float alpha=Float.parseFloat(str[1]);
                if(alpha>0.01)
                 currentMtl.setAlpha(alpha);
            } 
            else if ( line.startsWith("d")) {
                String[] str = line.split(SPLIT_EXPRESSION);
                currentMtl.setDissolve(Float.parseFloat(str[1]));
            }
            else if (line.startsWith("Ns")) {
                String[] str = line.split(SPLIT_EXPRESSION);
                currentMtl.setShine(Float.parseFloat(str[1]));
            } else if (line.startsWith("illum")) {
                String[] str = line.split(SPLIT_EXPRESSION);
                currentMtl.setIllum(Integer.parseInt(str[1]));
            } else if (line.startsWith("map_Ka")||line.startsWith("map_Kd")||line.startsWith("map_bump")||line.startsWith("map_Ke")) {
                String[] str = line.split(SPLIT_EXPRESSION);
                if(str!=null&& str.length>1)
                	currentMtl.setTextureFile(str[1].replace("\\", File.separator));
            }
        }

        
            if (currentMtl != null)
                materials.add(currentMtl);
            reader.close();
        }catch(IOException e)
        {
            e.printStackTrace();
        }

        return materials;

    }


}
