import okhttp3.OkHttpClient
import okhttp3.Interceptor

fun provideOkHttpClient(): OkHttpClient {
    return OkHttpClient.Builder()
        .addInterceptor { chain ->
            val originalRequest = chain.request()
            val compressedRequest = originalRequest.newBuilder()
                .header("Accept-Encoding", "gzip")
                .build()
            chain.proceed(compressedRequest)
        }
        .build()
}