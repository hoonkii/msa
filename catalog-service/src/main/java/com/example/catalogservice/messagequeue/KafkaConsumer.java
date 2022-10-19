package com.example.catalogservice.messagequeue;

import com.example.catalogservice.domain.Catalog;
import com.example.catalogservice.domain.CatalogRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaConsumer {

    private final CatalogRepository catalogRepository;

    @KafkaListener(topics = "example-catalog-topic", groupId = "consumerGroupId")
    public void updateQty(String kafkaMessage) {
        log.info("Kafka message: ->" + kafkaMessage);
        Map<Object, Object> map = new HashMap<>();

        ObjectMapper mapper = new ObjectMapper();
        try {
            // Json String 형태의 카프카 메시지를 컨버팅
            map = mapper.readValue(kafkaMessage, new TypeReference<Map<Object, Object>>() {
            });
        } catch (JsonProcessingException exception) {
            exception.printStackTrace();
        }

        Catalog catalog = catalogRepository.findByProductId((String) map.get("productId"));

        if (catalog != null) {
            catalog.setStock(catalog.getStock() - (Integer) map.get("qty"));
            catalogRepository.save(catalog);
        }
    }
}
