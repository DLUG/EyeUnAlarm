package org.dlug.android.facealarm;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
	private int maxWidth;
	private int maxHeight;
	private int jpegQuality;
	private int previewWidth;
	private int previewHeight;
	private int format;
	
	ImageView modifyImage;

	int judgement = 0;
	
	private boolean trigger = true;

//	private ImageView ivCam;

	private SurfaceHolder mHolder;
	private Camera mCamera;

	private byte[] previewData;
	int[] result = null;
	
	int recogValue[] = new int[2];
	
	AlarmPlay parent;

	CameraPreview(Context context, int maxWidth, int maxHeight, int jpegQuality, AlarmPlay parent) {
		super(context);

		this.parent = parent;
		
		mHolder = getHolder();
		mHolder.addCallback(this);
		mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		this.maxWidth = maxWidth;
		this.maxHeight = maxHeight;
		this.jpegQuality = jpegQuality;
	}

	public void surfaceCreated(SurfaceHolder holder) {
//		mCamera = Camera.open();
//		mCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT);
		
		int useCameraId = 99;
		
		Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
		int cameraCount = Camera.getNumberOfCameras();
		
		for(int i = 0; i < cameraCount; i++){
			Camera.getCameraInfo( i, cameraInfo );
			if ( cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT  ) {
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

		try {
			mCamera.setPreviewDisplay(holder);
			setPreviewEvent();
		} catch (IOException exception) {
			mCamera.release();
			mCamera = null;
			// TODO: add more exception handling logic here
		}
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		mCamera.stopPreview();
		mCamera.release();
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
		if(trigger){
//			boolean supportResolution = false;

			Camera.Parameters parameters = mCamera.getParameters();

			Log.i("Still","int w:"+w+"/int h:"+h);
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
			Log.i("Still","preview size.width>" + previewWidth + "/size.height" + previewHeight);
			parameters.setPreviewSize(previewWidth, previewHeight);

			
// Pick Picture Resolution (Not use)	
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
			mCamera.startPreview();
		}
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

// Capture Picture from Preview Image	
	public byte[] videoCapture(){
		YuvImage image = new YuvImage(previewData, format, previewWidth, previewHeight, null);

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		Rect area = new Rect(0, 0, previewWidth, previewHeight);
		image.compressToJpeg(area, jpegQuality, out);

		return out.toByteArray();
	}

// Pausing	
	public void pause(){
		trigger = false;
		this.mCamera.setPreviewCallback(null);
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
				
				if(recogValue[0] == 1)
					judgement++;
					
				if(judgement > 20){
					parent.ok();
				}
				
//				Bitmap bm = BitmapFactory.decodeByteArray(byte_src, 0, byte_src.length);
//				Bitmap bm = BitmapFactory.decodeByteArray(result2, 0, result2.length);
				Bitmap bm = Bitmap.createBitmap(previewWidth, previewHeight, Bitmap.Config.ARGB_8888);
				bm.setPixels(result, 0/* offset */, previewWidth /* stride */, 0, 0, previewWidth, previewHeight);
				
				modifyImage.setImageBitmap(bm);
				
			}
		});
	}

	/*	TODO: Get Image from Preview Video
	public void onPreviewFrame(byte[] data, Camera camera, String fileName) 
	{


	    Camera.Parameters parameters = camera.getParameters();
	    int imageFormat = parameters.getPreviewFormat();
	    if (imageFormat == ImageFormat.NV21)
	    {
	        Rect rect = new Rect(0, 0, IMAGE_WIDTH, IMAGE_HEIGHT); 
	        YuvImage img = new YuvImage(data, ImageFormat.NV21, IMAGE_WIDTH, IMAGE_HEIGHT, null);
	        OutputStream outStream = null;
	        File file = new File(fileName);
	        try 
	        {
	            outStream = new FileOutputStream(file);
	            img.compressToJpeg(rect, 100, outStream);
	            outStream.flush();
	            outStream.close();
	        } 
	        catch (FileNotFoundException e) 
	        {
	            e.printStackTrace();
	        }
	        catch (IOException e) 
	        {
	            e.printStackTrace();
	        }
	    }
	}
	 */


	/*
	 	private Size getOptimalPreviewSize(List<Size> sizes, int w, int h) {
		final double ASPECT_TOLERANCE = 0.05;
		double targetRatio = (double) w / h;
		if (sizes == null) return null;

		Size optimalSize = null;
		double minDiff = Double.MAX_VALUE;

		int targetHeight = h;

		// Try to find an size match aspect ratio and size
		for (Size size : sizes) {
			double ratio = (double) size.width / size.height;
			if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) continue;
			if (Math.abs(size.height - targetHeight) < minDiff) {
				optimalSize = size;
				minDiff = Math.abs(size.height - targetHeight);
			}
		}

		// Cannot find the one match the aspect ratio, ignore the requirement
		if (optimalSize == null) {
			minDiff = Double.MAX_VALUE;
			for (Size size : sizes) {
				if (Math.abs(size.height - targetHeight) < minDiff) {
					optimalSize = size;
					minDiff = Math.abs(size.height - targetHeight);
				}
			}
		}
		return optimalSize;
	}
	 */
	
	public native void ObjectRecog(int width, int height, byte yuv[], int[] rgba, int[] value);

	public void setModifyView(ImageView modifyImage) {
		this.modifyImage = modifyImage;
	}
}