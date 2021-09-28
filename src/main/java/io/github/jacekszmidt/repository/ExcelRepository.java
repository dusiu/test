package io.github.jacekszmidt.repository;

import io.github.jacekszmidt.model.ExcelEntity;
import io.github.jacekszmidt.model.User;
import io.github.jacekszmidt.service.UserService;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;
import java.util.List;

public interface ExcelRepository extends CrudRepository<ExcelEntity, Long> {
}
