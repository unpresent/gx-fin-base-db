package ru.gx.fin.base.db.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.gx.fin.base.db.dbcontroller.DbController;
import ru.gx.kafka.offsets.PartitionOffset;

@RestController
@RequestMapping(value = "base-db")
public class SnapshotsController {
    @Autowired
    private DbController dbController;

    @GetMapping("get-start-offset")
    public PartitionOffset getStartOffset(@RequestParam("topic") String topic) {
        return this.dbController.getLastPublishedSnapshotOffset(topic);
    }

    @PostMapping("publish-snapshot")
    public void publishSnapshot(@RequestBody PublishSnapshotRequestBody params) throws Exception {
        this.dbController.publishSnapshot(params.getTopic());
    }

}
