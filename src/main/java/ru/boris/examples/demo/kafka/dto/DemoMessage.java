package ru.boris.examples.demo.kafka.dto;

import lombok.Data;

@Data
public class DemoMessage {

    private String key;

    private DemoValue value;
}
