package io.github.alenalex.bridger.interfaces;

import io.github.alenalex.bridger.Bridger;

public interface IHandler {

    boolean initHandler();

    void prepareHandler();

    Bridger plugin();

    default void disableHandler() {

    }

    default void reloadHandler(){

    }
}
