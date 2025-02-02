package com.capstone.community_membership_service.service;

import com.capstone.community_membership_service.entity.CommunityMembershipEntity;
import com.capstone.community_membership_service.pojo.CommunityMembershipAddPojo;
import com.capstone.community_membership_service.pojo.CommunityMembershipPojo;
import com.capstone.community_membership_service.pojo.CommunityMembershipUpdateAmountPojo;
import com.capstone.community_membership_service.pojo.CommunityMembershipWithUserDetailsPojo;
import com.capstone.community_membership_service.pojo.CommunityPojo;
import com.capstone.community_membership_service.pojo.UserOutputDataPojo;
import com.capstone.community_membership_service.repository.CommunityMembershipRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommunityMembershipService {

    @Autowired
    CommunityMembershipRepository communityMembershipRepository;

    private CommunityMembershipPojo convertEntityToPojo(CommunityMembershipEntity entity) {
        CommunityMembershipPojo pojo = new CommunityMembershipPojo();
        BeanUtils.copyProperties(entity, pojo);
        return pojo;
    }

    // Add a new membership
    public CommunityMembershipPojo addMembership(CommunityMembershipAddPojo newMembership) {
        List<CommunityMembershipEntity> exists = communityMembershipRepository
                .findByCommunityIdAndEmail(newMembership.getCommunityId(), newMembership.getEmail());
        if (exists.size() <= 0) {

            CommunityMembershipEntity communityMembershipEntity = new CommunityMembershipEntity(0,
                    newMembership.getCommunityId(), newMembership.getEmail(), 0, false, false, false);

            CommunityMembershipEntity savedEntity = communityMembershipRepository.save(communityMembershipEntity);
            return convertEntityToPojo(savedEntity);
        }
        return null;
    }

    public CommunityMembershipPojo setToAccepted(CommunityMembershipAddPojo details) {
        List<CommunityMembershipEntity> communityMembershipEntity = communityMembershipRepository
                .findByCommunityIdAndEmail(details.getCommunityId(), details.getEmail());
        if (communityMembershipEntity.size() == 1) {
            communityMembershipEntity.get(0).setAccepted(true);
            communityMembershipRepository.save(communityMembershipEntity.get(0));
            return convertEntityToPojo(communityMembershipEntity.get(0));
        }
        return null;
    }

    // Get memberships by community ID
    public List<CommunityMembershipWithUserDetailsPojo> getMembershipsByCommunityId(int communityId,
            boolean isAccepted) {
        List<CommunityMembershipEntity> entities = communityMembershipRepository.findByCommunityIdAndIsAccepted(
                communityId,
                isAccepted);
        List<CommunityMembershipWithUserDetailsPojo> pojos = new ArrayList<>();
        for (CommunityMembershipEntity entity : entities) {
            CommunityMembershipWithUserDetailsPojo communityMembershipWithUserDetailsPojo = new CommunityMembershipWithUserDetailsPojo();
            BeanUtils.copyProperties(entity, communityMembershipWithUserDetailsPojo);
            RestClient restClient = RestClient.create();
            UserOutputDataPojo responseUser = restClient.get()
                    .uri("http://localhost:5001/api/users/email/" + entity.getEmail())
                    .retrieve().body(UserOutputDataPojo.class);
            communityMembershipWithUserDetailsPojo.setUser(responseUser);
            CommunityPojo responseCommunity = restClient.get()
                    .uri("http://localhost:5002/api/communities/" + entity.getCommunityId())
                    .retrieve().body(CommunityPojo.class);
            communityMembershipWithUserDetailsPojo.setCommunity(responseCommunity);
            pojos.add(communityMembershipWithUserDetailsPojo);
        }
        return pojos;
    }

    // Get memberships by username
    public List<CommunityMembershipWithUserDetailsPojo> getMembershipsByEmail(String email) {
        List<CommunityMembershipEntity> entities = communityMembershipRepository.findByEmail(email);
        List<CommunityMembershipWithUserDetailsPojo> pojos = new ArrayList<>();
        for (CommunityMembershipEntity entity : entities) {
            CommunityMembershipWithUserDetailsPojo communityMembershipWithUserDetailsPojo = new CommunityMembershipWithUserDetailsPojo();
            BeanUtils.copyProperties(entity, communityMembershipWithUserDetailsPojo);
            RestClient restClient = RestClient.create();
            UserOutputDataPojo responseUser = restClient.get()
                    .uri("http://localhost:5001/api/users/email/" + entity.getEmail())
                    .retrieve().body(UserOutputDataPojo.class);
            communityMembershipWithUserDetailsPojo.setUser(responseUser);
            CommunityPojo responseCommunity = restClient.get()
                    .uri("http://localhost:5002/api/communities/" + entity.getCommunityId())
                    .retrieve().body(CommunityPojo.class);
            communityMembershipWithUserDetailsPojo.setCommunity(responseCommunity);
            pojos.add(communityMembershipWithUserDetailsPojo);
        }
        return pojos;
    }

    // Get memberships by username
    public List<CommunityMembershipPojo> getMembershipsByCommunityIdEmail(int communityId, String email) {
        List<CommunityMembershipEntity> entities = communityMembershipRepository.findByCommunityIdAndEmail(
                communityId, email);
        List<CommunityMembershipPojo> pojos = new ArrayList<>();
        for (CommunityMembershipEntity entity : entities) {
            pojos.add(convertEntityToPojo(entity));
        }
        return pojos;
    }

    public CommunityMembershipPojo updateLoanTakenCommunityMembership(
            CommunityMembershipUpdateAmountPojo updateCommunityMembership) {
        List<CommunityMembershipEntity> entities = communityMembershipRepository.findByCommunityIdAndEmail(
                updateCommunityMembership.getCommunityId(), updateCommunityMembership.getEmail());
        if (entities.size() == 1) {
            CommunityMembershipEntity entity = entities.get(0);
            entity.setLoanTaken(true);
            communityMembershipRepository.save(entity);
            return convertEntityToPojo(entity);
        }
        return null;
    } 

    public CommunityMembershipPojo updateAmountCommunityMembership(
            CommunityMembershipUpdateAmountPojo updateCommunityMembership) {
        List<CommunityMembershipEntity> entities = communityMembershipRepository.findByCommunityIdAndEmail(
                updateCommunityMembership.getCommunityId(), updateCommunityMembership.getEmail());
        if (entities.size() == 1) {
            CommunityMembershipEntity entity = entities.get(0);
            entity.setAmount(
                Math.round((entity.getAmount() + updateCommunityMembership.getAmount()) * 100.0) / 100.0
            );            
            if(entity.getAmount() >= 0){
                entity.setLoanTaken(false);
                entity.setLoanDefaulter(false);
            }
            communityMembershipRepository.save(entity);
            return convertEntityToPojo(entity);
        }
        return null;
    }

    // Get membership details by ID
    public CommunityMembershipPojo getMembershipById(int communityMembershipId) {
        CommunityMembershipEntity entity = communityMembershipRepository.findById(communityMembershipId)
                .orElse(null);
        return convertEntityToPojo(entity);
    }

    public void deleteMembershipById(int communityMembershipId) {
        CommunityMembershipEntity entity = communityMembershipRepository.findById(communityMembershipId)
                .orElse(null);
        if (entity != null) {
            communityMembershipRepository.deleteById(communityMembershipId);
        }
    }

    public void setLoanDefaulter(int membershipId, boolean loanDefaulter){
        CommunityMembershipEntity communityMembershipEntity = communityMembershipRepository.findById(membershipId).orElse(null);
        if(communityMembershipEntity != null){
            communityMembershipEntity.setLoanDefaulter(loanDefaulter);
            communityMembershipEntity = communityMembershipRepository.save(communityMembershipEntity);
        }
    }

}
