package com.bn.ms3d.core;
import java.io.IOException;
import com.bn.ms3d.io.SmallEndianInputStream;
//����Ϣ
public class MS3DGroup{	
	private int[] indicies;		//���ڵ������εĶ�Ӧ����	
	private byte materialIndex;	//��������
	private MS3DGroup(){} 
	//��������Ϣ�ķ���
	public final static MS3DGroup[] load(SmallEndianInputStream is) throws IOException{
		int count = is.readUnsignedShort();//��ȡ������
		//��������Ϣ��������
		MS3DGroup[] groups = new MS3DGroup[count];
		for(int i=0; i<count; i++){//ѭ������ÿ�������Ϣ
			MS3DGroup group = new MS3DGroup();
			is.readByte();  //��־--��ʱ���ã������ӵ�
			is.readString(32);//��ȡ������--��ʱ���ã������ӵ�
			int indexCount = is.readUnsignedShort();//��ȡ��������������
			group.indicies = new int[indexCount];//����������������������
			for(int j=0; j<indexCount; j++){ //�������ڸ��������ε�����
				group.indicies[j] = is.readUnsignedShort();
			}
			group.materialIndex = is.readByte();//��ȡ��������
			groups[i] = group;
		}
		return groups;
	}
	public final int[] getIndicies(){
		return indicies;//���ڵ������εĶ�Ӧ����
	}
	public final byte getMaterialIndex(){
		return materialIndex;//��������
	}}
