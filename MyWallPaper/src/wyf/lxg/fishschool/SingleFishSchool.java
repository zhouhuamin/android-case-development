package wyf.lxg.fishschool;

import com.bn.ms3d.core.MS3DModel;
import wyf.lxg.Constant.Constant;
import wyf.lxg.Constant.MatrixState;
import wyf.lxg.Constant.Vector3f;

public class SingleFishSchool {
	// ��ʼ����
		public Vector3f position;
		public Vector3f speed;
		public Vector3f force;
		// ÿ������һ���Լ���λ�ã������Լ����õ�λ��һ��ָ��÷�����������ã������뿪��λ��ʱ�����ͻ����
		public Vector3f ConstantPosition = new Vector3f(0, 0, 0);
		// ���ܵ���ָ��̶���λ�Ĵ�С�㶨����
		public Vector3f ConstantForce;
		// ����������㱾���ܵ���������������ɷ���
		public float weight;
		// ����������ת���Ƕ�Z���Y��!
		float yAngle;
		float zAngle;
		float tempY;
		float tempZ;
		MS3DModel mt;
		float time;
		int texid;
		// ��ʼ���������
		//LoadedObjectVertexNormalTexture fishSchool;
		// ������
		public SingleFishSchool(MS3DModel md,int texid,
				Vector3f Position, Vector3f Speed, Vector3f force,
				Vector3f ConstantForce, float weight) {
			this.texid=texid;
			this.position = Position;
			this.speed = Speed;
			this.force = force;
			this.weight = weight;
			this.mt=md;
			this.ConstantPosition.x = Position.x;
			this.ConstantPosition.y = Position.y;
			this.ConstantPosition.z = Position.z;
			this.ConstantForce = ConstantForce;
		}
		// drawSelf������
		public void drawSelf() {
			MatrixState.pushMatrix();//��������
			MatrixState.translate(this.position.x, this.position.y, this.position.z);//ƽ��
			MatrixState.rotate(yAngle, 0, 1, 0);//��y����תһ���Ƕ�
	 		MatrixState.rotate(zAngle, 0, 0, 1);//��z����תһ���Ƕ�
			//����Ⱥ
			if (mt != null) {
				this.mt.animate(time,texid); 
			}
			MatrixState.popMatrix();//�ָ�����
			//����ģ�Ͷ���ʱ��
	        time += 0.050f;
	        //����ǰ����ʱ������ܵĶ���ʱ����ʵ�ʲ���ʱ����ڵ�ǰ����ʱ���ȥ�ܵĶ���ʱ��
	        if(time > this.mt.getTotalTime()) 
	        {
	        	time = time - this.mt.getTotalTime();
	        }
		}
		// ������ζ��ķ��������ݵ�ǰ������λ���Լ��ٶ������������һ��λ��!
		public void fishschoolMove() {
			/**
			 * һ��ע���ж�x��z�ٶ�ͬʱΪ0 �����
			 */
			// �ж��ٶȷ�ֹ������ĸΪ����������
			if (speed.x == 0 && speed.z == 0 && speed.y > 0) {
				tempZ = -90;
				tempY = 0;

			} else if (speed.x == 0 && speed.z == 0 && speed.y < 0) {
				tempZ = 90;
				tempY = 0;
			} else if (speed.x == 0 && speed.z == 0 && speed.y == 0) {
				tempZ = 90;
				tempY = 0;
			} else {
				float fz = (speed.x * speed.x + speed.y * 0 + speed.z * speed.z);
				// ��ĸ
				float fm = (float) (Math.sqrt(speed.x * speed.x + speed.y * speed.y
						+ speed.z * speed.z) * Math.sqrt(speed.x * speed.x
						+ speed.z * speed.z));
				// cosֵ
				float zhi = fz / fm;
				// �����Ǻ���
				tempZ = (float) (180f / Math.PI) * (float) Math.acos(zhi);
				fz = (speed.x * Constant.initializeschool.x + speed.z
						* Constant.initializeschool.z);
				// ��ĸ
				fm = (float) (Math.sqrt(Constant.initializeschool.x
						* Constant.initializeschool.x + Constant.initializeschool.z
						* Constant.initializeschool.z) * Math.sqrt(speed.x * speed.x
						+ speed.z * speed.z));
				// cosֵ
				zhi = fz / fm;
				// �����Ǻ���
				tempY = (float) (180f / Math.PI) * (float) Math.acos(zhi);
			}
			// �õ��нǸ���speed.y����������ȷ���нǵ������ԣ���������ĳ��ĽǶȾ�Ϊ��ֵ��
			if (speed.y <= 0) 
			{
				zAngle = tempZ;
			} else
			{
				zAngle = -tempZ;
			}
			// �õ��нǸ���speed.z����������ȷ���нǵ������ԣ���������ĳ��ĽǶȾ�Ϊ��ֵ��
			if (speed.z > 0)
			{
				yAngle = tempY;
			} 
			else 
			{
				yAngle = -tempY;
			}
			// ��̬���޸�����ٶȣ���̽�Եļ������ٶȣ���������涨�ķ�Χ������ٶȲ�������
			if (Math.abs(speed.x + force.x) <Constant.SchoolMaxSpeed) 
			{
				speed.x += force.x;
			}
			if (Math.abs(speed.y + force.y) <Constant.SchoolMaxSpeed)
			{
				speed.y += force.y;
			}
			if (Math.abs(speed.z + force.z) < Constant.SchoolMaxSpeed)
			{
				speed.z += force.z;
			}
			// ����������
			if (Math.abs(speed.x + ConstantForce.x) < Constant.SchoolMaxSpeed)
			{
				speed.x += ConstantForce.x;
			}
			if (Math.abs(speed.y + ConstantForce.y) < Constant.SchoolMaxSpeed)
			{
				speed.y += ConstantForce.y;
			}
			if (Math.abs(speed.z + ConstantForce.z) < Constant.SchoolMaxSpeed) 
			{
				speed.z += ConstantForce.z;
			}
			/**
			 * ��̬���޸����λ��
			 */
			position.plus(speed);
			// ÿ�μ���ÿ���������֮ǰ�������ܵ�������
			this.force.x = 0;
			this.force.y = 0;
			this.force.z = 0;
		}
}
