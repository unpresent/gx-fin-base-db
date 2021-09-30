package ru.gx.fin.core.base.db.descriptors;

import ru.gx.fin.core.base.db.dto.Place;
import ru.gx.fin.core.base.db.dto.PlacesPackage;
import ru.gx.fin.core.base.db.entities.PlaceEntity;
import ru.gx.kafka.upload.OutcomeTopicUploadingDescriptorsDefaults;
import ru.gx.kafka.upload.UploadingEntityDescriptor;

public class PlacesUploadingDescriptor extends UploadingEntityDescriptor<Place, PlacesPackage, PlaceEntity> {
    public PlacesUploadingDescriptor(String topic, OutcomeTopicUploadingDescriptorsDefaults defaults) {
        super(topic, defaults);
    }
}
