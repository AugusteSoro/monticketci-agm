/**
 * 
 */
package com.wizard.monticketci.entities;

import lombok.Data;

/**
 * @author Soro created on June 25, 2021
 */


@Data
public class PaymentNotify {

	private String _cpm_site_id;
	private String _signature;
	private String _cpm_amount;
	private String _cpm_trans_id; 
	private String _cashDeskUri; 
	private String _cpm_custom; 
	private String _cpm_currency; 
	private String _cpm_payid; 
	private String _cpm_payment_date; 
	private String _cpm_payment_time; 
	private String _cpm_error_message; 
	private String _payment_method; 
	private String _cpm_phone_prefixe; 
	private String _cel_phone_num; 
	private String _cpm_ipn_ack; 
	private String _created_at; 
	private String _updated_at; 
	private String _cpm_result; 
	private String _cpm_trans_status; 
	private String _cpm_designation; 
	private String _buyer_name; 

	


}
