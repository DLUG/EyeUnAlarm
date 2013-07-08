package org.dlug.android.eyeunalarm.alarm;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.content.Context;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.ImageView;

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback, Camera.PreviewCallback{
	private int maxWidth;
	private int maxHeight;
	private int previewWidth;
	private int previewHeight;
	private int format;
	
	Bitmap bm;
	
	ImageView modifyImage;

	int judgement = 0;
	
	boolean isPreviewRunning = false;

//	private ImageView ivCam;

	private SurfaceHolder mHolder;
	private Camera mCamera;

	private byte[] previewData;
	int[] result = null;
	
	int recogValue[] = new int[2];
	
	int judgement_thresold;
	
	AlarmPlayImpl parent;

	@SuppressWarnings("deprecation")
	CameraPreview(Context context, int maxWidth, int maxHeight, AlarmPlayImpl parent, int judgement_thresold) throws RuntimeException, NullPointerException{
		super(context);

		System.loadLibrary("DetectEye");
		
		this.judgement_thresold = judgement_thresold;
		this.parent = parent;
		
		this.maxWidth = maxWidth;
		this.maxHeight = maxHeight;
		
		mHolder = getHolder();
		mHolder.addCallback(this);
		mHolder.setType(SurfaceHolder.SURFACE_TYPE_NORMAL);
		setWillNotDraw(false);
		
		
	}

	public void start(){
		mCamera.startPreview();
		Log.d("Camera", "Started");
	}
	
// Pausing	
	public void pause(){
		
	}

	public native void ObjectRecog(int width, int height, byte yuv[], int rgba[], int[] value);

	public void setModifyView(ImageView modifyImage) {
		this.modifyImage = modifyImage;
	}

	@Override
	public void onPreviewFrame(byte[] _data, Camera camera) {
		if(result == null){
			result = new int[_data.length];
		}
		
		recogValue[0] = 0;
		recogValue[1] = 0;
		ObjectRecog(previewWidth, previewHeight, _data, result, recogValue);
		
		if(recogValue[0] == 1){
			judgement++;
			parent.setProgressBar(judgement);
		}
			
		
		if(judgement > judgement_thresold){
			parent.pass();
		}
		
		
		if(bm != null)
			bm.recycle();
		bm = Bitmap.createBitmap(previewHeight, previewWidth, Bitmap.Config.ARGB_8888);
		bm.setPixels(result, 0, previewHeight, 0, 0, previewHeight, previewWidth);
		

		
		
//		modifyImage.setImageBitmap(bm);
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		
		mCamera.stopPreview();
		
		Log.i("Still", "surfaceCreated(SurfaceHolder holder) ");
		
		mCamera.setDisplayOrientation(90);	// Rotate Camera

		  

		Camera.Parameters parameters = mCamera.getParameters();
		parameters.set("orientation", "landscape");
		format = parameters.getPreviewFormat();

//			boolean supportResolution = false;


		List<Camera.Size> sizes = parameters.getSupportedPreviewSizes();
		List<Camera.Size> supportsizes = parameters.getSupportedPictureSizes();

		previewWidth = 0;
		previewHeight = 0;

// Pick Preview Resolution
		for (Camera.Size size : sizes){
			Log.i("Still","preview size.height>"+size.height+"/size.width"+size.width );
			if(size.width <= maxWidth && size.height <= maxHeight){
				previewWidth = size.width;
				previewHeight = size.height;
				break;
			}
		}
		Log.i("Still","Using size.width>" + previewWidth + "/size.height" + previewHeight);
		parameters.setPreviewSize(previewWidth, previewHeight);

		
// Pick Picture Resolution (Not use, but Need)	
		int pictureWidth = 0;
		int pictureHeight = 0;
		for(Camera.Size size : supportsizes){
			Log.i("Still","Picture size.width>" + size.width + "/size.height" + size.height );
			if(size.width <= maxWidth && size.height <= maxHeight){
				pictureWidth = size.width;
				pictureHeight = size.height;

				break;
			}
		}

		Log.i("Still","Picture size.width>" + pictureWidth + "/size.height" + pictureHeight);
		parameters.setPictureSize(pictureWidth, pictureHeight);
		mCamera.setParameters(parameters);
		
		try {
			mCamera.setPreviewDisplay(getHolder());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		mCamera.setPreviewCallback(this);
		mCamera.startPreview();
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		int useCameraId = 99;
		
		this.setWillNotDraw(false);
		
		Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
		int cameraCount = Camera.getNumberOfCameras();
		
		for(int i = 0; i < cameraCount; i++){
			Camera.getCameraInfo( i, cameraInfo );
			if ( cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            	useCameraId = i;
			}
		}
		if(useCameraId == 99)
			mCamera = Camera.open();
		else
			mCamera = Camera.open(useCameraId);
		
		isPreviewRunning = true;
		
		try{
            mCamera.setPreviewCallback(this);
        }catch(Exception e){
            android.util.Log.e("", e.getMessage());
        }
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		mCamera.setPreviewCallback(null);
		mCamera.stopPreview();
		mCamera.release();
		
		isPreviewRunning = false;
	}
	
	@Override //from SurfaceView
	public void onDraw(Canvas canvas) {
		if(canvas != null && bm != null){
			int viewWidth = this.getWidth();
			int viewHeight = this.getHeight();
			
			canvas.drawBitmap(bm , null, new RectF(0, 0, viewWidth, viewHeight), new Paint());
		    invalidate();
		}
	}
}