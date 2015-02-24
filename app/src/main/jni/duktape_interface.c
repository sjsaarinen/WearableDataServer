#include <string.h>
#include <jni.h>
#include "duktape.h"

 jint JNI_OnLoad(JavaVM* vm, void* reserved) {
     return JNI_VERSION_1_6;
 }

jstring
Java_com_ohtu_wearable_wearabledataservice_javascript_JavascriptEvaluator_eval( JNIEnv* env,
                                                  jobject thiz, jstring script )
{
    const char *cScript = (*env)->GetStringUTFChars(env, script, NULL);
    if (NULL == cScript) return NULL;
    duk_context *ctx = duk_create_heap_default();
    duk_eval_string(ctx, cScript);
    char returnStr[255];
    if (duk_get_type(ctx, -1) == DUK_TYPE_NUMBER) {
       sprintf(returnStr, "%lf", (double)duk_get_number(ctx, -1));
    }
    if (duk_get_type(ctx, -1) == DUK_TYPE_STRING) {
       sprintf(returnStr, "%s", (char*) duk_get_string(ctx, -1));
    }
    duk_destroy_heap(ctx);
    return (*env)->NewStringUTF(env, returnStr);
}
