package ru.gxfin.core.base.db.descriptors;

import ru.gxfin.common.kafka.uploader.OutcomeTopicUploadingDescriptorsDefaults;
import ru.gxfin.core.base.db.entities.PlaceEntity;
import ru.gxfin.core.base.db.utils.UploadingEntityDescriptor;
import ru.gxfin.core.base.db.dto.Place;
import ru.gxfin.core.base.db.dto.PlacesPackage;

public class PlacesUploadingDescriptor extends UploadingEntityDescriptor<Place, PlacesPackage, PlaceEntity> {
    public PlacesUploadingDescriptor(String topic, OutcomeTopicUploadingDescriptorsDefaults defaults) {
        super(topic, defaults);
    }
}
