package com.xq.testplan.repository;

import com.xq.testplan.entity.Requirements;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface RequirementsRepository extends JpaRepository<Requirements, Long> {

    Optional<Requirements> findByUuid(String uuid);

    @Transactional
    @Modifying
    void deleteByReqId(Long reqId);

    Optional<Requirements> findByTitle(String title);
}
