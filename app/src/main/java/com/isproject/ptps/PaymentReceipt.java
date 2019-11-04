package com.isproject.ptps;

public class PaymentReceipt extends DataObject implements DataModels {

    private long amount;
    private String finish;
    private String licencePlate;
    private String mpesaReceiptNumber;
    private long phoneNumber;
    private String start;
    private long transactionDate;

    public PaymentReceipt() {
    }

    public PaymentReceipt(long amount, String finish, String licencePlate, String mpesaReceiptNumber,
                          long phoneNumber, String start, long transactionDate) {
        this.amount = amount;
        this.finish = finish;
        this.licencePlate = licencePlate;
        this.mpesaReceiptNumber = mpesaReceiptNumber;
        this.phoneNumber = phoneNumber;
        this.start = start;
        this.transactionDate = transactionDate;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public String getFinish() {
        return finish;
    }

    public void setFinish(String finish) {
        this.finish = finish;
    }

    public String getLicencePlate() {
        return licencePlate;
    }

    public void setLicencePlate(String licencePlate) {
        this.licencePlate = licencePlate;
    }

    public String getMpesaReceiptNumber() {
        return mpesaReceiptNumber;
    }

    public void setMpesaReceiptNumber(String mpesaReceiptNumber) {
        this.mpesaReceiptNumber = mpesaReceiptNumber;
    }

    public long getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(long phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public long getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(long transactionDate) {
        this.transactionDate = transactionDate;
    }

    @Override
    public int getModelType() {
        return DataModels.MODEL_PAYMENT_RECEIPTS;
    }
}
