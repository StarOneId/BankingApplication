package com.bank.auth.service;

import com.bank.auth.entity.Role;
import com.bank.auth.repository.RoleRepository;
import com.bank.common.exception.BusinessException;
import com.bank.common.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    public Role createRole(String name, String description) {
        if (roleRepository.findByName(name).isPresent()) {
            throw new BusinessException("ROLE_EXISTS", "Role already exists: " + name);
        }

        Role role = Role.builder()
            .name(name)
            .description(description)
            .build();

        return roleRepository.save(role);
    }

    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    public Role getRoleById(Long id) {
        return roleRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Role not found with id: " + id));
    }

    public Role getRoleByName(String name) {
        return roleRepository.findByName(name)
            .orElseThrow(() -> new ResourceNotFoundException("Role not found: " + name));
    }
}
