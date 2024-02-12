package com.tenniscourts.guests;

import com.tenniscourts.config.BaseRestController;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@Controller
@RequestMapping("/guest")
public class GuestController extends BaseRestController {

    private final GuestService guestService;

    @GetMapping("addguest")
    public ResponseEntity<Guest> createGuest(@RequestBody CreateGuestDTO createGuestDTO) {
        return ResponseEntity.created(locationByEntity(guestService.createGuest(createGuestDTO).getId())).build();
    }

    @PutMapping("/")
    public Guest updateGuest(@RequestBody UpdateGuestDTO updateGuestDTO) throws Exception {
        return guestService.updateGuest(updateGuestDTO);
    }

    @GetMapping("/guest")
    public ResponseEntity<List<Guest>> findAllGuest() throws Exception {
        return ResponseEntity.ok(guestService.findAll());
    }

    @DeleteMapping(path = "/{guestId}")
    public void deleteGuest(@PathVariable("guestId") Long guestId) throws Exception {
        guestService.deleteGuest(guestId);
    }

    @RequestMapping(path = "/guest/{id}", method = RequestMethod.GET)
    public ResponseEntity<Guest> findGuest(@PathVariable("id") Long id) throws Exception {
        return ResponseEntity.ok(guestService.findById(id));
    }

    @GetMapping("/{guestName}")
    public ResponseEntity<Guest> findGuest(@PathVariable("guestName") String guestName) throws Exception {
        return ResponseEntity.ok(guestService.findByName(guestName));
    }
}