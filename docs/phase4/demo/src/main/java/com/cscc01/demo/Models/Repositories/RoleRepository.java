package com.cscc01.demo.Models.Repositories;

import com.cscc01.demo.Models.SchemaBeans.Role;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RoleRepository extends MongoRepository<Role, String> {
	Role findByRole(String group);
}
