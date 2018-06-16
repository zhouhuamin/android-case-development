package com.bn.ms3d.texutil;
import java.util.LinkedHashMap;
import java.util.Map;
import com.bn.ms3d.io.BitmapReader;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLUtils;

//���������
public class TextureManager{	 
	Map<String, Integer> textures = new LinkedHashMap<String, Integer>();
	Resources res;	
	public TextureManager(Resources res){
		this.res=res;
	}
	//���������ķ���
	public void addTexture(String texName, String fileName){
		if(!textures.keySet().contains(texName)){
			Bitmap bitmap = null;
			try{
				bitmap = BitmapReader.load(fileName,res);
		        int[] textures = new int[1];
				GLES20.glGenTextures(
						1,          //����������id������
						textures,   //����id������
						0           //ƫ����
				);    
		        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textures[0]);
				GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER,GLES20.GL_NEAREST);
				GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_MAG_FILTER,GLES20.GL_LINEAR);
				GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S,GLES20.GL_CLAMP_TO_EDGE);
				GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T,GLES20.GL_CLAMP_TO_EDGE);
			        GLUtils.texImage2D
		        (
		        		GLES20.GL_TEXTURE_2D,   //�������ͣ���OpenGL ES�б���ΪGL10.GL_TEXTURE_2D
		        		0, 					  //����Ĳ�Σ�0��ʾ����ͼ��㣬�������Ϊֱ����ͼ
		        		bitmap, 			  //����ͼ��
		        		0					  //����߿�ߴ�
		        );
			    
			    bitmap.recycle();				
				this.textures.put(texName, textures[0]);						
			} 
			catch(Exception e) 
			{
				e.printStackTrace();
			}}}
		//��ȡָ�����Ƶ������Ӧ������id�ķ���
	public final int getTexture(String texName){
		Integer tex = this.textures.get(texName); 
		return (tex != null)?tex.intValue():0;
	}
	//����ǰ����ʹ��ָ������ķ���
	public final void fillTexture(String texName){
		int texture = this.getTexture(texName);
		if(texture != 0){
			//����2D����
			GLES20.glEnable(GLES20.GL_TEXTURE_2D);			
			//������
	        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
	        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture);
		}}
	//�ͷ�ָ�����Ƶ�������Դ
	public final void dispose(String texName){
		if(textures.keySet().contains(texName)){
			GLES20.glDeleteTextures(1, new int[]{this.textures.get(texName)}, 0);
			System.gc();
		}}}
