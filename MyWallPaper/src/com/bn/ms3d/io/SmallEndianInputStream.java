package com.bn.ms3d.io;
import java.io.IOException;
import java.io.InputStream;
//�Լ������ķ���SmallEndian�ֽ�˳��Ĵ�����
public class SmallEndianInputStream{
	InputStream is;  //��װ��������
	public SmallEndianInputStream(InputStream is){
		this.is = is;
	}
	//��SmallEndian�ֽ�˳���ȡһ��32λ����������
	public int readInt() throws IOException	{
		byte[] buff = new byte[4];
		is.read(buff);
		return (buff[3] << 24) 
			+ ((buff[2] << 24) >>> 8) 
			+ ((buff[1] << 24) >>> 16) 
			+ ((buff[0] << 24) >>> 24);
	}
	//��SmallEndian�ֽ�˳���ȡһ��16λ�޷�������������
	public int readUnsignedShort()throws IOException{
		byte[] buff = new byte[2];
		is.read(buff);
		return ((buff[1] << 24) >>> 16) 
				+ ((buff[0] << 24) >>> 24);
	}
	//��ȡһ���ֽ�
	public byte readByte() throws IOException{
		return (byte) is.read();
	}
	//��ȡһ��������
	public final float readFloat() throws IOException{
		return Float.intBitsToFloat(this.readInt());
	}
	public int read(byte[] buff) throws IOException{	//��ȡ�ֽ�����
		int count = this.is.read(buff);
		return count;
	}
	public String readString(int length) throws IOException{//��ȡ�ַ���
		byte[] buff = new byte[length];
		this.is.read(buff);
		return this.makeSafeString(buff);
	}
	//���ֽ������е����ݰ�ȫ��ת��Ϊ�ַ���������
	public String makeSafeString(byte buffer[]){
        final int len = buffer.length;
        for (int i = 0; i < len; i++){
            if (buffer[i] == (byte) 0){
                return new String(buffer, 0, i);
            }}
        return new String(buffer).trim();
    }
	public void close() throws IOException{	//�ر���
		if(is != null)
			is.close();
	}}
