package io.github.alenalex.bridger.abstracts;

import com.zaxxer.hikari.HikariConfig;
import io.github.alenalex.bridger.Bridger;
import io.github.alenalex.bridger.database.sql.config.ConnectionConfig;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class AbstractSQL {

    protected static final String JDBC_REMOTE_URL = "jdbc:"
            + "%s" //Type of DB (postgresql,mysql)
            + "://"
            + "%s" // host
            + ":"
            + "%d" // port
            + "/"
            + "%s" // database
            + "?autoReconnect="
            + "%s" // auto reconnect
            + "&"
            + "useSSL="
            + "%s" // use ssl
            ;
    protected static final String JDBC_LOCAL_URL = "jdbc:" +
            "%s" +//Type of DB (sqlite, h2, hsqldb)
            ":" +
            "%s" // File path of the database
            ;

    private final Bridger plugin;
    protected Connection connection;

    public AbstractSQL(Bridger plugin) {
        this.plugin = plugin;
    }

    public boolean connect(@NotNull String driver, @NotNull String fileName) {
        try {
                this.connection = DriverManager.getConnection(
                        String.format(
                                JDBC_LOCAL_URL,
                                driver,
                                plugin.getDataFolder().getPath()+ File.separator+"database"+File.separator+fileName
                        )
                );
            return isConnected();
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean connect(@NotNull String driver, @NotNull ConnectionConfig connectionConfig) {
        try {
            final HikariConfig config = connectionConfig.asHikariConfig();
            config.setJdbcUrl(
                    String.format(
                            JDBC_REMOTE_URL,
                            driver,
                            connectionConfig.getHost(),
                            connectionConfig.getPort(),
                            connectionConfig.getDatabase(),
                            connectionConfig.isAutoReconnect(),
                            connectionConfig.useSSL()
                    )
            );
            this.connection = config.getDataSource().getConnection();
            return isConnected();
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean isConnected() {
        if(connection == null)
            return false;

        try {
            return !connection.isClosed();
        } catch (SQLException ignored) {
            return false;
        }
    }

    public void closeConnection() {
        if(connection == null)
            return;

        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Bridger getPlugin() {
        return plugin;
    }
}
