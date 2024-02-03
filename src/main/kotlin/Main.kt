package me.snowlight

import mu.KotlinLogging
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

private val logger = KotlinLogging.logger {}

fun main() {

//    Mono.just(1)
//        .map { it + 1 }
//        .doOnNext {
//            logger.debug {">> from publisher -> ${it}"}
//        }.log().subscribe()


    // LEARN Array vs Stream 차이점
    // 1, 3, 5, 7, 9 은 Array 가 아니고 Stream 이다.
//    Flux.just(1, 3, 5, 7, 9).map {it + 1}.log().subscribe()
//    Flux.just(arrayOf(1,2,3,4), arrayOf(7,8,9)).map {it + 1}.log().subscribe()
    // Block
//    Flux.range(1, 10).map {it * it}.log().subscribe()
    // Non Block
//    Flux.range(1, 10).flatMap { Mono.just(it * it) }.log().subscribe() // flatMap

    Mono.just(1).flux().log().subscribe() // 형변환한다.

}

/**
 * Mono 단건 publishing
 * 20:48:33.652|INFO |main|reactor.Mono.PeekFuseable.1.info|| onSubscribe([Fuseable] FluxPeekFuseable.PeekFuseableSubscriber)
 * 20:48:33.653|INFO |main|reactor.Mono.PeekFuseable.1.info|| request(unbounded)
 * 20:48:33.653|DEBUG|main|me.snowlight.Main.invoke|>> from publisher -> 2
 * 20:48:33.654|INFO |main|reactor.Mono.PeekFuseable.1.info|| onNext(2)
 * 20:48:33.654|INFO |main|reactor.Mono.PeekFuseable.1.info|| onComplete()
 */

/**
 * Flux 다건 publishing --> stream
 *
 * 20:49:34.765|INFO |main|reactor.Flux.MapFuseable.1.info|| onSubscribe([Fuseable] FluxMapFuseable.MapFuseableSubscriber)
 * 20:49:34.766|INFO |main|reactor.Flux.MapFuseable.1.info|| request(unbounded)
 * 20:49:34.766|INFO |main|reactor.Flux.MapFuseable.1.info|| onNext(2)
 * 20:49:34.766|INFO |main|reactor.Flux.MapFuseable.1.info|| onNext(4)
 * 20:49:34.766|INFO |main|reactor.Flux.MapFuseable.1.info|| onNext(6)
 * 20:49:34.766|INFO |main|reactor.Flux.MapFuseable.1.info|| onNext(8)
 * 20:49:34.766|INFO |main|reactor.Flux.MapFuseable.1.info|| onNext(10)
 * 20:49:34.766|INFO |main|reactor.Flux.MapFuseable.1.info|| onComplete()
 */
