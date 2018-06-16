package wyf.lxg.fishfood;

import wyf.lxg.Constant.Constant;
import wyf.lxg.Constant.MatrixState;
import wyf.lxg.load.LoadedObjectVertexNormalTexture;
import wyf.lxg.mywallpaper.MySurfaceView;

public class SingleFood {
	public FoodThread Ft;
	public AttractThread At;
	public MySurfaceView mv;
	// ��Y��Z���嵽����������
	public float Ypositon =Constant.FoodPositionMax_Y;
	LoadedObjectVertexNormalTexture fishFoods;
	int texld;
	// ������ʳ�Ķ���
	public SingleFood(int texld,LoadedObjectVertexNormalTexture fishfoods, MySurfaceView mv) {
		this.texld=texld;
		this.mv = mv;
		//start=true;
		// ʳ��
		fishFoods = fishfoods;
		// ��̬�ı�Y��λ�ú�X��λ��X��λ����Ҫ����ʳ�ﶶ��Tread������һ����׼λ�����ڱ�׼�Ƿ�ʼ�̵߳ı�־�����α�־λλtrue
	    Ft = new FoodThread(this);
	    At = new AttractThread(this);  
	   
	}
	public void StartFeed()   
	{
		Ft.start();//������ʳ�ƶ��߳�
		At.start();//�����������߳�
	}
	// �ڻ��Ƶı�־λλtrue��ʱ��ʼ����
	public void drawSelf()  
	{
		MatrixState.pushMatrix();//��������
		MatrixState.translate(mv.Xposition,this.Ypositon,mv.Zposition);//ƽ��
		fishFoods.drawSelf(texld);//������ʳ
		MatrixState.popMatrix();//�ָ�����
	}

}