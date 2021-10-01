package ru.gx.fin.base.db.descriptors;

import ru.gx.fin.base.db.dto.Place;
import ru.gx.fin.base.db.dto.PlacesPackage;
import ru.gx.fin.base.db.entities.PlaceEntity;
import ru.gx.kafka.upload.OutcomeTopicUploadingDescriptorsDefaults;
import ru.gx.std.upload.UploadingEntityDescriptor;

public class PlacesUploadingDescriptor extends UploadingEntityDescriptor<Place, PlacesPackage, PlaceEntity> {
    public PlacesUploadingDescriptor(String topic, OutcomeTopicUploadingDescriptorsDefaults defaults) {
        super(topic, defaults);
    }
}
