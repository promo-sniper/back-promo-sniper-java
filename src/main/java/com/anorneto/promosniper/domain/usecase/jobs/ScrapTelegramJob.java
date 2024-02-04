package com.anorneto.promosniper.domain.usecase.jobs;

import com.anorneto.promosniper.domain.dto.PromoDTO;
import com.anorneto.promosniper.domain.dto.TelegramPromoDTO;
import com.anorneto.promosniper.infrastructure.repositories.PromoRepository;
import com.anorneto.promosniper.infrastructure.repositories.TelegramRepository;
import io.dropwizard.jobs.Job;
import io.dropwizard.jobs.annotations.DelayStart;
import io.dropwizard.jobs.annotations.Every;
import jakarta.inject.Inject;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

/**
 * @author Anor Neto
 */
@DelayStart("30s")
@Every(value = "30m", jobName = "scrap-telegram")
public class ScrapTelegramJob extends Job {

    private static final Logger logger = Logger.getLogger(ScrapTelegramJob.class.getName());

    private final PromoRepository promoRepository;
    private final TelegramRepository telegramRepository;

    @Inject
    public ScrapTelegramJob(final PromoRepository promoRepository, final TelegramRepository telegramRepository) {
        this.promoRepository = promoRepository;
        this.telegramRepository = telegramRepository;
    }

    @Override
    public void doJob(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        LocalDateTime startTime = LocalDateTime.now();
        // TODO -> put this logic inside UseCase

        // TODO: READ THIS LIST FROM DB
        List<String> listChannelToScrap = List.of(
                "rapaduraofertas",
                "BenchPromos",
                "ctofertas",
                "gtOFERTAS",
                "mmofertas",
                "ofertasadrenaline",
                "ofertaskabum",
                // "tecmundo_ofertas",
                "teniscertocupons",
                "TukOferta");

        logger.info("Starting ScrapTelegramJob");

        List<TelegramPromoDTO> combined = new ArrayList<>();

        HashMap<String, Integer> maxIdentifierBySourceName = promoRepository.getMaxIdentifierBySourceName("Telegram");

        for (String channel : listChannelToScrap) {
            List<TelegramPromoDTO> telegramPromoDTOList;

            Integer afterIdForChannel = maxIdentifierBySourceName.getOrDefault(channel, null);
            try {
                telegramPromoDTOList = telegramRepository.getAll(channel, afterIdForChannel);
            } catch (IOException ex) {
                logger.severe("Error while getting Telegram Promos: " + ex.getMessage());
                throw new JobExecutionException(ex, true);
            }

            combined.addAll(telegramPromoDTOList);
        }

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
