package wyf.lxg.Constant;

public class Constant {
	// ��Ļ�ĳ�
	public static float SCREEN_HEGHT=1280;
	// ��Ļ�Ŀ�
	public static float SCREEN_WIDTH=720;
		// ��Ļ�ĳ������
		public static float leftABS;
		public static float topABS;
		public static float UNIT_SIZE=1.0f;    	

		public static float nearABS =2.1f ;
		public static float farABS = 100.0f;
		//�ӽǵ����ű�
		public static float View_SCALE = 0.6f;
		//����ͼ�����ű�
		public static float SCREEN_SCALEX=1.0f;
		public static float SCREEN_SCALEY = 1.8f * 0.6f;
		public static float SCREEN_SCALEZ = 1.5f * 0.6f;
		//�������λ��
		public static float CameraX = 0;
		public static float CameraY = 5.0f;
		public static float CameraZ = 23.0f;
		//�������Up����
		public static float UpX = 0.0f;
		public static float UpY = 0.9746f;//(float) 23/ (float) Math.sqrt(554)23.5372
		public static float UpZ = -0.2124f;//(float) 5/ (float) Math.sqrt(554)23.5372
		//������ƶ������ű�
		public static float CameraMove_SCALE = 120;
		//�����������ƶ�����
		public static float MaxCameraMove = 4;
		//������۲��λ��
		public static float TargetX = 0;
		public static float TargetY = 0f;
		public static float TargetZ = 0;
		//����ĸ߶�
		public static float Y_HEIGHT =  -6;
		//Z��Ĵ��ط�Χ
		public static float ZTouch_Max =  -1;
		public static float ZTouch_Min =  -9;
		//��ʳ��X��Ļζ�����
		public static float FoodMove_X = 0.01f;
		//��ʳÿ������ľ���
		public static float FoodSpeed = 0.15f; 
		//��Ե���ʳ���ж���Χ
		public static float FoodFeedDistance = 0.5f;
		// ��ʳ��Y�᷽���ϵ���С����
		public static float FoodPositionMin_Y =-6f;
		// ʳ��ĳ�ʼYposition  ʳ����Y���������
		public static float FoodPositionMax_Y = 8f;
		// ���ݵ�����
		public static int BUBBLE_NUM = 24;
		//����ÿ���ƶ��ľ���
		public static float BubbleMoveDistance = 0.005f;
		
		// �����ٶȱ仯���ֵ��������λ���ܵõ�û�������ϵ����ı仯 
		public static float MaxChangeSpeed = 0.01f;
		// ������֮�����ײ����  С���������֮�󽫲�����������
		public static float MinDistances = 4.0f;

		// ����������Ⱥ֮�����ײ����  С���������֮�������������
		public static float SMinDistaces = 2.5f;
		// ����ĳ�ʼλ������
		public static Vector3f initialize = new Vector3f(-1, 0, 0);
		public static Vector3f initializeschool = new Vector3f(-1, 0, 0);
		
		// ��Ⱥ�İ뾶
		//public static float Radius = 1.5f;
		public static float Radius = 1.5f;
		// ���������ű���
		public static float ConstantForceScals = 200;
		// ��Ⱥ�͵�����֮������ű�
		public static float WeightScals = 11;
		//������ֵ
		public static float Thold=10;
		// �ջ��������ű���
		public static float AttractForceScals = 56.1f;
		
		// �����Ƿ�
		public static boolean isFeed = true;
		
		//�ٶȵ���ֵ
		//public static float MaxSpeed=0.07f;
		public static float MaxSpeed=0.07f;

		//��Ⱥ������������ζ��ٶ�
		public static float SchoolMaxSpeed=0.05f;
		//ιʳ
		public static boolean feeding=false;
}
