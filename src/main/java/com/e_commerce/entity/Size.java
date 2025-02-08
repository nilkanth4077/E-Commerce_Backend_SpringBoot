package com.e_commerce.entity;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class Size {

	private String name;
	
	private int quantity;

	public Size() {
		super();
		// TODO Auto-generated constructor stub
	}

}
