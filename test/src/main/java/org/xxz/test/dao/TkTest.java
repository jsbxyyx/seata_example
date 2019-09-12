package org.xxz.test.dao;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author jsbxyyx
 */
@Setter
@Getter
@ToString
@NoArgsConstructor
public class TkTest {

    private Long id;
    private String name;
    private String name2;

    public TkTest(Long id, String name, String name2) {
        this.id = id;
        this.name = name;
        this.name2 = name2;
    }

}
