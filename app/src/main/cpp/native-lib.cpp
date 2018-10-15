#include <jni.h>
#include <string>
#include <iostream>
#include "ServerH.h"



extern "C" JNIEXPORT jstring JNICALL
Java_com_example_ahmed_secondndk_MainActivity_stringFromJNI(
        JNIEnv *env,
        jobject /*jstring cString*/) {

   /* const char *path = env->GetStringUTFChars(cString, NULL);

    server();
    std::string rstring;
    client(cString,rstring);
    std::string hello = rstring;
    return env->NewStringUTF(hello.c_str());*/
    std::cout << "Hello from C++ !!" << std::endl;
}
