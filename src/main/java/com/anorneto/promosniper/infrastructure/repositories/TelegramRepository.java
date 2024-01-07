package com.anorneto.promosniper.infrastructure.repositories;

import com.anorneto.promosniper.domain.dto.TelegramPromoDTO;
import jakarta.ws.rs.core.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

import static java.lang.String.format;


public class TelegramRepository {

    private static void log(String msg, String... vals) {
        System.out.println(format(msg, vals));
    }

    public Response getAll() throws IOException {
        ArrayList<TelegramPromoDTO> telegramPromoList = new ArrayList<>();
        Document doc = Jsoup.connect("https://t.me/s/rapaduraofertas").get();
        log(doc.title());

        //Pattern pattern = Pattern.compile("w3schools", Pattern.CASE_INSENSITIVE);
        // Matcher matcher = pattern.matcher("Visit W3Schools!");

        Elements telegramHtmlElementList = doc.select("div[data-post] > div.tgme_widget_message_bubble");
        for (Element telegramHtmlElement : telegramHtmlElementList) {
            String photoURL = telegramHtmlElement.select("div.tgme_widget_message_bubble > a.tgme_widget_message_photo_wrap").attr("style");
            String text = telegramHtmlElement.select("div.tgme_widget_message_text").text();
            Element topicFooter = telegramHtmlElement.select("div.tgme_widget_message_footer > div.tgme_widget_message_info").getFirst();
            String numVisulizations = topicFooter.getElementsByClass("tgme_widget_message_views").getFirst().text();
            String topicDateTime = topicFooter.select("span.tgme_widget_message_meta > a.tgme_widget_message_date > time").getFirst().attr("datetime");
            log(
                    "%s\n%s\n%s\n%s\n", photoURL, text, numVisulizations, topicDateTime
            );
            telegramPromoList.add(
                    new TelegramPromoDTO(
                            numVisulizations, text, photoURL, topicDateTime
                    )
            );
        }
        return Response.ok().entity(telegramPromoList).build();
    }
}
