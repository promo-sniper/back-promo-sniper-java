package com.anorneto.promosniper.domain.usecase.jobs;

import com.anorneto.promosniper.domain.dto.PromoDTO;
import com.anorneto.promosniper.domain.dto.TelegramPromoDTO;
import com.anorneto.promosniper.infrastructure.repositories.PromoRepository;
import com.anorneto.promosniper.infrastructure.repositories.TelegramRepository;
import io.dropwizard.jobs.Job;
import io.dropwizard.jobs.annotations.DelayStart;
import io.dropwizard.jobs.annotations.Every;
import lombok.extern.java.Log;
import org.jdbi.v3.core.Jdbi;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Log
@DelayStart("30s")
@Every(value = "30m", jobName = "scrap-telegram")
public class ScrapTelegramJob extends Job {

    private final Jdbi jdbi;

    public ScrapTelegramJob(Jdbi jdbi) {
        this.jdbi = jdbi;
    }

    @Override
    public void doJob(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        LocalDateTime startTime = LocalDateTime.now();
        // TODO -> put this logic inside UseCase
        // Set Guice Injector + JDBI Plugin for it
        TelegramRepository telegramRepository = new TelegramRepository();
        PromoRepository promoRepository = new PromoRepository(jdbi);

        List<TelegramPromoDTO> telegramPromoListRapadura = null, telegramPromoListBenchPromo = null;
        logger.info("Starting ScrapTelegramJob");

        HashMap<String, Integer> maxIdentifierBySourceName = promoRepository.getMaxIdentifierBySourceName("Telegram");
        Integer afterIdRapadura = maxIdentifierBySourceName.getOrDefault("rapaduraofertas", null);
        Integer afterIdBenchPromo = maxIdentifierBySourceName.getOrDefault("BenchPromos", null);

        try {
            telegramPromoListRapadura = telegramRepository.getAll("rapaduraofertas", afterIdRapadura);
            telegramPromoListBenchPromo = telegramRepository.getAll("BenchPromos", afterIdBenchPromo);
        } catch (IOException ex) {
            logger.severe("Error while getting Telegram Promos: " + ex.getMessage());
            throw new JobExecutionException(ex, true);
        }

        List<TelegramPromoDTO> combined = new ArrayList<>();
        combined.addAll(telegramPromoListRapadura);
        combined.addAll(telegramPromoListBenchPromo);

        List<PromoDTO> promoDTOList =
                combined.stream().map(TelegramPromoDTO::toPromoDTO).toList();

        promoRepository.batchInsert(promoDTOList);

        LocalDateTime endTime = LocalDateTime.now();
        long elaspedTimeMillis = ChronoUnit.MILLIS.between(startTime, endTime);
        logger.info("Finish ScrapTelegramJob - Inserted: " + promoDTOList.size()
                + " | Elasped: "
                + elaspedTimeMillis
                + " ms");
    }
}
