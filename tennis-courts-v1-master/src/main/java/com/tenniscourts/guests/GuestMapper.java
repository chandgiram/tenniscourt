package com.tenniscourts.guests;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GuestMapper {

    @InheritInverseConfiguration
    Guest map(CreateGuestDTO source);

    CreateGuestDTO map(Guest source);
}
