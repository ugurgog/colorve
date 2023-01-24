package com.example.colorve.model.color;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ColorImage implements Serializable{

	private static final long serialVersionUID = -5465024941199433315L;

	@JsonProperty("bare")
	private String bare;
	
	@JsonProperty("named")
	private String named;

	public String getBare() {
		return bare;
	}

	public void setBare(String bare) {
		this.bare = bare;
	}

	public String getNamed() {
		return named;
	}

	public void setNamed(String named) {
		this.named = named;
	}
}
