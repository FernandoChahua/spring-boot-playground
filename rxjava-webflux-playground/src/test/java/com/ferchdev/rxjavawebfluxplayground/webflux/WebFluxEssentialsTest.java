package com.ferchdev.rxjavawebfluxplayground.webflux;


import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

public class WebFluxEssentialsTest {

    @Test
    public void testResolveSingleString() {
        Mono<String> mono = WebFluxEssentials.resolveSingleString("Hello");

        StepVerifier.create(mono)
                .expectNext("Hello")
                .verifyComplete();
    }

    @Test
    public void testMergeSinglesFromSingleListWithError() {
        List<Mono<Object>> monos = Arrays.asList(
                Mono.just((Object)"Hello").delayElement(Duration.ofSeconds(2)),
                Mono.error(new RuntimeException("Error from test")));

        StepVerifier.create(WebFluxEssentials.mergeSinglesFromSingleListWithError(monos))
                .verifyError(RuntimeException.class);
    }

    @Test
    public void testMergeAllSinglesEvenWithError() {
        List<Mono<Object>> monos = Arrays.asList(
                Mono.just("Hello"),
                Mono.error(new RuntimeException("Error from test"))
        );

        StepVerifier.create(WebFluxEssentials.mergeAllSinglesEvenWithError(monos))
                .expectNext("Hello")
                .verifyError(RuntimeException.class);
    }

    @Test
    public void testMergeSinglesFromStringList() {
        List<String> strs = Arrays.asList("Hello", "World");

        StepVerifier.create(WebFluxEssentials.mergeSinglesFromStringList(strs))
                .expectNext("Hello", "World")
                .verifyComplete();
    }

    @Test
    public void testResolveMaybeString() {
        Mono<String> mono = WebFluxEssentials.resolveMaybeString("Hello");

        StepVerifier.create(mono)
                .expectNext("Hello")
                .verifyComplete();
    }

    @Test
    public void testResolveObservableString() {
        Flux<String> flux = WebFluxEssentials.resolveObservableString("Hello", "World");

        StepVerifier.create(flux)
                .expectNext("Hello", "World")
                .verifyComplete();
    }

    @Test
    public void testResolveCompletableString() {
        Mono<Void> mono = WebFluxEssentials.resolveCompletableString("Hello");

        StepVerifier.create(mono)
                .verifyComplete();
    }
}
