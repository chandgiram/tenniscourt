package com.tenniscourts.reservations;

import com.tenniscourts.config.BaseRestController;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Setter
@Getter
@RestController
@RequestMapping("/reservation")
public class ReservationController extends BaseRestController {

    private  ReservationService reservationService;



    @PostMapping("bookreservation")
    public ResponseEntity<Void> bookReservation(CreateReservationRequestDTO createReservationRequestDTO) throws Exception{
        return ResponseEntity.created(locationByEntity(reservationService.bookReservation(createReservationRequestDTO).getId())).build();
    }

    @GetMapping("getreservation/{reservationId}")
    public ResponseEntity<ReservationDTO> findReservation(@PathVariable("reservationId")  Long reservationId) {
        return ResponseEntity.ok(reservationService.findReservation(reservationId));
    }

    @PostMapping("cancelreseration/{reservationId}")
    public ResponseEntity<ReservationDTO> cancelReservation(@PathVariable("reservationId") Long reservationId) {
        return ResponseEntity.ok(reservationService.cancelReservation(reservationId));
    }
    @PostMapping("/reschedule/{reservationId}/{scheduleId}")
    public ResponseEntity<ReservationDTO> rescheduleReservation(@PathVariable("reservationId") Long reservationId, @PathVariable("scheduleId") Long scheduleId) throws Exception {
        return ResponseEntity.ok(reservationService.rescheduleReservation(reservationId, scheduleId));
    }

    @GetMapping("/past")
    public ResponseEntity<List<ReservationDTO>> findPastReservation() {
        return ResponseEntity.ok(reservationService.findPastReservation());
    }
}
