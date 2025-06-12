package com.xq.testplan.service;

import com.xq.testplan.dto.ListRequirementsDto;
import com.xq.testplan.dto.RequirementsDto;

public interface IRequirementsService {
    String createRequirement(RequirementsDto requirementsDto);
    RequirementsDto fetchRequirement(String uuid);
    boolean updateRequirement(String uuid, RequirementsDto requirementsDto);
    boolean deleteRequirement(String uuid);
    boolean deleteAllRequirements(String key);
    ListRequirementsDto getAllRequirements();
}
