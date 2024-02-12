package com.tenniscourts.schedules;

import com.tenniscourts.config.BaseRestController;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@RestController
@RequestMapping("schedule")
public class ScheduleController extends BaseRestController {

    private final ScheduleService scheduleService;

    @PostMapping("/addSchedule")
    public ResponseEntity<Void> addScheduleTennisCourt(@RequestBody CreateScheduleRequestDTO createScheduleRequestDTO) throws Exception {
        return ResponseEntity.created(locationByEntity(scheduleService.addSchedule(createScheduleRequestDTO).getId())).build();
    }

    @RequestMapping(value = "/schedule/availability/{startDate}/{endDate}", method = RequestMethod.GET)
    public ResponseEntity<Map<String, List<String>>> findSchedulesByDates(@PathVariable("startDate") @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate startDate,
                                                                          @PathVariable("endDate") @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate endDate) {
        return ResponseEntity.ok(scheduleService.findSchedulesByDates(LocalDateTime.of(startDate, LocalTime.of(0, 0)), LocalDateTime.of(endDate, LocalTime.of(23, 59))));
    }

    @RequestMapping(value = "/schedule/{id}", method = RequestMethod.GET)
    public ResponseEntity<ScheduleDTO> findByScheduleId(@PathVariable("id") Long scheduleId) throws Exception {
        return ResponseEntity.ok(scheduleService.findSchedule(scheduleId));
    }

    @PostMapping("/create")
    public ResponseEntity<ScheduleDTO> createScheduleSlot(@RequestBody CreateScheduleRequestDTO createScheduleRequestDTO) {
        ScheduleDTO createdSchedule = scheduleService.createScheduleSlot(createScheduleRequestDTO);
        return ResponseEntity.created(URI.create("/api/schedules/" + createdSchedule.getId())).body(createdSchedule);
    }

}