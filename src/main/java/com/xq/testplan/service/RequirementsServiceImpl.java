package com.xq.testplan.service;

import com.xq.testplan.dto.ListRequirementsDto;
import com.xq.testplan.dto.RequirementsDto;
import com.xq.testplan.entity.Requirements;
import com.xq.testplan.exception.RequirementAlreadyExistsException;
import com.xq.testplan.exception.ResourceNotFoundException;
import com.xq.testplan.mapper.RequirementMapper;
import com.xq.testplan.repository.RequirementsRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class RequirementsServiceImpl implements IRequirementsService {
    private RequirementsRepository requirementsRepository;

    @Override
    @Transactional
    public String createRequirement(RequirementsDto requirementsDto) {
        log.info("Creating requirement for {}", requirementsDto);
        Optional<Requirements> requirement = requirementsRepository.findByTitle(requirementsDto.getTitle());
        if (requirement.isPresent()) {
            throw new RequirementAlreadyExistsException(String.format(
                    "Requirement already exist: %s", requirementsDto.getTitle()));
        }
        Requirements requirements = RequirementMapper.mapToRequirement(requirementsDto, new Requirements());
        requirements.setUuid(UUID.randomUUID().toString());
        requirements = requirementsRepository.save(requirements);
        return requirements.getUuid();
    }

    @Override
    public RequirementsDto fetchRequirement(String uuid) {
        Requirements requirement = requirementsRepository.findByUuid(uuid).orElseThrow(
                () -> new ResourceNotFoundException("Requirement", "uuid", uuid)
        );
        return RequirementMapper.mapToRequirementDto(requirement, new RequirementsDto());
    }

    @Override
    @Transactional
    public boolean updateRequirement(String uuid, RequirementsDto requirementsDto) {
        Requirements requirement = requirementsRepository.findByUuid(uuid).orElseThrow(
                () -> new ResourceNotFoundException("Requirement", "uuid", uuid)
        );
        RequirementMapper.mapToRequirement(requirementsDto, requirement);
        requirementsRepository.saveAndFlush(requirement);
        return true;
    }

    @Override
    public boolean deleteRequirement(String uuid) {
        Requirements requirement = requirementsRepository.findByUuid(uuid).orElseThrow(
                () -> new ResourceNotFoundException("Requirement", "uuid", uuid)
        );
        requirementsRepository.deleteByReqId(requirement.getReqId());
        return true;
    }

    @Override
    @Transactional
    public boolean deleteAllRequirements(String key) {
        if (key.equals("akaj3971y1aksjda")) {
            requirementsRepository.deleteAll();
            requirementsRepository.flush();
            return true;
        }
        return false;
    }

    @Override
    public ListRequirementsDto getAllRequirements() {
        List<RequirementsDto> requirementsDtos = requirementsRepository
                .findAll()
                .stream()
                .map(requirement
                        -> RequirementMapper.mapToRequirementDto(requirement, new RequirementsDto()))
                .toList();
        return ListRequirementsDto.builder()
                .total(requirementsDtos.size())
                .requirements(requirementsDtos)
                .build();
    }
}
