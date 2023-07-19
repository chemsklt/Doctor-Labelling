package com.cocus.doctorLablling.service;

import com.cocus.doctorLablling.model.Label;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public interface LabelService {
    Label createLabel(Label label);

    Optional<Label> getLabelById(String code);

    Optional<Label> updateLabel(String code, Label label);

    boolean deleteLabel(String code);

    List<Label> getAllLabels();
}