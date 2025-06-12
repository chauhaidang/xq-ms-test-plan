package com.xq.testplan.unit.service;

import com.xq.testplan.dto.ListRequirementsDto;
import com.xq.testplan.dto.RequirementsDto;
import com.xq.testplan.entity.Requirements;
import com.xq.testplan.exception.RequirementAlreadyExistsException;
import com.xq.testplan.exception.ResourceNotFoundException;
import com.xq.testplan.mapper.RequirementMapper;
import com.xq.testplan.repository.RequirementsRepository;
import com.xq.testplan.service.RequirementsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IRequirementsServiceTest {

    @Mock
    private RequirementsRepository requirementsRepository;

    @InjectMocks
    private RequirementsServiceImpl requirementsService;

    private RequirementsDto requirementsDto;
    private Requirements requirements;
    private String uuid;

    @BeforeEach
    void setUp() {
        uuid = "test-uuid";  // Using a fixed UUID for predictable testing
        requirementsDto = new RequirementsDto();
        requirementsDto.setTitle("Test Requirement");
        requirementsDto.setDescription("Test Description");

        requirements = new Requirements();
        requirements.setUuid(uuid);
        requirements.setReqId(1L);  // Setting reqId for delete operation
        requirements.setTitle("Test Requirement");
        requirements.setDescription("Test Description");
    }

    @Test
    void createRequirement_ShouldReturnSuccessMessage() {
        try (MockedStatic<RequirementMapper> mockedStatic = mockStatic(RequirementMapper.class)) {
            // Given
            when(requirementsRepository.findByTitle(requirementsDto.getTitle())).thenReturn(Optional.empty());
            when(requirementsRepository.save(any(Requirements.class))).thenReturn(requirements);
            mockedStatic.when(() -> RequirementMapper.mapToRequirement(eq(requirementsDto), any(Requirements.class)))
                    .thenReturn(requirements);

            // When
            String result = requirementsService.createRequirement(requirementsDto);

            // Then
            assertNotNull(result);
            verify(requirementsRepository, times(1)).save(any(Requirements.class));
        }
    }

    @Test
    void createRequirement_WithExistingTitle_ShouldThrowException() {
        when(requirementsRepository.findByTitle(requirementsDto.getTitle())).thenReturn(Optional.of(requirements));

        assertThrows(RequirementAlreadyExistsException.class, () -> {
            requirementsService.createRequirement(requirementsDto);
        });
    }

    @Test
    void fetchRequirement_ShouldReturnRequirementsDto() {
        try (MockedStatic<RequirementMapper> mockedStatic = mockStatic(RequirementMapper.class)) {
            // Given
            when(requirementsRepository.findByUuid(uuid)).thenReturn(Optional.of(requirements));
            mockedStatic.when(() -> RequirementMapper.mapToRequirementDto(any(), any()))
                    .thenAnswer(invocation -> {
                        RequirementsDto dto = new RequirementsDto();
                        Requirements req = invocation.getArgument(0);
                        dto.setTitle(req.getTitle());
                        dto.setDescription(req.getDescription());
                        return dto;
                    });

            // When
            RequirementsDto result = requirementsService.fetchRequirement(uuid);

            // Then
            assertNotNull(result);
            assertEquals(requirements.getTitle(), result.getTitle());
            assertEquals(requirements.getDescription(), result.getDescription());
        }
    }

    @Test
    void fetchRequirement_WithNonExistentUuid_ShouldThrowException() {
        when(requirementsRepository.findByUuid(uuid)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            requirementsService.fetchRequirement(uuid);
        });
    }

    @Test
    void updateRequirement_ShouldReturnTrue() {
        when(requirementsRepository.findByUuid(uuid)).thenReturn(Optional.of(requirements));
        when(requirementsRepository.saveAndFlush(any(Requirements.class))).thenReturn(requirements);

        boolean result = requirementsService.updateRequirement(uuid, requirementsDto);

        assertTrue(result);
        verify(requirementsRepository, times(1)).saveAndFlush(any(Requirements.class));
    }

    @Test
    void updateRequirement_WithNonExistentUuid_ShouldThrowException() {
        when(requirementsRepository.findByUuid(uuid)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            requirementsService.updateRequirement(uuid, requirementsDto);
        });
        verify(requirementsRepository, never()).saveAndFlush(any(Requirements.class));
    }

    @Test
    void updateRequirement_WithNonExistentUuid_ShouldReturnFalse() {
        when(requirementsRepository.findByUuid(uuid)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> {
            requirementsService.updateRequirement(uuid, requirementsDto);
        });
    }

    @Test
    void deleteRequirement_ShouldReturnTrue() {
        when(requirementsRepository.findByUuid(uuid)).thenReturn(Optional.of(requirements));
        doNothing().when(requirementsRepository).deleteByReqId(any(Long.class));

        boolean result = requirementsService.deleteRequirement(uuid);

        assertTrue(result);
        verify(requirementsRepository, times(1)).deleteByReqId(any(Long.class));
    }

    @Test
    void deleteRequirement_WithNonExistentUuid_ShouldThrowException() {
        when(requirementsRepository.findByUuid(uuid)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            requirementsService.deleteRequirement(uuid);
        });
    }

    @Test
    void deleteAllRequirements_WithValidKey_ShouldReturnTrue() {
        doNothing().when(requirementsRepository).deleteAll();
        doNothing().when(requirementsRepository).flush();

        boolean result = requirementsService.deleteAllRequirements("akaj3971y1aksjda");

        assertTrue(result);
        verify(requirementsRepository, times(1)).deleteAll();
        verify(requirementsRepository, times(1)).flush();
    }

    @Test
    void deleteAllRequirements_WithInvalidKey_ShouldReturnFalse() {
        boolean result = requirementsService.deleteAllRequirements("invalid-key");

        assertFalse(result);
        verify(requirementsRepository, never()).deleteAll();
        verify(requirementsRepository, never()).flush();
    }

    @Test
    void getAllRequirements_ShouldReturnListRequirementsDto() {
        try (MockedStatic<RequirementMapper> mockedStatic = mockStatic(RequirementMapper.class)) {
            // Given
            when(requirementsRepository.findAll()).thenReturn(Arrays.asList(requirements));
            mockedStatic.when(() -> RequirementMapper.mapToRequirementDto(any(Requirements.class), any(RequirementsDto.class)))
                    .thenReturn(requirementsDto);

            // When
            ListRequirementsDto result = requirementsService.getAllRequirements();

            // Then
            assertNotNull(result);
            assertFalse(result.getRequirements().isEmpty());
            assertEquals(1, result.getRequirements().size());
            assertEquals(requirementsDto.getTitle(), result.getRequirements().get(0).getTitle());
        }
    }

    @Test
    void getAllRequirements_WhenNoRequirements_ShouldReturnEmptyList() {
        when(requirementsRepository.findAll()).thenReturn(Arrays.asList());

        ListRequirementsDto result = requirementsService.getAllRequirements();

        assertNotNull(result);
        assertTrue(result.getRequirements().isEmpty());
    }
}

