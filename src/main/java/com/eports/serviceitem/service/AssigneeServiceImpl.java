package com.eports.serviceitem.service;

import com.eports.serviceitem.model.CountryDto;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: kevin
 * @Date: 2018/12/13 下午2:53
 * @Version 1.0
 */
public class AssigneeServiceImpl implements TaskListener {

    @Override
    public void notify(DelegateTask delegateTask) {
        List<String> countrylist = new ArrayList<>();
        countrylist.add("china");
        countrylist.add("japan");
        countrylist.add("england");

        CountryDto countryDto = new CountryDto();
        countryDto.setCountryList(countrylist);

        delegateTask.setVariable("countrydto",countryDto);

        //指定办理人
        String weituo="kevvv";
        delegateTask.setAssignee(weituo);
    }
}
