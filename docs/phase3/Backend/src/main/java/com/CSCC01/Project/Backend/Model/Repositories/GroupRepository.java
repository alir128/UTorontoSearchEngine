package com.CSCC01.Project.Backend.Model.Repositories;

import com.CSCC01.Project.Backend.Model.SchemaBeans.Group;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface GroupRepository extends MongoRepository<Group, String> {
	Group findByGroup(String group);
}
