package com.yixia.cavas;

import android.text.TextUtils;

import java.io.Serializable;

/**
 * Created by zhaoxiaopo on 2018/8/8.
 */
public class RedCondition implements Serializable {
    private static final long serialVersionUID = -4333487464676126771L;

    private String mConditionName;
    private int mConditionType;

    public RedCondition(String conditionName, int conditionType) {
        mConditionName = conditionName;
        mConditionType = conditionType;
    }

    public String getConditionName() {
        return TextUtils.isEmpty(mConditionName) ? "" : mConditionName;
    }

    public void setConditionName(String conditionName) {
        mConditionName = conditionName;
    }

    public int getConditionType() {
        return mConditionType;
    }

    public void setConditionType(int conditionType) {
        mConditionType = conditionType;
    }
}