package com.spro.pcshop.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "item_details")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    private Integer warranty;
    private String producingCountry;
    private String color;
    private int brightness;
    private double diagonal;
    private int frequency;
    private String maxDisplayResolution;
    private String matrixType;
    @ManyToMany(fetch = FetchType.EAGER, cascade = {
            CascadeType.DETACH,
            CascadeType.MERGE,
            CascadeType.PERSIST,
            CascadeType.REFRESH
    })
    @JoinTable(name = "itemdetails_connectioninterfaces",
            joinColumns = @JoinColumn(
                    name = "item_detail_id", referencedColumnName = "id"
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "connection_interface_id", referencedColumnName = "id"
            )
    )
    private List<ConnectionInterface> interfaces;
    @ManyToMany(fetch = FetchType.EAGER, cascade = {
            CascadeType.DETACH,
            CascadeType.MERGE,
            CascadeType.PERSIST,
            CascadeType.REFRESH
    })
    @JoinTable(name = "itemdetails_features",
            joinColumns = @JoinColumn(
                    name = "item_detail_id", referencedColumnName = "id"
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "feature_id", referencedColumnName = "id"
            )
    )
    private List<Feature> features;

}
