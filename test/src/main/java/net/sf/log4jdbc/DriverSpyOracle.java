package net.sf.log4jdbc;

import java.sql.SQLException;

/**
 * @author jsbxyyx
 */
public class DriverSpyOracle extends DriverSpy {

    public DriverSpyOracle()
    {
        /**
         * 解决与druid整合报not support major version < 10问题
         */
        if (getMajorVersion() == 1) {
            try {
                acceptsURL("jdbc:log4jdbc:oracle:thin:@127.0.0.1:XE");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
