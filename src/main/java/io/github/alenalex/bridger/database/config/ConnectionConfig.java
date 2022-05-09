package io.github.alenalex.bridger.database.config;

import com.zaxxer.hikari.HikariConfig;
import de.leonhard.storage.sections.FlatFileSection;
import io.github.alenalex.bridger.variables.ConfigurationPaths;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

public final class ConnectionConfig {

    private final String username;
    private final String password;
    private final String host;
    private final String port;
    private final String database;
    private final boolean ssl;

    public static ConnectionConfig of(FlatFileSection section){
        return new ConnectionConfig(
                section.getString(ConfigurationPaths.STORAGE_USERNAME.getPath()),
                section.getString(ConfigurationPaths.STORAGE_PASSWORD.getPath()),
                section.getString(ConfigurationPaths.STORAGE_HOST.getPath()),
                section.getString(ConfigurationPaths.STORAGE_PORT.getPath()),
                section.getString(ConfigurationPaths.STORAGE_DATABASE_NAME.getPath()),
                section.getBoolean(ConfigurationPaths.STORAGE_SSL.getPath())
        );
    }

    private ConnectionConfig(String username, String password, String host, String port, String database, boolean ssl) {
        Validate.notNull(username, "Database Username cannot be null");
        Validate.notNull(host, "Database Host Address cannot be null");
        Validate.notNull(database, "Database Name cannot be null");


        this.username = username;
        this.password = password;
        this.host = host;
        if(StringUtils.isBlank(port))
            this.port = "3306";
        else this.port = port;
        this.database = database;
        this.ssl = ssl;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return Integer.parseInt(port);
    }


    public String getDatabase() {
        return database;
    }

    public boolean isAutoReconnect(){
        return true;
    }

    public boolean useSSL(){
        return ssl;
    }

    public HikariConfig asHikariConfig(){
        HikariConfig config = new HikariConfig();
        config.setUsername(username);
        config.setPassword(password);

        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        config.addDataSourceProperty("useServerPrepStmts", "true");
        config.addDataSourceProperty("useLocalSessionState", "true");
        config.addDataSourceProperty("rewriteBatchedStatements", "true");
        config.addDataSourceProperty("cacheResultSetMetadata", "true");
        config.addDataSourceProperty("cacheServerConfiguration", "true");
        config.addDataSourceProperty("elideSetAutoCommits", "true");
        config.addDataSourceProperty("maintainTimeStats", "false");
        return config;
    }


}
