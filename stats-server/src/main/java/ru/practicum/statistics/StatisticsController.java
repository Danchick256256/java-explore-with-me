package ru.practicum.statistics;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.statistics.dto.HitPayload;
import ru.practicum.statistics.dto.StatisticMessage;
import ru.practicum.statistics.model.Hit;

import javax.validation.constraints.NotNull;
import java.util.List;


@RestController
@RequestMapping
@Slf4j
@Validated
@RequiredArgsConstructor
public class StatisticsController {
    private final StatisticsService statisticsService;

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public Hit addHit(@RequestBody HitPayload hitPayload) {
        return statisticsService.addHit(hitPayload);
    }

    @GetMapping("/stats")
    public List<StatisticMessage> getStatistics(@RequestParam(name = "start") @NotNull String start,
                                                @RequestParam(name = "end") @NotNull String end,
                                                @RequestParam(name = "uris", defaultValue = "") List<String> uris,
                                                @RequestParam(name = "unique", defaultValue = "false") Boolean unique) {
        return statisticsService.getStatistic(start, end, uris, unique);
    }
}