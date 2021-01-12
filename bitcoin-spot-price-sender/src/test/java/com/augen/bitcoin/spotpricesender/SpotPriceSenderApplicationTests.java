package com.augen.bitcoin.spotpricesender;

import org.junit.jupiter.api.Test;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.stream.binder.test.OutputDestination;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.messaging.Message;
import org.springframework.messaging.converter.CompositeMessageConverter;
import org.springframework.messaging.converter.MessageConverter;

import com.augen.bitcoin.SpotPriceSenderApplication;
import com.augen.bitcoin.domain.SpotPriceDetail;

import static org.assertj.core.api.Assertions.assertThat;

public class SpotPriceSenderApplicationTests {

	@Test
	public void contextLoads() {
	}

	@Test
	public void testSpotPriceSender() {
		try (ConfigurableApplicationContext context = new SpringApplicationBuilder(
				TestChannelBinderConfiguration
						.getCompleteConfiguration(SpotPriceSenderApplication.class))
				.web(WebApplicationType.NONE)
				.run()) {

			OutputDestination target = context.getBean(OutputDestination.class);
			Message<byte[]> sourceMessage = target.receive(10000);

			final MessageConverter converter = context.getBean(CompositeMessageConverter.class);
			SpotPriceDetail spotPriceDetail = (SpotPriceDetail) converter
					.fromMessage(sourceMessage, SpotPriceDetail.class);

			assertThat(spotPriceDetail.getCurrency()).isNotEmpty();
			assertThat(spotPriceDetail.getPrice()).isBetween(0.0, 1500000.0);
		}
	}
}
