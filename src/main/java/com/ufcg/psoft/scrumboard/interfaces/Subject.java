package com.ufcg.psoft.scrumboard.interfaces;

public interface Subject {

    void registerListener(Listener listener, String id);
    void removeListener(String id);
    void notifyListeners(String evenType);

}
