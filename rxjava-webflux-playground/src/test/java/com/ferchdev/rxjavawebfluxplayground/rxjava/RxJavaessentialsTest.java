package com.ferchdev.rxjavawebfluxplayground.rxjava;


import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.observers.TestObserver;
import io.reactivex.rxjava3.subscribers.TestSubscriber;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class RxJavaessentialsTest {

    @Test
    public void testResolveSingle() {
        RxJavaEssentials.resolveSingleString("Hola soy Fernando")
                .test()
                .assertValue("Hola soy Fernando"::equals)
                .assertComplete();
    }

    @Test
    public void testResolveMaybe() {
        RxJavaEssentials.resolveMaybeString("Hola soy Fernando")
                .test()
                .assertValue("Hola soy Fernando"::equals)
                .assertComplete();
    }

    @Test
    public void testResolveObservable() {
        RxJavaEssentials.resolveObservableString("Hola soy Fernando", "Hola soy dev", "Hola soy backend")
                .test()
                .assertComplete();
    }

    @Test
    public void testResolveCompletable() {
        RxJavaEssentials.resolveCompletableString("Hola soy Fernando")
                .test()
                .assertComplete();
    }

    @Test
    public void testResolveMergeSingle() {
        RxJavaEssentials.mergeSinglesFromStringList(List.of(new String[]{"Hola soy Fernando", "agsdag"}))
                .test()
                .assertComplete();
    }

    @Test
    public void testResolveMergeSingleWithError() throws InterruptedException {
        TestObserver<String> testObserver = new TestObserver<>();

        Single<String> s1 = Single.just("Hola soy Fernando")
                .delay(5, TimeUnit.SECONDS)
                .doOnSuccess(str -> System.out.println("Sucess 1 >v"));

        Single<String> s2 = Single.just("Soy un error")
                        .doOnSuccess(str -> System.out.println("Sucess 2 >v"))
                                .delay(4, TimeUnit.SECONDS)
                                        .flatMap(str -> Single.error(new RuntimeException("Error")));

        RxJavaEssentials.mergeSinglesFromSingleListWithError(List.of(s1, s2))
                .subscribe(testObserver);
        testObserver.await();
        testObserver.assertError(RuntimeException.class);
    }

    @Test
    public void testResolveMergeEvenWithError() throws InterruptedException {
        TestSubscriber<Object> testSubscriber = new TestSubscriber<>();

        Single<Object> s1 = Single.just((Object) "Hola soy Fernando s1")
                .delay(5, TimeUnit.SECONDS)
                .doOnSuccess(str -> System.out.println("Success 1 >v"));

        Single<Object> s3 = Single.just((Object) "Hola soy Fernando s3")
                .delay(8, TimeUnit.SECONDS)
                .doOnSuccess(str -> System.out.println("Success 3 >v"));

        Single<Object> s2 = Single.just("Soy un error s2")
                .delay(4, TimeUnit.SECONDS)
                .flatMap(str -> Single.error(new RuntimeException("Error s2")));

        Single<Object> s4 = Single.just("Soy un error s4")
                .delay(3, TimeUnit.SECONDS)
                .flatMap(str -> Single.error(new RuntimeException("Error s4")));

        RxJavaEssentials.mergeAllSinglesEvenWithError(List.of(s1, s2, s3, s4))
                .subscribeWith(testSubscriber);
        testSubscriber.await();
        long errorCount = testSubscriber.values().stream()
                .filter(value -> value instanceof RuntimeException)
                .count();

        assertEquals(2, errorCount);
    }
}