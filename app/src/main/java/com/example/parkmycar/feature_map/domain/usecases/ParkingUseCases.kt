package com.example.parkmycar.feature_map.domain.usecases

data class ParkingUseCases(
    val findParkingLots: FindParkingLots,
    val findRouteToParkingLot: FindRouteToParkingLot,
    val getSavedSpot: GetSavedSpot,
    val getSavedSpots: GetSavedSpots,
    val saveSpot: SaveSpot,
    val deleteSpotFromDb: DeleteSpotFromDb,
    val checkIfSaved: CheckIfSaved,
    val computeRoute: ComputeRoute
)
