precision mediump float;
varying vec2 vTextureCoord; //���մӶ�����ɫ�������Ĳ���
uniform sampler2D sTexture;//������������
uniform sampler2D sTextureHd;//������������

varying vec3 vPosition;
varying vec2 vMoveST;
varying vec4 ambient;//������
varying vec4 diffuse;//ɢ���
varying vec4 specular;//�����
void main()                         
{ 
   //����ƬԪ�������в�������ɫֵ   
   vec4 finalColorDay;   
   vec4 finalColorzj;         
   //����ƬԪ�������в�������ɫֵ 
   finalColorDay= texture2D(sTexture, vTextureCoord); 
   finalColorzj =finalColorDay;//*(1.0+f);     
   gl_FragColor=finalColorzj*ambient+finalColorzj*specular+finalColorzj*diffuse; 
}               