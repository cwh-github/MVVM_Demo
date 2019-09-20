package com.example.mvvm_jetpack_lib.utils.exception.core

import com.example.mvvm_jetpack_lib.utils.exception.retry.ObservableRetry
import com.example.mvvm_jetpack_lib.utils.exception.retry.RetryConfig
import io.reactivex.*
import org.reactivestreams.Publisher
import java.util.concurrent.TimeUnit

/**
 * OnNextInterceptor 主要是对发射的数据进行一次处理，让后再发射处理
 *
 * 类似于okhttp 中的拦截器的功能
 */
typealias OnNextInterceptor<T> = (T) -> Observable<T>

/**
 * 对于出现错误时进行处理，然后接着向下游发送
 */
typealias OnErrorResumeNext<T> = (Throwable) -> Observable<T>

/**
 * 重试策略
 *
 * RetryConfig 指定了重试次数   重试 delay  Single<Boolean> 决定是否重试
 */
typealias OnErrorRetryOpe = (Throwable) -> RetryConfig

/**
 * 最后在OnError中执行，出现错误的操作
 */
typealias OnErrorOpe = (Throwable) -> Unit

/**
 * @param onNextInterceptor 指定发射数据的拦截器操作，默认不做操作，直接将数据往下发送
 *
 * @param onErrorResumeNext 指定当发生错误时，如何处理数据后将其发送到下游，默认直接发送错误到下游
 *
 * @param onErrorRetryOpe 指定当发生错误时，是否进行重试的操作，默认不进行重试操作
 *
 * @param onErrorOpe 默认不做操作
 *
 * 实现相关的compose操作符需要调用的Transformer接口，达到所有数据源都可以使用的作用
 *
 * 创建该类的实例，实现相关参数，从而实现相关操作
 *
 *
 * Description:RxJava 中全局的处理Error的关键类
 *
 * 参考大佬：https://github.com/qingmei2/RxWeaver
 *
 * 主要用compose()操作符，对error进行转换为一个observable，
 * 在其中通过onErrorResumeNext()对Error进行处理，如：获取到Token 过期，跳转到登录界面
 * retryWhen() 决定是否进行重试操作
 *
 * onError() 做一些最终的处理，如：弹出Toast,
 *
 * Date：2019/7/16-15:44
 * Author: cwh
 *
 */
class GlobalErrorTransformer<T>(
    private val onNextInterceptor: OnNextInterceptor<T> = { Observable.just(it) },
    private val onErrorResumeNext: OnErrorResumeNext<T> = { Observable.error(it) },
    private val onErrorRetryOpe: OnErrorRetryOpe = { RetryConfig.none() },
    private val onErrorOpe: OnErrorOpe = {}
) : ObservableTransformer<T, T>, FlowableTransformer<T, T>, SingleTransformer<T, T>,
    MaybeTransformer<T, T>, CompletableTransformer {



    var mRetryCount = 0

    override fun apply(upstream: Observable<T>): ObservableSource<T> {
        return upstream.flatMap {
            onNextInterceptor(it)
        }.onErrorResumeNext { throwable: Throwable ->
            onErrorResumeNext(throwable)
        }.retryWhen { throwOld ->
            throwOld.flatMap {
                val config = onErrorRetryOpe(it)
                if (++mRetryCount <= config.maxTime) {
                    config.isRetry().flatMapObservable { retry ->
                        if (retry) {
                            Observable.timer(config.delay, TimeUnit.MILLISECONDS)
                        } else {
                            Observable.error<Any>(it)
                        }
                    }
                } else {
                    Observable.error<Any>(it)
                }
            }

        }.doOnError {
            onErrorOpe(it)
        }
    }

    override fun apply(upstream: Flowable<T>): Publisher<T> {
        return upstream.flatMap {
            onNextInterceptor(it).toFlowable(BackpressureStrategy.BUFFER)
        }.onErrorResumeNext { throwable: Throwable ->
            onErrorResumeNext(throwable).toFlowable(BackpressureStrategy.BUFFER)
        }.retryWhen { throwOld ->
            throwOld.flatMap {
                val config = onErrorRetryOpe(it)
                if (++mRetryCount <= config.maxTime) {
                    config.isRetry().flatMapPublisher { retry ->
                        if (retry) {
                            Flowable.timer(config.delay, TimeUnit.MILLISECONDS)
                        } else {
                            Flowable.error<Any>(it)
                        }
                    }
                } else {
                    Flowable.error<Any>(it)
                }
            }

        }.doOnError {
            onErrorOpe(it)
        }
    }

    override fun apply(upstream: Single<T>): SingleSource<T> {
        return upstream.flatMap {
            onNextInterceptor(it).firstOrError()
        }.onErrorResumeNext { throwable: Throwable ->
            onErrorResumeNext(throwable).firstOrError()
        }.retryWhen { throwOld ->
            throwOld.flatMap {
                val config = onErrorRetryOpe(it)
                if (++mRetryCount <= config.maxTime) {
                    config.isRetry().flatMapPublisher { retry ->
                        if (retry) {
                            Flowable.timer(config.delay, TimeUnit.MILLISECONDS)
                        } else {
                            Flowable.error<Any>(it)
                        }
                    }
                } else {
                    Flowable.error<Any>(it)
                }
            }
        }.doOnError {
            onErrorOpe(it)
        }
    }

    override fun apply(upstream: Maybe<T>): MaybeSource<T> {
        return upstream.flatMap {
            onNextInterceptor(it).firstElement()
        }.onErrorResumeNext { throwable: Throwable ->
            onErrorResumeNext(throwable).firstElement()
        }.retryWhen { throwOld ->
            throwOld.flatMap {
                val config = onErrorRetryOpe(it)
                if (++mRetryCount <= config.maxTime) {
                    config.isRetry().flatMapPublisher { retry ->
                        if (retry) {
                            Flowable.timer(config.delay, TimeUnit.MILLISECONDS)
                        } else {
                            Flowable.error<Any>(it)
                        }
                    }
                } else {
                    Flowable.error<Any>(it)
                }
            }

        }.doOnError {
            onErrorOpe(it)
        }
    }

    override fun apply(upstream: Completable): CompletableSource {
        return upstream.onErrorResumeNext { throwable: Throwable ->
            onErrorResumeNext(throwable).ignoreElements()
        }.retryWhen { throwOld ->
            throwOld.flatMap {
                val config = onErrorRetryOpe(it)
                if (++mRetryCount <= config.maxTime) {
                    config.isRetry().flatMapPublisher { retry ->
                        if (retry) {
                            Flowable.timer(config.delay, TimeUnit.MILLISECONDS)
                        } else {
                            Flowable.error<Any>(it)
                        }
                    }
                } else {
                    Flowable.error<Any>(it)
                }
            }

        }.doOnError {
            onErrorOpe(it)
        }
    }


}