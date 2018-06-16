package wyf.lxg.fishschool;

import java.util.ArrayList;

import com.bn.ms3d.core.MS3DModel;

import wyf.lxg.Constant.Constant;
import wyf.lxg.Constant.MatrixState;
import wyf.lxg.Constant.Vector3f;
import wyf.lxg.mywallpaper.MySurfaceView;

public class FishSchoolControl {
	public ArrayList<SingleFishSchool> fishSchool = new ArrayList<SingleFishSchool>();//��Ⱥ�б�
	public MySurfaceView Tr;
	FishschoolThread Thread;
    int texid;
    float x;
	public FishSchoolControl(MS3DModel md,int texid, MySurfaceView tr,Vector3f weizhi,Vector3f sudu,float weight) {
		this.Tr = tr;
		this.texid=texid;
		if(sudu.x>0)//���ݵ�һ������ٶȼ��������������ٶ�
		{
			x=sudu.x-0.01f;
		}else
		{
			x=sudu.x+0.01f;
		}
		// �������!//λ��    �ٶ�     ����     ������      ����
		fishSchool.add(new SingleFishSchool(md,this.texid,
				weizhi, sudu,//��һ����
				new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), weight));
		fishSchool.add(new SingleFishSchool(md,this.texid,
				new Vector3f(weizhi.x, weizhi.y, -Constant.Radius), new Vector3f(x,
						0.00f, x), new Vector3f(0, 0, 0), new Vector3f(0,
						0, 0), weight));//�ڶ�����
		fishSchool.add(new SingleFishSchool(md,this.texid,
				new Vector3f(Constant.Radius, weizhi.y, weizhi.z), new Vector3f(x,
						0.00f, x), new Vector3f(0, 0, 0), new Vector3f(0,
						0, 0), weight));//��������
		fishSchool.add(new SingleFishSchool(md,this.texid,
				new Vector3f(weizhi.x, weizhi.y, Constant.Radius), new Vector3f(x,
						0.00f, x), new Vector3f(0, 0, 0), new Vector3f(0,
						0, 0), weight));//��������

		Thread = new FishschoolThread(this);
		Thread.start();//�����߳�
	}

	public void drawSelf() {
		// ��Ļ���
		try {
			for (int i = 0; i < this.fishSchool.size(); i++)
			{
				MatrixState.pushMatrix();//��������
				fishSchool.get(i).drawSelf();//������Ⱥ
				MatrixState.popMatrix();//�ָ�����
			}
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
