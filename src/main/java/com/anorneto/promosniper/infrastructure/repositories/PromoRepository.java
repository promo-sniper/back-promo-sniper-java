package com.anorneto.promosniper.infrastructure.repositories;

import com.anorneto.promosniper.domain.dto.PromoDTO;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.statement.PreparedBatch;
import org.jdbi.v3.sqlobject.SqlObject;
import ru.vyarus.guicey.jdbi3.installer.repository.JdbiRepository;
import ru.vyarus.guicey.jdbi3.tx.InTransaction;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;

/**
 * @author Anor Neto
 */
@JdbiRepository
@InTransaction
public interface PromoRepository extends SqlObject {

    default void insert(PromoDTO promo) {
        try (Handle handle = getHandle()) {
            handle.createUpdate(
                            "INSERT INTO promo (source_type, source_name, source_identifier, description, product_url, product_name, product_price, product_photo, created_date) "
                                    + "VALUES (:sourceType, :sourceName, :sourceIdentifier, :description, :productUrl, :productName, :productPrice, :productPhoto, :createdDate) "
                                    + "ON CONFLICT (source_name,source_identifier) DO NOTHING")
                    .bindBean(promo)
                    .execute();
        }
    }

    default void batchInsert(List<PromoDTO> promoDTOList) {
        try (Handle handle = getHandle()) {
            PreparedBatch batch = handle.prepareBatch(
                    "INSERT INTO promo (source_type, source_name, source_identifier, description, product_url, product_name, product_price, product_photo, created_date) "
                            + "VALUES (:sourceType, :sourceName, :sourceIdentifier, :description, :productUrl, :productName, :productPrice, :productPhoto, :createdDate) "
                            + "ON CONFLICT (source_name,source_identifier) DO NOTHING");

            for (PromoDTO promo : promoDTOList) {
                batch.bindBean(promo).add();
            }

            batch.execute();
        }
    }

    default HashMap<String, Integer> getMaxIdentifierBySourceName(String sourceType) {
        try (Handle handle = getHandle()) {
            return handle.createQuery("SELECT source_name, MAX(source_identifier) AS source_identifier\n"
                            + "FROM promo WHERE source_type = :sourceType GROUP BY source_name")
                    .bind("sourceType", sourceType)
                    .mapToMap()
                    .collect(Collector.of(
                            HashMap::new,
                            (accum, item) -> {
                                accum.put((String) item.get("source_name"), (Integer) item.get("source_identifier"));
                            },
                            (l, r) -> {
                                l.putAll(r); // While jdbi does not process rows in parallel,
                                return l; // the Collector contract encourages writing combiners
                            },
                            Collector.Characteristics.IDENTITY_FINISH));
        }
    }

    default List<PromoDTO> getForSouceName(String sourceName) {
        try (Handle handle = getHandle()) {
            return handle.createQuery("SELECT * FROM promo where source_name = :sourceName")
                    .bind("sourceName", sourceName)
                    .mapToBean(PromoDTO.class)
                    .list();
        }
    }

    default PromoDTO getById(int id) {
        try (Handle handle = getHandle()) {
            Optional<PromoDTO> result = handle.createQuery("SELECT * FROM promo WHERE id = :id")
                    .bind("id", id)
                    .mapToBean(PromoDTO.class)
                    .findFirst();
            return result.orElse(null);
        }
    }

    default void deleteById(int id) {
        try (Handle handle = getHandle()) {
            handle.createUpdate("DELETE FROM promo WHERE id = :id")
                    .bind("id", id)
                    .execute();
        }
    }

    default void update(PromoDTO promo) {
        try (Handle handle = getHandle()) {
            handle.createUpdate(
                            "UPDATE promo SET source_type = :sourceType, source_name = :sourceName, source_identifier = :sourceIdentifier, description = :description, product_url = :productUrl, product_name = :productName, product_price = :productPrice, product_photo = :productPhoto, created_date = :createdDate WHERE id = :id")
                    .bindBean(promo)
                    .execute();
        }
    }
}