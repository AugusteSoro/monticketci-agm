package com.wizard.monticketci.dao;

import javax.validation.constraints.NotBlank;

public class ParameterDto {
	
	
	public String parameterid;
    
    public String parameterlibele;
    
    @NotBlank
    public String parametertype;
	
	public float value;

	public float value1;
	
	@NotBlank
	public String parametercode;
	
	public String parameterstatut;
	
	public Boolean parameterenable;
	
	public String parameterdescription;
	
	public String userpisteaudit ;

}
