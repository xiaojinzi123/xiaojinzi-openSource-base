package xiaojinzi.base.android.image;

import android.content.Context;
import android.graphics.Bitmap;

import xiaojinzi.base.android.os.ScreenUtils;

/**
 * 有关图像的工具类
 * 
 * @author xiaojinzi
 *
 */
public class ImageUtil {

	/**
	 * 设置bitmap的宽和高
	 * 
	 * @param b
	 * @param width
	 * @param height
	 * @return
	 */
	public static Bitmap setBitmap(Bitmap b, int width, int height) {
		return Bitmap.createScaledBitmap(b, width < 1 ? 1 : width, height < 1 ? 1 : height, false);
	}

	/**
	 * 获取一个自适应的图片资源
	 * 
	 * @param bitmap
	 * @param context
	 * @return
	 */
	public static Bitmap getResizedBitmap(Bitmap bitmap, Context context) {
		int height = ScreenUtils.getScreenHeight(context);
		int width = ScreenUtils.getScreenWidth(context);

		if (height < 480 && width < 320) {
			return Bitmap.createScaledBitmap(bitmap, 32, 32, false);
		} else if (height < 800 && width < 480) {
			return Bitmap.createScaledBitmap(bitmap, 48, 48, false);
		} else if (height < 1024 && width < 600) {
			return Bitmap.createScaledBitmap(bitmap, 72, 72, false);
		} else {
			return Bitmap.createScaledBitmap(bitmap, 96, 96, false);
		}
	}

}
