package net.sf.log4jdbc.extension;

import net.sf.log4jdbc.DriverSpy;

import java.sql.SQLException;

/**
 * @author jsbxyyx
 */
public class DriverSpySQLServer extends DriverSpy {

    public DriverSpySQLServer()
    {
        /**
         * 解决与druid整合报not support major version < 10问题
         */
        if (getMajorVersion() == 1) {
            try {
                acceptsURL("jdbc:log4jdbc:sqlserver://127.0.0.1:1433;databaseName=test");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
