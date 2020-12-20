package com.microservice.nav.dao;

public class Security {
	
	/**
	 * Providing JSON structure for securities data to be used
	 */
	private int totalQty=0;
	private double price=0;
	private double assetsValue;
	
	@Override
	public String toString() {
		return "totalQty: "+totalQty+" price: "+price;
	}
	
	public void setTotalQty(int qty) {
		totalQty = qty;
		updateAssetValue();
	}
	
	public void addTotalQty(int qty) {
		totalQty+= qty;
		updateAssetValue();
	}
	
	public double getAssetsValue() {
		return assetsValue;
	}
	
	public void setPrice(double price) {
		this.price = price;
		updateAssetValue();
	}
	
	public void updateAssetValue() {
		assetsValue = totalQty*price;
	}
}
