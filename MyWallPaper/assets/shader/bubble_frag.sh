precision mediump float;
uniform sampler2D sTexture;//������������
varying vec2 vTextureCoord; //���մӶ�����ɫ�������Ĳ���
void main()                         
{
   //�����������ɫ����ƬԪ
   vec4 finalColor=texture2D(sTexture, vTextureCoord);
   //����ƬԪ��ɫֵ 
   gl_FragColor = finalColor;//����ƬԪ��ɫֵ
}              