package wyf.lxg.bubble;

import java.util.ArrayList;
import java.util.Collections;

import wyf.lxg.Constant.Constant;
import wyf.lxg.Constant.MatrixState;
import wyf.lxg.mywallpaper.MySurfaceView;
public class BubbleControl {
	// ��������б�
	public ArrayList<SingleBubble> BubbleSingle = new ArrayList<SingleBubble>();
	// ���ݵ�����ID
	int texld;
    MySurfaceView mv;
	public BubbleControl(MySurfaceView mv,int texld ) {
		this.mv=mv;
		// �õ�ID
		this.texld = texld;

		// ��������
		for (int i = 0; i <Constant.BUBBLE_NUM; i++) {
			BubbleSingle.add(new SingleBubble(mv,this.texld));//������ݶ���
		}
		// ���������������ƶ��߳�
		BubbleThread Bgt = new BubbleThread(this);
		Bgt.start();
	}

	public void drawSelf() {
		try {
			Collections.sort(this.BubbleSingle);
			// ��������
			for (int i = 0; i < this.BubbleSingle.size(); i++) {
				MatrixState.pushMatrix();//��������
				BubbleSingle.get(i).drawSelf();//��������
				MatrixState.popMatrix();//�ָ�����
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
