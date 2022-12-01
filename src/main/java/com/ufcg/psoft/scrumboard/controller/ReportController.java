package com.ufcg.psoft.scrumboard.controller;

import com.ufcg.psoft.scrumboard.enums.Position;
import com.ufcg.psoft.scrumboard.enums.PositionConverter;
import com.ufcg.psoft.scrumboard.enums.PositionSet;
import com.ufcg.psoft.scrumboard.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class ReportController {

    @Autowired
    private ReportService reportService;

    @GetMapping(value = "/report/{userID}/{projectID}/")
    public ResponseEntity<?> createReport(@NotNull @RequestParam PositionSet position, @PathVariable String projectID, @PathVariable String userID) {

        String taskResponse;
        taskResponse = reportService.creatReportByProject(userID, projectID, position);
        return new ResponseEntity<String>(taskResponse, HttpStatus.CREATED);
    }

}
