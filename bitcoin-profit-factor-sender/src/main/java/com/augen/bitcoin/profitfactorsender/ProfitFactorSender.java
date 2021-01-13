package com.augen.bitcoin.profitfactorsender;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.augen.bitcoin.domain.ProfitFactorDetail;

/**
 * This class is about to send ramdom profit factor value to kafka topic "profit-factor"
 * @author quoca
 *
 */
@Configuration
public class ProfitFactorSender {

	private List<Double> profitFactors = Arrays.asList(0.05, 0.1, 0.12);

	/**
	 * The method helps to send random profit factor value to kafka topic "profit-factor" per seconds
	 * @return
	 */
	@Bean
	public Supplier<ProfitFactorDetail> sendEvents() {
		return () -> {
			System.out.println("Send new profit factor: ");

			double profitFactor = profitFactors.get(new Random().nextInt(profitFactors.size()));

			ProfitFactorDetail profitFactorDetail = new ProfitFactorDetail();
			profitFactorDetail.setProfitFatorValue(profitFactor);
			return profitFactorDetail;
		};
	}

}
