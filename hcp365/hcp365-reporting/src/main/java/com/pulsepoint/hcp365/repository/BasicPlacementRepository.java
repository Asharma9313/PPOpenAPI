package com.pulsepoint.hcp365.repository;

import com.pulsepoint.hcp365.modal.BasicPlacement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BasicPlacementRepository extends JpaRepository<BasicPlacement, Long> {
    @Query(value = "select id as collectionid, b.token as token from Hcp365Placement a \n" +
            "join AdvPixel b \n" +
            "on a.pixelId=b.pixelId where b.token is not null and a.id is not null \n" +
            "UNION \n"+
            "select a.id as collectionid, rtrim(ltrim(b.siteId)) as token from Hcp365Placement a \n" +
            "join Hcp365AdvPixelSitePixelXRef b \n" +
            "on a.pixelId=b.pixelId where b.siteId is not null and a.id is not null \n" +
            "order by collectionid", nativeQuery = true)
    List<Object[]> getPlacementPixelTokens();

    List<BasicPlacement> findByAccountIdAndAdvIdAndStatus(Long accountId, Long advId, Boolean status);


}
