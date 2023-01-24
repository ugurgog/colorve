package com.example.colorve.model.color;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ColorDetailModel implements Serializable{

	private static final long serialVersionUID = -5465024941199433315L;

	@JsonProperty("rgb")
	private ColorRgb rgb;
	
	@JsonProperty("name")
	private ColorName name;
	
	@JsonProperty("image")
	private ColorImage image;

	public ColorRgb getRgb() {
		return rgb;
	}

	public void setRgb(ColorRgb rgb) {
		this.rgb = rgb;
	}

	public ColorName getName() {
		return name;
	}

	public void setName(ColorName name) {
		this.name = name;
	}

	public ColorImage getImage() {
		return image;
	}

	public void setImage(ColorImage image) {
		this.image = image;
	}
}
