package wyf.lxg.bubble;

//��ʱ�˶�����������߳�
public class BubbleThread extends Thread {
	float x;
	float y;
//	float num;
	boolean flag = true;
	BubbleControl Bcl;//���ݵĿ�����
	public BubbleThread(BubbleControl Bcl) {
		this.Bcl=Bcl;
}
	public void run()
	{
		// ѭ����ʱ�ƶ�����
		while (flag) {
			try {
				for(int i=0;i<Bcl.BubbleSingle.size();i++)
				{
					if((i+3)%3==0)//�����ݵ����������з�Ϊ3��						
					{
						if(((i+3)/3)%2==0)//���зֳ�����һ�����ݶ��У�������ż�жϣ�Ϊ���ݵ�x��z�᷽��ƫ����׼��
						{
							y=1;//ż��λ���ݱ�־λΪ1
						}else{
							y=-1;//����λ���ݱ�־λ-1
						}
						x=1;//��һ������λ�ö��б�־λ
					}else if((i+3)%3==1){//�����ݵ����������з�Ϊ3��	
						if(((i+3)/3)%2==0)//���зֳ�����һ�����ݶ��У�������ż�жϣ�Ϊ���ݵ�x��z�᷽��ƫ����׼��
						{
							y=1;//ż��λ���ݱ�־λΪ1
						}else{
							y=-1;//����λ���ݱ�־λ-1
						}
						x=-1;//�ڶ�������λ�ö��б�־λ
					}else if((i+3)%3==2)//�����ݵ����������з�Ϊ3��	
					{
						if(((i+3)/3)%2==0)//���зֳ�����һ�����ݶ��У�������ż�жϣ�Ϊ���ݵ�x��z�᷽��ƫ����׼��
						{
							y=1;//ż��λ���ݱ�־λΪ1
						}else{
							y=-1;//����λ���ݱ�־λ-1
						}
						x=0;//�ڶ�������λ�ö��б�־λ
					}
					Bcl.BubbleSingle.get(i).bubbleMove(x,y);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				Thread.sleep(10);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
