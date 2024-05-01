package com.ferchdev.rxjavawebfluxplayground.rxjava;
import io.reactivex.rxjava3.core.*;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class RxJavaEssentials {

    public static Single<String> resolveSingleString(String greeting) {
        return Single.just(greeting)
                .doOnSuccess(str1 -> log.info("Single Success -> {}", str1));
    }

    public static Maybe<String> resolveMaybeString(String greeting) {
        return Maybe.just(greeting)
                .doOnSuccess(str1 -> log.info("Maybe Success -> {}", str1));
    }

    public static Completable resolveCompletableString(String greeting) {
        return Completable.fromSingle(Single.just(greeting))
                .doOnComplete(() -> log.info("Completable Success"));
    }

    public static Observable<String> resolveObservableString(String ...greetings) {
        return Observable.fromArray(greetings)
                .doOnNext(str -> log.info("Observable Success -> {}",  str));
    }

    public static Single<String> mergeSinglesFromStringList(List<String> stringList) {
        List<Single<String>> singles = stringList.stream()
                .map(Single::just)
                .collect(Collectors.toList());

        return Single.zip(singles, results -> Arrays.stream(results)
                .map(Object::toString)
                .collect(Collectors.joining(", ")))
                .doOnSuccess(str -> log.info("Merged Single Success -> {}", str))
                .doOnError(throwable -> log.error("Merged Single Error -> {}", throwable.getMessage()));
    }

    public static Single<String> mergeSinglesFromSingleListWithError(List<Single<String>> singles) {
        return Single.zip(singles, results -> Arrays.stream(results)
                        .map(Object::toString)
                        .collect(Collectors.joining(", ")))
                .doOnSuccess(str -> log.info("Merged Single Success -> {}", str))
                .doOnError(throwable -> log.error("Merged Single Error ", throwable));
    }

    public static Flowable<Object> mergeAllSinglesEvenWithError(List<Single<Object>> singles) {
        return Flowable.range(0, singles.size())
                .flatMap(index -> singles.get(index)
                        .toObservable()
                        .toFlowable(BackpressureStrategy.BUFFER)
                        .onErrorReturn(throwable -> {
                            log.error("Error in Single at index " + index, throwable);
                            return new RuntimeException("Error", throwable);
                        }), true)
                .doOnNext(str -> log.info("Merged Single Success -> {}", str))
                .doOnError(throwable -> log.error("Merged Single Error ", throwable));
    }
}
