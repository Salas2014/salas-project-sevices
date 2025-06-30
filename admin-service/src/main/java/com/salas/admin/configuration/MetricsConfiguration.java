package com.salas.admin.configuration;

import feign.Feign;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.InvocationTargetException;

@Configuration
public class MetricsConfiguration {

    @Bean
    public Feign.Builder feignBuilder(MeterRegistry meterRegistry) {
        return Feign.builder()
                .invocationHandlerFactory((target, dispatch) ->
                        (proxy, method, args) -> {
                            Counter counter = meterRegistry.counter(
                                    "feign.client.calls",
                                    "client", target.type().getSimpleName(), "method", method.getName()
                            );
                            counter.increment();

                            try {
                                return dispatch.get(method).invoke(args);
                            } catch (InvocationTargetException e) {
                                throw e.getTargetException();
                            }
                        });
    }
}
