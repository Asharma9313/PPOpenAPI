package com.pulsepoint.hcp365.trigger.repository;

import com.pulsepoint.hcp365.trigger.modal.NPIGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NPIGroupRepository extends JpaRepository<NPIGroup, Long> {
    List<NPIGroup> findByAccountIdInAndIdIn(List<Long> accountIds, List<Long> ids);

    @Query(value="select * from NpiGroup where (accountid = :accountId OR AccountId is null OR AccountId = -1) and Id in(:ids)", nativeQuery = true)
    List<NPIGroup> findByAccountIdAndIds(@Param("accountId") Long accountId, @Param("ids") List<Long> ids);
}
