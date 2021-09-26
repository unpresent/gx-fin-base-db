package ru.gxfin.core.base.db.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.gxfin.common.kafka.loader.PartitionOffset;
import ru.gxfin.core.base.db.dbcontroller.DbController;

@RestController
@RequestMapping(value = "base-db")
public class RestProcessor {
    @Autowired
    private DbController dbController;

    @GetMapping("get-start-offset")
    public PartitionOffset getStartOffset(@RequestParam("topic") String topic) {
        final var publishedOffsetEntity = this.dbController.getOffset(topic);
        return new PartitionOffset(publishedOffsetEntity.getPartition(), publishedOffsetEntity.getStartOffset());
    }

    @PostMapping("publish-snapshot")
    public void publishSnapshot(@RequestBody PublishSnapshotRequestBody params) throws Exception {
        this.dbController.publishSnapshot(params.getTopic());
    }

}
