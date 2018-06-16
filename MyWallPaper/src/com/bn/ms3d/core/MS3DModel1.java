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
	public FloatBuffer[] vertexCoordingBuffer;	//�����������ݻ���	
	public FloatBuffer[] texCoordingBuffer;	//�����������ݻ���	
	public FloatBuffer[] normalCoordingBuffer;//���㷨��������
	
	public TextureManager textureManager;		//���������
	public MS3DHeader header;				//ͷ��Ϣ	
	public MS3DVertex[] vertexs;			//������Ϣ	
	public MS3DTriangle[] triangles;		//����������	
	public MS3DGroup[] groups;				//����Ϣ	
	public MS3DMaterial[] materials;		//������Ϣ(Ŀǰ�õ�����Ҫ��������)	
	public float fps;						//fps��Ϣ	
	public float current_time;				//��ǰʱ��	
	public float totalTime;					//��ʱ��	
	public float frame_count;				//�ؼ�֡��	
	public MS3DJoint[] joints;				//�ؽ���Ϣ
    int muMMatrixHandle;//λ�á���ת�任����
	int mProgram;//�Զ�����Ⱦ���߳���id
    int muMVPMatrixHandle;//�ܱ任��������id
    int maPositionHandle; //����λ����������id  
    int maTexCoorHandle; //��������������������id  
    int maLightLocationHandle;//��Դλ����������  
    int maCameraHandle; //�����λ���������� 
    int maNormalHandle; //���㷨������������ 
    int MovestHandle;
    int BenWl;
    String mVertexShader;//������ɫ��    	 
    String mFragmentShader;//ƬԪ��ɫ��
	private MS3DModel1(MySurfaceView mv){
		initShader(mv);
	}
    //��ʼ����ɫ��
    public void initShader(MySurfaceView mv){
    	//���ض�����ɫ���Ľű�����
        mVertexShader=ShaderUtil.loadFromAssetsFile("vertex.sh", mv.getResources());
        //����ƬԪ��ɫ���Ľű�����
        mFragmentShader=ShaderUtil.loadFromAssetsFile("frag.sh", mv.getResources());  
        //���ڶ�����ɫ����ƬԪ��ɫ����������
        mProgram = ShaderUtil.createProgram(mVertexShader, mFragmentShader);
        //��ȡ�����ж���λ����������id  
        maPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");
        //��ȡλ�á���ת�任��������
        muMMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMMatrix");
        //��ȡ�����ж��㷨������������  
        maNormalHandle= GLES20.glGetAttribLocation(mProgram, "aNormal");
        //��ȡ�����й�Դλ����������
        maLightLocationHandle=GLES20.glGetUniformLocation(mProgram, "uLightLocation");
        //��ȡ�����������λ������
        maCameraHandle=GLES20.glGetUniformLocation(mProgram, "uCamera");
        //��ȡ�����ж�������������������id  
        maTexCoorHandle= GLES20.glGetAttribLocation(mProgram, "aTexCoor");
        MovestHandle=GLES20.glGetUniformLocation(mProgram, "MoveST");
        BenWl=GLES20.glGetUniformLocation(mProgram, "sTextureHd");  
        //��ȡ�������ܱ任��������id
        muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");  
    }
	//���ж����ķ���
	public final void animate(float time,int texid){
		if(this.current_time != time){//��ͬʱ�䲻������
			this.updateJoint(time);	//���¹ؽ�
			this.updateVectexs();	//���¶���
			this.draw(true,texid);	//ִ�л���
		} 
		else{
			//ִ�л���
			this.draw(false,texid);
		}}
	public void updateJoint(float time){//���¹ؽڵķ���
		this.current_time = time;//���µ�ǰʱ��
		if(this.current_time > this.totalTime){//ʱ�䳬����ʱ����Ϊ��
			this.current_time = 0.0f;
		}			
		int size = this.joints.length;	//��ȡ�ؽ�����
		for(int i=0; i<size; i++){//����ÿ���ؽ�
			this.joints[i].update(this.current_time);
		}}
	public void draw(boolean isUpdate,int texid){//����ģ��
   	    GLES20.glUseProgram(mProgram); //ָ��ʹ��ĳ��shader���� 
   	    MatrixState.copyMVMatrix();
        //�����ձ任������shader����
        GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(), 0); 
        GLES20.glUniformMatrix4fv(muMMatrixHandle, 1, false, MatrixState.getMMatrix(), 0);
