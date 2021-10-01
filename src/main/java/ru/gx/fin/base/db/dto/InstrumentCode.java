package ru.gx.fin.base.db.dto;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import ru.gx.data.AbstractDataObject;

import java.time.LocalDate;

@Getter
@Setter
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class InstrumentCode extends AbstractDataObject {
    /**
     * Код провайдера, который идентифицирует инструмент, которому принадлежит данная запись
     */
    @JsonIdentityReference(alwaysAsId = true)
    private Provider provider;

    /**
     * Код инструмента (идентификатор) на данном провайдере для данного инструмента
     */
    private String code;

    /**
     * Название инструмента на данном провайдере
     */
    private String name;

    /**
     * Порядковый номер кода (для случаев, когда провайдер о данном инструменте транслирует несколько кодов)
     */
    private int index;

    /**
     * Дата, начиная с которой данный код начал действовать
     */
    private LocalDate dateFrom;

    /**
     * Дата, до (не включая) которой данный код действовал
     */
    private LocalDate dateTo;
}
