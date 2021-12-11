package com.co2.sensor.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@NoArgsConstructor
@Data
public class SensorDTO implements Serializable
{
	@NotNull(message = "Sensor level is mandatory")
	private Integer co2Level;

	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@NotNull(message = "Sensor date is mandatory")
	private Date date;

	public SensorDTO(Integer co2Level, Date date)
	{
		this.co2Level = co2Level;
		this.date = date;
	}
}
