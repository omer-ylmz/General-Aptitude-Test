package com.gyt.managementservice.business.concretes;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.gyt.corepackage.business.abstracts.MessageService;
import com.gyt.corepackage.utils.exceptions.types.BusinessException;
import com.gyt.managementservice.business.dtos.response.get.GetRoleResponse;
import com.gyt.managementservice.business.messages.Messages;
import com.gyt.managementservice.dataAccess.abstracts.RoleRepository;
import com.gyt.managementservice.entities.concretes.Role;
import com.gyt.managementservice.mapper.RoleMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

class RoleManagerTest {

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private RoleMapper roleMapper;

    @Mock
    private MessageService messageService;

    @InjectMocks
    private RoleManager roleManager;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetByIdRole_Success() {
        Long roleId = 1L;
        Role role = new Role();
        role.setId(roleId);
        GetRoleResponse roleResponse = new GetRoleResponse();

        when(roleRepository.findById(roleId)).thenReturn(Optional.of(role));
        when(roleMapper.getRoleToResponse(role)).thenReturn(roleResponse);

        GetRoleResponse result = roleManager.getByIdRole(roleId);

        assertNotNull(result);
        assertEquals(roleResponse, result);
        verify(roleRepository).findById(roleId);
        verify(roleMapper).getRoleToResponse(role);
    }

    @Test
    void testGetByIdRole_RoleNotFound() {
        Long roleId = 1L;

        when(roleRepository.findById(roleId)).thenReturn(Optional.empty());
        when(messageService.getMessage(Messages.RoleErrors.RoleShouldBeExists))
                .thenReturn("Role should exist");

        BusinessException thrown = assertThrows(BusinessException.class, () -> roleManager.getByIdRole(roleId));
        assertEquals("Role should exist", thrown.getMessage());
    }
}