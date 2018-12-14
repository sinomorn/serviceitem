package com.eports.serviceitem.model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: kevin
 * @Date: 2018/12/14 上午9:37
 * @Version 1.0
 */
@Data
public class CountryDto implements Serializable {

    private static final long serialVersionUID = 5758536945895738604L;

    private List<String> countryList;

}
