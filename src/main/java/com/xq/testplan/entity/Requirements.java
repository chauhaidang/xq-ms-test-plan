package com.xq.testplan.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Requirements extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "req_id")
    private long reqId;

    @Column(name = "uuid", nullable = false, length = 40)
    private String uuid;

    @Column(name = "title", nullable = false, length = 100)
    private String title;

    @Column(name = "description", nullable = false, length = 500)
    private String description;

    @Column(name = "scopes", nullable = false, length = 500)
    private String scopes;

    @Column(name = "tags", nullable = false, length = 200)
    private String tags;

    @Column(name = "references", nullable = false, length = 500)
    private String references;
}
