package sigletonobserver;

import observer.Observer;

import java.util.ArrayList;
import java.util.List;

public class ChatString {
    private static ChatString instance;
    private String storedString;

    private List<Observer> observers = new ArrayList<>();

    private ChatString() {}

    public static synchronized ChatString getInstance() {
        if (instance == null) {
            instance = new ChatString();
        }
        return instance;
    }

    public void setString(String string) {
        this.storedString = string;
        notifyObservers();
    }

    public String getString() {
        return this.storedString;
    }

    public void registerObserver(Observer observer) {
        observers.add(observer);
    }

    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    private void notifyObservers() {
        for (Observer observer : observers) {
            observer.update(storedString);
        }
    }
}
