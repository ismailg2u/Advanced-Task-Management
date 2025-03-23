package dev.definex.finalproject.servicetest;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import dev.definex.finalproject.dto.DepartmentDto;
import dev.definex.finalproject.entity.Department;
import dev.definex.finalproject.exception.DepartmentNotFoundException;
import dev.definex.finalproject.mapper.DepartmentMapper;
import dev.definex.finalproject.mapper.ProjectMapper;
import dev.definex.finalproject.repository.DepartmentRepository;
import dev.definex.finalproject.service.impl.DepartmentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.*;

public class DepartmentServiceTest {

      @Mock
      private DepartmentRepository departmentRepository;

      @Mock
      private DepartmentMapper departmentMapper;

        @Mock
        private ProjectMapper projectMapper;

        @InjectMocks
        private DepartmentServiceImpl departmentService;

        private DepartmentDto departmentDto;
        private Department department;

        @BeforeEach
        public void setUp() {
            MockitoAnnotations.openMocks(this);
            departmentDto = DepartmentDto.builder()
                    .id(UUID.randomUUID())
                    .name("Engineering")
                    .projects(new ArrayList<>())
                    .build();
            department = Department.builder()
                    .id(departmentDto.getId())
                    .name(departmentDto.getName())
                    .projects(new ArrayList<>())
                    .build();
        }

        @Test
        public void testSave() {
            when(departmentMapper.toDepartment(departmentDto)).thenReturn(department);
            when(departmentRepository.save(department)).thenReturn(department);
            when(departmentMapper.toDepartmentDto(department)).thenReturn(departmentDto);
            DepartmentDto result = departmentService.save(departmentDto);
            assertNotNull(result);
            assertEquals(departmentDto.getName(), result.getName());
            verify(departmentRepository).save(department);
        }

        @Test
        public void testFindAll() {
            List<Department> departmentList = new ArrayList<>();
            departmentList.add(department);
            when(departmentRepository.findAll()).thenReturn(departmentList);
            when(departmentMapper.toDepartmentDto(department)).thenReturn(departmentDto);
            List<DepartmentDto> result = departmentService.findAll();
            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals(departmentDto.getName(), result.get(0).getName());
        }

        @Test
        public void testFindNotDeleted() {
            List<Department> departmentList = new ArrayList<>();
            departmentList.add(department);
            when(departmentRepository.findByIsDeletedFalse()).thenReturn(departmentList);
            when(departmentMapper.toDepartmentDto(department)).thenReturn(departmentDto);
            List<DepartmentDto> result = departmentService.findNotDeleted();
            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals(departmentDto.getName(), result.get(0).getName());
        }

        @Test
        public void testUpdate() throws DepartmentNotFoundException {
            when(departmentRepository.findById(departmentDto.getId())).thenReturn(Optional.of(department));
            when(departmentMapper.toDepartment(departmentDto)).thenReturn(department);
            when(departmentRepository.save(department)).thenReturn(department);
            when(departmentMapper.toDepartmentDto(department)).thenReturn(departmentDto);
            DepartmentDto result = departmentService.update(departmentDto);
            assertNotNull(result);
            assertEquals(departmentDto.getName(), result.getName());
            verify(departmentRepository).save(department);
        }

        @Test
        public void testUpdateDepartmentNotFound() {
            when(departmentRepository.findById(departmentDto.getId())).thenReturn(Optional.empty());
            assertThrows(DepartmentNotFoundException.class, () -> {
                departmentService.update(departmentDto);
            });
        }

        @Test
        public void testDelete() throws DepartmentNotFoundException {
            when(departmentRepository.existsById(departmentDto.getId())).thenReturn(true);
            departmentService.delete(departmentDto.getId());
            verify(departmentRepository).softDeleteById(departmentDto.getId());
        }

        @Test
        public void testDeleteDepartmentNotFound() {
            when(departmentRepository.existsById(departmentDto.getId())).thenReturn(false);
            assertThrows(DepartmentNotFoundException.class, () -> {
                departmentService.delete(departmentDto.getId());
            });
        }

        @Test
        public void testFindById() throws DepartmentNotFoundException {
            when(departmentRepository.findById(departmentDto.getId())).thenReturn(Optional.of(department));
            when(departmentMapper.toDepartmentDto(department)).thenReturn(departmentDto);
            DepartmentDto result = departmentService.findById(departmentDto.getId());
            assertNotNull(result);
            assertEquals(departmentDto.getName(), result.getName());
        }

        @Test
        public void testFindByIdDepartmentNotFound() {
            when(departmentRepository.findById(departmentDto.getId())).thenReturn(Optional.empty());
            assertThrows(DepartmentNotFoundException.class, () -> {
                departmentService.findById(departmentDto.getId());
            });
        }
    }


