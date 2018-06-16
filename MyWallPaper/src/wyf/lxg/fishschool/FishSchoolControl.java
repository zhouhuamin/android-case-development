package wyf.lxg.fishschool;

import java.util.ArrayList;

import com.bn.ms3d.core.MS3DModel;

import wyf.lxg.Constant.Constant;
import wyf.lxg.Constant.MatrixState;
import wyf.lxg.Constant.Vector3f;
import wyf.lxg.mywallpaper.MySurfaceView;

public class FishSchoolControl {
	public ArrayList<SingleFishSchool> fishSchool = new ArrayList<SingleFishSchool>();//鱼群列表
	public MySurfaceView Tr;
	FishschoolThread Thread;
    int texid;
    float x;
	public FishSchoolControl(MS3DModel md,int texid, MySurfaceView tr,Vector3f weizhi,Vector3f sudu,float weight) {
		this.Tr = tr;
		this.texid=texid;
		if(sudu.x>0)//根据第一条鱼的速度计算后面三条鱼的速度
		{
			x=sudu.x-0.01f;
		}else
		{
			x=sudu.x+0.01f;
		}
		// 添加鱼类!//位置    速度     方向     吸引力      重力
		fishSchool.add(new SingleFishSchool(md,this.texid,
				weizhi, sudu,//第一条鱼
				new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), weight));
		fishSchool.add(new SingleFishSchool(md,this.texid,
				new Vector3f(weizhi.x, weizhi.y, -Constant.Radius), new Vector3f(x,
						0.00f, x), new Vector3f(0, 0, 0), new Vector3f(0,
						0, 0), weight));//第二条鱼
		fishSchool.add(new SingleFishSchool(md,this.texid,
				new Vector3f(Constant.Radius, weizhi.y, weizhi.z), new Vector3f(x,
						0.00f, x), new Vector3f(0, 0, 0), new Vector3f(0,
						0, 0), weight));//第三条鱼
		fishSchool.add(new SingleFishSchool(md,this.texid,
				new Vector3f(weizhi.x, weizhi.y, Constant.Radius), new Vector3f(x,
						0.00f, x), new Vector3f(0, 0, 0), new Vector3f(0,
						0, 0), weight));//第四条鱼

		Thread = new FishschoolThread(this);
		Thread.start();//启动线程
	}

	public void drawSelf() {
		// 鱼的绘制
		try {
			for (int i = 0; i < this.fishSchool.size(); i++)
			{
				MatrixState.pushMatrix();//保护矩阵
				fishSchool.get(i).drawSelf();//绘制鱼群
				MatrixState.popMatrix();//恢复矩阵
			}
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
