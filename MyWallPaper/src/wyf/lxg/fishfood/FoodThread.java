package wyf.lxg.fishfood;

import wyf.lxg.Constant.Constant;

//��ʱ�˶�ʵ����߳�
public class FoodThread extends Thread {
	//�̵߳ı�־λ
	public  boolean flag1 = true;
    //SingleFood��Y��Z�Ƿ����õı�־λλ�����ñ�־λ
	public boolean Fresit=true;
	//�ƶ�x����ı�־λ
	boolean FxMove=true;
	//�߳�������㷨�Ƿ��ߵı�־λ
	public boolean Go=false;
	public  SingleFood SingleF;
	public FoodThread(SingleFood singleF)
	{
		this.SingleF=singleF;
	}
	public void run()
	{
		while (flag1) {
			try 
			{
				//�����־λΪtrue
				if(Go)
				{				
					//�޸�SingleFood��xyzλ�ò���ʵ��ζ���Ч��
					if(FxMove)
					{
						SingleF.mv.Xposition+=Constant.FoodMove_X;
						FxMove=!FxMove;
					}
					else
					{
						SingleF.mv.Xposition-=Constant.FoodMove_X;									
						FxMove=!FxMove;
					}
					//��ʱ���޸�Y���꣡ʳ�����������ٶȵ�
					SingleF.Ypositon-=Constant.FoodSpeed;
				}
			}

			catch (Exception e) {
				e.printStackTrace();
			}
			try {
				Thread.sleep(100);
			} catch (Exception e) {
				e.printStackTrace();
			
			}
		}
	}
}
