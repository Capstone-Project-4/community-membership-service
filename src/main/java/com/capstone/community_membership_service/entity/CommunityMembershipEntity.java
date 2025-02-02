package com.capstone.community_membership_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "community_membership_entity")
public class CommunityMembershipEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "community_membership_id")
    private int communityMembershipId;
    @Column(name = "community_id")
    private int communityId;
    @Column(name = "email")
    private String email;
    @Column(name = "amount")
    private double amount;
    @Column(name = "is_accepted")
    private boolean isAccepted;
    @Column(name = "is_loan_taken")
    private boolean isLoanTaken;
    @Column(name = "is_loan_defaulter")
    private boolean isLoanDefaulter;

}
