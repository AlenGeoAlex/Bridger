package io.github.alenalex.bridger.database.sql;

import io.github.alenalex.bridger.Bridger;
import io.github.alenalex.bridger.abstracts.AbstractSQL;
import io.github.alenalex.bridger.interfaces.IDatabaseProvider;

import java.sql.PreparedStatement;

public class SQLite extends AbstractSQL implements IDatabaseProvider {

    public SQLite(Bridger plugin) {
        super(plugin);
    }

    @Override
    public String getPrepareDatabaseQuery() {
        return "";
    }

    @Override
    public boolean connect() {
        return connect("sqlite", "bridger.db");
    }

    @Override
    public boolean isConnectionOpen() {
        return isConnected();
    }

    @Override
    public boolean prepareDatabase() {
        return createDatabase();
    }
}
