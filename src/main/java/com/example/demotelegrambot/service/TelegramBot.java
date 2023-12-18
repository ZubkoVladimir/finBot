package com.example.demotelegrambot.service;

import com.example.demotelegrambot.Command.Currencies;
import com.example.demotelegrambot.config.BotConfig;
import com.example.demotelegrambot.service.model.CurrencyModel;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.HashSet;

@Component
@AllArgsConstructor
public class TelegramBot extends TelegramLongPollingBot {
    private final BotConfig botconfig;

    public String getBotName() {
        return botconfig.getBotName();
    }

    public String getBotToken() {
        return botconfig.getBotToken();
    }


    @Override
    public void onUpdateReceived(Update update) {

        HashSet<String> cur = Currencies.abbreviation();

        CurrencyModel currencyModel = new CurrencyModel();
        String currency = "";
        HashSet<String> commands = new HashSet<>();
        commands.add("/start");
        commands.add("/rate");
        commands.add("/convertor");
        commands.add("/help");


        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();

            long chatId = update.getMessage().getChatId();
            String name = update.getMessage().getChat().getFirstName();

            if (messageText.equals("/start")) {
                sendMessage(chatId, "HI, " + name + " !  !  !" + "\n" +
                        "I'm a Finance Bot. I'll give you a list of commands that I can do:" + "\n" +
                        "/rate - send the current exchange rate to BYN" + "\n" +
                        "/convertor - convert desired currency to BYN" + "\n" +
                        "/help - send all commands");
                update.getMessage();
            }

            if(messageText.equals("/help")){
                sendMessage(chatId, "/rate - send the current exchange rate to BYN" + "\n" +
                        "/convertor - convert desired currency to BYN"  + "\n" +
                        "/help - send all commands");
            }

            if (messageText.equals("/rate")) {
                sendMessage(chatId, "ok let's write desired currency. For example: USD, PLN, etc.");
                update.getMessage();
            }

            if (cur.contains(messageText)) {
                try {
                    currency = CurrencyService.getCurrencyRate(messageText, currencyModel);
                } catch (IOException e) {
                    sendMessage(chatId, "We have not found such a currency." + "\n" +
                            "Enter the currency whose official exchange rate" + "\n" +
                            "you want to know in relation to BYN." + "\n" +
                            "For example: USD");
                } catch (ParseException e) {
                    throw new RuntimeException("Unable to parse date");
                }
                sendMessage(chatId, currency);
                update.getMessage();
            }

            if(messageText.equals("/convertor")){
                sendMessage(chatId, "ok. try to send message with amount of currency. Example: 1000 USD.");
            }

            if(!cur.contains(messageText) && !commands.contains(messageText)){
                sendMessage(chatId, convertor(messageText));
            }


        }



    }



    @SneakyThrows
    private String convertor(String messageText){
        CurrencyModel currencyModel = new CurrencyModel();

        String[] text = messageText.split(" ");
        String cur = text[1];

       CurrencyService.getCurrencyRate(cur, currencyModel);

       Double rate = currencyModel.getCur_OfficialRate();
       Double amount = Double.valueOf(text[0]);
       Double scale = Double.valueOf(currencyModel.getCur_Scale());

       Double result = amount*rate/scale;

       DecimalFormat dF = new DecimalFormat( "#.###" );

       return "If you wanna buy " + amount + " " + cur +"."+ "\n"+
               "You have to spend " + dF.format(result) + " BYN.";
    }


    private void sendMessage(Long chatId, String textSend) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText(textSend);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException("не фартануло");
        }


    }

    @Override
    public String getBotUsername() {
        return getBotToken();
    }
}
