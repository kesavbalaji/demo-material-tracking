package com.example.demo.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "casting_yard_details_hostel")
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

    @Column(name = "print_status")
    private String printStatus;

    @Column(name = "print_count")
    private Integer printCount;

    @Column(name = "dispatch_id")
    private String dispatchId;

    @Column(name = "location_status")
    private String locationStatus;

    @Column(name = "created_date")
    private Date createdDate;

    @Column(name = "reprint_reason")
    private String reprintReason;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSegmentBarcodeId() {
        return segmentBarcodeId;
    }

    public void setSegmentBarcodeId(String segmentBarcodeId) {
        this.segmentBarcodeId = segmentBarcodeId;
    }

    public String getCastingDate() {
        return castingDate;
    }

    public void setCastingDate(String castingDate) {
        this.castingDate = castingDate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getReferenceLevel() {
        return referenceLevel;
    }

    public void setReferenceLevel(String referenceLevel) {
        this.referenceLevel = referenceLevel;
    }

    public String getFamily() {
        return family;
    }

    public void setFamily(String family) {
        this.family = family;
    }

    public String getFamilyType() {
        return familyType;
    }

    public void setFamilyType(String familyType) {
        this.familyType = familyType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getLeftCorbelDistance() {
        return leftCorbelDistance;
    }

    public void setLeftCorbelDistance(String leftCorbelDistance) {
        this.leftCorbelDistance = leftCorbelDistance;
    }

    public String getRightCorbelDistance() {
        return rightCorbelDistance;
    }

    public void setRightCorbelDistance(String rightCorbelDistance) {
        this.rightCorbelDistance = rightCorbelDistance;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getPrintStatus() {
        return printStatus;
    }

    public void setPrintStatus(String printStatus) {
        this.printStatus = printStatus;
    }

    public Integer getPrintCount() {
        return printCount;
    }

    public void setPrintCount(Integer printCount) {
        this.printCount = printCount;
    }

    public String getDispatchId() {
        return dispatchId;
    }

    public void setDispatchId(String dispatchId) {
        this.dispatchId = dispatchId;
    }

    public String getLocationStatus() {
        return locationStatus;
    }

    public void setLocationStatus(String locationStatus) {
        this.locationStatus = locationStatus;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getReprintReason() {
        return reprintReason;
    }

    public void setReprintReason(String reprintReason) {
        this.reprintReason = reprintReason;
    }
}
