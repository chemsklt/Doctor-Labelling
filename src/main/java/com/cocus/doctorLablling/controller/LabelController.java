package com.cocus.doctorLablling.controller;

import com.cocus.doctorLablling.exception.ResourceNotFoundException;
import com.cocus.doctorLablling.model.Label;
import com.cocus.doctorLablling.service.LabelService;
import com.cocus.doctorLablling.service.LabelServiceImpl;
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

@RestController
@RequestMapping("/labels")
public class LabelController {
    @Autowired
    private LabelService labelService;

    @PostMapping
    @Operation(summary = "Create a new label")
    @ApiResponse(responseCode = "201", description = "Label created", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = Label.class))
    })
    public ResponseEntity<Label> createLabel(@RequestBody Label label) {
        Label createdLabel = labelService.createLabel(label);
        return new ResponseEntity<>(createdLabel, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Get all labels")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found all labels", content = {
                    @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Label.class))) }),
            @ApiResponse(responseCode = "404", description = "Labels not found", content = @Content)
    })
    public ResponseEntity<List<Label>> getAllLabels() {
        List<Label> labels = labelService.getAllLabels();
        return new ResponseEntity<>(labels, HttpStatus.OK);
    }

    @GetMapping("/{labelCode}")
    @Operation(summary = "Get a label by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Label found", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Label.class))
            }),
            @ApiResponse(responseCode = "404", description = "Label not found", content = @Content)
    })
    public ResponseEntity<Label> getLabelById(@PathVariable String labelCode) {
        return labelService.getLabelById(labelCode)
                .map(label -> ResponseEntity.ok(label))
                .orElseThrow(() -> new ResourceNotFoundException("Label not found with code: " + labelCode));
    }

    @PutMapping("/{labelCode}")
    @Operation(summary = "Update an existing label by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Label updated", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Label.class))
            }),
            @ApiResponse(responseCode = "404", description = "Label not found", content = @Content)
    })
    public ResponseEntity<Label> updateLabel(@PathVariable String labelCode, @RequestBody Label label) {
        return labelService.updateLabel(labelCode, label)
                .map(updatedLabel -> ResponseEntity.ok(updatedLabel))
                .orElseThrow(() -> new ResourceNotFoundException("Label not found with code: " + labelCode));
    }

    @DeleteMapping("/{labelCode}")
    @Operation(summary = "Delete a label by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Label deleted"),
            @ApiResponse(responseCode = "404", description = "Label not found", content = @Content)
    })
    public ResponseEntity<Void> deleteLabel(@PathVariable String labelCode) {
        boolean deleted = labelService.deleteLabel(labelCode);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            throw new ResourceNotFoundException("Label not found with code: " + labelCode);
        }
    }
}
