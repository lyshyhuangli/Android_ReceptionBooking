package com.huizhou.receptionbooking.afterLogin.definePublishMeeting;

import com.huizhou.receptionbooking.afterLogin.contactGroup.checkbox.Model;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/12/13.
 */

public class DefineMeetingModel
{
    private List<Model> models = new ArrayList<>();

    public List<Model> getModels()
    {
        return models;
    }

    public void setModels(List<Model> models)
    {
        this.models = models;
    }
    public String toString()
    {
        String str = ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
        return str;
    }

}
