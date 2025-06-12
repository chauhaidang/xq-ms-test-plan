package com.xq.testplan.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "linked_requirements")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class LinkedRequirements extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "link_id")
    private long linkId;

    @Column(name = "reqa_id", nullable = false)
    private int reqAId;

    @Column(name = "reqb_id", nullable = false)
    private int reqBId;
}
