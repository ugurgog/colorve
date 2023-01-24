package com.example.colorve.model.color;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ColorRgb implements Serializable{

	private static final long serialVersionUID = -5465024941199433315L;

	@JsonProperty("r")
	private Integer r;
	
	@JsonProperty("g")
	private Integer g;
	
	@JsonProperty("b")
	private Integer b;
	
	@JsonProperty("value")
	private String value;

	public Integer getR() {
		return r;
	}

	public void setR(Integer r) {
		this.r = r;
	}

	public Integer getG() {
		return g;
	}

	public void setG(Integer g) {
		this.g = g;
	}

	public Integer getB() {
		return b;
	}

	public void setB(Integer b) {
		this.b = b;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
