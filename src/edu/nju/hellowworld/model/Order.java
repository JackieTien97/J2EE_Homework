package edu.nju.hellowworld.model;

import java.io.Serializable;
import java.sql.Timestamp;

public class Order implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2838269941211866246L;
	
	// 订单编号
	private int orderId;
	// 订单生成时间
	private Timestamp time;
	// 货物编号
	private String articleId;
	// 货物数量
	private int num;
	// 订单总价
	private int totalPrice;
	// 是否缺货
	private boolean isStockOut;
	
	
	public int getOrderId() {
		return orderId;
	}
	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}
	public Timestamp getTime() {
		return time;
	}
	public void setTime(Timestamp time) {
		this.time = time;
	}
	public String getArticleId() {
		return articleId;
	}
	public void setArticleId(String articleId) {
		this.articleId = articleId;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public int getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(int totalPrice) {
		this.totalPrice = totalPrice;
	}
	public boolean isStockOut() {
		return isStockOut;
	}
	public void setStockOut(boolean isStockOut) {
		this.isStockOut = isStockOut;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
	

}
