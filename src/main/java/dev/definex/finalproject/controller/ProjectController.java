package dev.definex.finalproject.controller;

import dev.definex.finalproject.dto.DepartmentDto;
import dev.definex.finalproject.dto.ProjectDto;
import dev.definex.finalproject.service.ProjectService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/project")
public class ProjectController {
    private ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }


    @GetMapping("/findAll")
    public ResponseEntity<List<ProjectDto>> findAll(){
        return ResponseEntity.ok(projectService.findAll());
    }
    @GetMapping("/findNotDeleted")
    public ResponseEntity<List<ProjectDto>> findNotDeleted(){
        return ResponseEntity.ok(projectService.findNotDeleted());
    }
    @GetMapping("/find/{id}")
    public ResponseEntity<ProjectDto> findById(@PathVariable UUID id){
        return ResponseEntity.ok(projectService.findById(id));
    }
    @PostMapping("/save")
    public ResponseEntity<ProjectDto> save(@RequestBody ProjectDto projectDto){
        return ResponseEntity.ok(projectService.save(projectDto));
    }
    @PutMapping("/update")
    public ResponseEntity<ProjectDto> update(@RequestBody ProjectDto projectDto){
        return ResponseEntity.ok(projectService.update(projectDto));
    }
    @PutMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id){
        projectService.delete(id);
        return ResponseEntity.ok().build();
    }



}
