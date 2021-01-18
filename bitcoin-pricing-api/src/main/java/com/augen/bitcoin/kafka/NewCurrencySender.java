package com.augen.bitcoin.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

/**
 * This class is about to send new currency data to kafka.
 * 
 * @author quoca
 *
 */
@Component
public class NewCurrencySender {
	

	@Autowired
	private KafkaTemplate<Object, Object> template;

	public void sendCurrency(String currency) {
		System.out.println("Send currency to topic: " + currency + ": new-currency");
		ListenableFuture<SendResult<Object, Object>> future= this.template.send("new-currency", currency.getBytes());
		
		future.addCallback(new ListenableFutureCallback<SendResult<Object, Object>>() {

	        @Override
	        public void onSuccess(SendResult<Object, Object> result) {
	            System.out.println("Sent message=[" + currency + 
	              "] with offset=[" + result.getRecordMetadata().offset() + "]");
	        }
	        @Override
	        public void onFailure(Throwable ex) {
	            System.out.println("Unable to send message=[" 
	              + currency + "] due to : " + ex.getMessage());
	        }
	    });
	}

	
}