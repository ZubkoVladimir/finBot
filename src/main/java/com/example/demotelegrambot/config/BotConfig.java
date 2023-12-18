package com.example.demotelegrambot.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;



@Configuration
@Data
@PropertySource("application.properties")
public class BotConfig {

    @Value("${bot.name}")
    String botName = "myDemoFinBot_bot";

    @Value("${bot.token}")
    String botToken = "6802564176:AAENwSfaWjGBYQ_naenOnk3KRZ6z8QbkZto";
}
