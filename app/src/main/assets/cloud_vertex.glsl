 uniform mat4 uMVPMatrix; 
 uniform mat4 uMMatrix; //变换矩阵    包括平移  旋转  缩放  
 uniform vec3 uLightLocationSun;   //太阳光源位置 
 uniform vec4 abientLight;//环境光
 uniform vec4 diffuseLight;//散射光
 uniform vec3 cameraPosition;  //相机位置
 uniform vec4 mSpecLight; //镜面光
 attribute vec2 mDayTexCoordLoc;
attribute vec2 mNightTexCoordLoc;
attribute vec3 mPositionLoc;
attribute vec3 mNormalPositionLoc;   



 
varying vec2 vTextureCoord;    //纹理坐标
varying vec4 vAmbient;    //环境光定义
varying vec4 vDiffuse;   //散射光分量 传递给片元
varying vec4 vSpec;   //spec光分量 传递给片元
 


 

//计算光照效果

void calculateLight(in vec3 normal,in vec3 aPosition, inout vec4 ambientLightTemp,inout  vec4 diffuseLightTemp,inout vec4 specLightTemp, in vec3 lightLocation ,in vec4 ambientLight,in vec4 diffuseLight,in vec4 specLight)
{

	 vec3 normalTarget=normal + aPosition;
	  vec3 newPosition= vec3( (uMMatrix * vec4(aPosition,1)).xyz); //变换后顶点位置
	  vec3 newNormal = vec3( (uMMatrix * vec4(normalTarget,1)).xyz ) - newPosition; //计算变换后的法向量。
	  newNormal= normalize ( newNormal);
  
	  vec3 lightVector= lightLocation- newPosition;   //光照到顶点的向量
	  
	  
	  float diffuseFactor = max(0.0,dot(newNormal, normalize(lightVector))); //计算散射光强度
	  
	  diffuseLightTemp= diffuseLight * diffuseFactor;  //计算散射光最终强度
	  
	  vec3 cameraVector= cameraPosition - newPosition ;   //相机到顶点的向量
  
  
  
	  vec3 halfVector= normalize( lightVector + cameraVector);
	  
	  float shininess=10.0 	;						// 粗糙度 越小越光滑
	  float nDotView= dot(halfVector, newNormal);   
	  float powerFactor= max(0.0,pow(nDotView,shininess));  //计算镜面光  强度因子
	  
	  specLightTemp= specLight * powerFactor;   //计算镜面光最终强度
	  
	  
  
  
		ambientLightTemp=ambientLight;  //直接复制散射光
	  
}


 void main()
{

	gl_Position =  uMVPMatrix *  vec4(mPositionLoc,1);
	 
	vTextureCoord = mDayTexCoordLoc;
	
	
	
	  
	 vec4 ambientLightTemp= vec4(0.0,0.0,0.0,0.0);
	 vec4 diffuseTemp= vec4(0.0,0.0,0.0,0.0);
	 vec4 specTemp= vec4(0.0,0.0,0.0,0.0);
	 
	  calculateLight(normalize( mNormalPositionLoc) , mPositionLoc,   ambientLightTemp,diffuseTemp,specTemp,  uLightLocationSun ,abientLight,diffuseLight,  mSpecLight);
	 
	 
	 vAmbient =   ambientLightTemp;
	  vDiffuse=diffuseTemp;
	  vSpec=specTemp;
	 
	

}

