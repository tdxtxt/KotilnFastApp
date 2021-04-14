package com.baselib.helper.eventbus

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

/**
 * 功能描述:
 * @author tangdexiang
 * @since 2021/4/13
 */
object EventBus {
    private val publisher = PublishSubject.create<Any>()

    fun publish(event: Any) {
        publisher.onNext(event)
    }

    // Listen should return an Observable and not the publisher
    // Using ofType we filter only events that match that class type
    fun <T> listen(eventType: Class<T>): Observable<T> = publisher.ofType(eventType)
}