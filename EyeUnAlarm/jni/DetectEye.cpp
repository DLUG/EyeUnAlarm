#include <jni.h>
#include <android/log.h>
#include <opencv2/core/core.hpp>
#include <opencv2/imgproc/imgproc.hpp>
#include <opencv2/objdetect/objdetect.hpp>

#include <opencv2/features2d/features2d.hpp>
#include <vector>

#define Logi(...) ((void)__android_log_print(ANDROID_LOG_INFO, "jni", __VA_ARGS__))
#define Loge(...) ((void)__android_log_print(ANDROID_LOG_ERROR, "jni", __VA_ARGS__))

using namespace std;
using namespace cv;

extern "C" {
JNIEXPORT void JNICALL Java_org_dlug_android_eyeunalarm_alarm_CameraPreview_ObjectRecog(JNIEnv* env, jobject, jint width, jint height, jbyteArray yuv, jintArray bgra, jintArray recogValue)
{
    jbyte* _yuv  = env->GetByteArrayElements(yuv, 0);
    jint*  _bgra = env->GetIntArrayElements(bgra, 0);
    jint*  _recogValue = env->GetIntArrayElements(recogValue, 0);

    Mat myuv(height + height/2, width, CV_8UC1, (unsigned char *)_yuv);
    Mat mbgra(height, width, CV_8UC4, (unsigned char *)_bgra);
//    Mat mgray(height, width, CV_8UC1, (unsigned char *)_yuv);

    //Please make attention about BGRA byte order
    //ARGB stored in java as int array becomes BGRA at native level
    cvtColor(myuv, mbgra, CV_YUV420sp2BGR, 4);

 //Start ===============================================
    int i = 0;
	double t = 0;
	int scale = 3;


	vector<Rect> faces;
	const static Scalar colors[] =  { CV_RGB(0,0,255),
		CV_RGB(0,128,255),
		CV_RGB(0,255,255),
		CV_RGB(0,255,0),
		CV_RGB(255,128,0),
		CV_RGB(255,255,0),
		CV_RGB(255,0,0),
		CV_RGB(255,0,255)} ;

	Mat gray, smallImg( cvRound (mbgra.rows/scale), cvRound(mbgra.cols/scale), CV_8UC1 );

	CascadeClassifier cascade;

	if(!cascade.load("/data/data/org.dlug.android.eyeunalarm/files/eyes.xml")){
			Loge("ERROR: Could not load classifier cascade");
			return;
	}

	cvtColor( mbgra, gray, CV_BGR2GRAY );
	resize( gray, smallImg, smallImg.size(), 0, 0, INTER_LINEAR );
//	equalizeHist( smallImg, smallImg );

	cascade.detectMultiScale( smallImg, faces,
		1.1, 2, 0
		//|CV_HAAR_FIND_BIGGEST_OBJECT
		//|CV_HAAR_DO_ROUGH_SEARCH
		|CV_HAAR_SCALE_IMAGE
		,
		Size(30, 30) );

	int judge = 0;
	for( vector<Rect>::const_iterator r = faces.begin(); r != faces.end(); r++, i++ )
	{
		judge++;
		Mat smallImgROI;
		vector<Rect> nestedObjects;
		Point center;
		Scalar color = colors[i%8];
		int radius;
		center.x = cvRound((r->x + r->width*0.5)*scale);
		center.y = cvRound((r->y + r->height*0.5)*scale);
		radius = cvRound((r->width + r->height)*0.25*scale);
		circle( mbgra, center, radius, color, 3, 8, 0 );
 //End  ===============================================
	}

	if(judge > 0)
		_recogValue[0] = 1;

    env->ReleaseIntArrayElements(bgra, _bgra, 0);
    env->ReleaseByteArrayElements(yuv, _yuv, 0);
    env->ReleaseIntArrayElements(recogValue, _recogValue, 0);
}
}
