package com.capstone.community_membership_service.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserOutputDataPojo {

    Long userId;
    String email;
    String firstName;
    String lastName;
    String phoneNo;
    boolean isDeleted;
    String roleName;

}