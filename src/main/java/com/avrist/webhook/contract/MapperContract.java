package com.avrist.webhook.contract;

import com.avrist.webhook.exception.MapperException;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public interface MapperContract <INPUT, OUTPUT> {

    OUTPUT map(INPUT input) throws MapperException;

    default List<OUTPUT> map(List<INPUT> inputs) throws MapperException {
        if (inputs == null || inputs.isEmpty()) {
            return Collections.emptyList();
        }
        try {
            return inputs.stream()
                    .filter(Objects::nonNull)
                    .map(this::map)
                    .collect(Collectors.toList());
        } catch (RuntimeException e) {
            throw new MapperException("Failed to map input list", e);
        }
    }

}
