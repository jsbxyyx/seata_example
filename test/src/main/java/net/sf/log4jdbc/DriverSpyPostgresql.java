package net.sf.log4jdbc;

import java.sql.SQLException;

/**
 * @author jsbxyyx
 */
public class DriverSpyPostgresql extends DriverSpy {

    public DriverSpyPostgresql()
    {
        /**
         * 解决与druid整合报not support major version < 10问题
         */
        if (getMajorVersion() == 1) {
            try {
                acceptsURL("jdbc:log4jdbc:postgresql://127.0.0.1:5432/postgres");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
