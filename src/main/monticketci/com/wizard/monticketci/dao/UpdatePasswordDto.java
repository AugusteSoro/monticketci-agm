package com.wizard.monticketci.dao;

import lombok.Data;

@Data
public class UpdatePasswordDto {
	
	private String email;
	private String ancienPassword;
	private String newPassword;

}
