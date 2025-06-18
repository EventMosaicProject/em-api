package com.neighbor.eventmosaic.api.document;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.OffsetDateTime;

/**
 * Представление документа упоминания GDELT в Elasticsearch для использования в em-api.
 * Индекс создается и управляется Kafka Connect, поэтому createIndex = false.
 * ВАЖНО: У упоминаний GDELT нет простого уникального ID. Поле id здесь добавлено
 * как требование Spring Data Elasticsearch. При реальном использовании оно, скорее всего,
 * не будет напрямую использоваться для поиска, либо его нужно генерировать при записи
 * (что не наш случай, так как запись идет через Kafka Connect).
 * Для чтения, поиск будет по GlobalEventID и другим полям.
 */
@Data
@NoArgsConstructor
@Document(indexName = "gdelt-mentions-*", createIndex = false)
public class MentionDocument {

    @Id
    private String id;

    @Field(type = FieldType.Long, name = "globalEventId")
    private Long globalEventId;

    @Field(type = FieldType.Date, format = DateFormat.date_optional_time, name = "eventTimeDate")
    private OffsetDateTime eventTimeDate;

    @Field(type = FieldType.Date, format = DateFormat.date_optional_time, name = "mentionTimeDate")
    private OffsetDateTime mentionTimeDate;

    @Field(type = FieldType.Integer, name = "mentionType")
    private Integer mentionType;

    @Field(type = FieldType.Keyword, name = "mentionSourceName")
    private String mentionSourceName;

    @Field(type = FieldType.Keyword, name = "mentionIdentifier")
    private String mentionIdentifier;

    @Field(type = FieldType.Integer, name = "sentenceId")
    private Integer sentenceId;

    @Field(type = FieldType.Integer, name = "actor1CharOffset")
    private Integer actor1CharOffset;

    @Field(type = FieldType.Integer, name = "actor2CharOffset")
    private Integer actor2CharOffset;

    @Field(type = FieldType.Integer, name = "actionCharOffset")
    private Integer actionCharOffset;

    @Field(type = FieldType.Integer, name = "inRawText")
    private Integer inRawText;

    @Field(type = FieldType.Integer, name = "confidence")
    private Integer confidence;

    @Field(type = FieldType.Integer, name = "mentionDocLen")
    private Integer mentionDocLen;

    @Field(type = FieldType.Double, name = "mentionDocTone")
    private Double mentionDocTone;

    @Field(type = FieldType.Text, name = "mentionDocTranslationInfo", index = false)
    private String mentionDocTranslationInfo;
}