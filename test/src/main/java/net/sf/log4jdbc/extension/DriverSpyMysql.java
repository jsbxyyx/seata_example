package net.sf.log4jdbc.extension;

import net.sf.log4jdbc.DriverSpy;

import java.sql.SQLException;

/**
 * @author jsbxyyx
 */
public class DriverSpyMysql extends DriverSpy {

    public DriverSpyMysql()
    {
        /**
         * 解决与druid整合报not support major version < 10问题
         */
        if (getMajorVersion() == 1) {
            try {
                acceptsURL("jdbc:log4jdbc:mysql://127.0.0.1:3306/test");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
