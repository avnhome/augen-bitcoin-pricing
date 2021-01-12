package com.augen.bitcoin.processor;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface InputMergerCustomProcessor {

	String SPOT_PRICE_INPUT = "spotPrice";
	String PROFIT_FACTOR_INPUT = "profitFactor";

	String INPUT_MERGER_OUTPUT = "inputMerger";

	@Input(SPOT_PRICE_INPUT)
	SubscribableChannel sportPriceInput();

	@Input(PROFIT_FACTOR_INPUT)
	SubscribableChannel profitFactorInput();

	@Output(INPUT_MERGER_OUTPUT)
	MessageChannel inputMergerOutput();

}
