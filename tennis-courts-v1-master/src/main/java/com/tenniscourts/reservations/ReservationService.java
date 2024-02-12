package com.tenniscourts.reservations;

import com.tenniscourts.exceptions.EntityNotFoundException;
import com.tenniscourts.guests.Guest;
import com.tenniscourts.guests.GuestService;
import com.tenniscourts.schedules.Schedule;
import com.tenniscourts.schedules.ScheduleMapper;
import com.tenniscourts.schedules.ScheduleService;
import com.tenniscourts.tenniscourts.TennisCourtService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
@Setter
@Getter
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationMapper reservationMapper;
    private  ScheduleService scheduleService;
    private final GuestService guestService;

    public ReservationService(ReservationRepository reservationRepository,
                              ReservationMapper reservationMapper,
                              GuestService guestService) {
        this.reservationRepository = reservationRepository;
        this.reservationMapper = reservationMapper;
        this.guestService = guestService;
    }

    public void setScheduleService(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }


    private  ScheduleMapper scheduleMapper;

    private static final BigDecimal DEPOSIT_AMOUNT = BigDecimal.TEN;

    public ReservationDTO bookReservation(CreateReservationRequestDTO createReservationRequestDTO) throws Exception {
        Schedule schedule = scheduleService.findScheduleData(createReservationRequestDTO.getScheduleId());
        Guest guest = guestService.findById(createReservationRequestDTO.getGuestId());
        Reservation reservation = reservationMapper.map(createReservationRequestDTO);
        reservation.setSchedule(schedule);
        reservation.setPrice(DEPOSIT_AMOUNT);
        reservation.setReservationStatus(ReservationStatusEnum.READY_TO_PLAY);
        reservation.setGuest(guest);
        reservation.setPrice(reservation.getPrice());
        reservation.setRefundValue(getRefundValue(reservation));
        reservationMapper.map(reservationRepository.save(reservation));

        ReservationDTO reservationDTO = new ReservationDTO();
        reservationDTO.setId(reservation.getId());
        reservationDTO.setSchedule(scheduleMapper.map(schedule));
        reservationDTO.setGuestId(createReservationRequestDTO.getGuestId());
        reservationDTO.setReservationStatus(reservation.getReservationStatus().toString());
        reservationDTO.setRefundValue(DEPOSIT_AMOUNT);

        return reservationDTO;

    }
    public ReservationDTO findReservation(Long reservationId) {
        return reservationRepository.findById(reservationId).map(reservationMapper::map).orElseThrow(() -> {
            throw new EntityNotFoundException("Reservation not found.");
        });
    }

    public ReservationDTO cancelReservation(Long reservationId) {
        Reservation reser = reservationRepository.findById(reservationId).map(reservation -> {
            BigDecimal refundValue = getRefundValue(reservation);
            return this.updateReservation(reservation, refundValue, ReservationStatusEnum.CANCELLED);
        }).orElseThrow(() -> {
            throw new EntityNotFoundException("Reservation not found.");
        });
        return reservationMapper.map(reser);
    }

    private Reservation updateReservation(Reservation reservation, BigDecimal refundValue, ReservationStatusEnum status) {
        reservation.setReservationStatus(status);
        LocalDateTime currentDateTime = LocalDateTime.now();
        LocalDateTime scheduleDateTime = reservation.getSchedule().getStartDateTime();
        if(Duration.between(currentDateTime, scheduleDateTime).toHours() > 24) {
            reservation.setPrice(reservation.getPrice().subtract(refundValue));
        }
        reservation.setRefundValue(refundValue);
        return reservationRepository.save(reservation);
    }


    public BigDecimal getRefundValue(Reservation reservation) {
        long hours = ChronoUnit.MINUTES.between(LocalDateTime.now(), reservation.getSchedule().getStartDateTime());

        if (hours < 0) {
            return BigDecimal.ZERO;
        }
        if (hours > 0 && hours < 2 * 60) {
            return reservation.getPrice().multiply(BigDecimal.valueOf(3 / 4));
        }
        if (hours >= 2 * 60 && hours < 12 * 60) {
            return reservation.getPrice().multiply(BigDecimal.valueOf(1 / 2));
        }
        if (hours >= 12 * 60 && hours < 24 * 60) {
            return reservation.getPrice().multiply(BigDecimal.valueOf(1 / 4));
        }
        if (hours >= 24 * 60) {
            return reservation.getPrice();
        }
        return BigDecimal.ZERO;
    }

    public ReservationDTO rescheduleReservation(Long preReservationId, Long scheduleId) throws Exception{
        Reservation preReservation = reservationRepository.findById(preReservationId).map(reservation -> {
            BigDecimal refundValue = getRefundValue(reservation);
            return this.updateReservation(reservation, refundValue, ReservationStatusEnum.CANCELLED);
        }).orElseThrow(() -> {
            throw new EntityNotFoundException("Reservation not found.");
        });
        preReservation.setReservationStatus(ReservationStatusEnum.RESCHEDULED);
        reservationRepository.save(preReservation);
        ReservationDTO newReservation = bookReservation(CreateReservationRequestDTO.builder()
                .guestId(preReservation.getGuest().getId())
                .scheduleId(scheduleId)
                .build());
        newReservation.setPreviousReservation(reservationMapper.map(preReservation));
        return newReservation;
    }
    public List<ReservationDTO> findPastReservation() {
        List<Long> pastReservations = reservationRepository.findPastReservations();
        List<ReservationDTO> reservationDTO = new ArrayList<>();
        for (Long r : pastReservations) {
            Reservation reservation = reservationRepository.findById(r).get();
            reservationDTO.add(reservationMapper.map(reservation));
        }
        return reservationDTO;
    }
}
