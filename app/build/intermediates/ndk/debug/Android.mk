LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)

LOCAL_MODULE := bindings
LOCAL_LDFLAGS := -Wl,--build-id
LOCAL_SRC_FILES := \
	D:\Arquivos_Programacao\NUPASD\AppCompare-master\app\src\main\jni\Android.mk \
	D:\Arquivos_Programacao\NUPASD\AppCompare-master\app\src\main\jni\bindings.cpp \

LOCAL_C_INCLUDES += D:\Arquivos_Programacao\NUPASD\AppCompare-master\app\src\main\jni
LOCAL_C_INCLUDES += D:\Arquivos_Programacao\NUPASD\AppCompare-master\app\src\debug\jni

include $(BUILD_SHARED_LIBRARY)
