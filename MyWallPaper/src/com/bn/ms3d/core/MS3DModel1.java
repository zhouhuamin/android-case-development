package com.bn.ms3d.core;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import wyf.lxg.Constant.Constant;
import wyf.lxg.Constant.MatrixState;
import wyf.lxg.Constant.ShaderUtil;
import wyf.lxg.mywallpaper.MySurfaceView;
import android.opengl.GLES20;
import com.bn.ms3d.io.SmallEndianInputStream;
import com.bn.ms3d.texutil.TextureManager;
public class MS3DModel1{
	public FloatBuffer[] vertexCoordingBuffer;	//顶点坐标数据缓冲	
	public FloatBuffer[] texCoordingBuffer;	//纹理坐标数据缓冲	
	public FloatBuffer[] normalCoordingBuffer;//顶点法向量缓冲
	
	public TextureManager textureManager;		//纹理管理器
	public MS3DHeader header;				//头信息	
	public MS3DVertex[] vertexs;			//顶点信息	
	public MS3DTriangle[] triangles;		//三角形索引	
	public MS3DGroup[] groups;				//组信息	
	public MS3DMaterial[] materials;		//材质信息(目前用到的主要是纹理部分)	
	public float fps;						//fps信息	
	public float current_time;				//当前时间	
	public float totalTime;					//总时间	
	public float frame_count;				//关键帧数	
	public MS3DJoint[] joints;				//关节信息
    int muMMatrixHandle;//位置、旋转变换矩阵
	int mProgram;//自定义渲染管线程序id
    int muMVPMatrixHandle;//总变换矩阵引用id
    int maPositionHandle; //顶点位置属性引用id  
    int maTexCoorHandle; //顶点纹理坐标属性引用id  
    int maLightLocationHandle;//光源位置属性引用  
    int maCameraHandle; //摄像机位置属性引用 
    int maNormalHandle; //顶点法向量属性引用 
    int MovestHandle;
    int BenWl;
    String mVertexShader;//顶点着色器    	 
    String mFragmentShader;//片元着色器
	private MS3DModel1(MySurfaceView mv){
		initShader(mv);
	}
    //初始化着色器
    public void initShader(MySurfaceView mv){
    	//加载顶点着色器的脚本内容
        mVertexShader=ShaderUtil.loadFromAssetsFile("vertex.sh", mv.getResources());
        //加载片元着色器的脚本内容
        mFragmentShader=ShaderUtil.loadFromAssetsFile("frag.sh", mv.getResources());  
        //基于顶点着色器与片元着色器创建程序
        mProgram = ShaderUtil.createProgram(mVertexShader, mFragmentShader);
        //获取程序中顶点位置属性引用id  
        maPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");
        //获取位置、旋转变换矩阵引用
        muMMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMMatrix");
        //获取程序中顶点法向量属性引用  
        maNormalHandle= GLES20.glGetAttribLocation(mProgram, "aNormal");
        //获取程序中光源位置属性引用
        maLightLocationHandle=GLES20.glGetUniformLocation(mProgram, "uLightLocation");
        //获取程序中摄像机位置引用
        maCameraHandle=GLES20.glGetUniformLocation(mProgram, "uCamera");
        //获取程序中顶点纹理坐标属性引用id  
        maTexCoorHandle= GLES20.glGetAttribLocation(mProgram, "aTexCoor");
        MovestHandle=GLES20.glGetUniformLocation(mProgram, "MoveST");
        BenWl=GLES20.glGetUniformLocation(mProgram, "sTextureHd");  
        //获取程序中总变换矩阵引用id
        muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");  
    }
	//进行动画的方法
	public final void animate(float time,int texid){
		if(this.current_time != time){//相同时间不做更新
			this.updateJoint(time);	//更新关节
			this.updateVectexs();	//更新顶点
			this.draw(true,texid);	//执行绘制
		} 
		else{
			//执行绘制
			this.draw(false,texid);
		}}
	public void updateJoint(float time){//更新关节的方法
		this.current_time = time;//更新当前时间
		if(this.current_time > this.totalTime){//时间超过总时间置为零
			this.current_time = 0.0f;
		}			
		int size = this.joints.length;	//获取关节数量
		for(int i=0; i<size; i++){//更新每个关节
			this.joints[i].update(this.current_time);
		}}
	public void draw(boolean isUpdate,int texid){//绘制模型
   	    GLES20.glUseProgram(mProgram); //指定使用某套shader程序 
   	    MatrixState.copyMVMatrix();
        //将最终变换矩阵传入shader程序
        GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(), 0); 
        GLES20.glUniformMatrix4fv(muMMatrixHandle, 1, false, MatrixState.getMMatrix(), 0);
//      //将光源位置传入着色器程序   
        GLES20.glUniform3fv(maLightLocationHandle, 1, MatrixState.lightPositionFB);
        //将摄像机位置传入着色器程序   
        GLES20.glUniform3fv(maCameraHandle, 1, MatrixState.cameraFB);
		//启用顶点坐标数组
        int group_size = this.groups.length;         
        MS3DTriangle triangle = null;
        MS3DGroup group = null;
        int[] indexs = null;  
        int[] vertexIndexs = null;	//顶点索引
        FloatBuffer buffer = null;	//buffer缓冲
        MS3DMaterial material = null;   //材质
        for(int i=0; i<group_size; i++){
        	group = this.groups[i];	//获取当前组信息对象
        	indexs = group.getIndicies();//获取组内三角形的索引数组
        	int triangleCount  = indexs.length;//获取组内三角形的数量
        	//有材质（这里主要是指需要贴纹理）
        	if(group.getMaterialIndex() > -1){
        		material = this.materials[group.getMaterialIndex()];
        		this.textureManager.fillTexture(material.getName());
                GLES20.glVertexAttribPointer(//将纹理坐标缓冲送入渲染管线
               		    maTexCoorHandle, //纹理坐标
                		2, 
                		GLES20.GL_FLOAT, //顶点纹理坐标属性引用id
                		false,
                        2*4,   
                        this.texCoordingBuffer[i]//纹理坐标数组
                );   
                //启用纹理坐标数组
    			GLES20.glEnableVertexAttribArray(maTexCoorHandle);
        	}
        	if(isUpdate){//更新顶点缓冲
        		buffer = this.vertexCoordingBuffer[i];
        		for(int j=0; j<triangleCount; j++){//对组内的每个三角形循环
        			triangle = this.triangles[indexs[j]];//获取当前要处理的三角形信息对象
        			vertexIndexs = triangle.getIndexs();	//获取三角形中三个顶点的顶点索引
        			//将三角形的三个顶点的数据放入顶点数据缓冲（实际是完成三角形的组装）
        			for(int k=0; k<3; k++){
        				buffer.put(this.vertexs[vertexIndexs[k]].getCurrPosition().getVector3fArray());
        			}}
        		buffer.position(0);
        	}
            GLES20.glVertexAttribPointer(	//将顶点坐标缓冲送入渲染管线   
        		maPositionHandle,   //顶点位置属性引用id
        		3, 					
        		GLES20.GL_FLOAT, 	//顶点类型
        		false,
                3*4,   	
                this.vertexCoordingBuffer[i]//顶点数组
            );
            //将顶点法向量数据传入渲染管线
            GLES20.glVertexAttribPointer  
            (
           		maNormalHandle, 
        		3,   
        		GLES20.GL_FLOAT, 
        		false,
                3*4,   
                this.normalCoordingBuffer[i]//顶点法向量数组   
            ); 
            GLES20.glEnableVertexAttribArray(maPositionHandle);
            GLES20.glEnableVertexAttribArray(maNormalHandle); 
            GLES20.glActiveTexture(GLES20.GL_TEXTURE1);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texid);
            GLES20.glUniform1i(BenWl, 1);
        	//用顶点法进行绘制
            GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, triangleCount * 3); 
        }}
	private void updateVectexs(){//动画中更新顶点数据的方法
		int count = this.vertexs.length;//获取顶点数量
		for(int i=0; i<count; i++){	//更新每个顶点
			this.updateVectex(i);
		}}
	private void updateVectex(int index){//更新特定顶点的方法
		//获取当前需要更新的顶点对应的顶点信息对象
		MS3DVertex vertex = this.vertexs[index];
		//是否有关节骨骼ID
		if(vertex.getBone() == -1){//无关节控制
			
			vertex.setCurrPosition(vertex.getInitPosition());
		} 
		else{//有关节控制
			MS3DJoint joint = this.joints[vertex.getBone()];//获取对应的关节
			//根据关节的实时变换情况计算出顶点经关节影响后的位置
			vertex.setCurrPosition(joint.getMatrix().transform(joint.getAbsolute().invTransformAndRotate(vertex.getInitPosition())));
		}}
	//加载模型的方法
	public final static MS3DModel1 load(InputStream is, TextureManager manager,MySurfaceView mv){
		MS3DModel1 model = null;
		SmallEndianInputStream fis = null;
		try{
			//将输入流封装为SmallEndian格式的输入流
			fis = new SmallEndianInputStream(is);
			model = new MS3DModel1(mv);
			model.textureManager = manager;//纹理管理器
			model.header = MS3DHeader.load(fis);	//加载头信息
			model.vertexs = MS3DVertex.load(fis);//加载顶点信息
			model.triangles = MS3DTriangle.load(fis);//加载三角形组装索引信息
			model.groups = MS3DGroup.load(fis);//加载组信息
			model.materials = MS3DMaterial.load(fis, manager);//加载材质信息
			model.fps = fis.readFloat();//加载帧速率信息
			model.current_time = fis.readFloat();//当前时间
			model.frame_count = fis.readInt();//关键帧数
			model.totalTime = model.frame_count / model.fps;//计算动画总时间
			model.joints = MS3DJoint.load(fis);//加载关节信息
			model.initBuffer();//初始化缓冲
		} 
		catch (IOException e){
			e.printStackTrace();
		} 
		finally{
			if(fis != null){
				try {
					fis.close();
					} 
				catch (IOException e){
					e.printStackTrace();
				}}}
		System.gc();//申请垃圾回收
		return model;
	}
	private void initBuffer(){//初始化缓冲
		//将关节更新到起始时间（时间为0的时间）
		this.updateJoint(0.0f);
		this.updateVectexs();//更新顶点坐标	
		int count = this.groups.length;//组数量
		int triangleCount = 0;//每组三角形个数
		MS3DGroup group = null;//临时组信息
		MS3DTriangle triangle = null;//临时三角形信息
		this.texCoordingBuffer = new FloatBuffer[count];//材质坐标缓冲
		this.vertexCoordingBuffer = new FloatBuffer[count];//顶点坐标缓冲
		this.normalCoordingBuffer=new FloatBuffer[count];//顶点法向量坐标缓冲
		int[] indexs = null;//三角形索引
		int[] vertexIndexs = null;//顶点索引
		FloatBuffer buffer = null;	//数据缓冲
		for(int i=0; i<count; i++){//对模型中的每个组进行循环处理
			group = this.groups[i];//获取当前要处理的组
			indexs = group.getIndicies();  //获取组内三角形索引数组
			triangleCount = indexs.length;//获取组内三角形的数量
			//根据组内三角形的数量开辟合适大小的纹理坐标缓冲
			ByteBuffer byteBuffer = ByteBuffer.allocateDirect(triangleCount*6*4);
			byteBuffer.order(ByteOrder.nativeOrder());
			this.texCoordingBuffer[i] = byteBuffer.asFloatBuffer();
			//根据组内三角形的数量开辟合适大小的顶点坐标缓冲
			byteBuffer = ByteBuffer.allocateDirect(triangleCount*9*4);
			byteBuffer.order(ByteOrder.nativeOrder());
			this.vertexCoordingBuffer[i] = byteBuffer.asFloatBuffer();
			//根据组内三角形的数量开辟合适大小的顶点法向量缓冲
			byteBuffer = ByteBuffer.allocateDirect(triangleCount*9*4);
			byteBuffer.order(ByteOrder.nativeOrder());
			this.normalCoordingBuffer[i] = byteBuffer.asFloatBuffer();
			//循环对组内的每个三角形进行处理
			for(int j=0; j<triangleCount; j++){
				triangle = this.triangles[indexs[j]];//获取当前要处理的三角形
				vertexIndexs = triangle.getIndexs();//获取三角形中各个顶点的索引
				for(int k=0; k<3; k++){//对三角形中的三个顶点进行循环处理
					//获取当前组的纹理坐标数据缓冲
					buffer = this.texCoordingBuffer[i];
					//将当前遍历到的顶点的纹理ST坐标送入缓冲
					buffer.put(triangle.getS().getVector3fArray()[k]);
					buffer.put(triangle.getT().getVector3fArray()[k]);
					//获取当前组的顶点坐标数据缓冲
					buffer = this.vertexCoordingBuffer[i];
					//将当前遍历到的顶点的坐标送入缓冲
					buffer.put(this.vertexs[vertexIndexs[k]].getCurrPosition().getVector3fArray());
					//将当前遍历到的顶点法向量送入缓冲
					buffer=this.normalCoordingBuffer[i];
					buffer.put(triangle.getNormals()[k].getVector3fArray());
				}}
			//设置当前组的纹理坐标缓冲起始位置为0
			this.texCoordingBuffer[i].position(0);
			//设置当前组的顶点坐标缓冲起始位置为0
			this.vertexCoordingBuffer[i].position(0);
			//设置当前组的法向量缓冲起始位置为0
			this.normalCoordingBuffer[i].position(0);
		}}
	public final float getTotalTime(){
		return totalTime;    
	}}
