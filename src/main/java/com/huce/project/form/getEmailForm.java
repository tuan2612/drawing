package com.huce.project.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class getEmailForm {
    private List<EsimOptionKey> esim_option;
    private String email;
    private String payment_id;
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class EsimOptionKey {
        private int esim_id;
        private int option_id;
    }
}