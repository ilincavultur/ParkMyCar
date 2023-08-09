package com.example.parkmycar.feature_map.domain.usecases

data class ParkingUseCases(
    val findParkingLot: FindParkingLot,
    val findRouteToParkingLot: FindRouteToParkingLot,
    val getSavedSpot: GetSavedSpot,
    val savedSpot: SaveSpot
)
