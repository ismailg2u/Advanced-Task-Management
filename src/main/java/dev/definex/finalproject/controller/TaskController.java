package dev.definex.finalproject.controller;



import dev.definex.finalproject.dto.ProjectDto;
import dev.definex.finalproject.dto.TaskDto;
import dev.definex.finalproject.service.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/task")
public class TaskController {

    private TaskService taskService;
    public TaskController(TaskService taskService){
        this.taskService = taskService;
    }

    @GetMapping("/findAll")
    public ResponseEntity<List<TaskDto>> findAll(){
        return ResponseEntity.ok(taskService.findAll());
    }
    @GetMapping("/findNotDeleted")
    public ResponseEntity<List<TaskDto>> findNotDeleted(){
        return ResponseEntity.ok(taskService.findNotDeleted());
    }
    @GetMapping("/find/{id}")
    public ResponseEntity<TaskDto> findById(@PathVariable UUID id){
        return ResponseEntity.ok(taskService.findById(id));
    }
    @PostMapping("/save")
    public ResponseEntity<TaskDto> save(@RequestBody TaskDto taskDto){
        return ResponseEntity.ok(taskService.save(taskDto));
    }
    @PutMapping("/update")
    public ResponseEntity<TaskDto> update(@RequestBody TaskDto taskDto){
        return ResponseEntity.ok(taskService.update(taskDto));
    }
    @PutMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id){
        taskService.delete(id);
        return ResponseEntity.ok().build();
    }
}
