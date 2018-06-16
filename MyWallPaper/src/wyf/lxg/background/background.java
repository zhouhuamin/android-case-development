package wyf.lxg.background;
import static wyf.lxg.Constant.ShaderUtil.createProgram;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import wyf.lxg.Constant.Constant;
import wyf.lxg.Constant.MatrixState;
import wyf.lxg.Constant.ShaderUtil;
import wyf.lxg.mywallpaper.MySurfaceView;

import android.opengl.GLES20;

//����������
public class background 
{	
	int mProgram;//�Զ�����Ⱦ���߳���id
  int muMVPMatrixHandle;//�ܱ任��������id
  int maPositionHandle; //����λ����������id  
  int maTexCoorHandle; //��������������������id 
  
  String mVertexShader;//������ɫ��    	 
  String mFragmentShader;//ƬԪ��ɫ��
	
	FloatBuffer   mVertexBuffer;//�����������ݻ���
	FloatBuffer   mTexCoorBuffer;//���������������ݻ���
  int vCount=0;   
  public background(MySurfaceView mv)
  {    	
  	//��ʼ��������������ɫ����
  	initVertexData();
  	//��ʼ����ɫ��        
  	initShader(mv);
  }
  
  //��ʼ��������������ɫ���ݵķ���
  public void initVertexData()
  {
  	//�����������ݵĳ�ʼ��================begin============================
  	 vCount=6;//���������  
       float vertices[]=new float[]//����������������
       {
      		-24f*Constant.SCREEN_SCALEX,8f*Constant.SCREEN_SCALEY,-30*Constant.SCREEN_SCALEZ,
        	-24f*Constant.SCREEN_SCALEX,-22f*Constant.SCREEN_SCALEY,-30*Constant.SCREEN_SCALEZ,
        	24f*Constant.SCREEN_SCALEX,8f*Constant.SCREEN_SCALEY,-30*Constant.SCREEN_SCALEZ,
        	-24f*Constant.SCREEN_SCALEX,-22f*Constant.SCREEN_SCALEY,-30*Constant.SCREEN_SCALEZ,
        	24f*Constant.SCREEN_SCALEX,-22f*Constant.SCREEN_SCALEY,-30*Constant.SCREEN_SCALEZ,
        	24f*Constant.SCREEN_SCALEX,8f*Constant.SCREEN_SCALEY,-30*Constant.SCREEN_SCALEZ,   
       };
       //���������������ݻ���
       //vertices.length*4����Ϊһ�������ĸ��ֽ�
       ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);
       vbb.order(ByteOrder.nativeOrder());//�����ֽ�˳��
       mVertexBuffer = vbb.asFloatBuffer();//ת��ΪFloat�ͻ���
       mVertexBuffer.put(vertices);//�򻺳����з��붥����������
       mVertexBuffer.position(0);//���û�������ʼλ��
       //�ر���ʾ�����ڲ�ͬƽ̨�ֽ�˳��ͬ���ݵ�Ԫ�����ֽڵ�һ��Ҫ����ByteBuffer
       //ת�����ؼ���Ҫͨ��ByteOrder����nativeOrder()�������п��ܻ������
       //�����������ݵĳ�ʼ��================begin============================
     //�����������껺��
       float textureCoors[]=new float[]//��������S��T����ֵ����
      	{  		   
      	         		0,0,0,
      	         		1,1,0,
      	         		0,1,1,
      	         		1,1,0
      	 };       
       //���������������ݻ���
       //textureCoors.length��4����Ϊһ��float�������ĸ��ֽ�
       ByteBuffer cbb = ByteBuffer.allocateDirect(textureCoors.length*4);
       cbb.order(ByteOrder.nativeOrder());//�����ֽ�˳��
       mTexCoorBuffer = cbb.asFloatBuffer();//ת��ΪFloat�ͻ���
       mTexCoorBuffer.put(textureCoors);//�򻺳����з��붥����ɫ����
       mTexCoorBuffer.position(0);//���û�������ʼλ��
       //�ر���ʾ�����ڲ�ͬƽ̨�ֽ�˳��ͬ���ݵ�Ԫ�����ֽڵ�һ��Ҫ����ByteBuffer
       //ת�����ؼ���Ҫͨ��ByteOrder����nativeOrder()�������п��ܻ������
       //�����������ݵĳ�ʼ��================end============================
  }

  //��ʼ����ɫ��
  public void initShader(MySurfaceView mv)
  {
  	//���ض�����ɫ���Ľű�����
      mVertexShader=ShaderUtil.loadFromAssetsFile("back_vertex.sh", mv.getResources());
      //����ƬԪ��ɫ���Ľű�����
      mFragmentShader=ShaderUtil.loadFromAssetsFile("back_frag.sh", mv.getResources());  
      //���ڶ�����ɫ����ƬԪ��ɫ����������
      mProgram = createProgram(mVertexShader, mFragmentShader);
      //��ȡ�����ж���λ����������id  
      maPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");
      //��ȡ�����ж�������������������id  
      maTexCoorHandle= GLES20.glGetAttribLocation(mProgram, "aTexCoor");
      //��ȡ�������ܱ任��������id
      muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");  
  }
  
  public void drawSelf(int texId)
  {        
  	 //�ƶ�ʹ��ĳ��shader����
  	 GLES20.glUseProgram(mProgram);        
  	 
  	 MatrixState.setInitStack();
       //�����ձ任������shader����
       GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(), 0); 
       //Ϊ����ָ������λ������
       GLES20.glVertexAttribPointer  
       (
       		maPositionHandle,   
       		3, 
       		GLES20.GL_FLOAT, 
       		false,
              3*4,   
              mVertexBuffer
       );       
       //Ϊ����ָ������������������
       GLES20.glVertexAttribPointer  
       (
      		maTexCoorHandle, 
       		2, 
       		GLES20.GL_FLOAT, 
       		false,
              2*4,   
              mTexCoorBuffer
       );   
       //��������λ����������
       GLES20.glEnableVertexAttribArray(maPositionHandle);  
       GLES20.glEnableVertexAttribArray(maTexCoorHandle);  
       
       //������
       GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
       GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texId);
       
       //������������
       GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vCount); 
  }
}