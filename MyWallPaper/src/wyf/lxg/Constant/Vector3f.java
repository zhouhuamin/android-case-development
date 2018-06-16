package wyf.lxg.Constant;

public class Vector3f {
	public float x;
	public float y;
	public float z;
	// ������
	public Vector3f(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	// �����ĳ��ȵ�ģ
	public float Vectormodule() {
		float Vectormodule = (float) Math.sqrt(this.x * this.x + this.y
				* this.y + this.z * this.z);
		return Vectormodule;
	}
	// �����ļӷ����ٶ������ܵ����������ӷ��õ��µ��ٶȻ���λ�����ٶ����ӷ��ĵ��µ�λ�ơ�
	public void plus(Vector3f Vector) {
		this.x += Vector.x;
		this.y += Vector.y;
		this.z += Vector.z;
	}
	// ��������һ���ı�������
	public void ChangeStep(float Length) {
		this.x = this.x / Length;
		this.y = this.y / Length;
		this.z = this.z / Length;
	}

	// ��ȡ���Ĵ�С���������ɷ��ȵĹ�ϵ
	public void getforce(float weight) {
		if (weight != 0) {
			this.x = this.x / weight;
			this.y = this.y / weight;
			this.z = this.z / weight;
		}
	}
	// �õ�ָ���뾶������
	public Vector3f getNeedradiusvector(float R) 
	{
		Vector3f Vnd = new Vector3f(this.x, this.y, this.z);
		// ��������ģ��
		float Length = Vnd.Vectormodule();
		if (Length != 0) {
			// ��ɵ�λ����
			Vnd.ChangeStep(Length);
		}
		// ���ָ��ģ��������
		Vnd.ChangeStep(1 / R);
		return Vnd;
	}

	// ������������һ��
	public Vector3f cutGetforce(Vector3f Vector) 
	{
		/**
		 * �ѵ�ǰ��������һ�������������ĵ�ͬһ���µ�������������Ϊ����һ����ָ����������������������ķ��򲢸�����һ��
		 */
		Vector3f Vtr = new Vector3f(this.x - Vector.x, this.y - Vector.y,
				this.z - Vector.z);
		float Length = Vtr.Vectormodule();
		if (Length != 0)
		{
			Vtr.ChangeStep(Length);
		}
		return Vtr;
	}

	// �õ��ӵ�Position��ConstantPositon������
	public Vector3f cutPc(Vector3f Vector)
	{
		//�ѵ�ǰ��������һ�������������ĵ�ͬһ���µ�������������Ϊ����һ����ָ����������������������ķ��򲢸�����һ��
		Vector3f Vtr = new Vector3f(this.x - Vector.x, this.y - Vector.y,
				this.z - Vector.z);
		return Vtr;
	}

	// �����ļ�����Ҫ��Ϊ���õ����������������ֻ����һ���ľ�Ż������������
	public Vector3f cut(Vector3f Vector, float MinDistances) 
	{
		 // �ѵ�ǰ��������һ�������������ĵ�ͬһ���µ�������������Ϊ����һ����ָ����������������������ķ��򲢸�����һ��
		Vector3f Vtr = new Vector3f(this.x - Vector.x, this.y - Vector.y,
				this.z - Vector.z);
		float Length = Vtr.Vectormodule();
		// �ж�һ�¼���һ��������ľ����ǲ����ڹ涨����ֵ��Χ�ڣ�������������ڽ�Vtr��һ��֮��ͷ��ؼ���
		// ���������һ���ķ�Χ�򲻽��й�һ��ֱ�ӽ�Vtr�ĸ�������ȫ�����0�����ؾ͵��η�������ǲ����ڵ�
		// ����������ľ�������������𽥵ı�С����Maxdistances֮�������֮���������ʧ�˲��Ҿ���Խ��������
		if (Length <= MinDistances) 
		{
			// ������һ�������Ҳ��Ҵ�С����֮��ľ���ɷ��Ȳ��ҽ��б�������
			if (Length != 0) 
			{
				Vtr.ChangeStep(Length * Length);
			}
		} 
		else 
		{
			// ������ľ��볬��һ����Χ����ʧ
			Vtr.x = 0;
			Vtr.y = 0;
			Vtr.z = 0;
		}
		return Vtr;
	}
}
