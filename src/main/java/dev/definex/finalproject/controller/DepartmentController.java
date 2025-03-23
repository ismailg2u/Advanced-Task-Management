package dev.definex.finalproject.controller;

import dev.definex.finalproject.dto.DepartmentDto;
import dev.definex.finalproject.dto.UserDto;
import dev.definex.finalproject.service.DepartmentService;
import dev.definex.finalproject.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/department")
public class DepartmentController {

    private DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService){
        this.departmentService = departmentService;
    }


    @GetMapping("/findAll")
    public ResponseEntity<List<DepartmentDto>> findAll(){
        return ResponseEntity.ok(departmentService.findAll());
    }
    @GetMapping("/findNotDeleted")
    public ResponseEntity<List<DepartmentDto>> findNotDeleted(){
        return ResponseEntity.ok(departmentService.findNotDeleted());
    }
    @GetMapping("/find/{id}")
    public ResponseEntity<DepartmentDto> findById(@PathVariable UUID id){
        return ResponseEntity.ok(departmentService.findById(id));
    }
    @PostMapping("/save")
    public ResponseEntity<DepartmentDto> save(@RequestBody DepartmentDto departmentDto){
        return ResponseEntity.ok(departmentService.save(departmentDto));
    }
    @PutMapping("/update")
    public ResponseEntity<DepartmentDto> update(@RequestBody DepartmentDto departmentDto){
        return ResponseEntity.ok(departmentService.update(departmentDto));
    }
    @PutMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id){
        departmentService.delete(id);
        return ResponseEntity.ok().build();
    }

}
