package com.example.colorve.model.color;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ColorName implements Serializable{

	private static final long serialVersionUID = -5465024941199433315L;

	@JsonProperty("value")
	private String value;
	
	@JsonProperty("closest_named_hex")
	private String closestNamedHex;
	
	@JsonProperty("exact_match_name")
	private Boolean exactMatchName;
	
	@JsonProperty("distance")
	private Integer distance;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getClosestNamedHex() {
		return closestNamedHex;
	}

	public void setClosestNamedHex(String closestNamedHex) {
		this.closestNamedHex = closestNamedHex;
	}

	public Boolean getExactMatchName() {
		return exactMatchName;
	}

	public void setExactMatchName(Boolean exactMatchName) {
		this.exactMatchName = exactMatchName;
	}

	public Integer getDistance() {
		return distance;
	}

	public void setDistance(Integer distance) {
		this.distance = distance;
	}	
}
