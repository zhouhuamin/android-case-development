package wyf.lxg.fishschool;

import wyf.lxg.Constant.Constant;
import wyf.lxg.Constant.Vector3f;
import wyf.lxg.fishschool.FishSchoolControl;

//��ʱ�˶�����������߳�
public class FishschoolThread extends Thread {
	boolean flag = true;
	FishSchoolControl fishschools;
	float Length;

	public FishschoolThread(FishSchoolControl fishschools) {
		this.fishschools = fishschools;
	}
	public void run() {
		while (flag) {// ѭ����ʱ�ƶ�����
			try {
				// ��������е��������������Ⱥ����������Ĵ�С
				outside: for (int i = 1; i < fishschools.fishSchool.size(); i++) {
					// ͨ��һ��ѭ�����������Ⱥ�����ĳһ��������������������
					for (int j = 0; j < fishschools.Tr.fishControl.fishAl.size(); j++) {
						//�����Ⱥ���������Ϊ�����뿪��Ⱥһ�ξ���֮��Ͳ��ܵ��������������(��Ϊ����Ⱥ��������
						//������ľ���<4ʱ����ܵ����������ô�ʱ��Ⱥ������㲻���ܵ����������ã����ǵ���Ⱥ��������뿪һ��λ�ã�
						//����ConstantPosition>4ʱ��Ⱥ�������Ͳ����ܵ�����������������ֻ�ܵ��������ã������������������ĵ���Ч��)
						if (Length > Constant.SMinDistaces-0.5) {
							continue outside;
						}
						Vector3f V3 = null;
						// ��ǰ����һ����������������õ����ĸı䷽�������Ĵ�С�;���ɷ���.
						V3 = fishschools.fishSchool.get(i).position.cut(
								fishschools.Tr.fishControl.fishAl.get(j).position,
								Constant.SMinDistaces);
						// �������űȣ���ͬ�ڵ����������
						V3.getforce(Constant.WeightScals);
						// ������֮�����
						fishschools.fishSchool.get(i).force.plus(V3);
					}
				}
				Vector3f Vwall = null;
				// ��������ĵ�ǰλ��
				float Cx = fishschools.fishSchool.get(0).position.x;
				float Cy = fishschools.fishSchool.get(0).position.y;
				float Cz = fishschools.fishSchool.get(0).position.z;
				//��Ⱥ����3���ܶ�����
				int j=1;
				for(int i=-90;i<=90.;i=i+90)
				{
						fishschools.fishSchool.get(j).ConstantPosition.x = (float) (Cx+Constant.Radius*Math.cos(i));
						fishschools.fishSchool.get(j).ConstantPosition.y = Cy;
						fishschools.fishSchool.get(j).ConstantPosition.z = (float) (Cz+Constant.Radius*Math.sin(i));
						 j++;
				}
				// ÿ�����ܵ��ӵ�ǰλ��ָ��Ӧ������λ��(Home)����(�������ã�,
				for (int i = 1; i < fishschools.fishSchool.size(); i++)
				{
					// ����������м����
					Vector3f VL = null;
					VL = fishschools.fishSchool.get(i).ConstantPosition
							.cutGetforce(fishschools.fishSchool.get(i).position);
					//�õ���Position��ConstantPosition�ĵ���������
					Length = VL.Vectormodule(); 
					if ((Length) >= Constant.SMinDistaces)
					{
						// ���������������ConstantPositon̫Զ����ô���������Ϊԭ����3��
						VL.getforce(Constant.ConstantForceScals / 8f);
					}
					else if (Length<= 0.3)// ����С��ûĳһ��ֵʱ�������������ã��Ӷ������Ķ�������
					{
						VL.x = VL.y = VL.z = 0;
					} 
					else
					{
						VL.getforce(Constant.ConstantForceScals);
					}
					float MediaLength = fishschools.fishSchool.get(i).force
							.Vectormodule();
					// �������������Ѻ������Ϊ0��ֻ�����������ã�ֻ����������ʧ������º����ֲ����ˣ��Ӷ����������ConstantPosition��λ��
					if (Math.abs(MediaLength) == 0) {
						// �Ѽ���õ��ĺ�����������ConstantForce.
						fishschools.fishSchool.get(i).ConstantForce.x = VL.x;
						fishschools.fishSchool.get(i).ConstantForce.y = VL.y;
						fishschools.fishSchool.get(i).ConstantForce.z = VL.z;
					} else {
						// �Ѽ���õ��ĺ�����������ConstantForce.
						fishschools.fishSchool.get(i).ConstantForce.x = 0;
						fishschools.fishSchool.get(i).ConstantForce.y = 0;
						fishschools.fishSchool.get(i).ConstantForce.z = 0;
					}
				}
				// �ж����ǽ�ڵ���ײ����������ˣ�����force��X�᷽���ϲ���һ�������෴����
				Vwall = new Vector3f(0, 0, 0);
				// ���ڼ��ʱ�����ӽ�����ͽ�ƽ��ʱ��Ҫ��д���������Χ���������ض������Ӿ�Ч�������Һ�������Դ�д�����Ϊ�γ������˵��ӽǡ�
				//������������һ�������Ǿ�����Ҫ��ȫ��ͬ��ֹ����ĳһ�����ϵ��ٶȱ�Ϊ�㣨����ת�ϣ��Ѿ��������Ч�������˴���
				if (fishschools.fishSchool.get(0).position.x <= -8.5f) {
					Vwall.x = 0.0013215f;
				}
				if (fishschools.fishSchool.get(0).position.x >4.5f) {
					Vwall.x = -0.0013212f;
				}
				if (fishschools.fishSchool.get(0).position.y >= 7) {
					Vwall.y = -0.0013213f;
				}
				if (fishschools.fishSchool.get(0).position.y <= -5f) {
					Vwall.y = 0.002214f;
				}
				if (fishschools.fishSchool.get(0).position.z < -15) {
					Vwall.z = 0.0014214f;
				}
				if (fishschools.fishSchool.get(0).position.z > 3) {
					Vwall.z = -0.002213f;
				}
				fishschools.fishSchool.get(0).force.plus(Vwall);
				// }

				// ��ʱ�޸�����ٶȺ�λ-*-��.'/
				for (int i = 0; i < fishschools.fishSchool.size(); i++) {
					fishschools.fishSchool.get(i).fishschoolMove();
				}

				// ���ڼ�⣡ֻ���м�����������������ķ�Χ��һ�㣬���м��������ʱ���м���λ�ƺ��ٶȻظı䣬ͬʱ������ĺ���Ҳ��ı䣬�����ﵽͬʱ�����Ч��
				// ���Ҿ����Ӱ��Ч����һЩ

			} catch (Exception e) {
				e.printStackTrace();
			}

			try {
				Thread.sleep(50);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
