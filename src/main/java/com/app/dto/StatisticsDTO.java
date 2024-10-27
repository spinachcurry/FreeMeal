package com.app.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class StatisticsDTO {
	private String areaNm;
	private double meanOfPrice;
	private double stdOfPrice;
	private double meanOfParty;
	private double stdOfParty;
}
