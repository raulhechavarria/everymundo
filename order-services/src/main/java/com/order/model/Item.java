package com.order.model;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

//import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import javax.annotation.Generated;


@Document(collection = "Item")
public class Item implements Serializable{

	
	  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Transient public static final String SEQUENCE_NAME = "users_sequence";
	  
	  @Id private long id;
	 
	@NotBlank
    private String name;
		
	@NotBlank
    private Double price;

	
	
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}



	

		
}
