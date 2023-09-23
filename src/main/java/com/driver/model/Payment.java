package com.driver.model;

import javax.persistence.*;

@Entity
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private PaymentMode paymentMode;

    private boolean paymentCompleted;

    @OneToOne
    @JoinColumn
    private Reservation reservation;

    public Payment(int id, boolean paymentCompleted, Reservation reservation , PaymentMode paymentMode) {
        this.id = id;
        this.paymentCompleted = paymentCompleted;
        this.reservation = reservation;
        this.paymentMode=paymentMode;
    }

    public Payment() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isPaymentCompleted() {
        return paymentCompleted;
    }

    public void setPaymentCompleted(boolean paymentCompleted) {
        this.paymentCompleted = paymentCompleted;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public PaymentMode getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(PaymentMode paymentMode) {
        this.paymentMode = paymentMode;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }
}
