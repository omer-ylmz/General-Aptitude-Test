package com.gyt.managementservice.dataAccess.abstracts;


import com.gyt.managementservice.entities.concretes.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role,Integer> {
}
