package wyf.lxg.mywallpaper;

import wyf.lxg.Constant.Constant;
import wyf.lxg.Constant.MatrixState;
import wyf.lxg.Constant.Vector3f;
import wyf.lxg.fishfood.IntersectantUtil;
import android.annotation.SuppressLint;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;
import android.os.Build;
import android.service.wallpaper.WallpaperService;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

public abstract class GLWallpaperService extends WallpaperService {

	public class GLEngine extends Engine {
		class WallpaperGLSurfaceView extends GLSurfaceView {
			//private static final String TAG = "WallpaperGLSurfaceView";

			WallpaperGLSurfaceView(Context context) {
				super(context);//��ȡ������
			}

			@Override
			public SurfaceHolder getHolder() {
				return getSurfaceHolder();
			}

			public void onDestroy() {

				super.onDetachedFromWindow();
			}
		}

		//private static final String TAG = "GLEngine";

		private WallpaperGLSurfaceView glSurfaceView;
		private boolean rendererHasBeenSet;		

		@Override
		public void onCreate(SurfaceHolder surfaceHolder) {

			super.onCreate(surfaceHolder);

			glSurfaceView = new WallpaperGLSurfaceView(GLWallpaperService.this);
		}

		@Override
		public void onVisibilityChanged(boolean visible) {

			super.onVisibilityChanged(visible);

			if (rendererHasBeenSet) {
				if (visible) {
					glSurfaceView.onResume();
				} else {					
					glSurfaceView.onPause();														
				}
			}
		}		

		@Override
		public void onDestroy() {

			super.onDestroy();
			glSurfaceView.onDestroy();
		}
		
		protected void setRenderer(Renderer renderer) {
			glSurfaceView.setRenderer(renderer);
			rendererHasBeenSet = true;
			
//			//---------��ӵ�---------
//			this.renderer=(MySurfaceView) renderer;
		}
		
		private float mPreviousY;//�ϴεĴ���λ��Y����
		private float mPreviousX;//�ϴεĴ���λ��Y����
		 @Override 
		 public void onTouchEvent(MotionEvent e) 
	    {
		    //mv=new MySurfaceView(null);
	        float y = e.getY();
	        float x = e.getX();
	        switch (e.getAction()) {
	        case MotionEvent.ACTION_DOWN:
	        	  Constant.feeding=true;
				break;
	        case MotionEvent.ACTION_MOVE:
				float dy = y - mPreviousY;// ���㴥�ر�Yλ��
				float dx = x - mPreviousX;// ���㴥�ر�Yλ��
				if (dx < 0)// �����xΪ�������ұ�xΪ��
				{
					if (Constant.CameraX <Constant.MaxCameraMove) {
						if(dx<-Constant.Thold)
						{
							Constant.feeding = false;
						}
						// �ƶ������������
						Constant.CameraX = Constant.CameraX - dx / Constant.CameraMove_SCALE ;
						Constant.TargetX=Constant.CameraX;
					}
				} else {

					if (Constant.CameraX >-Constant.MaxCameraMove) {
						if(dx>Constant.Thold)
						{
						
							Constant.feeding =false;
						}
						// �ƶ������������
						Constant.CameraX = Constant.CameraX - dx / Constant.CameraMove_SCALE ;
						Constant.TargetX=Constant.CameraX;
					}
				}
				// ���������λ����Ϣ���뵽������
				MatrixState.setCamera(
						Constant.CameraX, // ����λ�õ�X
						Constant.CameraY, // ����λ�õ�Y
						Constant.CameraZ, // ����λ�� ��Z
						Constant.TargetX, // �����򿴵ĵ�X
						Constant.TargetY, // �����򿴵ĵ�Y
						Constant.TargetZ, // �����򿴵ĵ�Z
						Constant.UpX, 
						Constant.UpY, 
						Constant.UpZ);
				break;
			case MotionEvent.ACTION_UP:
              if (Constant.feeding) {				
					if (Constant.isFeed) {
						// �ѱ�־λ��Ϊfalseֻ��ʳ��û�е�ʱ��Ű�isFeed�û�true���������ڶ�2�µڶ���û��Ӧ
						Constant.isFeed = false;		
						// ͨ������任�õ���������ϵ�еĵ�
						float[] AB = IntersectantUtil.calculateABPosition(
								x, // ���ص�X����
								y, // ���ص�Y����
								Constant.SCREEN_WIDTH, // ��Ļ���
								Constant.SCREEN_HEGHT, // ��Ļ����
								Constant.leftABS, // �ӽ�left��top����ֵ
								Constant.topABS, 
								Constant.nearABS, // �ӽ�near��farֵ
								Constant.farABS);
								// Fposition(��������ϵ)
								Vector3f Start = new Vector3f(AB[0], AB[1], AB[2]);//���
								Vector3f End = new Vector3f(AB[3], AB[4], AB[5]);//�յ�
								// �жϲ�λ������
						if (MySurfaceView.feedfish != null) {
							
						    MySurfaceView.feedfish.startFeed(Start, End);//��ʼιʳ
						}
					}
				}
				break;
				}
	        mPreviousY = y;//��¼���ر�λ��
	        mPreviousX = x;//��¼���ر�λ��
	        super.onTouchEvent(e);
	    }
		@SuppressLint("NewApi")
		protected void setPreserveEGLContextOnPause(boolean preserve) {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
				glSurfaceView.setPreserveEGLContextOnPause(preserve);
			}
		}		

		protected void setEGLContextClientVersion(int version) {
			glSurfaceView.setEGLContextClientVersion(version);
		}
	
	}
}
