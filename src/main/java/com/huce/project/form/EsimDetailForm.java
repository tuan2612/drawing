package com.huce.project.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EsimDetailForm {
    private int esim_id;       
    private String currency; 
}
