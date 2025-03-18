package com.librarymanagementsystem.observers;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class NotificationSubject {
    private final List<NotificationObserver> observers = new ArrayList<>();

    // Method to register an observer
    public void attach(NotificationObserver observer) {
        observers.add(observer);
    }

    // Method to remove an observer
    public void detach(NotificationObserver observer) {
        observers.remove(observer);
    }

    // Method to notify all observers
    protected void notifyObservers(UUID userId, String message, String type) {
        for (NotificationObserver observer : observers) {
            observer.update(userId, message, type);
        }
    }
}