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
    public boolean connect() {
        return connect("sqlite", "bridger.db");
    }

    @Override
    public boolean isConnectionOpen() {
        return isConnected();
    }

    @Override
    public boolean prepareDatabase() {
        if(!isConnectionOpen()){
            getPlugin().getLogger().severe("The plugin was unable to connect to the database!");
            getPlugin().getLogger().severe("The plugin will now disable!");
            return false;
        }

        try(final PreparedStatement ps = connection.prepareStatement("")) {
            ps.execute();
            ps.close();
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
