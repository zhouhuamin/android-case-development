package com.bn.ms3d.core;
import java.io.IOException;
import com.bn.ms3d.io.SmallEndianInputStream;
public class MS3DHeader{//MS3D�ļ�ͷ��Ϣ
	String id;
	int version;	
	private MS3DHeader(String id, int version){
		this.id = id;
		this.version = version;
	}
	//��ָ��ms3d�ļ������ж�ȡ�ļ�ͷ��Ϣ������MS3DHeader����
	public static MS3DHeader load(SmallEndianInputStream is) throws IOException{
		return new MS3DHeader(is.readString(10), is.readInt());
	}
	public String getId(){
		return id;
	}
	public int getVersion(){
		return version;
	}}
