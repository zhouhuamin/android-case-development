precision mediump float;
varying vec2 vTextureCoord; //���մӶ�����ɫ�������Ĳ���
uniform sampler2D sTexture;//������������
uniform sampler2D sTextureHd;//������������


varying vec3 vNormal;//������
varying vec3 vPosition;
varying vec4 ambient;//������
varying vec4 diffuse;//ɢ���
varying vec4 specular;//�����
void main()                         
{ 
    float f;
   //����ƬԪ�������в�������ɫֵ   
   vec4 finalColorDay;   
   vec4 finalColorNight;
   vec4 finalColorzj; 
      //����ƬԪ�������в�������ɫֵ 
   finalColorDay= texture2D(sTexture, vTextureCoord); 
   vec2 tempTexCoor=vec2((vPosition.x+20.8)/5.2,(vPosition.z+18.0)/2.5); //8*8
   
   if(vNormal.y>0.2)
   {
       finalColorNight = texture2D(sTextureHd, tempTexCoor); 
       f=(finalColorNight.r+finalColorNight.g+finalColorNight.b)/3.0; 
   }else if(vNormal.y<=0.2&&vNormal.y>=-0.2)
   {
       if(vNormal.y>=0.0&&vNormal.y<=0.2)
       {
           finalColorNight = texture2D(sTextureHd, tempTexCoor)*(1.0-2.5*(0.20-vNormal.y));//*(1��0-0.25*(0.20-vNormal.y)); 
           f=(finalColorNight.r+finalColorNight.g+finalColorNight.b)/3.0;
       }else if(vNormal.y<0.0&&vNormal.y>=-0.2){
       
           finalColorNight = texture2D(sTextureHd, tempTexCoor)*(0.5+2.5*vNormal.y); 
           f=(finalColorNight.r+finalColorNight.g+finalColorNight.b)/3.0;
       }
   }else if(vNormal.y<-0.2){
        f=0.0;
   }         
 
   finalColorzj =finalColorDay*(1.0+f*1.5);     
   gl_FragColor=finalColorzj*ambient+finalColorzj*specular+finalColorzj*diffuse; 
} 