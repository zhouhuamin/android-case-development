package wyf.lxg.fish;

import com.bn.ms3d.core.MS3DModel;
import wyf.lxg.Constant.Constant;
import wyf.lxg.Constant.MatrixState;
import wyf.lxg.Constant.Vector3f;


public class SingleFish {
	// ��ʼ���������λ�ã��ٶȣ��������
	public Vector3f position;
	public Vector3f speed;
	public Vector3f force;
	// �����ʳ֮֮���������
	public Vector3f attractforce;
	// ����������㱾���ܵ���������������ɷ���
	public float weight;
	// ����������ת���Ƕ�Z���Y��!
	float yAngle;
	float zAngle;
	float tempY;
	float tempZ;	
	// ��ʼ���������
	//LoadedObjectVertexNormalTextureFish lovn;
	int texld;
	MS3DModel md;
	int texid;
	float time;//λ��    �ٶ�     ��     ������      ����
	public SingleFish(MS3DModel  mx,int texid,
			Vector3f Position, Vector3f Speed, Vector3f force,
			Vector3f attractforce, float weight) {
		this.md=mx;
		this.texid=texid;
		this.position = Position;
		this.speed = Speed;
		this.force = force;
		this.attractforce = attractforce;
		this.weight = weight;
	}
 // drawSelf����,
 	public void drawSelf() {
 		MatrixState.pushMatrix();
 		MatrixState.translate(this.position.x, this.position.y, this.position.z);
 		MatrixState.rotate(yAngle, 0, 1, 0);
 		MatrixState.rotate(zAngle, 0, 0, 1);
 		if (md != null) {
 			this.md.animate(time,texid); 
 		}
 		MatrixState.popMatrix();
 		//����ģ�Ͷ���ʱ��
        time += 0.050f;
        //����ǰ����ʱ������ܵĶ���ʱ����ʵ�ʲ���ʱ����ڵ�ǰ����ʱ���ȥ�ܵĶ���ʱ��
        if(time > this.md.getTotalTime()) 
        {
        	time = time - this.md.getTotalTime();
        }
 	}
	// ������ζ��ķ��������ݵ�ǰ������λ���Լ��ٶ������������һ��λ��!
	public void fishMove() {
		/**
		 * һ��ע���ж�x��z�ٶ�ͬʱΪ0 �����
		 */
		// zAngle�ļ���
		float fz = (speed.x * speed.x + speed.y * 0 + speed.z * speed.z);
		// ��ĸ
		float fm = (float) (Math.sqrt(speed.x * speed.x + speed.y * speed.y
				+ speed.z * speed.z) * Math.sqrt(speed.x * speed.x + speed.z
				* speed.z));
		// cosֵ
		float angle = fz / fm;
		// ��Z�����ת�Ƕ�
		tempZ = (float) (180f / Math.PI) * (float) Math.acos(angle);
		// yAngle�ļ���
		fz = (speed.x * Constant.initialize.x + speed.z * Constant.initialize.z);
		// ��ĸ
		fm = (float) (Math.sqrt(Constant.initialize.x * Constant.initialize.x
				+ Constant.initialize.z * Constant.initialize.z) * Math
				.sqrt(speed.x * speed.x + speed.z * speed.z));
		// cosֵ
		angle = fz / fm;
		// ��Y�����ת�Ƕ�
		tempY = (float) (180f / Math.PI) * (float) Math.acos(angle);
		// �õ��нǸ���Speed.y����������ȷ���нǵ������ԣ���������ĳ��ĽǶȾ�Ϊ��ֵ��
		if (speed.y <= 0) {
			zAngle = tempZ;
		} else {
			zAngle = -tempZ;
		}
		// �õ��нǸ���Speed.z����������ȷ���нǵ������ԣ���������ĳ��ĽǶȾ�Ϊ��ֵ��
		if (speed.z > 0) {
			yAngle = tempY;
		} else {
			yAngle = -tempY;
		}
		// ��̬���޸�����ٶȣ���̽�Եļ������ٶȣ���������涨�ķ�Χ������ٶȲ�������
		// �������
		if (Math.abs(speed.x + force.x) < Constant.MaxSpeed) 
		{
			speed.x += force.x;
		}
		if (Math.abs(speed.y + force.y) < Constant.MaxSpeed)
		{
			speed.y += force.y;
		}
		if (Math.abs(speed.z + force.z) < Constant.MaxSpeed) 
		{
			speed.z += force.z;
		}

		// ��̬���޸�����ٶ�
		// ���������ʳ֮���������
		if (Math.abs(speed.x + attractforce.x) < Constant.MaxSpeed) 
		{
			speed.x += attractforce.x;
		}
		if (Math.abs(speed.y + attractforce.y) < Constant.MaxSpeed) 
		{
			speed.y += attractforce.y;
		}
		if (Math.abs(speed.z + attractforce.z) < Constant.MaxSpeed) 
		{
			speed.z += attractforce.z;
		}
		// �ı����λ��
		position.plus(speed);
		//��ֹ�㴩������

		// ÿ�μ���ÿ���������֮�󣬰����ܵ�������
		// ����
		this.force.x = 0;
		this.force.y = 0;
		this.force.z = 0;
		// ��ʳ�����������
		attractforce.x = 0;
		attractforce.y = 0;
		attractforce.z = 0;

	}
}
