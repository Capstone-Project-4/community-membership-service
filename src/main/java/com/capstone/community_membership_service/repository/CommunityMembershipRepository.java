package com.capstone.community_membership_service.repository;

import com.capstone.community_membership_service.entity.CommunityMembershipEntity;

import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@EnableDiscoveryClient
@Repository
public interface CommunityMembershipRepository extends JpaRepository<CommunityMembershipEntity, Integer> {
    List<CommunityMembershipEntity> findByCommunityIdAndIsAccepted(int communityId, Boolean isAccepted);

    List<CommunityMembershipEntity> findByEmail(String email);

    List<CommunityMembershipEntity> findByCommunityIdAndEmail(int communityId, String email);

    List<CommunityMembershipEntity> findByCommunityIdAndIsLoanDefaulter(int communityId, Boolean isLoanDefaulter);

}
