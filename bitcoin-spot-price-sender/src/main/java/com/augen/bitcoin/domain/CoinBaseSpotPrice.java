package com.augen.bitcoin.domain;

/**
 * This is data model for coinbase spot price api result
 * @author quoca
 *
 */
public class CoinBaseSpotPrice {
	CoinBaseSpotPriceData DataObject;


	 // Getter Methods 

	 public CoinBaseSpotPriceData getData() {
	  return DataObject;
	 }

	 // Setter Methods 

	 public void setData(CoinBaseSpotPriceData dataObject) {
	  this.DataObject = dataObject;
	 }
	}
	