/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class dnnUtil_dnnModel_DnnModel */

#ifndef _Included_dnnUtil_dnnModel_DnnModel
#define _Included_dnnUtil_dnnModel_DnnModel
#ifdef __cplusplus
extern "C" {
#endif
#undef dnnUtil_dnnModel_DnnModel_serialVersionUID
#define dnnUtil_dnnModel_DnnModel_serialVersionUID 1LL
/*
 * Class:     dnnUtil_dnnModel_DnnModel
 * Method:    jniCreateModel
 * Signature: ()[B
 */
JNIEXPORT jbyteArray JNICALL Java_dnnUtil_dnnModel_DnnModel_jniCreateModel
  (JNIEnv *, jobject);

/*
 * Class:     dnnUtil_dnnModel_DnnModel
 * Method:    jniUpdateModel
 * Signature: ()[B
 */
JNIEXPORT jbyteArray JNICALL Java_dnnUtil_dnnModel_DnnModel_jniUpdateModel
  (JNIEnv *, jobject);

/*
 * Class:     dnnUtil_dnnModel_DnnModel
 * Method:    jniLoadModel
 * Signature: ([B)V
 */
JNIEXPORT void JNICALL Java_dnnUtil_dnnModel_DnnModel_jniLoadModel
  (JNIEnv *, jobject, jbyteArray);

/*
 * Class:     dnnUtil_dnnModel_DnnModel
 * Method:    jniTrainModel
 * Signature: ()[B
 */
JNIEXPORT jbyteArray JNICALL Java_dnnUtil_dnnModel_DnnModel_jniTrainModel
  (JNIEnv *, jobject);

/*
 * Class:     dnnUtil_dnnModel_DnnModel
 * Method:    jniLoadTrainingData
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_dnnUtil_dnnModel_DnnModel_jniLoadTrainingData
  (JNIEnv *, jobject);

/*
 * Class:     dnnUtil_dnnModel_DnnModel
 * Method:    jniGetTraingData
 * Signature: (II)V
 */
JNIEXPORT void JNICALL Java_dnnUtil_dnnModel_DnnModel_jniGetTraingData
  (JNIEnv *, jobject, jint, jint);

#ifdef __cplusplus
}
#endif
#endif
