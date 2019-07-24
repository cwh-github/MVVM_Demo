package com.example.mvvm_jetpack_lib.utils.exception.retry

import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.functions.Function
import java.util.concurrent.TimeUnit

/**
 * Description: 执行retrywhen时执行的函数Function
 * Date：2019/7/16-16:55
 * Author: cwh
 */
class ObservableRetry(private val config: RetryConfig):
    Function<Observable<Throwable>, ObservableSource<*>>{

    private var mRetryCount=0

    override fun apply(throwableOld: Observable<Throwable>): ObservableSource<*> {
        return throwableOld.flatMap {
            if(++mRetryCount<=config.maxTime){
                config.isRetry().flatMapObservable {retry->
                    if(retry){
                        Observable.timer(config.delay,TimeUnit.MILLISECONDS)
                    }else{
                        Observable.error<Any>(it)
                    }
                }
            }else{
               Observable.error<Any>(it)
            }
        }

    }

}