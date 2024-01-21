package com.anorneto.promosniper.infrastructure.repositories;

import com.anorneto.promosniper.domain.dto.PromoDTO;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.statement.PreparedBatch;

import java.util.List;
import java.util.Optional;

public class PromoRepository {

    private final Jdbi jdbi;

    public PromoRepository(Jdbi jdbi) {
        this.jdbi = jdbi;
    }

    public void insert(PromoDTO promo) {
        try (Handle handle = jdbi.open()) {
            handle.createUpdate(
                            "INSERT INTO promo (source_type, source_name, source_identifier, description, product_url, product_name, product_price, product_photo, created_date) "
                                    + "VALUES (:sourceType, :sourceName, :sourceIdentifier, :description, :productUrl, :productName, :productPrice, :productPhoto, :createdDate) "
                                    + "ON CONFLICT (source_name,source_identifier) DO NOTHING")
                    .bindBean(promo)
                    .execute();
        }
    }

    public void batchInsert(List<PromoDTO> promoDTOList) {
        try (Handle handle = jdbi.open()) {
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

    public List<PromoDTO> getAll() {
        try (Handle handle = jdbi.open()) {
            return handle.createQuery("SELECT * FROM promo")
                    .mapToBean(PromoDTO.class)
                    .list();
        }
    }

    public PromoDTO getById(int id) {
        try (Handle handle = jdbi.open()) {
            Optional<PromoDTO> result = handle.createQuery("SELECT * FROM promo WHERE id = :id")
                    .bind("id", id)
                    .mapToBean(PromoDTO.class)
                    .findFirst();
            return result.orElse(null);
        }
    }

    public void deleteById(int id) {
        try (Handle handle = jdbi.open()) {
            handle.createUpdate("DELETE FROM promo WHERE id = :id")
                    .bind("id", id)
                    .execute();
        }
    }

    public void update(PromoDTO promo) {
        try (Handle handle = jdbi.open()) {
            handle.createUpdate(
                            "UPDATE promo SET source_type = :sourceType, source_name = :sourceName, source_identifier = :sourceIdentifier, description = :description, product_url = :productUrl, product_name = :productName, product_price = :productPrice, product_photo = :productPhoto, created_date = :createdDate WHERE id = :id")
                    .bindBean(promo)
                    .execute();
        }
    }
}