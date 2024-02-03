package me.snowlight

import mu.KotlinLogging
import reactor.core.publisher.Mono

private val logger = KotlinLogging.logger {}

// 체인이 아닌 경우
//fun getRequest(): Mono<Int> {
//    return Mono.just(1)
//}
//fun subA(request: Mono<Int>):Mono<Int> {
//    return request.map {it + 1}
//}
//fun subB(mono: Mono<Int>):Mono<Int> {
//    return mono.map { it + 2 }
//}

// 체인을 사용하는 경우 파라미터가 Mono 일 필요가 없다.
// Reactor 세상에서는 대부분 반환 타입이 Mono , Flux 이다
fun getRequest(): Mono<Int> {
    return Mono.just(1)
}
fun subA(request: Int):Mono<Int> {
    return Mono.fromCallable {request + 1}
}
fun subB(param: Int):Mono<Int> {
    return Mono.fromCallable {param + 2}
}

fun main() {
//    val request = getRequest().doOnNext { logger.debug { ">> request: ${it}" } }
//    val resA = subA(request).doOnNext { logger.debug { ">> request: ${it}" } }
//    val resB = subB(resA).doOnNext { logger.debug { ">> request: ${it}" } }
//    resB.subscribe()

    getRequest().doOnNext { logger.debug { ">> request: ${it}" } }
        .flatMap { subA(it) }
        .doOnNext { logger.debug { ">> request: ${it}" } }
        .flatMap { subB(it) }
        .doOnNext { logger.debug { ">> request: ${it}" } }
        .subscribe()

}