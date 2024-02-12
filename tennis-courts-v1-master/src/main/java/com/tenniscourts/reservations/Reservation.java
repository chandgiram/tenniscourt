package com.tenniscourts.reservations;

import com.tenniscourts.config.persistence.BaseEntity;
import com.tenniscourts.guests.Guest;
import com.tenniscourts.schedules.Schedule;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;


@Entity
@Table
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Builder(toBuilder = true)
public class Reservation extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @OneToOne
    private Guest guest;

    @ManyToOne
    @NotNull
    private Schedule schedule;


    @NotNull
    private BigDecimal price;

    @NotNull
    private ReservationStatusEnum reservationStatus = ReservationStatusEnum.READY_TO_PLAY;

    private BigDecimal refundValue;
}
