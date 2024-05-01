package com.ferchdev.rxjavawebfluxplayground.webflux;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;


@Slf4j
public class WebFluxEssentials {

    public static Mono<String> resolveSingleString(String str) {
        return Mono.just(str);
    }

    public static Mono<String> resolveMaybeString(String str) {
        return Mono.justOrEmpty(str);
    }

    public static Flux<String> resolveObservableString(String... strs) {
        return Flux.just(strs);
    }

    public static Mono<Void> resolveCompletableString(String str) {
        return Mono.just(str).then();
    }

    public static Flux<String> mergeSinglesFromStringList(List<String> strs) {
        return Flux.fromIterable(strs)
                .doOnNext(str -> System.out.println("Merged Single Success " + str))
                .doOnComplete(() -> System.out.println("Merged Single Complete"));
    }

    public static Flux<Object> mergeSinglesFromSingleListWithError(List<Mono<Object>> monos) {
        return Flux.merge(234, monos.toArray(Mono[]::new))
                .doOnNext(str -> log.info("Merged Single Success -> {}", str))
                .doOnError(e -> log.error("Error ocurred", e));
    }

    public static Flux<Object> mergeAllSinglesEvenWithError(List<Mono<Object>> monos) {
        return Flux.mergeDelayError(234, monos.toArray(Mono[]::new))
                .doOnNext(str -> log.info("Merged Single Success -> {}", str))
                .doOnError(e -> log.error("Error ocurred", e));
    }
}