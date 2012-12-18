package org.dlug.android.eyeunalarm.alarm;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.content.Context;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.ImageView;

public class CameraPreview extends SurfaceView{
	private int maxWidth;
	private int maxHeight;
	private int previewWidth;
	private int previewHeight;
	private int format;
	
	ImageView modifyImage;

	int judgement = 0;
	
	private boolean trigger = true;

//	private ImageView ivCam;

//	private SurfaceHolder mHolder;
	private Camera mCamera;

	private byte[] previewData;
	int[] result = null;
	
	int recogValue[] = new int[2];
	
	int judgement_thresold;
	
	AlarmPlay parent;

	@SuppressWarnings("deprecation")
	CameraPreview(Context context, int maxWidth, int maxHeight, AlarmPlay parent, int judgement_thresold) throws RuntimeException, NullPointerException{
		super(context);

		System.loadLibrary("DetectEye");
		
		this.judgement_thresold = judgement_thresold;
		this.parent = parent;
		
//		mHolder = getHolder();
//		mHolder.addCallback(this);
//		mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		this.maxWidth = maxWidth;
		this.maxHeight = maxHeight;
		
		int useCameraId = 99;
		
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
		
		setPreviewEvent();
	}

// Take Picture (Not Use) ================================
/*
	@Deprecated
	public boolean capture(Camera.PictureCallback jpegHandler){
		if(mCamera != null){
			mCamera.takePicture(null, null, jpegHandler);
			return true;
		}else{
			return false;
		}		
	}
*/

	public void start(){
		mCamera.startPreview();
	}
	
// Pausing	
	public void pause(){
		trigger = false;
		mCamera.stopPreview();
		mCamera.setPreviewCallback(null);
		mCamera.release();
	}

// PreviewEvent for Capture
	public void setPreviewEvent(){
		trigger = true;
		this.mCamera.setPreviewCallback(new PreviewCallback(){
			public void onPreviewFrame(byte[] _data, Camera _camera) {
				if(result == null)
					result = new int[_data.length];
//				_data = new byte[_data.length];
//				if(trigger){
//					previewData = _data;
//				}
				recogValue[0] = 0;
				recogValue[1] = 0;
				ObjectRecog(previewWidth, previewHeight, _data, result, recogValue);
				
				if(recogValue[0] == 1){
					judgement++;
					parent.barRecogEye.setProgress(judgement);
				}
					
				
				if(judgement > judgement_thresold){
					parent.pass();
				}
				
				
				Bitmap bm = Bitmap.createBitmap(previewHeight, previewWidth, Bitmap.Config.ARGB_8888);
				bm.setPixels(result, 0/* offset */, previewHeight /* stride */, 0, 0, previewHeight, previewWidth);

				modifyImage.setImageBitmap(bm);
				
			}
		});
	}

	
	public native void ObjectRecog(int width, int height, byte yuv[], int[] rgba, int[] value);

	public void setModifyView(ImageView modifyImage) {
		this.modifyImage = modifyImage;
	}
}