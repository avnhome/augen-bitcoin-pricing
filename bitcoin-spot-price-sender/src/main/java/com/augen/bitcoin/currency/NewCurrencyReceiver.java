package com.augen.bitcoin.currency;

import java.util.function.Consumer;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.SeekToCurrentErrorHandler;
import org.springframework.kafka.support.converter.RecordMessageConverter;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.util.backoff.FixedBackOff;

import com.augen.bitcoin.registry.CurrencyRegistry;

/**
 * This method is about to listens at input-merger kafka topic.
 * 
 * @author quoca
 *
 */
@Component
public class NewCurrencyReceiver {
	
	@Autowired
	private CurrencyRegistry currencyRegistry;

	/**
	 * his method is about to listens at input-merger kafka topic. When receive an
	 * incoming message, then add it to coin price registy. if the result return
	 * true then it is a change then log it if not, then no need to log
	 * 
	 * @return
	 */
	@Bean
	public NewTopic topic() {
		return new NewTopic("new-currency", 1, (short) 1);
	}

	@KafkaListener(id = "currencyGroup", topics = "new-currency")
	public void listen(String currency) {
		System.out.println("Receive new currency: " + currency);
		System.out.println("currency is instance of String object" + currency instanceof String);
		boolean addingResult = currencyRegistry.addNewCurrency(currency.replace("\"", ""));
		if(addingResult) {
			System.out.println("Adding new currency is done");
		}
		else {
			System.out.println("Seem like the currency is already existed");
		}
	}

/*	@Bean
	public SeekToCurrentErrorHandler errorHandler(KafkaTemplate<Object, Object> template) {
		return new SeekToCurrentErrorHandler(
				new DeadLetterPublishingRecoverer(template), new FixedBackOff(1000L, 2));
	}

	@Bean
	public RecordMessageConverter converter() {
		return new StringJsonMessageConverter();
	}*/
}