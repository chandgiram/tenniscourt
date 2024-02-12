package com.tenniscourts.schedules;

import com.tenniscourts.tenniscourts.TennisCourt;
import com.tenniscourts.tenniscourts.TennisCourtDTO;
import com.tenniscourts.tenniscourts.TennisCourtRepository;
import com.tenniscourts.tenniscourts.TennisCourtService;
import lombok.AllArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Setter
@Service
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final TennisCourtRepository tennisCourtRepository;

    private final ScheduleMapper scheduleMapper;

    private  TennisCourtService tennisCourtService;

    public ScheduleService(ScheduleRepository scheduleRepository,
                           TennisCourtRepository tennisCourtRepository,
                           ScheduleMapper scheduleMapper) {
        this.scheduleRepository = scheduleRepository;
        this.tennisCourtRepository = tennisCourtRepository;
        this.scheduleMapper = scheduleMapper;
    }

    public ScheduleDTO addSchedule(CreateScheduleRequestDTO createScheduleRequestDTO) throws Exception {
        Long tennisCourtId = createScheduleRequestDTO.getTennisCourtId();
        Optional<TennisCourt> court = tennisCourtRepository.findById(tennisCourtId);
        Schedule schedule = Schedule.builder()
                .tennisCourt(court.get())
                .startDateTime(createScheduleRequestDTO.getStartDateTime())
                .endDateTime(createScheduleRequestDTO.getStartDateTime().plusHours(1L))
                .build();
        return scheduleMapper.map(scheduleRepository.save(schedule));
    }

    public Map<String, List<String>> findSchedulesByDates(LocalDateTime startDate, LocalDateTime endDate) {
        Map<String, List<String>> result = new HashMap<>();
        List<TennisCourt> all = tennisCourtRepository.findAll();
        for (TennisCourt court : all) {
            List<String> list = new ArrayList<>();
            for (LocalDateTime date = startDate; date.isBefore(endDate); date = date.plusDays(1)) {
                for (int hour = 14; hour <= 23; hour++) {
                    List<Schedule> byTennisCourt_idAAndStartDateTime = scheduleRepository.mutualAidFlag(court.getId(), date.withHour(hour));
                    if (byTennisCourt_idAAndStartDateTime.size() == 0) list.add(date.withHour(hour).toString());
                }
            }
            result.put(court.getName(), list);
        }
        return result;
    }

    public Schedule findScheduleData(Long scheduleId) throws Exception {
        return scheduleRepository.findById(scheduleId).get();
    }

    public ScheduleDTO findSchedule(Long scheduleId) throws Exception {
        return scheduleMapper.map(findScheduleData(scheduleId));
    }

    public List<ScheduleDTO> findSchedulesByTennisCourtId(Long tennisCourtId) {
        return scheduleMapper.map(scheduleRepository.findByTennisCourt_IdOrderByStartDateTime(tennisCourtId));
    }

    public ScheduleDTO createScheduleSlot(CreateScheduleRequestDTO createScheduleRequestDTO) {
        TennisCourtDTO tennisCourt = tennisCourtService.findTennisCourtById(createScheduleRequestDTO.getTennisCourtId());
        ScheduleDTO scheduleDTO = ScheduleDTO.builder()
                .tennisCourtId(tennisCourt.getId())
                .startDateTime(createScheduleRequestDTO.getStartDateTime())
                .endDateTime(createScheduleRequestDTO.getStartDateTime().plusHours(1)) // Assuming each slot is 1 hour
                .build();
        return scheduleMapper.map(scheduleRepository.save(scheduleMapper.map(scheduleDTO)));
    }

}