package com.driver.services.impl;

import com.driver.model.ParkingLot;
import com.driver.model.Spot;
import com.driver.model.SpotType;
import com.driver.repository.ParkingLotRepository;
import com.driver.repository.SpotRepository;
import com.driver.services.ParkingLotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ParkingLotServiceImpl implements ParkingLotService {
    @Autowired
    ParkingLotRepository parkingLotRepository1;
    @Autowired
    SpotRepository spotRepository1;
    @Override
    public ParkingLot addParkingLot(String name, String address) {
        ParkingLot parkingLot = new ParkingLot();
        parkingLot.setName(name);
        parkingLot.setAddress(address);
        parkingLot.setSpotList(new ArrayList<>());
        return parkingLotRepository1.save(parkingLot);

    }

    @Override
    public Spot addSpot(int parkingLotId, Integer numberOfWheels, Integer pricePerHour) {
        Optional<ParkingLot> optionalParkingLot = parkingLotRepository1.findById(parkingLotId);
        if(!optionalParkingLot.isPresent()){
            return new Spot();
        }
        Spot spot = new Spot();
        if(numberOfWheels <= 2){
            spot.setSpotType(SpotType.TWO_WHEELER);
        }
        else if(numberOfWheels == 4 || numberOfWheels == 3){
            spot.setSpotType(SpotType.FOUR_WHEELER);
        }
        else {
            spot.setSpotType(SpotType.OTHERS);
        }
        spot.setOccupied(false);
        spot.setParkingLot(optionalParkingLot.get());
        spot.setReservationList(new ArrayList<>());
        spot.setPricePerHour(pricePerHour);

        Spot savedspot = spotRepository1.save(spot);
        List<Spot> spotList = optionalParkingLot.get().getSpotList();
        spotList.add(savedspot);
        optionalParkingLot.get().setSpotList(spotList);
        parkingLotRepository1.save(optionalParkingLot.get());
        return savedspot;
    }

    @Override
    public void deleteSpot(int spotId) {
        Optional<Spot> optionalSpot = spotRepository1.findById(spotId);
        if(!optionalSpot.isPresent()){
            return;
        }
        Spot spot = optionalSpot.get();
        Optional<ParkingLot> optionalParkingLot = parkingLotRepository1.findById(spot.getParkingLot().getId());
        if(optionalParkingLot.isPresent()){
            ParkingLot parkingLot = optionalParkingLot.get();
            List<Spot> spotList = parkingLot.getSpotList();
            spotList.remove(spot);
            parkingLot.setSpotList(spotList);
            parkingLotRepository1.save(parkingLot);
            spotRepository1.deleteById(spotId);
        }

    }

    @Override
    public Spot updateSpot(int parkingLotId, int spotId, int pricePerHour) {
        Optional<ParkingLot> optionalParkingLot = parkingLotRepository1.findById(parkingLotId);
        if(!optionalParkingLot.isPresent()){
            return null;
        }
        ParkingLot parkingLot = optionalParkingLot.get();
        for(Spot spot : parkingLot.getSpotList()){
            if(spot.getId() == spotId){
                spot.setPricePerHour(pricePerHour);
                return spotRepository1.save(spot);
            }
        }
        return null;

    }

    @Override
    public void deleteParkingLot(int parkingLotId) {
        parkingLotRepository1.deleteById(parkingLotId);

    }
}
