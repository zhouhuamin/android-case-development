package wyf.lxg.mywallpaper;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.view.SurfaceHolder;

public abstract class OpenGLES2WallpaperService extends GLWallpaperService {
	public interface Renderer {

	}

	@Override
	public Engine onCreateEngine() {
		return new OpenGLES2Engine();
	}
	
	class OpenGLES2Engine extends GLWallpaperService.GLEngine {

		@Override
		public void onCreate(SurfaceHolder surfaceHolder) {
			super.onCreate(surfaceHolder);
			
			// Check if the system supports OpenGL ES 2.0.
			//创建Activity管理器
			final ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		    //获取当前设备配置信息
			final ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
			//判断是否支持OpenGL ES 2.0
			final boolean supportsEs2 = configurationInfo.reqGlEsVersion >= 0x20000;
			
			if (supportsEs2) 
			{
				// Request an OpenGL ES 2.0 compatible context.
				setEGLContextClientVersion(2);//设置使用OpenGL ES 2.0
				
				// On Honeycomb+ devices, this improves the performance when
				// leaving and resuming the live wallpaper.
				setPreserveEGLContextOnPause(true);

				// Set the renderer to our user-defined renderer.
				setRenderer(getNewRenderer());//设置场景渲染器
//				this.renderer=(MySurfaceView) renderer;
//				setRenderer(renderer);
			} 
			else 
			{
				// This is where you could create an OpenGL ES 1.x compatible
				// renderer if you wanted to support both ES 1 and ES 2.
				return;
			}			
		}
		

	}	
	
	abstract android.opengl.GLSurfaceView.Renderer getNewRenderer();
}
