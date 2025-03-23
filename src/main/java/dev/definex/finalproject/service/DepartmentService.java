package dev.definex.finalproject.service;

import dev.definex.finalproject.dto.DepartmentDto;
import dev.definex.finalproject.exception.DepartmentNotFoundException;

import java.util.List;
import java.util.UUID;

public interface DepartmentService {

    DepartmentDto save(DepartmentDto departmentDto);
    List<DepartmentDto> findAll();
    List<DepartmentDto> findNotDeleted();
    DepartmentDto update(DepartmentDto departmentDto) throws DepartmentNotFoundException;
    void delete(UUID uuid) throws DepartmentNotFoundException;
    DepartmentDto findById(UUID uuid) throws DepartmentNotFoundException;

}
