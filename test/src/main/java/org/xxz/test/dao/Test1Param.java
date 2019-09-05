package org.xxz.test.dao;

/**
 * @author jsbxyyx
 */
public class Test1Param {

    private String name1;
    private String name2;

    @Override
    public String toString() {
        return "Test1Param{" +
                "name1='" + name1 + '\'' +
                ", name2='" + name2 + '\'' +
                '}';
    }

    public String getName1() {
        return name1;
    }

    public void setName1(String name1) {
        this.name1 = name1;
    }

    public String getName2() {
        return name2;
    }

    public void setName2(String name2) {
        this.name2 = name2;
    }
}
