package wyf.lxg.fish;

import wyf.lxg.Constant.Constant;
import wyf.lxg.Constant.Vector3f;
import wyf.lxg.fish.FishControl;


//��ʱ�˶�����������߳�
public class FishGoThread extends Thread {
	boolean flag = true;
	FishControl fishControl;

	public FishGoThread(FishControl fishGroupforcontrol) {
		this.fishControl = fishGroupforcontrol;
	}

	public void run() {
		while (flag) {// ѭ����ʱ�ƶ�����
			try {
				// ��̬���޸����ܵ������Ĵ�С
				for (int i = 0; i < fishControl.fishAl.size(); i++) {
					// ������Ⱥ�Ը�����������Ĵ�С
					Vector3f Vwall = null;
					// һ��forѭ��֮���i�������ܵ��ĵ�����ĺ����������
					inside: for (int j = 0; j < fishControl.fishAl.size(); j++) {
						Vector3f V3 = null;
						if (i == j) {
							continue inside;
						}
						// ��ǰ����һ����������������õ����ĸı䷽�������Ĵ�С�;���ɷ���.

						V3 = fishControl.fishAl.get(i).position.cut(
								fishControl.fishAl.get(j).position,
								Constant.MinDistances);
						V3.getforce(fishControl.fishAl.get(i).weight);
						// ������֮�����
						fishControl.fishAl.get(i).force.plus(V3);
					}
					// ������Ⱥ��ÿ��������(��i����)��������!
					// ��ǰ�������������������õ����ĸı䷽�������Ĵ�С�;���ɷ���
					if (fishControl.My.fishSchool != null
							&& fishControl.My.fishSchool.fishSchool.size() != 0) {
						Vector3f V4 = fishControl.fishAl.get(i).position
								.cut(fishControl.My.fishSchool.fishSchool
										.get(0).position, Constant.MinDistances);
						V4.getforce(fishControl.fishAl.get(i).weight);
						// ������֮�����
						fishControl.fishAl.get(i).force.plus(V4);
					}
					// �ж����ǽ�ڵ���ײ����������ˣ�����force��X�᷽���ϲ���һ�������෴����
					Vwall = new Vector3f(0, 0, 0);
					// ���ڼ��ʱ�����ӽ�����ͽ�ƽ��ʱ��Ҫ��д���������Χ���������ض������Ӿ�Ч�������Һ�������Դ�д�����Ϊ�γ������˵��ӽǡ�
					if (fishControl.fishAl.get(i).position.x <= -8.5f) {
						Vwall.x = 0.0013215f;
					}
					if (fishControl.fishAl.get(i).position.x > 4.5f) {
						Vwall.x = -0.0013212f;
					}
					if (fishControl.fishAl.get(i).position.y >= 4f) {
						Vwall.y = -0.0013213f;
					}
					if (fishControl.fishAl.get(i).position.y <= -3) {
						Vwall.y = 0.002214f;
					if(fishControl.fishAl.get(i).position.y <= -4)
						{
							Vwall.y =0.006428f;
						}
					}
					if (fishControl.fishAl.get(i).position.z < -20f) {
						Vwall.z = 0.0014214f;
					}
					if (fishControl.fishAl.get(i).position.z > 2) {
						Vwall.z = -0.002213f;
					}
					Vwall.y -= 0.000009;
					fishControl.fishAl.get(i).force.plus(Vwall);
				}
				// ��ʱ�޸�����ٶȺ�λ-*-��.'/
				for (int i = 0; i < fishControl.fishAl.size(); i++) {
					fishControl.fishAl.get(i).fishMove();
				}
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
