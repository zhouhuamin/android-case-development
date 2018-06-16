package wyf.lxg.mywallpaper;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.opengl.GLES20;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import com.bn.ms3d.core.MS3DModel;
import com.bn.ms3d.core.MS3DModel1;
import com.bn.ms3d.texutil.TextureManager;
import wyf.lxg.Constant.Constant;
import wyf.lxg.Constant.MatrixState;
import wyf.lxg.Constant.Vector3f;
import wyf.lxg.background.background;
import wyf.lxg.bubble.BubbleControl;
import wyf.lxg.fish.FishControl;
import wyf.lxg.fish.SingleFish;
import wyf.lxg.fishfood.FeedFish;
import wyf.lxg.fishfood.SingleFood;
import wyf.lxg.fishschool.FishSchoolControl;
import wyf.lxg.load.LoadUtil;
import wyf.lxg.load.LoadedObjectVertexNormalTexture;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;


public class MySurfaceView  extends GLSurfaceView 
implements GLSurfaceView.Renderer,OpenGLES2WallpaperService.Renderer
 {   
	public MySurfaceView(Context context) {
		super(context);
		 this.setEGLContextClientVersion(2); //����ʹ��OPENGL ES2.0
	}
		 public SingleFood singlefood;//��ʳ
		 public boolean Fooddraw = false;//�Ƿ�ι��
	    static FeedFish feedfish;//ι��
	    boolean threadFlag=true;
	    public float Zposition=0;//��ʳ��Zλ��
	    public float Xposition=0;//��ʳ��Xλ��
	    public FishControl fishControl;//Ⱥ�������
	    public ArrayList<SingleFish> fishAl=new ArrayList<SingleFish>();//Ⱥ���б�
		// ��Ⱥ
		public FishSchoolControl fishSchool;//С������Ⱥ
		public FishSchoolControl fishSchool1;//С������Ⱥ
		public FishSchoolControl fishSchool2;//С������Ⱥ
		public FishSchoolControl fishSchool3;//С������Ⱥ
	    int back;//����ͼ����id
	   	int bubbles;//��������ID
	   	int fishfood;//��ʳ����id
	   	int dpm;//����Ч��ͼ����ID
		TextureManager manager;	//���������
		MS3DModel ms3d;//����
		MS3DModel ms3d1;//������
     	MS3DModel ms3d2;//������
     	MS3DModel ms3d3;//С�Ƴ���
     	MS3DModel ms3d4;//�ڹ�
     	MS3DModel ms3d5;//С�ڳ���
     	MS3DModel ms3d6;//С����
     	MS3DModel1 haibei;//���鱴
    
    	background bg;//�������
    	LoadedObjectVertexNormalTexture fishfoods;
		BubbleControl bubble;
		BubbleControl bubble1;
		float time = 0;	
		public void onDrawFrame(GL10 gl) 
        { 
        	//�����Ȼ�������ɫ����
            GLES20.glClear( GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT); 
            MatrixState.pushMatrix();
            if(bg!=null)
            {
            	bg.drawSelf(back); //���Ʊ���ͼ
            }
            if(singlefood!=null)
            {
            	singlefood.drawSelf();//������ʳ
            	
            }
    		if (fishControl != null) {
    			fishControl.drawSelf();//����Ⱥ��
    		}
    		if (fishSchool != null) {
    			fishSchool.drawSelf();//С�Ƴ���Ⱥ
    		}
    		if (fishSchool1 != null) {
    			fishSchool1.drawSelf();//С�ڳ���Ⱥ
    		}
    		if (fishSchool2 != null) {
    			fishSchool2.drawSelf();//С����Ⱥ
    		}
    		if (fishSchool3 != null) {
    			fishSchool3.drawSelf();//С����Ⱥ
    		}
    		MatrixState.pushMatrix();//��������
    		MatrixState.translate(-5f,-16,-26.2f);//ƽ��
      		this.haibei.animate(time,dpm); //�������鱴
      		MatrixState.popMatrix();//�ָ�����
      		  time += 0.015f;
              //����ǰ����ʱ������ܵĶ���ʱ����ʵ�ʲ���ʱ����ڵ�ǰ����ʱ���ȥ�ܵĶ���ʱ��
              if(time > this.ms3d.getTotalTime()) {
              	time = time - this.ms3d.getTotalTime();
              }
    		MatrixState.popMatrix();//�ָ�����
			 GLES20.glEnable(GLES20.GL_BLEND);  
            //���û������c
            GLES20.glBlendFunc(GLES20.GL_SRC_COLOR, GLES20.GL_ONE_MINUS_SRC_COLOR);
            //�����ֳ�
            MatrixState.pushMatrix();//��������
            if(bubble!=null)
            {
            	bubble.drawSelf();//��������
            }
            MatrixState.popMatrix();//�ָ�����
            GLES20.glDisable(GLES20.GL_BLEND);//�رջ��
            
        }  
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            //�����Ӵ���С��λ�� 
        	GLES20.glViewport(0, 0, width, height); 
        	//����GLSurfaceView�Ŀ�߱�
    		float ratio = (float) width / height;
    		Constant.SCREEN_HEGHT=height;//��ȡ��Ļ�߶�
    		Constant.SCREEN_WIDTH=width;//��ȡ��Ļ���
    		Constant.leftABS=ratio*Constant.View_SCALE;
    		Constant.topABS=1 * Constant.View_SCALE;
    		Constant.SCREEN_SCALEX=Constant.View_SCALE*((ratio>1)?ratio:(1/ratio));
    		// ���ô˷����������͸��ͶӰ����
    		 MatrixState.setProjectFrustum(-Constant.leftABS, Constant.leftABS, -Constant.topABS, 
    				Constant.topABS, Constant.nearABS,Constant.farABS);
            //���ô˷������������9����λ�þ���
            MatrixState.setCamera(
            		Constant.CameraX, // ����λ�õ�X
    				Constant.CameraY, // ����λ�õ�Y
    				Constant.CameraZ, // ����λ�� ��Z
    				Constant.TargetX, // �����򿴵ĵ�X
    				Constant.TargetY, // �����򿴵ĵ�Y
    				Constant.TargetZ, // �����򿴵ĵ�Z
    				Constant.UpX, //Up����
    				Constant.UpY, 
    				Constant.UpZ);     
        }
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            //������Ļ����ɫRGBA
            GLES20.glClearColor(0.5f,0.5f,0.5f, 1.0f);    
            MatrixState.setInitStack();//��ʼ������
            //��ʼ����Դλ��
            MatrixState.setLightLocation(0,9,13);
            
          //�����������������
            manager = new TextureManager(getResources());
            //dpm=initTexture(R.drawable.dpm);
            dpm=initTexture(MySurfaceView.this.getResources(),"dpm.png");//��������Ч��ͼ
            //��ȡms3d�ļ���������
            String name="ms3d/";
            //��ȡms3d�ļ���������
            InputStream in=null;
            InputStream in1=null;
            InputStream in2=null;
            InputStream in3=null;
            InputStream in4=null;
            InputStream in5=null;
            InputStream in6=null;
            InputStream in7=null;
            try{
            	in=getResources().getAssets().open(name+"fish0.ms3d");//����
            	in1=getResources().getAssets().open(name+"fish1.ms3d");//������
            	in2=getResources().getAssets().open(name+"fish2.ms3d");//������
            	in3=getResources().getAssets().open(name+"fish3.ms3d");//С�Ƴ���
            	in4=getResources().getAssets().open(name+"fish4.ms3d");//�ڹ�
            	in5=getResources().getAssets().open(name+"fish5.ms3d");//С�ڳ���
            	in6=getResources().getAssets().open(name+"fish6.ms3d");//С�ƽ���
            	in7=getResources().getAssets().open(name+"bei.ms3d");//���鱴
            }
            catch(Exception e)
            {
            	e.printStackTrace();
            }
           	
            //������������ģ��
    		ms3d = MS3DModel.load(in,manager,MySurfaceView.this);
    		ms3d1=MS3DModel.load(in1,manager,MySurfaceView.this); 
    	    ms3d2=MS3DModel.load(in2,manager,MySurfaceView.this); 
    	    ms3d3=MS3DModel.load(in3,manager,MySurfaceView.this); 
    	    ms3d4=MS3DModel.load(in4,manager,MySurfaceView.this); 
    	    ms3d5=MS3DModel.load(in5,manager,MySurfaceView.this); 
    	    ms3d6=MS3DModel.load(in6,manager,MySurfaceView.this);
    	    haibei=MS3DModel1.load(in7,manager,MySurfaceView.this);
            	//fishAl.clear();
    	    if(fishAl.size() == 0)
    	    {//λ��    �ٶ�     ��     ������      ����
    	    	fishAl.add(new SingleFish(ms3d,dpm,
    					new Vector3f(-7, 5, -7), new Vector3f(-0.05f, 0.02f, 0.03f),
    					new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), 150));
            	fishAl.add(new SingleFish(ms3d1,dpm,
    					new Vector3f(-1, 7, -4),
    					new Vector3f(-0.06f, -0.02f, 0.02f), new Vector3f(0, 0, 0),
    					new Vector3f(0, 0, 0), 70));
    			fishAl.add(new SingleFish(ms3d2,dpm,
    					new Vector3f(-0, 7, -4), new Vector3f(0.06f, 0.01f, -0.01f),
    					new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), 70));
    			fishAl.add(new SingleFish(ms3d4,dpm,
    					new Vector3f(-1, 3, -13),
    					new Vector3f(-0.005f, 0.01f, 0.00f), new Vector3f(0, 0, 0),
    					new Vector3f(0, 0, 0), 130));
    			fishAl.add(new SingleFish(ms3d4,dpm,
    					new Vector3f(1, 0, -13),
    					new Vector3f(-0.005f, 0.01f, 0.00f), new Vector3f(0, 0, 0),
    					new Vector3f(0, 0, 0), 130));
    			fishAl.add(new SingleFish(ms3d2,dpm,
    					new Vector3f(-5, 6, -4),
    					new Vector3f(-0.06f, 0.03f, -0.02f), new Vector3f(0, 0, 0),
    					new Vector3f(0, 0, 0), 70));
    	    }	
            //��������//��ʼ������
            back=initTexture(MySurfaceView.this.getResources(),"background.png");
            fishfood=initTexture(MySurfaceView.this.getResources(),"fishfood.png");
            bubbles=initTexture(MySurfaceView.this.getResources(),"bubble.png");
           
        
            //���������ζԶ��� 
            bg=new background(MySurfaceView.this); 
            //����ȼ��
            GLES20.glEnable(GLES20.GL_DEPTH_TEST);
            //����Բ������
            bubble = new BubbleControl(MySurfaceView.this,bubbles);
            bubble1 = new BubbleControl(MySurfaceView.this,bubbles);
            fishfoods=LoadUtil.loadFromFile("fishfood.obj", MySurfaceView.this.getResources(),MySurfaceView.this); 
            singlefood=new SingleFood(fishfood,fishfoods, MySurfaceView.this);
            feedfish=new FeedFish(MySurfaceView.this);
            //�������������Control����
    		if (fishControl == null) {
    			fishControl = new FishControl(fishAl, MySurfaceView.this);
    		}
    		// // ������Ⱥ�����Control
    		if (fishSchool == null) {
    			fishSchool = new FishSchoolControl(ms3d3,dpm,MySurfaceView.this,
    					new Vector3f(5, -2, 4),new Vector3f(-0.05f, 0.0f, -0.05f),50);
    		}
    		if (fishSchool1 == null) {
    			fishSchool1 = new FishSchoolControl(ms3d5,dpm,MySurfaceView.this,
    					new Vector3f(0, 3, 0),new Vector3f(0.05f, 0.0f, 0.05f),50);
    		}
    		if (fishSchool2 == null) {
    			fishSchool2 = new FishSchoolControl(ms3d6,dpm,MySurfaceView.this,
    					new Vector3f(-5, -7f, -10),new Vector3f(-0.05f, 0.0f, -0.05f),30);
    		}
    		if (fishSchool3 == null) {
    			fishSchool3 = new FishSchoolControl(ms3d6,dpm,MySurfaceView.this,
    					new Vector3f(-5.3f, -7.3f, -10),new Vector3f(-0.05f, 0.0f, -0.05f),30);
    		}
            //�رձ������   
            GLES20.glDisable(GLES20.GL_CULL_FACE);
        }
        public int initTexture(Resources res,String pname)//textureId
	{
		//��������ID
		int[] textures = new int[1];
		GLES20.glGenTextures
		(
				1,          //����������id������
				textures,   //����id������
				0           //ƫ����
		);    
		int textureId=textures[0];    
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER,GLES20.GL_NEAREST);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_MAG_FILTER,GLES20.GL_LINEAR);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S,GLES20.GL_REPEAT);//���췽ʽΪ��ȡ��ʽ
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T,GLES20.GL_REPEAT);
        
     
		 //ͨ������������ͼƬ===============begin===================
		InputStream is = null;
		String name="pic/"+pname;
		try {
			is = res.getAssets().open(name);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
        Bitmap bitmapTmp;
        try {
        	bitmapTmp = BitmapFactory.decodeStream(is);
        } 
        finally {
            try {
                is.close();
            } 
            catch(IOException e) {
                e.printStackTrace();
            }
        }
        //ͨ������������ͼƬ===============end=====================  
        
        //ʵ�ʼ�������
        GLUtils.texImage2D
        (
        		GLES20.GL_TEXTURE_2D,   //�������ͣ���OpenGL ES�б���ΪGL10.GL_TEXTURE_2D
        		0, 					  //����Ĳ�Σ�0��ʾ����ͼ��㣬�������Ϊֱ����ͼ
        		bitmapTmp, 			  //����ͼ��
        		0					  //����߿�ߴ�
        );
        bitmapTmp.recycle(); 		  //������سɹ����ͷ�ͼƬ
        return textureId;
	}
}