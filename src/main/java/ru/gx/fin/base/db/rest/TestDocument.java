package ru.gx.fin.base.db.rest;

import lombok.*;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@Accessors(chain = true)
@EqualsAndHashCode
@RequiredArgsConstructor
@ToString
public class TestDocument {
    private final TestDocumentType type;
    private final String number;
    private final LocalDate date;
}
