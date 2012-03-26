package com.slieer.ejbpro.dao.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name="t_field_entity")

@NamedQueries({
		@NamedQuery(name = "FieldEntity.byId", 
				query = "select en from FieldEntity en where en.id = ? ")
})
public class FieldEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	Integer id;
	
	@Column(columnDefinition="float(7,0)",name = "amount", nullable = false)
	Float amount;
	
	public Integer getId() {
		return id;
	}

	public float getAmount() {
		return amount;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setAmount(Float amount) {
		this.amount = amount;
	}
}
