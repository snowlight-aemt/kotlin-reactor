package me.snowlight

import mu.KotlinLogging
import reactor.core.publisher.Flux
import reactor.core.scheduler.Schedulers
import java.time.Duration

private val logger = KotlinLogging.logger {}

private val single = Schedulers.newSingle("single")

fun main() {
    // LEARN doOnNext 는 변경이 아닌 흐려가는 데이터를 잡아서 어떤 작업을 하는 것이다.
    // LEARN 배합 관계 : 배합이 달려져서 scheduler 에서 병목이 생긴다. 이를 막기 위해서 나는 느린 곳에서 처리를 해줘 (`publishOn`) ,
    //  반대로 드려오는 것은 느린다. 빠져나서 처리해야 하는 곳은 빠른 경우 `subscribeOn` 를 사용
    //  (IMO: 처리가 오래 걸리는 곳 (BLOCKING) 은 여러 쓰레드 에서 돌려 놓고 그 오래 걸리는 결과가 나오면 빠르게 처리하여 내 보내기 하는 것을 하는 것이 아닐까?)

    Flux.range(1, 12)
        .doOnNext { logger.debug { "1st $it" }}
        .filter {it % 2 == 0}
        .doOnNext { logger.debug { "2nd $it" }}
        .filter {it % 3 == 0}
        .publishOn(single) // 4. single
        .delayElements(Duration.ofMillis(10)) // 2. parallel
        .publishOn(single) // 3. single
        .doOnNext { logger.debug { "3rd $it" }}
        .filter {it % 4 == 0}
        .doOnNext { logger.debug { "4td $it" }}
        .subscribeOn(single) // 1. single // 명시되지 않은 체인에 대해서만 바꿔준다.
        .blockLast()

    /**
     * 21:26:21.919|DEBUG|main|me.snowlight.ReactorPub.invoke|1st 5
     * 21:26:21.919|DEBUG|main|me.snowlight.ReactorPub.invoke|1st 6
     * 21:26:21.919|DEBUG|main|me.snowlight.ReactorPub.invoke|2nd 6
     * 21:26:21.935|DEBUG|parallel-1|me.snowlight.ReactorPub.invoke|3rd 6
     * 21:26:21.935|DEBUG|parallel-1|me.snowlight.ReactorPub.invoke|1st 7
     * 21:26:21.935|DEBUG|parallel-1|me.snowlight.ReactorPub.invoke|1st 8
     */

    /**
     * 21:31:28.395|DEBUG|main|me.snowlight.ReactorPub.invoke|1st 6
     * 21:31:28.395|DEBUG|main|me.snowlight.ReactorPub.invoke|2nd 6
     * 21:31:28.413|DEBUG|single-1|me.snowlight.ReactorPub.invoke|3rd 6
     * 21:31:28.413|DEBUG|single-1|me.snowlight.ReactorPub.invoke|1st 7
     */

    /**
     * 21:32:12.705|DEBUG|main|reactor.util.Loggers.debug|Using Slf4j logging framework
     * 21:32:12.763|DEBUG|single-1|me.snowlight.ReactorPub.invoke|1st 1
     * 21:32:12.764|DEBUG|single-1|me.snowlight.ReactorPub.invoke|1st 2
     * 21:32:12.764|DEBUG|single-1|me.snowlight.ReactorPub.invoke|2nd 2
     * 21:32:12.764|DEBUG|single-1|me.snowlight.ReactorPub.invoke|1st 3
     */
}