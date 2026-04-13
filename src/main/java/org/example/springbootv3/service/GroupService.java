package org.example.springbootv3.service;

import org.example.springbootv3.entity.Group;
import org.example.springbootv3.repository.GroupRepository;
import org.example.springbootv3.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupService {

    @Autowired
    private GroupRepository groupRepository;

    public List<Group> getAllGroups() {
        return groupRepository.findAll();
    }

    public Group getGroupById(Long id) {
        return groupRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Grupa", "id", id));
    }

    public Group saveGroup(Group group) {
        return groupRepository.save(group);
    }

    public Group updateGroup(Long id, Group groupDetails) {
        Group group = getGroupById(id);
        group.setName(groupDetails.getName());
        group.setDescription(groupDetails.getDescription());
        return groupRepository.save(group);
    }

    public void deleteGroup(Long id) {
        Group group = getGroupById(id);
        groupRepository.delete(group);
    }

    public boolean existsByName(String name) {
        return groupRepository.existsByName(name);
    }

    public Group findByName(String name) {
        return groupRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Grupa", "nazwa", name));
    }
}