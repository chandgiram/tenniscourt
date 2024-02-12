package com.tenniscourts.schedules;

import com.tenniscourts.config.persistence.BaseEntity;
import com.tenniscourts.reservations.Reservation;
import com.tenniscourts.tenniscourts.TennisCourt;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table
@Getter
@Setter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, exclude = "reservations")
public class Schedule extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @NotNull
    private TennisCourt tennisCourt;

    @Column
    @NotNull
    private LocalDateTime startDateTime;

    @Column
    @NotNull
    private LocalDateTime endDateTime;

    @OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL)
    private List<Reservation> reservations;

    public void addReservation(Reservation reservation) {
        if (this.reservations == null) {
            this.reservations = new ArrayList<>();
        }

        reservation.setSchedule(this);
        this.reservations.add(reservation);
    }
}
