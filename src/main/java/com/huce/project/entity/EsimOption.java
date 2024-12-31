package com.huce.project.entity;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "esim_options")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class EsimOption implements Serializable {
    @EmbeddedId
    private ComposeKeyEsimOption id;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "esim_id", referencedColumnName = "esim_id", insertable = false, updatable = false)
    private EsimEntity esim;
    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "option_id", referencedColumnName = "option_id", insertable = false, updatable = false)
    private OptionEntity option;
    @Column(name = "option_amount")
    private Long option_amount;

    @Column(name = "option_sale")
    private Long option_sale;

    @OneToMany(mappedBy = "esimOption", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference
    private List<OrderItemEntity> orderItems;

}
