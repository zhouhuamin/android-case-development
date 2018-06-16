package wyf.lxg.fish;

import java.util.ArrayList;

import wyf.lxg.Constant.MatrixState;
import wyf.lxg.mywallpaper.MySurfaceView;

public class FishControl {
	 //��Ⱥ�б�
	public ArrayList<SingleFish> fishAl;
	//��Go�߳�
	FishGoThread  fgt;
	int id;
	//��Ⱦ��
	public MySurfaceView My;
	//������
	public FishControl(ArrayList<SingleFish> fishAl,MySurfaceView my)
	{
		this.fishAl = fishAl;
		this.My=my;
		//��������ƶ��߳�
		fgt= new FishGoThread(this);
	    fgt.start();
	}
	public void drawSelf()
  {
		try {
			//ѭ������ÿһ����
			 for(int i=0;i<this.fishAl.size();i++)
			 {
				 MatrixState.pushMatrix();//��������
				 fishAl.get(i).drawSelf();//������
				 MatrixState.popMatrix();//�ָ�����
			  }
		 } 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}