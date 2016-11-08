package com.greenlucky.web.domain.frontend;

/**
 * Created by Mr Lam on 8/11/2016.
 */
public class ProAccountPayload extends BasicAccountPayLoad{

    /** The Serial Version UID for Serializable classes */
    private static final long serialVersionUID = 1L;

    private String cardNumber;
    private String cardCode;
    private String cardMonth;
    private String cardYear;

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardCode() {
        return cardCode;
    }

    public void setCardCode(String cardCode) {
        this.cardCode = cardCode;
    }

    public String getCardMonth() {
        return cardMonth;
    }

    public void setCardMonth(String cardMonth) {
        this.cardMonth = cardMonth;
    }

    public String getCardYear() {
        return cardYear;
    }

    public void setCardYear(String cardYear) {
        this.cardYear = cardYear;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProAccountPayload that = (ProAccountPayload) o;

        if (cardNumber != null ? !cardNumber.equals(that.cardNumber) : that.cardNumber != null) return false;
        if (cardCode != null ? !cardCode.equals(that.cardCode) : that.cardCode != null) return false;
        if (cardMonth != null ? !cardMonth.equals(that.cardMonth) : that.cardMonth != null) return false;
        return cardYear != null ? cardYear.equals(that.cardYear) : that.cardYear == null;

    }

    @Override
    public int hashCode() {
        int result = cardNumber != null ? cardNumber.hashCode() : 0;
        result = 31 * result + (cardCode != null ? cardCode.hashCode() : 0);
        result = 31 * result + (cardMonth != null ? cardMonth.hashCode() : 0);
        result = 31 * result + (cardYear != null ? cardYear.hashCode() : 0);
        return result;
    }
}
