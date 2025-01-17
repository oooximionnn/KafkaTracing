package org.example.config;

import io.micrometer.context.ContextExecutorService;
import io.micrometer.context.ContextScheduledExecutorService;
import io.micrometer.context.ContextSnapshot;
import io.micrometer.context.ContextSnapshotFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;

@Configuration(proxyBeanMethods = false)
class CommonConfiguration {
    // Для прокидывания traceId в ассинхронных вызовах

    // Example of Async Servlets setup
    @Configuration(proxyBeanMethods = false)
    @EnableAsync
    static class AsyncConfig implements AsyncConfigurer, WebMvcConfigurer {
        @Override
        public Executor getAsyncExecutor() {
            return ContextExecutorService.wrap(Executors.newCachedThreadPool(), ContextSnapshot::captureAll);
        }

        @Override
        public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
            configurer.setTaskExecutor(new SimpleAsyncTaskExecutor(r -> new Thread(ContextSnapshotFactory.builder().build().captureAll().wrap(r))));
        }
    }


    /**
     * NAME OF THE BEAN IS IMPORTANT!
     * <p>
     * We need to wrap this for @Async related things to propagate the context.
     *
     * @see EnableAsync
     */
    // [Observability] instrumenting executors
    @Bean(name = "taskExecutor", destroyMethod = "shutdown")
    ThreadPoolTaskScheduler threadPoolTaskScheduler() {
        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler() {
            @Override
            protected ExecutorService initializeExecutor(ThreadFactory threadFactory, RejectedExecutionHandler rejectedExecutionHandler) {
                ExecutorService executorService = super.initializeExecutor(threadFactory, rejectedExecutionHandler);
                return ContextExecutorService.wrap(executorService, ContextSnapshot::captureAll);
            }


            @Override
            public ScheduledExecutorService getScheduledExecutor() throws IllegalStateException {
                return ContextScheduledExecutorService.wrap(super.getScheduledExecutor());
            }
        };
        threadPoolTaskScheduler.initialize();
        return threadPoolTaskScheduler;
    }
}
