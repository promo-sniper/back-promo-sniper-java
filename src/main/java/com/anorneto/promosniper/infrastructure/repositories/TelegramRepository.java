package com.anorneto.promosniper.infrastructure.repositories;

import com.anorneto.promosniper.domain.dto.TelegramPromoDTO;
import jakarta.inject.Singleton;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.String.format;

/**
 * @author Anor Neto
 */
@Singleton // TODO -> make interface for this
public class TelegramRepository {

    // TODO: Refactor this to receive a list of channels
    public List<TelegramPromoDTO> getAll(@NotNull String channelName, @Null Integer afterId) throws IOException {
        ArrayList<TelegramPromoDTO> telegramPromoList = new ArrayList<>();
        String channelUrl = format("https://t.me/s/%s", channelName);
        if (afterId != null) {
            channelUrl = format("%s?after=%d", channelUrl, afterId);
        }
        // TODO -> improve timeout config here, treat its exception
        // TODO -> improve performace of scrapping? get multiple docs at once? and covert latter?
        // Return Array instead of list because we know size.
        Document doc = Jsoup.connect(channelUrl)
                .timeout((int) Duration.ofSeconds(30).toMillis())
                .get();

        String sourceHeadLink = doc.head().select("link[rel=canonical]").attr("href");
        Matcher sourceNameMatcher = Pattern.compile("/s/([a-zA-Z]+)\\?before=").matcher(sourceHeadLink);

        String sourceName = sourceNameMatcher.find() ? sourceNameMatcher.group(1) : sourceHeadLink;

        Elements telegramHtmlElementList = doc.select("div[data-post]");
        for (Element telegramHtmlElement : telegramHtmlElementList) {
            int sourceId =
                    Integer.parseInt(telegramHtmlElement.attr("data-post").split("/")[1]);
            String photoURL = telegramHtmlElement
                    .select("div.tgme_widget_message_bubble > a.tgme_widget_message_photo_wrap")
                    .attr("style");
            String text =
                    telegramHtmlElement.select("div.tgme_widget_message_text").text();
            Element topicFooter = telegramHtmlElement
                    .select("div.tgme_widget_message_footer > div.tgme_widget_message_info")
                    .getFirst();

            // TODO -> include this in database
            String numVisulizations = "0";
            try {
                numVisulizations = topicFooter
                        .getElementsByClass("tgme_widget_message_views")
                        .getFirst()
                        .text();
            } catch (Exception e) {
                // TODO -> log this exception
            }
            String topicDateTime = topicFooter
                    .select("span.tgme_widget_message_meta > a.tgme_widget_message_date > time")
                    .getFirst()
                    .attr("datetime");

            telegramPromoList.add(
                    new TelegramPromoDTO(numVisulizations, text, photoURL, topicDateTime, sourceId, channelName));
        }
        return telegramPromoList;
    }
}
