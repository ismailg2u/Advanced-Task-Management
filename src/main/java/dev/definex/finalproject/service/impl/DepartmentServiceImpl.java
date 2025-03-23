package dev.definex.finalproject.service.impl;

import dev.definex.finalproject.dto.DepartmentDto;
import dev.definex.finalproject.dto.UserDto;
import dev.definex.finalproject.entity.Department;
import dev.definex.finalproject.entity.User;
import dev.definex.finalproject.exception.DepartmentNotFoundException;
import dev.definex.finalproject.mapper.DepartmentMapper;
import dev.definex.finalproject.mapper.ProjectMapper;
import dev.definex.finalproject.mapper.UserMapper;
import dev.definex.finalproject.repository.DepartmentRepository;
import dev.definex.finalproject.repository.UserRepository;
import dev.definex.finalproject.service.DepartmentService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DepartmentServiceImpl implements DepartmentService {
    private final DepartmentMapper departmentMapper;
    private final ProjectMapper projectMapper;
    private final DepartmentRepository departmentRepository;

    public DepartmentServiceImpl(DepartmentMapper departmentMapper,DepartmentRepository departmentRepository
    ,ProjectMapper projectMapper) {
        this.projectMapper = projectMapper;
        this.departmentMapper = departmentMapper;
        this.departmentRepository = departmentRepository;

    }
    @Override
    public DepartmentDto save(DepartmentDto departmentDto) {
        Department savedDepartment = departmentRepository.save(departmentMapper.toDepartment(departmentDto));
        return departmentMapper.toDepartmentDto(savedDepartment);
    }

    @Override
    public List<DepartmentDto> findAll() {

        List<DepartmentDto> departmentDtoList;
        List<Department> departmentList = departmentRepository.findAll();
        departmentDtoList = departmentList.stream().
                map(department -> departmentMapper.toDepartmentDto(department)).toList();
        return departmentDtoList;
    }

    @Override
    public List<DepartmentDto> findNotDeleted() {
        List<DepartmentDto> departmentDtoList;
        List<Department> departmentList = departmentRepository.findByIsDeletedFalse();
        departmentDtoList = departmentList.stream().
                map(department -> departmentMapper.toDepartmentDto(department)).toList();
        return departmentDtoList;
    }

    @Override
    public DepartmentDto update(DepartmentDto departmentDto) throws DepartmentNotFoundException{
        Department department = departmentRepository.findById(departmentDto.getId()).orElseThrow(DepartmentNotFoundException::new);
        department.setName(departmentDto.getName());
        department.getProjects().clear();
        department.getProjects().addAll(departmentDto.getProjects().stream()
                .map(projectMapper::toProject)
                .collect(Collectors.toList()));
        departmentRepository.save(department);
        return departmentMapper.toDepartmentDto(department);

    }

    @Override
    public void delete(UUID uuid) throws DepartmentNotFoundException {
        if(departmentRepository.existsById(uuid)){
            departmentRepository.softDeleteById(uuid);
        }
        else
           throw new DepartmentNotFoundException();
    }

    @Override
    public DepartmentDto findById(UUID uuid) throws DepartmentNotFoundException{
        return departmentMapper.toDepartmentDto(departmentRepository.findById(uuid).orElseThrow(DepartmentNotFoundException::new));
    }
}
