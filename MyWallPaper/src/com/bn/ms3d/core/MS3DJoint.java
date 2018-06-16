package com.bn.ms3d.core;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import com.bn.ms3d.io.SmallEndianInputStream;
import com.bn.ms3d.mathutil.Matrix4f;
import com.bn.ms3d.mathutil.Vector3f;
import com.bn.ms3d.mathutil.Vector4f;
public class MS3DJoint {// �ؽ���Ϣ
	private String name; // �ؽ�����
	private String parentName; // ���ؽ�����
	private MS3DJoint parent; // ���ؽ�
	private Vector3f rotate; // ��ʼ��תֵ
	private Vector3f position; // ��ʼλ��
	private MS3DKeyFrameRotate[] rotates; // �ؼ�֡��תֵ
	private MS3DKeyFramePosition[] positions; // �ؼ�֡λ������
	private Matrix4f relative; // ��Ծ����ӹؽ��ڸ��ؽ�����ϵ�еı任����
	private Matrix4f absolute; // ��ʼ���Ծ����ӹؽ�����������ϵ�еĳ�ʼ�任����
	private Matrix4f matrix; // �任���󣨵�ǰ���Ծ���
	private MS3DJoint() {
	}
	// ���عؽ���Ϣ
	public final static MS3DJoint[] load(SmallEndianInputStream is)throws IOException {
		int count = is.readUnsignedShort();// ��ȡ�ؽ�����
		MS3DJoint[] joints = new MS3DJoint[count];// �����ؽ���Ϣ��������
		// �����ؽ���Ϣ���������Ƶ�map
		Map<String, MS3DJoint> map = new LinkedHashMap<String, MS3DJoint>();
		for (int i = 0; i < count; i++) {// ѭ������ÿ���ؽڵ���Ϣ
			MS3DJoint joint = new MS3DJoint();
			is.readByte();// ��־ ��ʱ���ã������ӵ�
			joint.name = is.readString(32);// ��ȡ�ؽ�����
			joint.parentName = is.readString(32);// ��ȡ���ؽ�����
			// ���عؽڵ���ת����
			joint.rotate = new Vector3f(is.readFloat(), is.readFloat(),is.readFloat());
			// ���عؽڵ�λ������
			joint.position = new Vector3f(is.readFloat(), is.readFloat(),is.readFloat());
			// ��ȡ�ؽ���ת�Ĺؼ�֡����
			int numKeyFramesRot = is.readUnsignedShort();
			// ��ȡ�ؽ�ƽ�ƵĹؼ�֡����
			int numKeyFramesPos = is.readUnsignedShort();
			// ���ؽ���ת�Ĺؼ�֡������Ϊ0������عؽ���ת�Ĺؼ�֡��ֵ
			if (numKeyFramesRot > 0) {
				joint.rotates = MS3DKeyFrameRotate.load(is, numKeyFramesRot);
			}
			// ���ؽ�ƽ�ƵĹؼ�֡������Ϊ0������عؽ�ƽ�ƵĹؼ�֡��ֵ
			if (numKeyFramesPos > 0) {
				joint.positions = MS3DKeyFramePosition.load(is, numKeyFramesPos);
			}
			joints[i] = joint;
			map.put(joint.name, joint);// ���ؽ���Ϣ����洢��map�Ա�����
			joint.parent = map.get(joint.parentName); // ��ô˹ؽڵĸ��ؽ�
			joint.relative = new Matrix4f();// ������Ծ���
			joint.relative.loadIdentity();
			// ������ת
			joint.relative.genRotationFromEulerAngle(joint.rotate);
			joint.relative.setTranslation(joint.position); // ����ƽ��
			joint.absolute = new Matrix4f();// ���þ��Ծ���
			joint.absolute.loadIdentity();
			if (joint.parent != null) {// �Ƿ��и��ؽ�
				// �и��ؽڵĻ����Ծ�����ڸ��ؽڵľ��Ծ�������ӹؽڵ���Ծ���
				joint.absolute.mul(joint.parent.absolute, joint.relative);
			} else {
				// �޸��ؽڵĻ���Ծ���Ϊ���Ծ���
				joint.absolute.copyFrom(joint.relative);
			}}
		map.clear();// ���map
		map = null;// ���map
		return joints;
	}
	public final void update(float time) {// ���´˹ؽ�
		// ����ת��ƽ�ƵĹؽ��䵱ǰ���Ծ�����ڳ�ʼ���Ծ���
		if (this.rotates == null && this.positions == null) {
			if (this.matrix == null) {
				this.matrix = new Matrix4f();
			}
			this.matrix.copyFrom(this.absolute);
			return;
		}
		Matrix4f matrix = this.rotate(time);// ����ת
		matrix.setTranslation(this.position(time));// ��ƽ��
		matrix.mul(this.relative, matrix);// ��������Ծ������
		if (this.parent != null) {// �Ƿ��и��ؽ�
			this.matrix = matrix.mul(this.parent.matrix, matrix);// �и��ؽ�
		} else {
			this.matrix = matrix;// �޸��ؽ�
		}}
	private Matrix4f rotate(float time) {// ���ݵ�ǰ����ʱ�������ת��ֵ����
		Matrix4f m = new Matrix4f();
		int index = 0;
		int size = this.rotates.length;
		// ����ʱ��ȷ����ǰ֡Ӧ������һ����ʼ�ؼ�֡�������
		while (index < size && this.rotates[index].getTime() < time) {
			index++;
		}
		if (index == 0) {
			m.genRotateFromQuaternion(this.rotates[0].getRotate());
		} 
		else if (index == size) {
			m.genRotateFromQuaternion(this.rotates[size - 1].getRotate());
		}
		else {
			MS3DKeyFrameRotate left = this.rotates[index - 1];// ��һ�ؼ�֡����ת
			MS3DKeyFrameRotate right = this.rotates[index];// �˹ؼ�֡����ת
			Vector4f v = new Vector4f();// ��ֵ������ǰ֡����ת
			v.interpolate(left.getRotate(), right.getRotate(),(time - left.getTime())/(right.getTime() - left.getTime()));
			// ����Ԫ����ʽ����ת��Ϊ������ʽ
			m.genRotateFromQuaternion(v);
		}
		return m;
	}
	private Vector3f position(float time) {// ���ݵ�ǰ����ʱ�����ƽ�Ʋ�ֵ����
		int index = 0;
		int size = this.positions.length;
		// ����ʱ��ȷ����ǰ֡Ӧ������һ����ʼ�ؼ�֡�������
		while (index < size && this.positions[index].getTime() < time) {
			index++;
		}
		if (index == 0) {
			return this.positions[0].getPosition();
		} 
		else if (index == size) {
			return this.positions[size - 1].getPosition();
		} 
		else {
			// ��һ�ؼ�֡��ƽ��
			MS3DKeyFramePosition left = this.positions[index - 1];
			// �˹ؼ�֡��ƽ��
			MS3DKeyFramePosition right = this.positions[index];
			// ��ֵ�������ǰ�ؼ�֡��ƽ��
			Vector3f v = new Vector3f();
			v.interpolate(left.getPosition(), right.getPosition(),
					(time - left.getTime())
							/ (right.getTime() - left.getTime()));
			return v;
		}}
	public final Matrix4f getMatrix() {
		return matrix;
	}
	public final Matrix4f getAbsolute() {
		return absolute;
	}}
