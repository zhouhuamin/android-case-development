precision mediump float;
varying vec2 vTextureCoord; //接收从顶点着色器过来的参数
uniform sampler2D sTexture;//纹理内容数据
uniform sampler2D sTextureHd;//纹理内容数据

varying vec3 vPosition;
varying vec2 vMoveST;
varying vec4 ambient;//环境光
varying vec4 diffuse;//散射光
varying vec4 specular;//镜面光
void main()                         
{ 
   //给此片元从纹理中采样出颜色值   
   vec4 finalColorDay;   
   vec4 finalColorzj;         
   //给此片元从纹理中采样出颜色值 
   finalColorDay= texture2D(sTexture, vTextureCoord); 
   finalColorzj =finalColorDay;//*(1.0+f);     
   gl_FragColor=finalColorzj*ambient+finalColorzj*specular+finalColorzj*diffuse; 
}               