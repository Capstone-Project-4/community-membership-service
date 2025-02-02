package com.capstone.community_membership_service.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CommunityMembershipAddPojo {

    private int communityId;
    private String email;

}
