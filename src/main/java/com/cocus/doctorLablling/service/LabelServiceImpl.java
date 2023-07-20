package com.cocus.doctorLablling.service;

import com.cocus.doctorLablling.model.Label;
import com.cocus.doctorLablling.repository.LabelRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class LabelServiceImpl implements LabelService {
    @Autowired
    private LabelRepository labelRepository;

    @Override
    @Cacheable(cacheNames = "labels")
    public Label createLabel(Label label) {
        log.info("Creating a new Label: {}", label);
        Label createdLabel =labelRepository.save(label);
        log.info("Created Label: {}", createdLabel);
        return createdLabel;
    }

    @Override
    @Cacheable(cacheNames = "labels")
    public Optional<Label> getLabelById(String code) {
        log.info("Retrieving Label with Label Code: {}", code);
        return labelRepository.findById(code);
    }

    @Override
    @Cacheable(cacheNames = "labels")
    public Optional<Label> updateLabel(String code, Label label) {
        log.info("Updating Label with ID: {}", code);

        Optional<Label> existingLabel = labelRepository.findById(code);
        if (existingLabel.isPresent()) {
            Label labelObj = existingLabel.get();
            labelObj.setDescription(label.getDescription());
            Label updatedLabelEntity =labelRepository.save(labelObj);
            log.info("Updated Label: {}", updatedLabelEntity);
            return Optional.of(updatedLabelEntity);
        } else {
            return Optional.empty();
        }
    }

    @Override
    @Cacheable(cacheNames = "labels")
    public boolean deleteLabel(String code) {
        log.info("Deleting Label with ID: {}", code);
        Optional<Label> existingLabel = labelRepository.findById(code);
        if (existingLabel.isPresent()) {
            labelRepository.deleteById(code);
            log.info("Label with ID {} deleted successfully", code);
            return true;
        } else {
            log.warn("Label with Label Code {} not found, deletion failed", code);
            return false;
        }
    }

    @Override
    @Cacheable(cacheNames = "labels")
    public List<Label> getAllLabels() {
        log.info("Retrieving all Labels");
        return labelRepository.findAll();
    }
}