//      //����Դλ�ô�����ɫ������   
        GLES20.glUniform3fv(maLightLocationHandle, 1, MatrixState.lightPositionFB);
        //�������λ�ô�����ɫ������   
        GLES20.glUniform3fv(maCameraHandle, 1, MatrixState.cameraFB);
		//���ö�����������
        int group_size = this.groups.length;         
        MS3DTriangle triangle = null;
        MS3DGroup group = null;
        int[] indexs = null;  
        int[] vertexIndexs = null;	//��������
        FloatBuffer buffer = null;	//buffer����
        MS3DMaterial material = null;   //����
        for(int i=0; i<group_size; i++){
        	group = this.groups[i];	//��ȡ��ǰ����Ϣ����
        	indexs = group.getIndicies();//��ȡ���������ε���������
        	int triangleCount  = indexs.length;//��ȡ���������ε�����
        	//�в��ʣ�������Ҫ��ָ��Ҫ������
        	if(group.getMaterialIndex() > -1){
        		material = this.materials[group.getMaterialIndex()];
        		this.textureManager.fillTexture(material.getName());
                GLES20.glVertexAttribPointer(//���������껺��������Ⱦ����
               		    maTexCoorHandle, //��������
                		2, 
                		GLES20.GL_FLOAT, //��������������������id
                		false,
                        2*4,   
                        this.texCoordingBuffer[i]//������������
                );   
                //����������������
    			GLES20.glEnableVertexAttribArray(maTexCoorHandle);
        	}
        	if(isUpdate){//���¶��㻺��
        		buffer = this.vertexCoordingBuffer[i];
        		for(int j=0; j<triangleCount; j++){//�����ڵ�ÿ��������ѭ��
        			triangle = this.triangles[indexs[j]];//��ȡ��ǰҪ�������������Ϣ����
        			vertexIndexs = triangle.getIndexs();	//��ȡ����������������Ķ�������
        			//�������ε�������������ݷ��붥�����ݻ��壨ʵ������������ε���װ��
        			for(int k=0; k<3; k++){
        				buffer.put(this.vertexs[vertexIndexs[k]].getCurrPosition().getVector3fArray());
        			}}
        		buffer.position(0);
        	}
            GLES20.glVertexAttribPointer(	//���������껺��������Ⱦ����   
        		maPositionHandle,   //����λ����������id
        		3, 					
        		GLES20.GL_FLOAT, 	//��������
        		false,
                3*4,   	
                this.vertexCoordingBuffer[i]//��������
            );
            //�����㷨�������ݴ�����Ⱦ����
            GLES20.glVertexAttribPointer  
            (
           		maNormalHandle, 
        		3,   
        		GLES20.GL_FLOAT, 
        		false,
                3*4,   
                this.normalCoordingBuffer[i]//���㷨��������   
            ); 
            GLES20.glEnableVertexAttribArray(maPositionHandle);
            GLES20.glEnableVertexAttribArray(maNormalHandle); 
            GLES20.glActiveTexture(GLES20.GL_TEXTURE1);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texid);
            GLES20.glUniform1i(BenWl, 1);
        	//�ö��㷨���л���
            GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, triangleCount * 3); 
        }}
	private void updateVectexs(){//�����и��¶������ݵķ���
		int count = this.vertexs.length;//��ȡ��������
		for(int i=0; i<count; i++){	//����ÿ������
			this.updateVectex(i);
		}}
	private void updateVectex(int index){//�����ض�����ķ���
		//��ȡ��ǰ��Ҫ���µĶ����Ӧ�Ķ�����Ϣ����
		MS3DVertex vertex = this.vertexs[index];
		//�Ƿ��йؽڹ���ID
		if(vertex.getBone() == -1){//�޹ؽڿ���
			
			vertex.setCurrPosition(vertex.getInitPosition());
		} 
		else{//�йؽڿ���
			MS3DJoint joint = this.joints[vertex.getBone()];//��ȡ��Ӧ�Ĺؽ�
			//���ݹؽڵ�ʵʱ�任�����������㾭�ؽ�Ӱ����λ��
			vertex.setCurrPosition(joint.getMatrix().transform(joint.getAbsolute().invTransformAndRotate(vertex.getInitPosition())));
		}}
	//����ģ�͵ķ���
	public final static MS3DModel1 load(InputStream is, TextureManager manager,MySurfaceView mv){
		MS3DModel1 model = null;
		SmallEndianInputStream fis = null;
		try{
			//����������װΪSmallEndian��ʽ��������
			fis = new SmallEndianInputStream(is);
			model = new MS3DModel1(mv);
			model.textureManager = manager;//���������
			model.header = MS3DHeader.load(fis);	//����ͷ��Ϣ
			model.vertexs = MS3DVertex.load(fis);//���ض�����Ϣ
			model.triangles = MS3DTriangle.load(fis);//������������װ������Ϣ
			model.groups = MS3DGroup.load(fis);//��������Ϣ
			model.materials = MS3DMaterial.load(fis, manager);//���ز�����Ϣ
			model.fps = fis.readFloat();//����֡������Ϣ
			model.current_time = fis.readFloat();//��ǰʱ��
			model.frame_count = fis.readInt();//�ؼ�֡��
			model.totalTime = model.frame_count / model.fps;//���㶯����ʱ��
			model.joints = MS3DJoint.load(fis);//���عؽ���Ϣ
			model.initBuffer();//��ʼ������
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
		System.gc();//������������
		return model;
	}
	private void initBuffer(){//��ʼ������
		//���ؽڸ��µ���ʼʱ�䣨ʱ��Ϊ0��ʱ�䣩
		this.updateJoint(0.0f);
		this.updateVectexs();//���¶�������	
		int count = this.groups.length;//������
		int triangleCount = 0;//ÿ�������θ���
		MS3DGroup group = null;//��ʱ����Ϣ
		MS3DTriangle triangle = null;//��ʱ��������Ϣ
		this.texCoordingBuffer = new FloatBuffer[count];//�������껺��
		this.vertexCoordingBuffer = new FloatBuffer[count];//�������껺��
		this.normalCoordingBuffer=new FloatBuffer[count];//���㷨�������껺��
		int[] indexs = null;//����������
		int[] vertexIndexs = null;//��������
		FloatBuffer buffer = null;	//���ݻ���
		for(int i=0; i<count; i++){//��ģ���е�ÿ�������ѭ������
			group = this.groups[i];//��ȡ��ǰҪ�������
			indexs = group.getIndicies();  //��ȡ������������������
			triangleCount = indexs.length;//��ȡ���������ε�����
			//�������������ε��������ٺ��ʴ�С���������껺��
			ByteBuffer byteBuffer = ByteBuffer.allocateDirect(triangleCount*6*4);
			byteBuffer.order(ByteOrder.nativeOrder());
			this.texCoordingBuffer[i] = byteBuffer.asFloatBuffer();
			//�������������ε��������ٺ��ʴ�С�Ķ������껺��
			byteBuffer = ByteBuffer.allocateDirect(triangleCount*9*4);
			byteBuffer.order(ByteOrder.nativeOrder());
			this.vertexCoordingBuffer[i] = byteBuffer.asFloatBuffer();
			//�������������ε��������ٺ��ʴ�С�Ķ��㷨��������
			byteBuffer = ByteBuffer.allocateDirect(triangleCount*9*4);
			byteBuffer.order(ByteOrder.nativeOrder());
			this.normalCoordingBuffer[i] = byteBuffer.asFloatBuffer();
			//ѭ�������ڵ�ÿ�������ν��д���
			for(int j=0; j<triangleCount; j++){
				triangle = this.triangles[indexs[j]];//��ȡ��ǰҪ�����������
				vertexIndexs = triangle.getIndexs();//��ȡ�������и������������
				for(int k=0; k<3; k++){//���������е������������ѭ������
					//��ȡ��ǰ��������������ݻ���
					buffer = this.texCoordingBuffer[i];
					//����ǰ�������Ķ��������ST�������뻺��
					buffer.put(triangle.getS().getVector3fArray()[k]);
					buffer.put(triangle.getT().getVector3fArray()[k]);
					//��ȡ��ǰ��Ķ����������ݻ���
					buffer = this.vertexCoordingBuffer[i];
					//����ǰ�������Ķ�����������뻺��
					buffer.put(this.vertexs[vertexIndexs[k]].getCurrPosition().getVector3fArray());
					//����ǰ�������Ķ��㷨�������뻺��
					buffer=this.normalCoordingBuffer[i];
					buffer.put(triangle.getNormals()[k].getVector3fArray());
				}}
			//���õ�ǰ����������껺����ʼλ��Ϊ0
			this.texCoordingBuffer[i].position(0);
			//���õ�ǰ��Ķ������껺����ʼλ��Ϊ0
			this.vertexCoordingBuffer[i].position(0);
			//���õ�ǰ��ķ�����������ʼλ��Ϊ0
			this.normalCoordingBuffer[i].position(0);
		}}
	public final float getTotalTime(){
		return totalTime;    
	}}
