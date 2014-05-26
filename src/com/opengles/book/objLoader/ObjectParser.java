package com.opengles.book.objLoader;

import android.content.Context;
import android.util.Log;

import com.opengles.book.utils.Vector3D;
import junit.framework.Assert;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

/**
 * 。obj 文件 解析读取
 * Created by davidleen29   qq:67320337
 * on 14-4-16.
 */
public class ObjectParser {

    public static ObjModel parse(Context context,String path, String fileName) {


        // 原始顶点坐标列表--直接从obj文件中加载
        List<Float> positions = new ArrayList<Float>();
        List<Float> textures = new ArrayList<Float>();
        List<Float> normals = new ArrayList<Float>();
        List<Group> groups = new ArrayList<Group>();
        Material.MaterialList materialList = null;


        Group part = new Group(new Material("defalult"));
        groups.add(part);


        String SPLIT_CHARS = "[ ]+";

        try {
           BufferedReader br= openFile(context,path,fileName);

            String readString = null;
            while ((readString = br.readLine()) != null) {
                String[] stringArray = readString.split(SPLIT_CHARS);
                String prefix = stringArray[0].trim();

                if (prefix.equalsIgnoreCase("v")) {
                    // 顶点。
                    positions.add(Float.parseFloat(stringArray[1]));
                    positions.add(Float.parseFloat(stringArray[2]));
                    positions.add(Float.parseFloat(stringArray[3]));

                } else if (prefix.equalsIgnoreCase("vt")) {
                    // 纹理
                    textures.add(Float.parseFloat(stringArray[1]));// s
                    textures.add(Float.parseFloat(stringArray[2])); // t

                } else if (prefix.equalsIgnoreCase("vn")) {
                    // 法向量
                    normals.add(Float.parseFloat(stringArray[1]));// x
                    normals.add(Float.parseFloat(stringArray[2]));// y
                    normals.add(Float.parseFloat(stringArray[3]));// z

                } else if (prefix.equalsIgnoreCase("f")) {
                    // 面 三角形。
                    // 三个顶点索引值的数组

                    part.addPolygonal(stringArray);


                } else if (prefix.equals("mtllib")) //SPLIT_CHARS
                {
                    materialList = MaterialParser.loadFile(context,path, stringArray[1]);
                } else if (prefix.equals("usemtl")) {
                    Assert.assertTrue(   "materialList must has set by mtllib",materialList != null);
                    Material currentMaterial = materialList.findByName(stringArray[1]);
                    part = new Group(currentMaterial);
                    groups.add(part);

                }
            }
            br.close();



            int CORD_SIZE = 3;
            int TEXTURE_SIZE = 2;
            int NORMAL_SIZE = 3;
            int VERTEX_FULL_SIZE = CORD_SIZE + TEXTURE_SIZE + NORMAL_SIZE;
            int VERTEX_COUNT_OF_TRIANGLE = 3;

            int vetexCount = positions.size() / CORD_SIZE;


            List<Float> vertexDataList = new ArrayList<Float>(vetexCount * VERTEX_FULL_SIZE);


            Map<String, Short> faceToIndex = new HashMap<String, Short>();


            boolean hasNormal = normals.size() > 0;
            List<ObjModelPart> objModelParts = new ArrayList<ObjModelPart>(groups.size());

            int triangleCount =0;
            //计算所有三角形个数
            for (Group group : groups) {
                triangleCount+= group.triangles.size();
            }
            //初始化索引数据
            short[] indexData = new short[triangleCount * VERTEX_COUNT_OF_TRIANGLE];
            int indicsDataStep = 0;
            for (Group group : groups) {


                if(group.triangles.size()<=0) continue;
            	 


                ObjModelPart objModelPart = new ObjModelPart(group.material);


                objModelPart.index=indicsDataStep;


                //遍历三角形各顶点
                for (String[] triangle : group.triangles) {
                    for (String s : triangle) {


                        Short index = faceToIndex.get(s);
                        if (index == null) {
                            String[] vertexString = s.split("/");
                            int positionLingIndex = (Integer.valueOf(vertexString[0]) - 1)
                                    * CORD_SIZE;
                            for (int i = 0; i < CORD_SIZE; i++) {
                                vertexDataList.add(positions
                                        .get(positionLingIndex + i));
                            }
                            float[] normalData = new float[NORMAL_SIZE];
                            //法向量数据
                            if (!hasNormal)// 无法向量暂时 直接使用坐标数据
                            {


                                for (int i = 0; i < NORMAL_SIZE; i++) {
                                    normalData[i] = vertexDataList.get(vertexDataList.size() - NORMAL_SIZE + i);

                                }


                            } else {
                                int normalCordLine = (Integer.valueOf(vertexString[2]) - 1)
                                        * NORMAL_SIZE;

                                for (int i = 0; i < NORMAL_SIZE; i++) {
                                    normalData[i] = normals
                                            .get(normalCordLine + i);

                                }

                            }

                            //标准化
                            Vector3D.normalize(normalData);
                            for (int i = 0; i < NORMAL_SIZE; i++) {

                                vertexDataList.add(normalData[i]);
                            }
                            	
                            int textCordLine =vertexString[1].trim().equals("")?-1: (Integer.valueOf(vertexString[1]) - 1)
                                    * TEXTURE_SIZE;
                            for (int i = 0; i < TEXTURE_SIZE; i++) {
                                vertexDataList.add(textCordLine==-1?0:textures
                                        .get(textCordLine + i));
                            }


                            //index start from 0
                            short indecs = (short) (vertexDataList.size() / VERTEX_FULL_SIZE - 1);
                            faceToIndex.put(s, indecs);

                            index = indecs;
                        }

                        indexData[indicsDataStep++] = index;

                    }
                }
                objModelPart.length=indicsDataStep-objModelPart.index;

                objModelParts.add(objModelPart);
            }
            int totalSize = vertexDataList.size();
            float[] vertexData = new float[totalSize];
            for (int i = 0; i < totalSize; i++) {
                vertexData[i] = vertexDataList.get(i);
            }
            vertexDataList.clear();


            //清空无用数据
            positions.clear();
            normals.clear();
            textures.clear();


            ObjModel objModel = new ObjModel();
            objModel.path=path;
            objModel.vertexData = vertexData;
            objModel.indexData=indexData;
            objModel.parts=objModelParts;

            return objModel;


        } catch (IOException e) {
            e.printStackTrace();
        }


        return null;
    }


    /**
     * 分组数据  包含Materil faces
     */
    public static class Group {

        public Material material;
        public ArrayList<String[]> triangles;
        public int line;


        public Group(Material material) {
            this.material = material;
            triangles = new ArrayList<String[]>();


        }

        /**
         * 添加f 行 数据
         * @param polygonal
         */
        public void addPolygonal(String[] polygonal) {

            toTriangle(Arrays.copyOfRange(polygonal, 1, polygonal.length));

        }

        private void toTriangle(String[] polygonal) {

            int length = polygonal.length;
            if (length <= 3)
                triangles.add(polygonal);
            else

                for (int i = 1; i < length - 1; i++) {
                    triangles.add(new String[]{polygonal[0], polygonal[i], polygonal[i + 1]});


                }


        }
    }

    /**
     * 打开文件  获取输入流
     * @param context
     * @param path
     * @param fileName
     * @return
     * @throws IOException
     */
    public static BufferedReader openFile(Context context,String path,String fileName) throws IOException {
        InputStream in = context.getResources().getAssets().open(path+fileName);
        InputStreamReader isr = new InputStreamReader(in);
       return new BufferedReader(isr);
    }
}
