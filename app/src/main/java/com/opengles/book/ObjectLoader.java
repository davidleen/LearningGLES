package com.opengles.book;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.res.Resources;
import android.util.Log;

import com.giants3.android.openglesframework.framework.utils.Vector3D;

/**
 * 
 * @author davidleen29
 * @create : 2013-12-28 下午2:36:49
 *         3D 模型加载类。
 */
public class ObjectLoader {

	// public FloatBuffer vertextBuffer;
	// public ShortBuffer indexBuffer;
	private static String TAG="ObjectLoader";

	 public float[] vertexData;

	public short[] indicsData;

	public int triangleCount;

	public ObjectLoader(Resources rs, String fileName)
	{

		// 原始顶点坐标列表--直接从obj文件中加载
		List<Float> positions = new ArrayList<Float>();
		List<Float> textures = new ArrayList<Float>();
		List<Float> normals = new ArrayList<Float>();
		List<String> faces = new ArrayList<String>();

		String SPLITCHARS = "[ ]+";

		try {
			InputStream in = rs.getAssets().open(fileName);

			InputStreamReader isr = new InputStreamReader(in);

			BufferedReader br = new BufferedReader(isr);

			String readString = null;
			while ((readString = br.readLine()) != null)
			{
				String[] stringArray = readString.split(SPLITCHARS);
				String prefix = stringArray[0].trim();

				if (prefix.equalsIgnoreCase("v"))
				{
					// 顶点。
					positions.add(Float.parseFloat(stringArray[1]));
					positions.add(Float.parseFloat(stringArray[2]));
					positions.add(Float.parseFloat(stringArray[3]));

				} else if (prefix.equalsIgnoreCase("vt")) {
					// 纹理
					textures.add(Float.parseFloat(stringArray[1]));// s
					textures.add(Float.parseFloat(stringArray[2])); // t

				} else if (prefix.equalsIgnoreCase("vn"))
				{
					// 法向量
					normals.add(Float.parseFloat(stringArray[1]));// x
					normals.add(Float.parseFloat(stringArray[2]));// y
					normals.add(Float.parseFloat(stringArray[3]));// z

				} else if (prefix.equalsIgnoreCase("f")) {
					// 面 三角形。
					// 三个顶点索引值的数组

					for (int i = 1; i < stringArray.length; i++)
					{
						faces.add(stringArray[i]);

					}

				}
			}
			
			Log.d(TAG,"vertexSize:"+positions.size()+",normalSize:"+normals.size()+",texturesSize:"+textures.size()
					+",faces:"+faces.size());

            int CORDSIZE = 3;
            int TEXTCORDSIZE = 2;
            int NORMALSIZE = 3;
            int VERTEXFULLSIZE = CORDSIZE + TEXTCORDSIZE + NORMALSIZE;
            int VETEXCOUNTINTRIANGLE = 3;

            int vetexCount = positions.size() / CORDSIZE;

            triangleCount = faces.size()/VETEXCOUNTINTRIANGLE;

            List<Float> vertexDataList=new ArrayList<Float>(vetexCount * VERTEXFULLSIZE);

            indicsData = new short[triangleCount * VETEXCOUNTINTRIANGLE];
            Map<String, Short> faceToIndex = new HashMap<String, Short>();

            int indicsDataStep = 0;

            boolean  hasNormal=normals.size()>0;

            for (String s : faces)
            {
                Short index = faceToIndex.get(s);
                if (index == null)
                {
                    String[] vetexString = s.split("/");


                    int positionLingIndex = (Integer.valueOf(vetexString[0])-1)
                            * CORDSIZE;
                    for (int i = 0; i < CORDSIZE; i++)
                    {
                        vertexDataList.add( positions
                                .get(positionLingIndex + i))  ;
                    }
                    float[] normalData=new float[NORMALSIZE];
                    //法向量数据
                    if (!hasNormal)// 无法向量暂时 直接使用坐标数据
                    {


                        for (int i = 0; i < NORMALSIZE; i++)
                        {
                            normalData[i]=vertexDataList.get(vertexDataList.size()-NORMALSIZE+i);

                        }


                    } else {
                        int normalCordLine = (Integer.valueOf(vetexString[2])-1)
                                * NORMALSIZE;

                        for (int i = 0; i < NORMALSIZE; i++)
                        {
                            normalData[i]= normals
                                    .get(normalCordLine + i);

                        }

                    }

                    //标准化
                    Vector3D.normalize(normalData);
                    for (int i = 0; i < NORMALSIZE; i++)
                    {

                        vertexDataList.add(normalData[i]);
                    }

                    int textCordLine = (Integer.valueOf(vetexString[1])-1)
                            * TEXTCORDSIZE;
                    for (int i = 0; i < TEXTCORDSIZE; i++)
                    {
                        vertexDataList.add( textures
                                .get(textCordLine + i)) ;
                    }


                    //index start from 0
                    short indecs = (short) (vertexDataList.size() / VERTEXFULLSIZE-1);
                    faceToIndex.put(s, indecs);

                    index = indecs;
                }

                indicsData[indicsDataStep++] = index;

            }
            int totalSize=vertexDataList.size();
            vertexData = new float[totalSize];
            for (int i = 0; i < totalSize; i++) {
                vertexData[i]=vertexDataList.get(i);
            }
            vertexDataList.clear();
			// vertextBuffer = FloatUtils.FloatArrayToNativeBuffer(vertexData);
			// indexBuffer = FloatUtils.ShortArrayToNativeBuffer(indicsData);

            br.close();
            isr.close();
            in.close();

		} catch (Throwable t)
		{
			t.printStackTrace();
		}finally {

        }

	}
}
