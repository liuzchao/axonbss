package com.ai.bss.api.shoppingcart.event;

public class ShoppingCartItemQuantityIncreasedEvent extends ShoppingCartItemEvent {
	private int newQuantity;
	private long price;
	public int getNewQuantity() {
		return newQuantity;
	}
	public void setNewQuantity(int newQuantity) {
		this.newQuantity = newQuantity;
	}
	public long getPrice() {
		return price;
	}
	public void setPrice(long price) {
		this.price = price;
	}

}
