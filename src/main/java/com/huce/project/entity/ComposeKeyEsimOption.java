package com.huce.project.entity;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class ComposeKeyEsimOption implements Serializable {
    @Column(name = "esim_id")
    private int esim_id;

    @Column(name = "option_id")
    private int option_id;

}
