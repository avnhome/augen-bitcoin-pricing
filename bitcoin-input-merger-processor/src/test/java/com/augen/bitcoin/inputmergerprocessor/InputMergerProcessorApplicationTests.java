package com.augen.bitcoin.inputmergerprocessor;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.stream.binder.test.InputDestination;
import org.springframework.cloud.stream.binder.test.OutputDestination;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.converter.CompositeMessageConverter;
import org.springframework.messaging.converter.MessageConverter;

import com.augen.bitcoin.InputMergerProcessorApplication;
import com.augen.bitcoin.domain.PriceFactorDetail;
import com.augen.bitcoin.domain.ProfitFactorDetail;
import com.augen.bitcoin.domain.SpotPriceDetail;

import static org.assertj.core.api.Assertions.assertThat;

public class InputMergerProcessorApplicationTests {

	@Test
	public void contextLoads() {
	}

	@Test
	public void testProcessSpotPrice() {
		try (ConfigurableApplicationContext context = new SpringApplicationBuilder(
				TestChannelBinderConfiguration.getCompleteConfiguration(
						InputMergerProcessorApplication.class)).web(WebApplicationType.NONE)
				.run("spring.cloud.stream.function.bindings.processSpotPrice-in-0=spotPrice",
						"spring.cloud.stream.function.bindings.sendPriceFactorDetail-out-0=inputMerger",
						"spring.cloud.stream.bindings.spotPrice.destination=spot-price",
						"spring.cloud.stream.bindings.inputMerger.destination=input-merger"
						
						)) {

			InputDestination source = context.getBean(InputDestination.class);

			SpotPriceDetail spotPriceDetail = new SpotPriceDetail();
			
			spotPriceDetail.setCurrency("NZD");
			spotPriceDetail.setPrice(50000.1);
			
			final MessageConverter converter = context.getBean(CompositeMessageConverter.class);
			Map<String, Object> headers = new HashMap<>();
			headers.put("contentType", "application/json");
			MessageHeaders messageHeaders = new MessageHeaders(headers);
			final Message<?> message = converter.toMessage(spotPriceDetail, messageHeaders);

			source.send(message, "spot-price");

			OutputDestination target = context.getBean(OutputDestination.class);
			Message<byte[]> sourceMessage = target.receive(10000, "input-merger");
			System.out.println("sourceMessage: testProcessSpotPrice 77777"+ sourceMessage);
			final PriceFactorDetail priceFactorDetail = (PriceFactorDetail) converter
					.fromMessage(sourceMessage, PriceFactorDetail.class);

			assertThat(priceFactorDetail.getCurrency()).isNotEmpty();
			assertThat(priceFactorDetail.getPrice()).isBetween(0.0, 500000.0);
		}
	}
	
	@Test
	public void testProcessProfitFactor() {
		try (ConfigurableApplicationContext context = new SpringApplicationBuilder(
				TestChannelBinderConfiguration.getCompleteConfiguration(
						InputMergerProcessorApplication.class)).web(WebApplicationType.NONE)
				.run("spring.cloud.stream.function.bindings.processProfitFactor-in-0=profitFactor",
						"spring.cloud.stream.function.bindings.sendPriceFactorDetail-out-0=inputMerger",
						"spring.cloud.stream.bindings.profitFactor.destination=profit-factor",
						"spring.cloud.stream.bindings.inputMerger.destination=input-merger"
						
						)) {

			InputDestination source = context.getBean(InputDestination.class);

			ProfitFactorDetail profitFactorDetail = new ProfitFactorDetail();
			
			profitFactorDetail.setProfitFatorValue(0.05);
			
			final MessageConverter converter = context.getBean(CompositeMessageConverter.class);
			Map<String, Object> headers = new HashMap<>();
			headers.put("contentType", "application/json");
			MessageHeaders messageHeaders = new MessageHeaders(headers);
			final Message<?> message = converter.toMessage(profitFactorDetail, messageHeaders);

			source.send(message, "profit-factor");

			OutputDestination target = context.getBean(OutputDestination.class);
			Message<byte[]> sourceMessage = target.receive(10000, "input-merger");

			System.out.println("sourceMessage: testProcessProfitFactor 88888"+ sourceMessage);
			final PriceFactorDetail priceFactorDetail = (PriceFactorDetail) converter
					.fromMessage(sourceMessage, PriceFactorDetail.class);

			
			assertThat(priceFactorDetail.getProfitFatorValue()).isNotNull();
		}
	}
	
}
