package com.anorneto.promosniper.presenters.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Duration;
import java.time.ZonedDateTime;

/**
 * @author Anor Neto
 */
@Getter
@AllArgsConstructor
public class CommonApiResponse<T> {
    private RequestMetrics requestMetrics;
    private T data;

    public CommonApiResponse() {
        this.requestMetrics = new RequestMetrics();
    }

    public void setData(T data) {
        this.data = data;
        this.requestMetrics.setEndTime();
    }

    public CommonApiResponse<T> ok(T data) {
        this.setData(data);
        return this;
    }

    public CommonApiResponse<T> created(T data) {
        this.setData(data);
        return this;
    }

    public CommonApiResponse<T> noContent() {
        this.setData(null);
        return this;
    }

    public CommonApiResponse<T> accepted() {
        this.setData(null);
        return this;
    }

    @Getter
    public static class RequestMetrics {
        private final ZonedDateTime initTime;
        private ZonedDateTime endTime;
        private long durationsMs;

        public RequestMetrics() {
            this.initTime = ZonedDateTime.now();
        }

        public void setEndTime() {
            this.endTime = ZonedDateTime.now();
            // Get the Different in milliseconds
            this.durationsMs = Duration.between(this.initTime, this.endTime).toMillis();
        }
    }
}
