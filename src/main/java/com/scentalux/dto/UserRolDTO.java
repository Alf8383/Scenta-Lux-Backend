package com.scentalux.dto;

import com.scentalux.model.Role;
import com.scentalux.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRolDTO {
    private Integer  userId; 
    private Integer  roleId; 
}
