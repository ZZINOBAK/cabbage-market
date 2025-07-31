package com.cabbage.biz.search.search;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class SearchVO {
    private String userId;
    private String searchKeyword;
    private Date searchDate;
    private String useYn;
}
