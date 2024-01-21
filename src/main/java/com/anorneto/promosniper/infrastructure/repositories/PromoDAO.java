package com.anorneto.promosniper.domain.dao;

import com.anorneto.promosniper.domain.dto.PromoDTO;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

public interface PromoDAO {

    @SqlUpdate(
            "INSERT INTO promo (id, source_type, source_name, source_identifier, description, product_url, product_name, product_price, product_photo, created_date) VALUES (:id, :sourceType, :sourceName, :sourceIdentifier, :description, :productUrl, :productName, :productPrice, :productPhoto, :createdDate)")
    void insert(PromoDTO promo);

    @SqlQuery("SELECT * FROM promo")
    @RegisterBeanMapper(PromoDTO.class)
    List<PromoDTO> getAll();

    @SqlQuery("SELECT * FROM promo WHERE id = :id")
    @RegisterBeanMapper(PromoDTO.class)
    PromoDTO getById(int id);

    @SqlUpdate("DELETE FROM promo WHERE id = :id")
    void deleteById(int id);

    @SqlUpdate(
            "UPDATE promo SET source_type = :sourceType, source_name = :sourceName, source_identifier = :sourceIdentifier, description = :description, product_url = :productUrl, product_name = :productName, product_price = :productPrice, product_photo = :productPhoto, created_date = :createdDate WHERE id = :id")
    void update(PromoDTO promo);
}