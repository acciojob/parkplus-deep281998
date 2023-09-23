package com.driver.services.impl;

import com.driver.model.*;
import com.driver.repository.ParkingLotRepository;
import com.driver.repository.ReservationRepository;
import com.driver.repository.SpotRepository;
import com.driver.repository.UserRepository;
import com.driver.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ReservationServiceImpl implements ReservationService {
    @Autowired
    UserRepository userRepository3;
    @Autowired
    SpotRepository spotRepository3;
    @Autowired
    ReservationRepository reservationRepository3;
    @Autowired
    ParkingLotRepository parkingLotRepository3;
    @Override
    public Reservation reserveSpot(Integer userId, Integer parkingLotId, Integer timeInHours,
                                   Integer numberOfWheels) throws Exception {
        Optional<User> optionalUser = userRepository3.findById(userId);
        if(!optionalUser.isPresent()){
            throw new Exception("Cannot make reservation");
        }
        User user = optionalUser.get();
        Optional<ParkingLot> optionalParkingLot = parkingLotRepository3.findById(parkingLotId);
        if(!optionalParkingLot.isPresent()){
            throw new Exception("Cannot make reservation");
        }
        ParkingLot parkingLot = optionalParkingLot.get();
        int minCost = 0;
        Spot minspot = new Spot();
        List<SpotType> spotTypeList = new ArrayList<>();
        if(numberOfWheels <= 2){
            spotTypeList.add(SpotType.TWO_WHEELER);
            spotTypeList.add(SpotType.FOUR_WHEELER);
            spotTypeList.add(SpotType.OTHERS);
        }
        else if(numberOfWheels == 3 || numberOfWheels == 4){
            spotTypeList.add(SpotType.FOUR_WHEELER);
            spotTypeList.add(SpotType.OTHERS);
        }
        else {
            spotTypeList.add(SpotType.OTHERS);
        }
        for(Spot spot : parkingLot.getSpotList()){
            SpotType spotType = spot.getSpotType();
            if(spotTypeList.contains(spotType)){
                int cost = spot.getPricePerHour()*timeInHours;
                if(minCost > cost){
                    minCost = cost;
                    minspot = spot;
                }
            }
        }
        if(minCost != 0){
            if(minspot.getOccupied() == true){
                throw new Exception("Cannot make reservation");
            }
            Reservation reservation = new Reservation();
            reservation.setNumberOfHours(timeInHours);
            reservation.setSpot(minspot);
            reservation.setPayment(new Payment());
            reservation.setUser(user);
            minspot.setOccupied(true);

            List<Reservation> reservationList = minspot.getReservationList();
            reservationList.add(reservation);
            minspot.setReservationList(reservationList);

            List<Reservation> reservations = user.getReservationList();
            reservations.add(reservation);
            user.setReservationList(reservations);
            userRepository3.save(user);

            spotRepository3.save(minspot);
            return reservationRepository3.save(reservation);

        }
        throw new Exception("Cannot make reservation");
    }
}
