package wyf.lxg.fishfood;

import java.util.ArrayList;

import wyf.lxg.Constant.Constant;
import wyf.lxg.Constant.Vector3f;
import wyf.lxg.fish.SingleFish;

//��ʱ�˶�����������߳�
public class AttractThread extends Thread {
	// �̵߳ı�־λ
	public boolean Feeding = true;
	// �Ƿ����������ĵ��б��õ��������ĵ�����б�
	public boolean Fforcefish = true;
	public boolean Go = false;
	float Length;
	// TDRender������
	SingleFood Sf;
	// ����������б�
	ArrayList<SingleFish> fl = new ArrayList<SingleFish>();

	public AttractThread(SingleFood sf) {
		this.Sf = sf;
	}
	// �ǵ��ջ���ÿ�ζ���ɿ�
	public void run() {
		while (Feeding) {
			try {
				// ����ܱ�����ʳ��������б�
				if (Go) {
					if (Fforcefish) {
						fl.clear();
						Fforcefish = false;
					}
//						// ע�����
						if (fl != null ) {
							// ����������б����������ķŵ�fl���棬������㿪ʼ�յ���������!
							for (int i = 0; i < Sf.mv.fishAl.size(); i++) {
								if (Sf.mv.fishAl.get(i).position.x > Sf.mv.Xposition
										&& Sf.mv.fishAl.get(i).speed.x < 0) {

									if (!fl.contains(Sf.mv.fishAl.get(i))) {
										fl.add(Sf.mv.fishAl.get(i));
									}
									}
								 else if (Sf.mv.fishAl.get(i).position.x < Sf.mv.Xposition
										&& Sf.mv.fishAl.get(i).speed.x > 0) {
									if (!fl.contains(Sf.mv.fishAl.get(i))) {
										fl.add(Sf.mv.fishAl.get(i));
									}
								}
							}
					}
					// ���ܿ���ʳ��������������
					if (fl.size() != 0) {
						for (int i = 0; i < fl.size(); i++) {
							// �����ջ������м����
							Vector3f VL = null;
							Vector3f Vl2 = null;
							Vl2 = new Vector3f(Sf.mv.Xposition,
									Sf.mv.singlefood.Ypositon, Sf.mv.Zposition);
							VL = Vl2.cutPc(fl.get(i).position);
							// �õ���Position��ConstantPosition�ĵ�������������������С����ֵ��ʱ�����Ϊ��ʳ���Ե������е���֮��ص��̵߳ı�־λ��Ϊfalse����������������б�
							// �ÿ�
							Length = VL.Vectormodule();
							if (Length != 0) {
								VL.ChangeStep(Length);
							}
							//��ʳ���Ե�������ʳ��������ֵ
							if (Length <= Constant.FoodFeedDistance || Sf.Ypositon < Constant.FoodPositionMin_Y) {
								StopAllThread();
							}
							// �ջ����ı���
							VL.getforce(Constant.AttractForceScals);
							// �Ѽ���õ����ջ��������ջ����ConstantForce!

							fl.get(i).attractforce.x = VL.x;
							fl.get(i).attractforce.y = VL.y;
							fl.get(i).attractforce.z = VL.z;
						}
					}
				}
				if (Sf.Ypositon < Constant.FoodPositionMin_Y) {
					StopAllThread();

				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				Thread.sleep(100);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void StopAllThread() {
		// ��ʳ�ﵽ��һ��λ�õ�ʱ�����ʳ�ﱻ��Ե�֮���̵߳�flag����Ϊfalse,�����ܵ����������б��Ϊnull����ж��������ط�ִ�У�����������б�ĸ���sh
		// ʩ������ʱ�򲻶ϵ��жϣ�Ҫ������һ�������Ͱ��ݵ��ջ���������б��ɿա������еı�־λ���䳵false
		// �жϷ�����������һʳ���Y����ĳ��λ�ã��������ʵ��֮��ľ����Сʱ��Ĭ��Ϊʵ�ﱻ�Ե��ˣ������еı�־λ�����false���Ұ�forceCondition.fl�б��óɿ�
		// ע�����!

		// ����SingleY
		Sf.Ypositon = Constant.FoodPositionMax_Y;

		// On_TouchEvent����ı�־λ��Ϊtrue��ʹ�õڶ����ܵ��
		this.Fforcefish = true;
		this.Go = false;
		Sf.Ft.Go = false;
		Constant.isFeed = true;
		// ���Ƶı�־λ!
		Sf.mv.Fooddraw = false;
		// ιʳʱ��־λfalse��ֹͣιʳ��ʱ���ڸ����

	}
}
