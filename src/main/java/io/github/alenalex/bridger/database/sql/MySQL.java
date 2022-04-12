package io.github.alenalex.bridger.database.sql;

import io.github.alenalex.bridger.Bridger;
import io.github.alenalex.bridger.abstracts.AbstractSQL;
import io.github.alenalex.bridger.database.sql.config.ConnectionConfig;
import io.github.alenalex.bridger.interfaces.IDatabaseProvider;

import java.util.ArrayList;
import java.util.List;


public class MySQL extends AbstractSQL implements  IDatabaseProvider {

    public MySQL(Bridger plugin) {
        super(plugin);
    }

    @Override
    public List<String> getPrepareDatabaseQuery() {
        return new ArrayList<String>(){{

        }};
    }

    @Override
    public boolean connect() {
        return super.connect("mysql",
                ConnectionConfig.of(getPlugin().configurationHandler().getConfigurationFile().getSectionOf("storage"))
                );
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
