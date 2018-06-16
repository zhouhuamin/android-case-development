uniform mat4 uMVPMatrix; //�ܱ任����
uniform mat4 uMMatrix; //�任����

uniform vec3 uLightLocation;	//��Դλ��
uniform vec3 uCamera;	//�����λ��

attribute vec3 aPosition;  //����λ��
attribute vec3 aNormal;    //���㷨����
attribute vec2 aTexCoor;    //������������

uniform vec2 MoveST;

varying vec3 vNormal;

varying vec4 ambient;
varying vec4 diffuse;
varying vec4 specular;
varying vec2 vTextureCoord;  //���ڴ��ݸ�ƬԪ��ɫ���ı���
varying vec3 vPosition;
varying vec2 vMoveST;

//��λ����ռ���ķ���
void pointLight(					//��λ����ռ���ķ���
  in vec3 normal,				//������
  inout vec4 ambient,			//����������ǿ��
  inout vec4 diffuse,				//ɢ�������ǿ��
  inout vec4 specular,			//���������ǿ��
  in vec3 lightLocation,			//��Դλ��
  in vec4 lightAmbient,			//������ǿ��
  in vec4 lightDiffuse,			//ɢ���ǿ��
  in vec4 lightSpecular			//�����ǿ��
){
  ambient=lightAmbient;			//ֱ�ӵó������������ǿ��  
  vec3 normalTarget=aPosition+normal;	//����任��ķ�����
  vec3 newNormal=(uMMatrix*vec4(normalTarget,1)).xyz-(uMMatrix*vec4(aPosition,1)).xyz;
  newNormal=normalize(newNormal); 	//�Է��������
  //����ӱ���㵽�����������
  vec3 eye= normalize(uCamera-(uMMatrix*vec4(aPosition,1)).xyz);  
  //����ӱ���㵽��Դλ�õ�����vp
  vec3 vp= normalize(lightLocation-(uMMatrix*vec4(aPosition,1)).xyz);  
  vp=normalize(vp);//��ʽ��vp
  vec3 halfVector=normalize(vp+eye);	//����������ߵİ�����    
  float shininess=50.0;				//�ֲڶȣ�ԽСԽ�⻬
  float nDotViewPosition=max(0.0,dot(newNormal,vp)); 	//��������vp�ĵ����0�����ֵ
  diffuse=lightDiffuse*nDotViewPosition;				//����ɢ��������ǿ��
  float nDotViewHalfVector=dot(newNormal,halfVector);	//������������ĵ�� 
  float powerFactor=max(0.0,pow(nDotViewHalfVector,shininess)); 	//���淴���ǿ������
  specular=lightSpecular*powerFactor;    			//���㾵��������ǿ��
}
void main()     
{                            		
   gl_Position = uMVPMatrix * vec4(aPosition,1); //�����ܱ任�������˴λ��ƴ˶���λ��
   vec4 ambientTemp, diffuseTemp, specularTemp;   //��Ż����⡢ɢ��⡢���淴������ʱ����
   pointLight(normalize(aNormal),ambientTemp,diffuseTemp,specularTemp,uLightLocation,vec4(0.255,0.255,0.255,1.0),vec4(0.9,0.9,0.9,1.0),vec4(0.4,0.4,0.4,1.0));  
   
   ambient=ambientTemp;
   diffuse=diffuseTemp;
   specular=specularTemp;
  vec3 normalTarget=aPosition+aNormal;	//����任��ķ�����
  vec3 newNormal=(uMMatrix*vec4(normalTarget,1)).xyz-(uMMatrix*vec4(aPosition,1)).xyz;
  vNormal=normalize(newNormal); 	//�Է��������
   //vNormal=aNormal;
   //vNormal=(uMMatrix*vec4(aNormal,1)).xyz;
   vTextureCoord = aTexCoor;//�����յ��������괫�ݸ�ƬԪ��ɫ��
   vPosition=(uMMatrix*vec4(aPosition,1)).xyz;
   vMoveST=MoveST;
}                        