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
		 this.setEGLContextClientVersion(2); //设置使用OPENGL ES2.0
	}
		 public SingleFood singlefood;//鱼食
		 public boolean Fooddraw = false;//是否喂鱼
	    static FeedFish feedfish;//喂鱼
	    boolean threadFlag=true;
	    public float Zposition=0;//鱼食的Z位置
	    public float Xposition=0;//鱼食的X位置
	    public FishControl fishControl;//群鱼控制类
	    public ArrayList<SingleFish> fishAl=new ArrayList<SingleFish>();//群鱼列表
		// 鱼群
		public FishSchoolControl fishSchool;//小丑鱼鱼群
		public FishSchoolControl fishSchool1;//小黑鱼鱼群
		public FishSchoolControl fishSchool2;//小黄鱼鱼群
		public FishSchoolControl fishSchool3;//小黄鱼鱼群
	    int back;//背景图纹理id
	   	int bubbles;//气球纹理ID
	   	int fishfood;//鱼食纹理id
	   	int dpm;//明暗效果图纹理ID
		TextureManager manager;	//纹理管理器
		MS3DModel ms3d;//鳐鱼
		MS3DModel ms3d1;//绿鲤鱼
     	MS3DModel ms3d2;//红鲤鱼
     	MS3DModel ms3d3;//小黄丑鱼
     	MS3DModel ms3d4;//乌龟
     	MS3DModel ms3d5;//小黑丑鱼
     	MS3DModel ms3d6;//小黄鱼
     	MS3DModel1 haibei;//珍珠贝
    
    	background bg;//纹理矩形
    	LoadedObjectVertexNormalTexture fishfoods;
		BubbleControl bubble;
		BubbleControl bubble1;
		float time = 0;	
		public void onDrawFrame(GL10 gl) 
        { 
        	//清除深度缓冲与颜色缓冲
            GLES20.glClear( GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT); 
            MatrixState.pushMatrix();
            if(bg!=null)
            {
            	bg.drawSelf(back); //绘制背景图
            }
            if(singlefood!=null)
            {
            	singlefood.drawSelf();//绘制鱼食
            	
            }
    		if (fishControl != null) {
    			fishControl.drawSelf();//绘制群鱼
    		}
    		if (fishSchool != null) {
    			fishSchool.drawSelf();//小黄丑鱼群
    		}
    		if (fishSchool1 != null) {
    			fishSchool1.drawSelf();//小黑丑鱼群
    		}
    		if (fishSchool2 != null) {
    			fishSchool2.drawSelf();//小黄鱼群
    		}
    		if (fishSchool3 != null) {
    			fishSchool3.drawSelf();//小黄鱼群
    		}
    		MatrixState.pushMatrix();//保护矩阵
    		MatrixState.translate(-5f,-16,-26.2f);//平移
      		this.haibei.animate(time,dpm); //绘制珍珠贝
      		MatrixState.popMatrix();//恢复矩阵
      		  time += 0.015f;
              //若当前播放时间大于总的动画时间则实际播放时间等于当前播放时间减去总的动画时间
              if(time > this.ms3d.getTotalTime()) {
              	time = time - this.ms3d.getTotalTime();
              }
    		MatrixState.popMatrix();//恢复矩阵
			 GLES20.glEnable(GLES20.GL_BLEND);  
            //设置混合因子c
            GLES20.glBlendFunc(GLES20.GL_SRC_COLOR, GLES20.GL_ONE_MINUS_SRC_COLOR);
            //保护现场
            MatrixState.pushMatrix();//保护矩阵
            if(bubble!=null)
            {
            	bubble.drawSelf();//绘制气泡
            }
            MatrixState.popMatrix();//恢复矩阵
            GLES20.glDisable(GLES20.GL_BLEND);//关闭混合
            
        }  
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            //设置视窗大小及位置 
        	GLES20.glViewport(0, 0, width, height); 
        	//计算GLSurfaceView的宽高比
    		float ratio = (float) width / height;
    		Constant.SCREEN_HEGHT=height;//获取屏幕高度
    		Constant.SCREEN_WIDTH=width;//获取屏幕宽度
    		Constant.leftABS=ratio*Constant.View_SCALE;
    		Constant.topABS=1 * Constant.View_SCALE;
    		Constant.SCREEN_SCALEX=Constant.View_SCALE*((ratio>1)?ratio:(1/ratio));
    		// 调用此方法计算产生透视投影矩阵
    		 MatrixState.setProjectFrustum(-Constant.leftABS, Constant.leftABS, -Constant.topABS, 
    				Constant.topABS, Constant.nearABS,Constant.farABS);
            //调用此方法产生摄像机9参数位置矩阵
            MatrixState.setCamera(
            		Constant.CameraX, // 人眼位置的X
    				Constant.CameraY, // 人眼位置的Y
    				Constant.CameraZ, // 人眼位置 的Z
    				Constant.TargetX, // 人眼球看的点X
    				Constant.TargetY, // 人眼球看的点Y
    				Constant.TargetZ, // 人眼球看的点Z
    				Constant.UpX, //Up向量
    				Constant.UpY, 
    				Constant.UpZ);     
        }
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            //设置屏幕背景色RGBA
            GLES20.glClearColor(0.5f,0.5f,0.5f, 1.0f);    
            MatrixState.setInitStack();//初始化矩阵
            //初始化光源位置
            MatrixState.setLightLocation(0,9,13);
            
          //创建纹理管理器对象
            manager = new TextureManager(getResources());
            //dpm=initTexture(R.drawable.dpm);
            dpm=initTexture(MySurfaceView.this.getResources(),"dpm.png");//加载明暗效果图
            //获取ms3d文件的输入流
            String name="ms3d/";
            //获取ms3d文件的输入流
            InputStream in=null;
            InputStream in1=null;
            InputStream in2=null;
            InputStream in3=null;
            InputStream in4=null;
            InputStream in5=null;
            InputStream in6=null;
            InputStream in7=null;
            try{
            	in=getResources().getAssets().open(name+"fish0.ms3d");//鳐鱼
            	in1=getResources().getAssets().open(name+"fish1.ms3d");//绿鲤鱼
            	in2=getResources().getAssets().open(name+"fish2.ms3d");//红鲤鱼
            	in3=getResources().getAssets().open(name+"fish3.ms3d");//小黄丑鱼
            	in4=getResources().getAssets().open(name+"fish4.ms3d");//乌龟
            	in5=getResources().getAssets().open(name+"fish5.ms3d");//小黑丑鱼
            	in6=getResources().getAssets().open(name+"fish6.ms3d");//小黄金鱼
            	in7=getResources().getAssets().open(name+"bei.ms3d");//珍珠贝
            }
            catch(Exception e)
            {
            	e.printStackTrace();
            }
           	
            //从输入流加载模型
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
    	    {//位置    速度     力     吸引力      重力
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
            //加载纹理//初始化纹理
            back=initTexture(MySurfaceView.this.getResources(),"background.png");
            fishfood=initTexture(MySurfaceView.this.getResources(),"fishfood.png");
            bubbles=initTexture(MySurfaceView.this.getResources(),"bubble.png");
           
        
            //创建三角形对对象 
            bg=new background(MySurfaceView.this); 
            //打开深度检测
            GLES20.glEnable(GLES20.GL_DEPTH_TEST);
            //创建圆柱对象
            bubble = new BubbleControl(MySurfaceView.this,bubbles);
            bubble1 = new BubbleControl(MySurfaceView.this,bubbles);
            fishfoods=LoadUtil.loadFromFile("fishfood.obj", MySurfaceView.this.getResources(),MySurfaceView.this); 
            singlefood=new SingleFood(fishfood,fishfoods, MySurfaceView.this);
            feedfish=new FeedFish(MySurfaceView.this);
            //创建对象鱼类的Control对象
    		if (fishControl == null) {
    			fishControl = new FishControl(fishAl, MySurfaceView.this);
    		}
    		// // 创建鱼群对象的Control
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
            //关闭背面剪裁   
            GLES20.glDisable(GLES20.GL_CULL_FACE);
        }
        public int initTexture(Resources res,String pname)//textureId
	{
		//生成纹理ID
		int[] textures = new int[1];
		GLES20.glGenTextures
		(
				1,          //产生的纹理id的数量
				textures,   //纹理id的数组
				0           //偏移量
		);    
		int textureId=textures[0];    
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER,GLES20.GL_NEAREST);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_MAG_FILTER,GLES20.GL_LINEAR);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S,GLES20.GL_REPEAT);//拉伸方式为截取方式
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T,GLES20.GL_REPEAT);
        
     
		 //通过输入流加载图片===============begin===================
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
        //通过输入流加载图片===============end=====================  
        
        //实际加载纹理
        GLUtils.texImage2D
        (
        		GLES20.GL_TEXTURE_2D,   //纹理类型，在OpenGL ES中必须为GL10.GL_TEXTURE_2D
        		0, 					  //纹理的层次，0表示基本图像层，可以理解为直接贴图
        		bitmapTmp, 			  //纹理图像
        		0					  //纹理边框尺寸
        );
        bitmapTmp.recycle(); 		  //纹理加载成功后释放图片
        return textureId;
	}
}