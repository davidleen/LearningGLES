precision mediump float;
varying vec2 vTextureCoord;//接收从顶点着色器过来的参数
uniform sampler2D sTextureGrass;//草皮纹理内容数据
uniform sampler2D sTextureRock;//岩石纹理内容数据
varying float currY;//高度位置
uniform float landStartY;							//过程纹理起始Y坐标
uniform float landYSpan;							//过程纹理跨度
void main()                         
{           
   
   		 
   		 
   		  vec4 gColor=texture2D(sTextureGrass, vTextureCoord); 	//从草皮纹理中采样出颜色
   vec4 rColor=texture2D(sTextureRock, vTextureCoord); 	//从岩石纹理中采样出颜色
   vec4 finalColor;									//最终颜色
   if(currY<landStartY){			
	  finalColor=gColor;	//当片元Y坐标小于过程纹理起始Y坐标时采用草皮纹理
   }else if(currY>landStartY+landYSpan){
	  finalColor=rColor;	//当片元Y坐标大于过程纹理起始Y坐标加跨度时采用岩石纹理
   }else{
       float currYRatio=(currY-landStartY)/landYSpan;	//计算岩石纹理所占的百分比
       finalColor= currYRatio*rColor+(1.0- currYRatio)*gColor;//将岩石、草皮纹理颜色按比例混合
   } 
	   gl_FragColor = finalColor; //给此片元最终颜色值    
   		
}              