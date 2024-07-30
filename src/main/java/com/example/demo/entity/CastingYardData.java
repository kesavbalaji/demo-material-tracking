package com.example.demo.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "casting_yard_details")
public class CastingYardData {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "segment_barcode_id")
    private String segmentBarcodeId;

    @Column(name = "casting_date")
    private String castingDate;

    @Column(name = "location")
    private String location;

    @Column(name = "reference_level")
    private String referenceLevel;

    @Column(name = "family")
    private String family;

    @Column(name = "family_type")
    private String familyType;

    @Column(name = "description")
    private String description;

    @Column(name = "mark")
    private String mark;

    @Column(name = "type")
    private String type;

    @Column(name = "length")
    private String length;

    @Column(name = "count")
    private String count;

    @Column(name = "left_corbel_distance")
    private String leftCorbelDistance;

    @Column(name = "right_corbel_distance")
    private String rightCorbelDistance;

    @Column(name = "volume")
    private String volume;
}
