package ru.gx.fin.base.db.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import ru.gx.fin.base.db.memdata.DerivativesMemoryRepository;

import java.time.LocalDate;

/**
 * Производный ФИ
 */
@Getter
@Setter
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(property = "guid", generator = ObjectIdGenerators.PropertyGenerator.class, resolver = DerivativesMemoryRepository.IdResolver.class)
public class Derivative extends AbstractInstrument {
    /**
     * Базовый инструмент (на который этот дериватив)
     */
    @JsonIdentityReference(alwaysAsId = true)
    private AbstractInstrument baseInstrument;

    private LocalDate expireDate;
}
