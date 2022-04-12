package io.github.alenalex.bridger.interfaces;

public interface IDatabaseProvider {

    boolean connect();

    boolean isConnectionOpen();

    void closeConnection();

    boolean prepareDatabase();

}
