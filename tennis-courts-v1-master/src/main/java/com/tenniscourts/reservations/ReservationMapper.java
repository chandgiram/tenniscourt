package com.tenniscourts.reservations;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReservationMapper {

    Reservation map(ReservationDTO source);

    ReservationDTO map(Reservation source);

    Reservation map(CreateReservationRequestDTO source);
}
