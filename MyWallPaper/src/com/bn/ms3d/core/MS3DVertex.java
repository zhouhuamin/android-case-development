package com.bn.ms3d.core;
import java.io.IOException;
import com.bn.ms3d.io.SmallEndianInputStream;
import com.bn.ms3d.mathutil.Vector3f;
public class MS3DVertex{//������Ϣ��
	private Vector3f initPosition;	//���ļ��ж�ȡ�Ķ���ԭʼxyz����
	private Vector3f currPosition;	//������ʵʱ�仯�Ķ���xyz����
	private byte bone;			//����ID	
	private MS3DVertex() {}
	//��ȡ������Ϣ
	public static MS3DVertex[] load(SmallEndianInputStream is) throws IOException{
		int count = is.readUnsignedShort();//��ȡ�������� 
		MS3DVertex[] vertexs = new MS3DVertex[count];//����������Ϣ��������
		for(int i=0; i<count; i++){//ѭ����ȡÿ���������Ϣ
			MS3DVertex vertex = new MS3DVertex();
			is.readByte();  //��־--��ʱ���ã������ӵ�
			vertex.initPosition = 			//����XYZ����
			  new Vector3f(is.readFloat(),is.readFloat(),is.readFloat());
			vertex.bone = is.readByte();//����ID
			is.readByte();  //��־--��ʱ���ã������ӵ�
			vertexs[i] = vertex;
		}
		return vertexs;
	}
	public final Vector3f getInitPosition(){
		return initPosition;
	}
	public final byte getBone(){
		return bone;
	}
	public final Vector3f getCurrPosition(){
		return currPosition;
	}
	public final void setCurrPosition(Vector3f buffer){
		this.currPosition = buffer;
	}}
