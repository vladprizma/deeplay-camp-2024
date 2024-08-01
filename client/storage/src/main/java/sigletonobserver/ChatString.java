package sigletonobserver;

import observer.Observer;

import java.util.ArrayList;
import java.util.List;

public class ChatString {
    private static ChatString instance;
    private String storedString;

    // Список наблюдателей
    private List<Observer> observers = new ArrayList<>();

    // Приватный конструктор, чтобы предотвратить создание экземпляров
    private ChatString() {}

    // Метод для получения единственного экземпляра класса
    public static synchronized ChatString getInstance() {
        if (instance == null) {
            instance = new ChatString();
        }
        return instance;
    }

    // Метод для установки строки
    public void setString(String string) {
        this.storedString = string;
        notifyObservers();
    }

    // Метод для получения строки
    public String getString() {
        return this.storedString;
    }

    // Метод для регистрации наблюдателя
    public void registerObserver(Observer observer) {
        observers.add(observer);
    }

    // Метод для удаления наблюдателя
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    // Метод для уведомления всех наблюдателей об изменении строки
    private void notifyObservers() {
        for (Observer observer : observers) {
            observer.update(storedString);
        }
    }
}
