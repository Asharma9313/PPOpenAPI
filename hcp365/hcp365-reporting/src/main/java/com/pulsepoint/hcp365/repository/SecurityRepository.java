package com.pulsepoint.hcp365.repository;

import com.pulsepoint.hcp365.modal.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SecurityRepository extends JpaRepository<User, Integer> {
    @Query(value="select X.AccountId from AccountUserXRef X join Users U on X.UserId = U.UserId and U.UserName = :userName and Status = 1", nativeQuery = true)
    List<Object[]> getUserAccounts(@Param("userName") String userName);

    @Query(value="select count(UR.UserId) from aspnet_UsersInRoles UR join \n" +
            "aspnet_Roles AR on AR.RoleId = UR.RoleId\n" +
            "Join Users U on U.ExternalUId = UR.UserId\n" +
            " where U.userName = :userName and  RoleName in ('Administrator', 'FinanceAdministrator')", nativeQuery = true)
    List<Object[]> checkIsAdmin(@Param("userName") String userName);

    User getByUserName(String userName);
}
