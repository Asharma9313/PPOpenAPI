package com.pulsepoint.journey.audience.service;

import com.pulsepoint.commons.exception.InvalidDataException;
import com.pulsepoint.journey.audience.modal.AudienceDefinition;
import com.pulsepoint.journey.audience.modal.LifeAudienceDefinition;
import com.pulsepoint.journey.audience.repository.LifeAudienceDefinitionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Service
//@Transactional(propagation=Propagation.REQUIRED, readOnly=true, noRollbackFor=Exception.class)
public class LifeAudienceDefinitionService {

    @Autowired
    private LifeAudienceDefinitionRepository audienceDefinitionRepository;

    public List<LifeAudienceDefinition> findByAccountId(Long accountId){
        return audienceDefinitionRepository.findByAccountIdAndActive(accountId, true);
    }

    public Page<LifeAudienceDefinition> search(Long accountId, String name, int pageNo){
        Sort sort = Sort.by("name").ascending();
        Specification<LifeAudienceDefinition> specification = (Specification<LifeAudienceDefinition>) (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(builder.equal(root.get("accountId"), accountId));
            if(name !=null && name.trim().length() >0){
                predicates.add(builder.like(root.get("name"), "%" + name + "%"));
            }
            predicates.add(builder.equal(root.get("active"), true));
            return builder.and(predicates.toArray(new Predicate[predicates.size()]));
        };
        Pageable paging = PageRequest.of(pageNo, 1000, sort);
        return audienceDefinitionRepository.findAll(specification, paging);
    }

    public void validateAudiencebyAccount(Long audienceId, Long accountId) {
        LifeAudienceDefinition audienceDefinition = audienceDefinitionRepository.getById(audienceId);
        if (audienceDefinition.getAccountId().equals(accountId) == false) {
            throw new InvalidDataException(" Invalid data");
        }
    }
    public void validateAudiencebyAccount(AudienceDefinition audienceDefinition, Long accountId) {
        if (audienceDefinition.getAccountId().equals(accountId) == false) {
            throw new InvalidDataException(" Invalid data");
        }
    }

    public LifeAudienceDefinition getById(Long id){
        return audienceDefinitionRepository.getById(id);
    }
    public LifeAudienceDefinition saveOrUpdate(LifeAudienceDefinition audienceDefinition){
        checkForDuplicates(audienceDefinition);
        return audienceDefinitionRepository.save(audienceDefinition);
    }
    public void checkForDuplicates(LifeAudienceDefinition audienceDefinition) {
        List<LifeAudienceDefinition> existingAudienceDefinitions = audienceDefinitionRepository.findByAccountIdAndActiveAndName(audienceDefinition.getAccountId(), true, audienceDefinition.getName());
        if (existingAudienceDefinitions.size() > 0) {
            if (existingAudienceDefinitions.size() == 1 && audienceDefinition.getId() != null && audienceDefinition.getId().equals(existingAudienceDefinitions.get(0).getId())) {
                return;
            }
            throw new InvalidDataException("Name already exists.");
        }
    }
}
