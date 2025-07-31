package com.cabbage.biz.search.post;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class PostVO {
    private Long postId;
    private String sellerId;
    private Long postCatId;
    private String postTitle;
    private Long postPrice;
    private String postContent;
    private String postStatus;
    private Date postRegdate;
    private Long postViews;
    private String buyerId;

    //검색용
    private String searchKeyword;
    private String fileName;
    private String postWishCount;
    private String chatRoomCount;
    private String userNickname;
}
