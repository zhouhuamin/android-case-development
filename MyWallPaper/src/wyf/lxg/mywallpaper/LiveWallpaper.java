package wyf.lxg.mywallpaper;

public class LiveWallpaper extends OpenGLES2WallpaperService{
	@Override
	android.opengl.GLSurfaceView.Renderer getNewRenderer() {
		// TODO Auto-generated method stub
		return new MySurfaceView(this);//�����Զ��峡����Ⱦ��
	}
}
