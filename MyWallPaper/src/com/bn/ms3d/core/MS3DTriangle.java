package com.bn.ms3d.core;
import java.io.IOException;
import com.bn.ms3d.io.SmallEndianInputStream;
import com.bn.ms3d.mathutil.Vector3f;
//�������������װ��������װ��������Ϣ
public class MS3DTriangle{
	private int[] indexs;		 //��װ����ֵ	
	private Vector3f[] normals;	 //3�����㷨������	
	private Vector3f s;			 //�������������S����
	private Vector3f t;			 //�������������T����
	private byte smoothingGroup; //ƽ����
	private byte groupIndex;	 //������
	private MS3DTriangle() {}
	//������������װ��Ϣ��������ķ���
	public static MS3DTriangle[] load(SmallEndianInputStream is) throws IOException{
		int count = is.readUnsignedShort();//��ȡ����������
	    //������������װ��Ϣ��������
		MS3DTriangle[] triangles = new MS3DTriangle[count];
		for(int i=0; i<count; i++){//ѭ������ÿһ�������ε���װ������Ϣ
			MS3DTriangle triangle= new MS3DTriangle();
			is.readUnsignedShort();//��־-��ʱ���ã������ӵ�
			triangle.indexs = new int[]{//��������
				is.readUnsignedShort(),
				is.readUnsignedShort(),
				is.readUnsignedShort()
			};
			triangle.normals = new Vector3f[3];//������������ķ�����
			for(int j=0; j<3; j++){
				triangle.normals[j] = new Vector3f(
					is.readFloat(),
					is.readFloat(),
					is.readFloat()
				);}
			triangle.s = new Vector3f(//�����������������S����
					is.readFloat(),
					is.readFloat(),
					is.readFloat()
			); 
			triangle.t = new Vector3f(//�����������������T����
					is.readFloat(),
					is.readFloat(),
					is.readFloat()
			);
			triangle.smoothingGroup = is.readByte();//����ƽ������Ϣ
			triangle.groupIndex = is.readByte();//��������Ϣ
			triangles[i] = triangle;
		}
		return triangles;
	}
	public final int[] getIndexs(){
		return indexs;
	}
	public final Vector3f[] getNormals(){
		return normals;
	}
	public final Vector3f getS(){
		return s;
	}
	public final Vector3f getT(){
		return t;
	}
	public final byte getSmoothingGroup(){
		return smoothingGroup;
	}
	public final byte getGroupIndex(){
		return groupIndex;
	}}
