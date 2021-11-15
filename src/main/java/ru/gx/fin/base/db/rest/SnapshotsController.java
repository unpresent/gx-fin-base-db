package ru.gx.fin.base.db.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.gx.fin.base.db.dbcontroller.DbController;
import ru.gx.kafka.offsets.PartitionOffset;

import java.time.LocalDate;

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

    /*
    @GetMapping("get-test-doc")
    public TestDocument getTestDocument(
            @RequestParam("type-code") String typeCode,
            @RequestParam("type-name") String typeName,
            @RequestParam("doc-number") String docNumber,
            @RequestParam("doc-date") LocalDate docDate
    ) {
        return new TestDocument(
                new TestDocumentType(
                        "T:" + typeCode,
                        "T:" + typeName
                ),
                "№:" + docNumber,
                docDate
        );
    }
    */

    /*
    @GetMapping("get-test-doc2")
    public TestDocument getTestDocument2(
            @RequestParam("doc-number") String docNumber,
            @RequestParam("doc-date") LocalDate docDate,
            @RequestBody TestDocumentType type
    ) {
        return new TestDocument(
                new TestDocumentType("X:" + type.getCode(), "X:" + type.getName()),
                "№:" + docNumber,
                docDate
        );
    }
    */
}
