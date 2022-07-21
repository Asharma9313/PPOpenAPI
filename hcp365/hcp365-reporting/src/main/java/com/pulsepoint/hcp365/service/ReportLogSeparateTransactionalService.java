package com.pulsepoint.hcp365.service;

import com.pulsepoint.hcp365.modal.ReportLog;
import com.pulsepoint.hcp365.repository.ReportLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReportLogSeparateTransactionalService {

    @Autowired
    ReportLogRepository reportLogRepository;

    /*@Autowired
    private PlatformTransactionManager transactionManager;

    @Autowired
    private EntityManager entityManager;*/

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public ReportLog saveReportLog(ReportLog reportLog){
        return reportLogRepository.saveAndFlush(reportLog);
    }

/*    ReportLog saveReportLog1(ReportLog reportLog){
        TransactionTemplate transactionTemplate =new TransactionTemplate(transactionManager);
        DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
        definition.setIsolationLevel(TransactionDefinition.ISOLATION_REPEATABLE_READ);
        definition.setTimeout(-1);
        TransactionStatus status = transactionManager.getTransaction(definition);
        entityManager.persist(reportLog);
        transactionManager.commit(status);
        return reportLog;
    }*/
}
