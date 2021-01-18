# AUGEN BITCOIN PRICING


## 1. How To build and run application.

### 1.1 PREREQUISITE
	- Java 8 or above
	
	- maven
	
	- kafka and apache-zookeeper
	
	Would you please help to make sure these above thing has install correctly before start the AUGEN BITCOIN PRICING APP.
	
### 1.2 Detail step to run this application.

	step 1: Execute the following command to "mvn clean package" to build and run unit test for this project.
	
	step 2: Start bitcoin-profit-factor-sender module using the following command.
	
		java -jar bitcoin-profit-factor-sender\target\bitcoin-profit-factor-sender-0.0.1-SNAPSHOT.jar
		
	step 3: Start bitcoin-spot-price-sender module using the following command.
	
		java -jar bitcoin-spot-price-sender\target\bitcoin-spot-price-sender-0.0.1-SNAPSHOT.jar
		
    step 4: Start bitcoin-input-merger-processor module using the following command.

		java -jar bitcoin-input-merger-processor\target\bitcoin-input-merger-processor-0.0.1-SNAPSHOT.jar
		
	step 5 Start bitcoin-pricing-api module using the following command.
	
 		java -jar bitcoin-pricing-api\target\bitcoin-pricing-api-0.0.1-SNAPSHOT.jar
	
The API endpoints available at: http://localhost:7083/quotes/btc?amount=10&currency=NZD

	This API will return a quot base on amount of bitcoin and currency.
	
	At the moment only NZD dolar is supported.

	How many btc by this amount of coin.
	http://localhost:7083/quotes/money?amount=10&currency=NZD
	
	
Regarding how to calculate the price:

	A. Spot price * profit factor * amount = profit margin
	
	B. Buy or Sell price = spot price * amount + profit margin.
	
NOTE: It is a bit difference from the formula for the project description.

	
## 2. Explains which API you have chosen to retrieve pricing information and why

	I have chosen the Option 2: Poll the Coinbase REST API with a 1 second interval.
	
		Option 2: Poll the Coinbase REST API with a 1 second interval
		
		(https://developers.coinbase.com/docs/wallet/guides/price-data)
		
	Because I am most familiar with this option.

## 3. Explains application of design / patterns.


- The project has 4 modules:
	1. bitcoin-profit-factor-sender is in charge of generating a profit factor value per second then send it to kafka topic "profit-factor"
	2. bitcoin-spot-price-sender is in charge of polling the Coinbase REST API with a 1 second interval then send it to kafka topic "spot-price"
	3. bitcoin-input-merger-processor is in charge of merging the result from the above module by lising on both kafka topic "profit-factor" and "spot-price"
		then send the result to kafka topic "input-merger"
	4. bitcoin-pricing-api receive the result of bitcoin-input-merger-processor module by listing on kafka topic "input-merger"
		then transform and store it in PriceDetailRegistry.
		The PriceDetailRegistry is in memory-database to store all spot price and profit factor by currency.
		This module is also contains the Rest API http://localhost:7083/quote?amount=10&currency=NZD
	
	
- This project take benifit of multiple modules like:
	+ it follows micro service architecture.
	+ Make it easy and flexible to deploy the application and also scale up down the module upon the situation
	+ You can re-use the code from the modules across different projects.
	+ I make the code clear and easy to read and understand by others.
	
	
	
- The project follow SOLID and DRY is to make the code clean, easy to change, maintain and grow up in the near future.


## 4. Provides a summary breakdown of the approximate time that you have spent developing thesolution


○ Preparation: 30 minutes
○ Coding: 9 hours (including tests)
○ Documentation:  1 hour
○ Building and testing: 1 hour
○ Grand total: 11.5 hours
