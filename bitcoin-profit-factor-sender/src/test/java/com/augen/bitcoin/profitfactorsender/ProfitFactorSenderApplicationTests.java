package com.augen.bitcoin.profitfactorsender;

import org.junit.jupiter.api.Test;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.stream.binder.test.OutputDestination;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.messaging.Message;
import org.springframework.messaging.converter.CompositeMessageConverter;
import org.springframework.messaging.converter.MessageConverter;

import com.augen.bitcoin.ProfitFactorSenderApplication;
import com.augen.bitcoin.domain.ProfitFactorDetail;

import static org.assertj.core.api.Assertions.assertThat;

public class ProfitFactorSenderApplicationTests {

	@Test
	public void contextLoads() {
	}

	@Test
	public void testProfitFactorSender() {
		try (ConfigurableApplicationContext context = new SpringApplicationBuilder(
				TestChannelBinderConfiguration.getCompleteConfiguration(ProfitFactorSenderApplication.class))
						.web(WebApplicationType.NONE).run()) {

			OutputDestination target = context.getBean(OutputDestination.class);
			Message<byte[]> sourceMessage = target.receive(10000);

			final MessageConverter converter = context.getBean(CompositeMessageConverter.class);
			ProfitFactorDetail profitFactorDetail = (ProfitFactorDetail) converter.fromMessage(sourceMessage,
					ProfitFactorDetail.class);

			assertThat(profitFactorDetail.getProfitFatorValue()).isIn(0.05, 0.1, 0.12);

		}
	}
}
