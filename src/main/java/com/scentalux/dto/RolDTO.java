package com.scentalux.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RolDTO {

    private Integer idRole;
    private String name;
    private String description;

}
