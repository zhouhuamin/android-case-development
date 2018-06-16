package com.bn.ms3d.mathutil;

//��ʾ�����ĸ����������ݣ���������ꡢ��Ԫ���ȣ�
public class Vector4f 
{
	//����ĸ�����ֵ������
	float[] coording = new float[4];
	
	//������ת���ݣ���ڲ���Ϊŷ���ǵ���������
	//ת��Ϊ��Ԫ�����ĸ�����XYZW
	public final void setFromEulerAngleToQuaternion(float yaw, float pitch, float roll) 
	{
		float angle = 0.0f;
        float sr, sp, sy, cr, cp, cy;
        angle = yaw * 0.5f;
        sr = (float) Math.sin(angle);
        cr = (float) Math.cos(angle);
        angle = pitch * 0.5f;
        sp = (float) Math.sin(angle);
        cp = (float) Math.cos(angle);
        angle = roll * 0.5f;
        sy = (float) Math.sin(angle);
        cy = (float) Math.cos(angle);       

        this.setX(sr * cp * cy - cr * sp * sy); // X
        this.setY(cr * sp * cy + sr * cp * sy); // Y
        this.setZ(cr * cp * sy - sr * sp * cy); // Z
        this.setW(cr * cp * cy + sr * sp * sy); // W
	}
	
	//ʵʩv1��v2������Ԫ������alpha��1-alpha�ı����ںϲ�ֵ
	public final void interpolate(Vector4f v1, Vector4f v2, float alpha) 
	{
        double dot = 0, s1, s2, om, sinom;
        //��������Ԫ���ĵ��
        for(int i=0; i<4; i++)
        {
        	dot +=  v2.coording[i] * v1.coording[i];
        }        	
        Vector4f v0 = null;
        //�����ֵС��0�� v1�÷���ͬʱ�����ֵ�÷�
        if (dot < 0) 
        {
        	v0 = new Vector4f();
        	for(int i=0; i<4; i++)
        	{
        		v0.coording[i] = - v1.coording[i];
        	}        		
            dot = -dot;
        } 
        else  //����ֱ�Ӳ���v1��ֵ
        {
        	v0 = v1;
        }
       
        //�����ֵ�ӽ�1����˵��������Ԫ����ʾ����ת�ǳ��ӽ�
        //ֱ�����Բ�ֵ����
        if(dot>0.999999)
        {
        	s1 = 1.0 - alpha;
            s2 = alpha;
        }
        else
        {//������˼ά�ռ��е�Բ���������ֵϵ��
            om = Math.acos(dot);
            sinom = Math.sin(om);
            s1 = Math.sin((1.0 - alpha) * om) / sinom;
            s2 = Math.sin(alpha * om) / sinom;
        } 
        
        //ͨ����ֵϵ��s1��s2����v1��v2������Ԫ��������ֵ��
        //������Ԫ�����ĸ�����
        this.setW((float) (s1 * v0.getW() + s2 * v2.getW()));
        this.setX((float) (s1 * v0.getX() + s2 * v2.getX()));
        this.setY((float) (s1 * v0.getY() + s2 * v2.getY()));
        this.setZ((float) (s1 * v0.getZ() + s2 * v2.getZ()));
    }
	
	public final void setX(float x) 
	{
		this.coording[0] = x;
	}
	
	public final void setY(float y) 
	{
		this.coording[1] = y;
	}
	
	public final void setZ(float z) 
	{
		this.coording[2] = z;
	}
	
	public final void setW(float w) 
	{
		this.coording[3] = w;
	}
	
	public final float getX() 
	{
		return this.coording[0];
	}
	
	public final float getY() 
	{
		return this.coording[1];
	}
	
	public final float getZ() 
	{
		return this.coording[2];
	}
	
	public final float getW() 
	{
		return this.coording[3];
	}
	
}
