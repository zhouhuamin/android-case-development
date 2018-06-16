package wyf.lxg.fishfood;

import wyf.lxg.Constant.Constant;
import wyf.lxg.Constant.Vector3f;
import wyf.lxg.mywallpaper.MySurfaceView;


public class FeedFish {
	MySurfaceView Tr;
	boolean start;

	public FeedFish(MySurfaceView tr) {
		start = true;
		this.Tr = tr;
	}
	public void startFeed(Vector3f Start,Vector3f End) {
		
		
		// ιʳ��λ��
		  Vector3f dv=End.cutPc(Start);
		 //���ݵ���ĸ߶�ΪConstant.Y_HEIGHT���tֵ
		   //float t=(Constant.Y_HEIGHT -Start.y)/dv.y;               
	   	   //����t����������X��Y����ֵ
		  float t=(Constant.Y_HEIGHT -Start.y)/dv.y;
	   	   //����t����������X��Y����ֵ
	   	   float xd=Start.x+t*dv.x;
	   	   float zd=Start.z+t*dv.z;
	  
	   	   //����һ����Χ��ʳ�Ĵ�С���ı䣬����λ�ò��ı䣬ͬʱʳ��Ͳ�������
	   	   if(zd<=Constant.ZTouch_Min ||zd>Constant.ZTouch_Max)
	   	   {
	   		   Constant.isFeed=true;
	   		   return;
	   	   }
	   	   //ʳ���λ��
		Tr.Xposition = xd;
		Tr.Zposition = zd;
		 
		
		// //����ʳ��ı�־λ
		Tr.Fooddraw = true;
		// ͬʱ������Yposition�ı�־λ��Ϊtrue
		Tr.singlefood.Ft.Fresit = true;
		//���������̵߳���ӿ�����ʳ�ı�־λ��ΪTrue
		Tr.singlefood.At.Go = true;
		//��ιʳ�߳�
		Tr.singlefood.Ft.Go = true;
		// ���ô˷�����ʼ�ƶ�ʳ��ķ���
		 
		if (start) {
			
			Tr.singlefood.StartFeed();
			start = false;
		}
	}

}