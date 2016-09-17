package com.zzq.pictureverifycode.entry;

/**
 * 购物车实体类
 */
public class Top_upCommitBean {
	/**
	 * CardValue : 100.0 DiscountAmount : 1.0 DiscountRate : 0.01 State : true
	 */

	public Top_upCommitBean(String discountAmount, String cardValue,
			int cardCount) {
		CardCount = cardCount;
		CardValue = cardValue;
		DiscountAmount = discountAmount;
	}

	// 商品数量
	private int CardCount;

	private String CardValue;
	private String DiscountAmount;

	public int getCardCount() {
		return CardCount;
	}

	public void setCardCount(int CardCount) {
		this.CardCount = CardCount;
	}

	public String getCardValue() {
		return CardValue;
	}

	public void setCardValue(String CardValue) {
		this.CardValue = CardValue;
	}

	public String getDiscountAmount() {
		return DiscountAmount;
	}

	public void setDiscountAmount(String DiscountAmount) {
		this.DiscountAmount = DiscountAmount;
	}

}
