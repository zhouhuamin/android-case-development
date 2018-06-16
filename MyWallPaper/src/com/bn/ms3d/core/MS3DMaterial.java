package com.bn.ms3d.core;
import java.io.IOException;

import com.bn.ms3d.io.SmallEndianInputStream;
import com.bn.ms3d.texutil.TextureManager;
//������Ϣ
public class MS3DMaterial{	
	String name;		//��������	
	float[] ambient_color;	//������	
	float[] diffuse_color;	//ɢ���	
	float[] specular_color;	//�����	
	float[] emissive_color;	//�Է���	
	float shininess;		//�ֲڶ� 0-128	
	float transparency;		//͸���� 0-1	
	String textureName;		//�����ļ�����	
	private MS3DMaterial() {}
	//���ز�����Ϣ�ķ���
	public final static MS3DMaterial[] load(SmallEndianInputStream is, TextureManager manager) throws IOException{
		int count = is.readUnsignedShort();//��ȡ��������
		//����������Ϣ��������
		MS3DMaterial[] mals = new MS3DMaterial[count];
		for(int i=0; i<count; i++){//ѭ������ÿ�����ʵ���Ϣ
			MS3DMaterial mal = new MS3DMaterial();
			mal.name = is.readString(32);//��ȡ���ʵ�����
			mal.ambient_color = new float[4];//��ȡ��������Ϣ
			for(int j=0; j<4; j++){
				mal.ambient_color[j] = is.readFloat();
			}
			mal.diffuse_color = new float[4];//��ȡɢ�����Ϣ
			for(int j=0; j<4; j++){
				mal.diffuse_color[j] = is.readFloat();
			}
			mal.specular_color = new float[4];//��ȡ�������Ϣ
			for(int j=0; j<4; j++){
				mal.specular_color[j] = is.readFloat();
			}
			mal.emissive_color = new float[4];//��ȡ�Է�����Ϣ
			for(int j=0; j<4; j++){
				mal.emissive_color[j] = is.readFloat();
			}
			mal.shininess = is.readFloat();//��ȡ�ֲڶ���Ϣ
			mal.transparency = is.readFloat();//��ȡ͸������Ϣ
			is.readByte();//mode ��ʱ���ã������ӵ�
			//��ȡ����ͼƬ����
			mal.textureName = format(is.readString(128));
			is.readString(128);//͸������ ��ʱ���ã������ӵ�
			mals[i] = mal;
			//�������Ҳ���Ǽ�������ͼ��
			manager.addTexture(mal.name, mal.textureName);
		}
		return mals;
	}
	//���ļ�·����ժȡ������ͼ���ļ������硰xx.jpg��
	private final static String format(String path){
		int offset = path.lastIndexOf("\\");
		if(offset > -1) {
			return path.substring(offset + 1);
		}
		return path;
	}
	public final String getName(){
		return name;
	}}
