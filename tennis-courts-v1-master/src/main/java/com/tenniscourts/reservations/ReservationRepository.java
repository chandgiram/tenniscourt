package com.tenniscourts.reservations;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepository extends CrudRepository<Reservation, Long> {

    List<Reservation> findBySchedule_Id(Long scheduleId);


    @Query(value = "SELECT r.id FROM RESERVATION  r JOIN SCHEDULE s ON r.SCHEDULE_ID = s.ID WHERE  s.START_DATE_TIME  <  NOW();", nativeQuery = true)
    List<Long> findPastReservations();

}
