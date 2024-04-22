package com.weighbridge.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "material_master")
public class MaterialMaster {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "material_id")
    private long materialId;

    @Column(name = "material_name")
    private String materialName;

    @Column(name = "material_status")
    private String materialStatus="ACTIVE";

    @Column(name = "material_created_by")
    private String materialCreatedBy;

    @Column(name = "material_created_date")
    private LocalDateTime materialCreatedDate;

    @Column(name = "material_modified_by")
    private String materialModifiedBy;

    @Column(name = "material_modified_date")
    private LocalDateTime materialModifiedDate;
}