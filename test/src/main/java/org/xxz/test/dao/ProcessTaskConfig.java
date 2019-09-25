package org.xxz.test.dao;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author jsbxyyx
 */
@Setter
@Getter
@ToString
public class ProcessTaskConfig {

    private String id;
    private String processDefId;
    private String taskKey;
    private String fromId;
    private String printLabels;
    private String roles;
    private Double timeLimit;
    private Integer rapidSubmitType;
    private String rapidSubmitTarget;
    private String attachFlag;
    private String subType;
    private String reverseFlag;
    private String taskType;
    private String menuLabels;
    private String isVirtual;
    private String runtimeLabels;
    private String templateLabels;
    private String handOverPrechecks;
    private String smsLabels;
    private String revision;
    private String remark;
    private String dataLoadType;
    private Integer lockVersion;
    private String refundFalg;

}
