package com.hms.profile.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;

import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;

import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.ExponentialBackOffWithMaxRetries;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaConfig {

    private static final String BOOTSTRAP = "localhost:9092"; // change for production

    // -----------------------------------------------------------
    // PRODUCER CONFIG
    // -----------------------------------------------------------
    @Bean
    public ProducerFactory<String, Object> producerFactory() {

        Map<String, Object> config = new HashMap<>();

        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP);
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        config.put(JsonSerializer.ADD_TYPE_INFO_HEADERS, false);
        config.put(ProducerConfig.ACKS_CONFIG, "all"); // safest

        return new DefaultKafkaProducerFactory<>(config);
    }

    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    // -----------------------------------------------------------
    // CONSUMER CONFIG
    // -----------------------------------------------------------
    @Bean
    public ConsumerFactory<String, Object> consumerFactory(ObjectMapper mapper) {

        JsonDeserializer<Object> deserializer = new JsonDeserializer<>(mapper);
        deserializer.addTrustedPackages("*");
        deserializer.setUseTypeMapperForKey(true);

        Map<String, Object> config = new HashMap<>();

        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP);
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);

        config.put(ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS, StringDeserializer.class);
        config.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class);

        config.put(ConsumerConfig.GROUP_ID_CONFIG, "kyc-worker-group");

        return new DefaultKafkaConsumerFactory<>(config, new StringDeserializer(), deserializer);
    }

    // -----------------------------------------------------------
    // KAFKA LISTENER FACTORY (With Retry + DLQ)
    // -----------------------------------------------------------
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Object> kafkaListenerFactory(
            ConsumerFactory<String, Object> consumerFactory) {

        ConcurrentKafkaListenerContainerFactory<String, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();

        factory.setConsumerFactory(consumerFactory);

        // ---- ERROR HANDLING ----
        ExponentialBackOffWithMaxRetries backoff = new ExponentialBackOffWithMaxRetries(3);
        backoff.setInitialInterval(2000); // 2 sec
        backoff.setMultiplier(2.0); // 2x
        backoff.setMaxInterval(15000); // 15 sec max

        factory.setCommonErrorHandler(new DefaultErrorHandler(backoff));

        return factory;
    }
}
