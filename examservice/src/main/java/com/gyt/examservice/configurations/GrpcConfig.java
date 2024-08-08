package com.gyt.examservice.configurations;

import com.gyt.questionservice.QuestionServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GrpcConfig {

    @Bean
    public ManagedChannel questionServiceChannel() {
        return ManagedChannelBuilder.forAddress("localhost", 7676)
                .usePlaintext()  // SSL/TLS'i devre dışı bırakır
                .build();
    }

    @Bean
    public QuestionServiceGrpc.QuestionServiceBlockingStub questionServiceBlockingStub(ManagedChannel channel) {
        return QuestionServiceGrpc.newBlockingStub(channel);
    }
}
