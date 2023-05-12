package io.github.mrzexin.springbootdemo;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * CompletableFutureTest
 *
 * @author Zexin Li
 * @date 2023-05-11 14:04
 */
public class CompletableFutureTest {

    @Test
    public void test20230511_1404() throws ExecutionException, InterruptedException {
        CompletableFuture<List<String>> completableFuture = new CompletableFuture<>();
        completableFuture.get();
        completableFuture.handleAsync(new BiFunction<List<String>, Throwable, List<String>>() {
            @Override
            public List<String> apply(List<String> o, Throwable throwable) {
                return null;
            }
        });

        completableFuture.thenApplyAsync(new Function<List<String>, Object>() {
            @Override
            public Object apply(List<String> strings) {
                return null;
            }
        });

        CompletableFuture.supplyAsync(new Supplier<String>() {
            @Override
            public String get() {
                return null;
            }
        });
    }
}
