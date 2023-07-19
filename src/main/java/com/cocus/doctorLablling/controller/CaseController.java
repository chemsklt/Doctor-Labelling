package com.cocus.doctorLablling.controller;

import com.cocus.doctorLablling.exception.ResourceNotFoundException;
import com.cocus.doctorLablling.model.Case;
import com.cocus.doctorLablling.model.Label;
import com.cocus.doctorLablling.service.CaseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/cases")
public class CaseController {
    @Autowired
    private CaseService caseService;

    @PostMapping
    @Operation(summary = "Create a new Case")
    @ApiResponse(responseCode = "201", description = "Doctor Label created", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = Case.class))
    })
    public ResponseEntity<Case> createCase(@RequestBody Case caseObj) {
        Case createdCase = caseService.createCase(caseObj);
        return new ResponseEntity<>(createdCase, HttpStatus.CREATED);
    }

    @PostMapping("/{caseId}/labels")
    @Operation(summary = "Create a new label in a case")
    @ApiResponse(responseCode = "201", description = "Label created", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = Case.class))
    })
    public ResponseEntity<Case> createNewLabelInCase(@PathVariable String caseId, @RequestBody Label label) {
        Case updatedCase = caseService.createNewLabelInCase(caseId, label)
                .orElseThrow(() -> new ResourceNotFoundException("Case not found with ID: " + caseId));

        return new ResponseEntity<>(updatedCase, HttpStatus.OK);
    }

    @GetMapping
    @Operation(summary = "Get all Cases")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found all Doctor Labels", content = {
                    @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Case.class))) }),
            @ApiResponse(responseCode = "404", description = "Doctor Labels not found", content = @Content)
    })
    public ResponseEntity<List<Case>> getAllCases() {
        List<Case> cases = caseService.getAllCases();
        return new ResponseEntity<>(cases, HttpStatus.OK);
    }

    @GetMapping("/{caseId}")
    @Operation(summary = "Get a Case by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the Doctor Label", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Case.class))
            }),
            @ApiResponse(responseCode = "404", description = "Doctor Label not found", content = @Content)
    })
    public ResponseEntity<Case> getCaseById(@PathVariable String caseId) {
        return caseService.getCaseById(caseId)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Case not found with ID: " + caseId));
    }

    @GetMapping(params = "/label")
    @Operation(summary = "Get cases by label")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Doctor Label deleted"),
            @ApiResponse(responseCode = "404", description = "Doctor Label not found", content = @Content)
    })
    public ResponseEntity<List<Case>> getCasesByLabel(@RequestParam("label") String labelCode) {
        List<Case> cases = caseService.getCasesByLabelCode(labelCode);
        if (!cases.isEmpty()) {
            return ResponseEntity.ok(cases);
        } else {
            throw new ResourceNotFoundException("No cases found with label code: " + labelCode);
        }
    }

    @DeleteMapping("/{caseId}/labels/{labelCode}")
    @Operation(summary = "Delete a label in a case")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Label deleted"),
            @ApiResponse(responseCode = "404", description = "Label not found", content = @Content)
    })
    public ResponseEntity<Void> deleteLabelInCase(@PathVariable String caseId, @PathVariable String labelCode) {
        boolean deleted = caseService.deleteLabelInCase(caseId, labelCode);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            throw new ResourceNotFoundException("Case not found with ID: " + caseId);
        }
    }


    @PutMapping("/{caseId}")
    @Operation(summary = "Update an existing case by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Doctor Label updated", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Case.class))
            }),
            @ApiResponse(responseCode = "404", description = "Doctor Label not found", content = @Content)
    })
    public ResponseEntity<Case> updateCase(@PathVariable String caseId, @RequestBody Case caseObj) {
        return caseService.updateCase(caseId, caseObj)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Case not found with ID: " + caseId));
    }
}
