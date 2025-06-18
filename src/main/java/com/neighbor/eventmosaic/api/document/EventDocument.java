package com.neighbor.eventmosaic.api.document;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.GeoPointField;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;

import java.time.OffsetDateTime;

/**
 * Представление документа события GDELT в Elasticsearch для использования в em-api.
 * Индекс создается и управляется Kafka Connect, поэтому createIndex = false.
 */
@Data
@NoArgsConstructor
@Document(indexName = "gdelt-events-*", createIndex = false)
public class EventDocument {

    @Id
    @Field(type = FieldType.Long, name = "globalEventId")
    private Long globalEventId;

    @Field(type = FieldType.Date, format = DateFormat.epoch_millis, name = "eventDate")
    private OffsetDateTime eventDate;

    @Field(type = FieldType.Keyword, name = "actor1Code")
    private String actor1Code;

    @Field(type = FieldType.Text, name = "actor1Name")
    private String actor1Name;

    @Field(type = FieldType.Keyword, name = "actor1CountryCode")
    private String actor1CountryCode;

    @Field(type = FieldType.Keyword, name = "actor1KnownGroupCode")
    private String actor1KnownGroupCode;

    @Field(type = FieldType.Keyword, name = "actor1EthnicCode")
    private String actor1EthnicCode;

    @Field(type = FieldType.Keyword, name = "actor1Religion1Code")
    private String actor1Religion1Code;

    @Field(type = FieldType.Keyword, name = "actor1Religion2Code")
    private String actor1Religion2Code;

    @Field(type = FieldType.Keyword, name = "actor1Type1Code")
    private String actor1Type1Code;

    @Field(type = FieldType.Keyword, name = "actor1Type2Code")
    private String actor1Type2Code;

    @Field(type = FieldType.Keyword, name = "actor1Type3Code")
    private String actor1Type3Code;

    @Field(type = FieldType.Keyword, name = "actor2Code")
    private String actor2Code;

    @Field(type = FieldType.Text, name = "actor2Name")
    private String actor2Name;

    @Field(type = FieldType.Keyword, name = "actor2CountryCode")
    private String actor2CountryCode;

    @Field(type = FieldType.Keyword, name = "actor2KnownGroupCode")
    private String actor2KnownGroupCode;

    @Field(type = FieldType.Keyword, name = "actor2EthnicCode")
    private String actor2EthnicCode;

    @Field(type = FieldType.Keyword, name = "actor2Religion1Code")
    private String actor2Religion1Code;

    @Field(type = FieldType.Keyword, name = "actor2Religion2Code")
    private String actor2Religion2Code;

    @Field(type = FieldType.Keyword, name = "actor2Type1Code")
    private String actor2Type1Code;

    @Field(type = FieldType.Keyword, name = "actor2Type2Code")
    private String actor2Type2Code;

    @Field(type = FieldType.Keyword, name = "actor2Type3Code")
    private String actor2Type3Code;

    @Field(type = FieldType.Byte, name = "isRootEvent")
    private Byte isRootEvent;

    @Field(type = FieldType.Keyword, name = "eventCode")
    private String eventCode;

    @Field(type = FieldType.Keyword, name = "eventBaseCode")
    private String eventBaseCode;

    @Field(type = FieldType.Keyword, name = "eventRootCode")
    private String eventRootCode;

    @Field(type = FieldType.Integer, name = "quadClass")
    private Integer quadClass;

    @Field(type = FieldType.Double, name = "goldsteinScale")
    private Double goldsteinScale;

    @Field(type = FieldType.Integer, name = "numMentions")
    private Integer numMentions;

    @Field(type = FieldType.Integer, name = "numSources")
    private Integer numSources;

    @Field(type = FieldType.Integer, name = "numArticles")
    private Integer numArticles;

    @Field(type = FieldType.Double, name = "avgTone")
    private Double avgTone;

    @Field(type = FieldType.Short, name = "actor1GeoType")
    private Short actor1GeoType;

    @Field(type = FieldType.Text, name = "actor1GeoFullName")
    private String actor1GeoFullName;

    @Field(type = FieldType.Keyword, name = "actor1GeoCountryCode")
    private String actor1GeoCountryCode;

    @Field(type = FieldType.Keyword, name = "actor1GeoAdm1Code")
    private String actor1GeoAdm1Code;

    @Field(type = FieldType.Keyword, name = "actor1GeoAdm2Code")
    private String actor1GeoAdm2Code;

    @GeoPointField
    @Field(name = "actor1Location")
    private GeoPoint actor1Location;

    @Field(type = FieldType.Keyword, name = "actor1GeoFeatureId")
    private String actor1GeoFeatureId;

    @Field(type = FieldType.Short, name = "actor2GeoType")
    private Short actor2GeoType;

    @Field(type = FieldType.Text, name = "actor2GeoFullName")
    private String actor2GeoFullName;

    @Field(type = FieldType.Keyword, name = "actor2GeoCountryCode")
    private String actor2GeoCountryCode;

    @Field(type = FieldType.Keyword, name = "actor2GeoAdm1Code")
    private String actor2GeoAdm1Code;

    @Field(type = FieldType.Keyword, name = "actor2GeoAdm2Code")
    private String actor2GeoAdm2Code;

    @GeoPointField
    @Field(name = "actor2Location")
    private GeoPoint actor2Location;

    @Field(type = FieldType.Keyword, name = "actor2GeoFeatureId")
    private String actor2GeoFeatureId;

    @Field(type = FieldType.Integer, name = "actionGeoType")
    private Integer actionGeoType;

    @Field(type = FieldType.Text, name = "actionGeoFullName")
    private String actionGeoFullName;

    @Field(type = FieldType.Keyword, name = "actionGeoCountryCode")
    private String actionGeoCountryCode;

    @Field(type = FieldType.Keyword, name = "actionGeoAdm1Code")
    private String actionGeoAdm1Code;

    @Field(type = FieldType.Keyword, name = "actionGeoAdm2Code")
    private String actionGeoAdm2Code;

    @GeoPointField
    @Field(name = "actionLocation")
    private GeoPoint actionLocation;

    @Field(type = FieldType.Keyword, name = "actionGeoFeatureId")
    private String actionGeoFeatureId;

    @Field(type = FieldType.Date, format = DateFormat.epoch_millis, name = "dateAdded")
    private OffsetDateTime dateAdded;

    @Field(type = FieldType.Keyword, name = "sourceUrl", index = false)
    private String sourceUrl;
} 