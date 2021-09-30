package ru.gx.fin.core.base.db.rest;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@ToString
public class PublishSnapshotRequestBody {
    private String topic;
}
