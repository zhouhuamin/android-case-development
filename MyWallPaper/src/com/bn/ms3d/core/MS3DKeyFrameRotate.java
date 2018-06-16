package com.bn.ms3d.core;
import java.io.IOException;
import com.bn.ms3d.io.SmallEndianInputStream;
import com.bn.ms3d.mathutil.Vector4f;
public class MS3DKeyFrameRotate{//�ؽ���ת�ؼ�֡��Ϣ
	private float time;			//ʱ��(��λΪ��)		
	private Vector4f rotate;	//��ת����
	private MS3DKeyFrameRotate() {}
	//���عؽ���ת�Ĺؼ�֡��Ϣ
	public final static MS3DKeyFrameRotate[] load(SmallEndianInputStream is, int num) throws IOException{
		//�����ؽ���ת�ؼ�֡��Ϣ���������
		MS3DKeyFrameRotate[] rotates = new MS3DKeyFrameRotate[num];
		for(int i=0; i<num; i++){//ѭ���������е���ת�ؼ�֡��Ϣ
			MS3DKeyFrameRotate rotateKF = new MS3DKeyFrameRotate();
			rotateKF.time = is.readFloat();//��ȡ�ؼ�֡ʱ��
			rotateKF.rotate = new Vector4f();//��ȡ�ؼ�֡��ת����
			//����ȡ����ŷ������ʽ����ת����ת��Ϊ��Ԫ����ʽ������Ϊ�˱����ڹؼ�֮֡����в�ֵ���㣩
			rotateKF.rotate.setFromEulerAngleToQuaternion(is.readFloat(),is.readFloat(),is.readFloat());
			rotates[i] = rotateKF;
		}
		return rotates;//������ת����
	}
	public final float getTime(){
		return time;
	}
	public final Vector4f getRotate(){
		return rotate;
	}}
