LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)
include ../OpenCV-android-sdk/sdk/native/jni/OpenCV.mk

LOCAL_MODULE    := DetectEye
LOCAL_SRC_FILES := DetectEye.cpp
LOCAL_LDLIBS +=  -llog -ldl

include $(BUILD_SHARED_LIBRARY)
