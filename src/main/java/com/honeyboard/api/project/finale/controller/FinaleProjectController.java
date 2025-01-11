//package com.honeyboard.api.project.finale.controller;
//
//import com.honeyboard.api.project.finale.service.FinaleProjectService;
//import com.honeyboard.api.project.finale.service.FinaleTeamService;
//import com.honeyboard.api.user.model.User;
//import com.honeyboard.api.user.service.UserService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.time.LocalDate;
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/v1/project/finale")
//@RequiredArgsConstructor
//public class FinaleProjectController {
//
//    private final
//    UserService userService;
//    private final
//    FinaleProjectService finaleProjectService;
//    private final
//    FinaleTeamService finaleTeamService;
//
//    @GetMapping()
//    public ResponseEntity<FinaleProjectResponse> getAllFinaleProjects(
//            @RequestParam(required = false) Integer generationId) {
//
//        if (generationId == null) {
//            generationId = userService.getActiveGenerationId();
//        }
//
//        List<FinaleProject> projects = finaleProjectService.getAllFinaleProject(generationId);
//        if (projects.isEmpty()) {
//            return ResponseEntity.noContent().build();
//        }
//
//        List<User> users = userService.getAllUsersWithTeamInfo(generationId);
//        List<FinaleTeam> submits = finaleTeamService.findStatusByDate(LocalDate.now());
//
//        FinaleProjectResponse response = new FinaleProjectResponse(projects, users, submits);
//        return ResponseEntity.ok(response);
//    }
//
//    @PostMapping
//    public
//    ResponseEntity<FinaleProject> createFinaleProject(@RequestBody FinaleProject project) {
//        finaleProjectService.saveFinaleProject(project);
//        return ResponseEntity.status(HttpStatus.CREATED).body(project);
//    }
//
//    @PutMapping("/team/update")
//    public ResponseEntity<FinaleProject> updateFinaleProject(
//            @RequestBody FinaleProject finaleProject) {
//
//        finaleProjectService.updateFinaleProject(finaleProject);
//        return ResponseEntity.ok(finaleProject);
//    }
//
//    @DeleteMapping("/team/{finaleProjectId}")
//    public ResponseEntity<Void> deleteFinaleProject(@PathVariable int finaleProjectId) {
//        finaleProjectService.softDeleteFinaleProject(finaleProjectId);
//        return ResponseEntity.ok().build();
//    }
//
//}
