package kz.timur;

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;


public class BotApp extends TelegramLongPollingBot {

    private static ResourceBundle resourceBundle = ResourceBundle.getBundle("botConfig", Locale.forLanguageTag("en_US"));

    public static void main(String[] args) {

        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            telegramBotsApi.registerBot(new BotApp());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        if (message != null && message.hasText()) {
            switch (message.getText()) {
                case "INFO":
                    sendMsg(message, "Привет, я могу подсказать текущие курсы доллара и евро к тенге.\nНажми на кнопку \"USD-KZT\" или \"EURO-KZT\"");
                    break;
                case "USD-KZT":
                    try {
                        sendMsg(message, "Курс доллара = " + MySqlDb.ReadDB("currencyDB.currencyUSD"));
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    break;
                case "EURO-KZT":
                    try {
                        sendMsg(message, "Курс евро = " + MySqlDb.ReadDB("currencyDB.currencyEURO"));
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    sendMsg(message, "Не корректный запрос");
                    break;
            }
        }
    }

    public void setButtons(SendMessage sendMessage) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        //создаем кнопки в Телеграм
        keyboardFirstRow.add(new KeyboardButton("INFO"));
        keyboardFirstRow.add(new KeyboardButton("EURO-KZT"));
        keyboardFirstRow.add(new KeyboardButton("USD-KZT"));

        keyboardRows.add(keyboardFirstRow);
        replyKeyboardMarkup.setKeyboard(keyboardRows);
    }

    public String getBotUsername() {
        return resourceBundle.getString("BotUserName");
    }

    public String getBotToken() {
        return resourceBundle.getString("BotToken");
    }

    public void sendMsg(Message message, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setReplyToMessageId(message.getMessageId());
        sendMessage.setText(text);
        try {
            setButtons(sendMessage);
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}