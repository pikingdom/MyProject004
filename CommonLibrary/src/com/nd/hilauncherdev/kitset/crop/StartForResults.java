package com.nd.hilauncherdev.kitset.crop;

import java.io.File;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

/**
 *  调用系统相册 或是 摄像头 获取图片数据
 * 	用法：
 * 	// 摄像头
	StartForResults.getPicFromCamera(DynamicCommentActivity.this);
	// 相册
	StartForResults.getPicFromAlbum(DynamicCommentActivity.this);
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent paramIntent) {
		//获取图片数据
		switch (requestCode) {
		// 图片上传
		case StartForResults.CAMERA_PICKED_WITH_DATA:
		case StartForResults.PHOTO_PICKED_WITH_DATA:
			PickData mData = StartForResults.onResult(this, requestCode,resultCode, paramIntent);
	}
		
	}
 * */
public final class StartForResults {
	
	public final static int PHOTO_PICKED_WITH_DATA = 99;
	public final static int CAMERA_PICKED_WITH_DATA = 98;
	public final static String TAG = "StartForResults";

	/**
	 * <br>Description: 调用相册
	 * <br>Author:caizp
	 * <br>Date:2011-8-1上午10:52:53
	 * @param activity
	 */
	public static void getPicFromAlbum(Activity activity, int requestCode) {
		Intent intent = new Intent(Intent.ACTION_PICK);
		intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
				MediaStore.Images.Media.CONTENT_TYPE);
		try {
			activity.startActivityForResult(intent, requestCode);// 启动相册
		} catch (ActivityNotFoundException e) {
		}
	}

	/**
	 * <br>Description: 调用拍照
	 * <br>Author:caizp
	 * <br>Date:2011-7-12下午06:22:48
	 * @param activity
	 */
	/*public static void getPicFromCamera(Activity activity) {
		// set camera intent
		Intent localIntent = new Intent(
				android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

		// temp file
		File dir = Environment.getExternalStorageDirectory();
		File localTemp = new File(dir, "camera-t.jpg");

		Uri localUri = Uri.fromFile(localTemp);
		localIntent.putExtra(MediaStore.EXTRA_OUTPUT, localUri);
		localIntent.putExtra( MediaStore.EXTRA_VIDEO_QUALITY,100);

		activity.startActivityForResult(localIntent, CAMERA_PICKED_WITH_DATA);
	}*/

	/**
	 * <br>Description: 图片相关数据
	 * <br>Author:caizp
	 * <br>Date:2011-7-12下午08:38:20
	 */
	public static class PickData {
		public Bitmap thumbBmp;
		public String localPath;
		public String title;
		
		//type:1 = GIF，2 = JPG，3 = PNG
		public int type;
		public long size;
		public int width;
		public int height;
		public PickData(){
		}
		public PickData(String lp){
			localPath = lp;
			type = 2;
			title = new File (localPath).getName();
			thumbBmp = getLocalBitmap(localPath, 60, this);
		}
	}
	
	/**
	 * <br>Description: 处理相册或相机返回的图片
	 * <br>Author:caizp
	 * <br>Date:2011-7-12下午08:32:08
	 * @param activity
	 * @param requestCode
	 * @param resultCode
	 * @param paramIntent
	 * @param isSave2Sys 是否保存到系统图库
	 * @return
	 */
	public static PickData onResult(Activity activity, int requestCode,
			int resultCode, Intent paramIntent, boolean isSave2Sys) {

		PickData pickData = null;
		
		if (resultCode != Activity.RESULT_OK)
			return pickData;
		
		pickData = new PickData();
		
		Uri photoUri = null;
		if (paramIntent != null)
			photoUri = paramIntent.getData();
		switch (requestCode) {
			// pick photo from album
		case StartForResults.PHOTO_PICKED_WITH_DATA:
			break;
			// pick photo from camera
		case StartForResults.CAMERA_PICKED_WITH_DATA:
			// 获取 拍照临时文件
			File sdDir = Environment.getExternalStorageDirectory();
			String tempPicName = "camera-t.jpg";
			File tempPic = new File(sdDir, tempPicName);
			if(isSave2Sys){
				try {
					//插入系统图库(图片太大容易造成内存溢出)
					photoUri = Uri.parse(MediaStore.Images.Media.insertImage(
							activity.getContentResolver(), tempPic
									.getAbsolutePath(), null, null));
	
					tempPic.delete();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else{
				// get path
				pickData.localPath = tempPic.getAbsolutePath();
				// get title
				pickData.title = new File (pickData.localPath).getName();
				// get size
				//pickData.size = getFileSize(pickData.localPath);
				// get thumbBmp , width , height
				pickData.thumbBmp = getLocalBitmap(tempPic.getAbsolutePath(), 300, pickData);
				// get type
				//TODO  
				pickData.type = 2;
				return pickData;
			}
			break;
		}
		
		// get path
		pickData.localPath = getLocalPath(photoUri, activity);
		// get title
//		pickData.title = new File (pickData.localPath).getName();
		// get size
		//pickData.size = getFileSize(pickData.localPath);
		// get thumbBmp , width , height
//		pickData.thumbBmp = getLocalBitmap(pickData.localPath, 300, pickData);
		// get type
		//TODO  
//		pickData.type = 2;
		return pickData;
	}

	/**
	 * <br>Description: 获取本地图片
	 * <br>Author:caizp
	 * <br>Date:2011-7-12下午06:20:08
	 * @param path
	 * @param size
	 * @param inoutData
	 * @return
	 */
	public static Bitmap getLocalBitmap(String path, int size , PickData inoutData) {
		BitmapFactory.Options options = new BitmapFactory.Options();

		options.outHeight = size;

		options.inJustDecodeBounds = true;

		// 获取这个图片的宽和高
		Bitmap bm = BitmapFactory.decodeFile(path, options); //此时返回bm为空
		
		options.inJustDecodeBounds = false;
		
		if(null != inoutData){
			inoutData.height = options.outHeight;
			inoutData.width = options.outWidth;
		}
		int be = options.outHeight / (int)(size/10);
		if(be%10 !=0) 
			be+=10;

		be=be/10;
		if (be <= 0)
			be = 1;

		options.inSampleSize = be;
		bm = BitmapFactory.decodeFile(path, options);
		
		return bm;
	}
	
	/**
	 * <br>Description: 根据URI获取本地图片路径
	 * <br>Author:caizp
	 * <br>Date:2011-7-12下午06:18:40
	 * @param uri
	 * @param activity
	 * @return
	 */
	private static String getLocalPath(Uri uri, Activity activity) {
		String path = "";
		// 获取文件的绝对路径
		String[] projection = { MediaStore.Images.Media._ID,
				MediaStore.Images.Media.DATA, MediaStore.Images.Media.SIZE };
		Cursor cursor = MediaStore.Images.Media.query(activity
				.getContentResolver(), uri, projection);
		if (cursor != null && cursor.moveToNext()) {
			path = cursor.getString(cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
		}else if(uri.toString().startsWith("file://")){
			path = uri.toString().substring(7);
		}
		return path;
	}
	
}
