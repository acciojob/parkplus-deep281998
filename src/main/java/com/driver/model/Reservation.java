package com.driver.model;

import javax.persistence.*;

@Entity
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int numberOfHour;


    @ManyToOne
    @JoinColumn
    private Spot spot;

    @ManyToOne
    @JoinColumn
    private User user;

    @OneToOne(mappedBy = "reservation" , cascade = CascadeType.ALL)
    private Payment payment;

    public Reservation(int id, int numberOfHour, Spot spot, User user, Payment payment) {
        this.id = id;
        this.numberOfHour = numberOfHour;
        this.spot = spot;
        this.user = user;
        this.payment = payment;
    }

    public Reservation() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNumberOfHour() {
        return numberOfHour;
    }

    public void setNumberOfHour(int numberOfHour) {
        this.numberOfHour = numberOfHour;
    }

    public Spot getSpot() {
        return spot;
    }

    public void setSpot(Spot spot) {
        this.spot = spot;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }
}
