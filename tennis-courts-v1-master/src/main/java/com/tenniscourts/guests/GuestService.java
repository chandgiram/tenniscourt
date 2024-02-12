package com.tenniscourts.guests;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class GuestService {

    private final GuestRepository guestRespository;

    private final GuestMapper guestMapper;


    public Guest findById(Long tennisCourtId) throws Exception {
        return guestRespository.findById(tennisCourtId).get();
    }

    public Guest findByName(String name) throws Exception {
        return guestRespository.findByNameContains(name).get();
    }

    public Guest createGuest(CreateGuestDTO createGuestDTO) {
        return guestRespository.save(guestMapper.map(createGuestDTO));
    }

    public Guest updateGuest(UpdateGuestDTO updateGuestDTO) throws Exception {
        Guest guest = guestRespository.findById(updateGuestDTO.getId()).get();
        guest.setName(updateGuestDTO.getName());
        return guestRespository.save(guest);
    }
    public List<Guest> findAll() {
        return guestRespository.findAll();
    }

    public void deleteGuest(Long guestId) {
        guestRespository.deleteById(guestId);
    }
}