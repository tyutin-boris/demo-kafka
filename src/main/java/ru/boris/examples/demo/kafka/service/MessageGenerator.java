package ru.boris.examples.demo.kafka.service;

import org.springframework.stereotype.Service;
import ru.boris.examples.demo.kafka.dto.DemoMessage;
import ru.boris.examples.demo.kafka.dto.DemoValue;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

import static ru.boris.examples.demo.kafka.dto.FirstName.FIRST_NAME;
import static ru.boris.examples.demo.kafka.dto.FirstName.LAST_NAME;

@Service
public class MessageGenerator {

    private final AtomicLong idGenerator = new AtomicLong();


    public DemoMessage getMessage() {
        String key = String.valueOf(idGenerator.getAndIncrement());

        DemoValue value = new DemoValue();

        Random rand = new Random();

        int firstNameSize = FIRST_NAME.size();
        int lastNameSize = LAST_NAME.size();

        value.setFirstName(FIRST_NAME.get(rand.nextInt(firstNameSize)));
        value.setLastName(LAST_NAME.get(rand.nextInt(lastNameSize)));

        DemoMessage demoMessage = new DemoMessage();
        demoMessage.setKey(key);
        demoMessage.setValue(value);

        return demoMessage;
    }
}
