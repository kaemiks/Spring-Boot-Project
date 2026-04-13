package org.example.springbootv3.controller;

import org.example.springbootv3.entity.Group;
import org.example.springbootv3.dto.GroupRequestDTO;
import org.example.springbootv3.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/groups")
public class GroupController {

    @Autowired
    private GroupService groupService;

    @GetMapping
    public ResponseEntity<List<Group>> getAllGroups() {
        List<Group> groups = groupService.getAllGroups();
        return ResponseEntity.ok(groups);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Group> getGroupById(@PathVariable Long id) {
        Group group = groupService.getGroupById(id);
        return ResponseEntity.ok(group);
    }

    @PostMapping
    public ResponseEntity<Group> createGroup(@RequestBody GroupRequestDTO groupRequest) {
        System.out.println("Otrzymany JSON: " + groupRequest);

        // Konwertuj DTO na Entity
        Group group = new Group(groupRequest.getName(), groupRequest.getDescription());
        Group savedGroup = groupService.saveGroup(group);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedGroup);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Group> updateGroup(@PathVariable Long id, @RequestBody GroupRequestDTO groupRequest) {
        Group group = new Group(groupRequest.getName(), groupRequest.getDescription());
        Group updatedGroup = groupService.updateGroup(id, group);
        return ResponseEntity.ok(updatedGroup);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGroup(@PathVariable Long id) {
        groupService.deleteGroup(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/fill-percentage")
    public ResponseEntity<Double> getGroupFillPercentage(@PathVariable Long id) {
        Group group = groupService.getGroupById(id);
        double fillPercentage = group.getEmployees().size() * 10.0;
        return ResponseEntity.ok(fillPercentage);
    }
}