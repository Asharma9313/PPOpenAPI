package com.pulsepoint.hcp365.trigger.repository;

import com.pulsepoint.hcp365.trigger.enums.AudienceType;
import com.pulsepoint.hcp365.trigger.modal.Audience;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AudienceRepository extends JpaRepository<Audience, Long>, JpaSpecificationExecutor<Audience> {
    List<Audience> findByTriggerIdAndAudienceTypeAndStatus(Long triggerId, AudienceType audienceType, boolean status);

    List<Audience> findByTriggerIdAndAudienceType(Long triggerId, AudienceType audienceType);

    List<Audience> findByAudienceTypeAndStatusAndAudienceValueId(AudienceType audienceType, boolean status, Long audienceValueId);
}
