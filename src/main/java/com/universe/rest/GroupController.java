package com.universe.rest;

import com.universe.dto.group.GroupDto;
import com.universe.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/groups")
public class GroupController {
    private final GroupService groupService;

    @GetMapping
    public Page<GroupDto> findAll(Pageable pageable) {
        return groupService.findAll(pageable);
    }

    @GetMapping("/{id}")
    public GroupDto getOneById(@PathVariable Long id) {
        return groupService.findOne(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<GroupDto> create(@RequestBody GroupDto groupDto) {
        var createdGroup = groupService.create(groupDto);
        var location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdGroup.getId()).toUri();
        return ResponseEntity.created(location).body(createdGroup);
    }

    @PutMapping("/{id}")
    public GroupDto update(@PathVariable Long id, @RequestBody GroupDto groupDto) {
        return groupService.update(id, groupDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        groupService.delete(id);
    }
}
