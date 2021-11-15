package ru.gx.fin.base.db.rest;

import lombok.*;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@EqualsAndHashCode
@RequiredArgsConstructor
@ToString
public class TestDocumentType {
    private final String code;
    private final String name;
}
