package com.bn.ms3d.core;
import java.io.IOException;
import com.bn.ms3d.io.SmallEndianInputStream;
import com.bn.ms3d.mathutil.Vector3f;
//�ؽ�ƽ�ƹؼ�֡��Ϣ
public class MS3DKeyFramePosition {
	private float time; // ʱ��(��λΪ��)
	private Vector3f position; // λ������
	private MS3DKeyFramePosition() {
	}
	// ���عؽ�ƽ�ƵĹؼ�֡��Ϣ
	public final static MS3DKeyFramePosition[] load(SmallEndianInputStream is,int num) throws IOException {
		// �����ؽ�ƽ�ƹؼ�֡��Ϣ��������
		MS3DKeyFramePosition[] positions = new MS3DKeyFramePosition[num];
		// ѭ������ÿ��ƽ�ƹؼ�֡����Ϣ
		for (int i = 0; i < num; i++) {
			MS3DKeyFramePosition position = new MS3DKeyFramePosition();
			position.time = is.readFloat();// ��ȡ�ؼ�֡ʱ��
			// ��ȡ�ؼ�֡λ����Ϣ
			position.position = new Vector3f(is.readFloat(), is.readFloat(),is.readFloat());
			positions[i] = position;
		}
		return positions;
	}
	public final float getTime() {
		return time;
	}
	public final Vector3f getPosition() {
		return position;
	}}
