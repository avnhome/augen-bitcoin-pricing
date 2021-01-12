package com.augen.bitcoin.pricingapi;

import java.util.HashMap;
import java.util.Map;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.cloud.stream.binder.test.InputDestination;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.converter.CompositeMessageConverter;
import org.springframework.messaging.converter.MessageConverter;
import com.augen.bitcoin.BitcoinPriceApiApplication;
import com.augen.bitcoin.domain.PriceFactorDetail;

@ExtendWith(OutputCaptureExtension.class)
public class BitcoinPricingLoggerTests {

	@Test
	public void contextLoads() {
	}

	@Test
	public void testBitCoinPricingLogger(CapturedOutput output) {
		try (ConfigurableApplicationContext context = new SpringApplicationBuilder(
				TestChannelBinderConfiguration
						.getCompleteConfiguration(BitcoinPriceApiApplication.class))
				.web(WebApplicationType.NONE)
				.run()) {

			InputDestination source = context.getBean(InputDestination.class);

			PriceFactorDetail priceFactorDetail = new PriceFactorDetail();
			priceFactorDetail.setCurrency("NZD");
			priceFactorDetail.setPrice(500000.1);
			priceFactorDetail.setProfitFatorValue(0.05);
			
			final MessageConverter converter = context.getBean(CompositeMessageConverter.class);
			Map<String, Object> headers = new HashMap<>();
			headers.put("contentType", "application/json");
			MessageHeaders messageHeaders = new MessageHeaders(headers);
			final Message<?> message = converter.toMessage(priceFactorDetail, messageHeaders);

			source.send(message);
			//Awaitility.await().until(output::getOut, value -> {System.out.println("88888: " + value.toString());return true;});
			Awaitility.await().until(output::getOut, value -> value.contains("PriceFactorDetail [profitFatorValue=0.05, currency=NZD, price=500000.1]"));
		}
	}
}
