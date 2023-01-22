(ns overtone.at-at.util
  {:author "LouD"
   :last-update "22-01-2023"
   :doc "This namespaces contains util functions for parsing time strings, java.time LocalTime LocalDateTime and Instant objects for scheduling with /at and /after"}
  (:require [clojure.string :as string])
  (:import [java.time
            Instant LocalDate LocalDateTime LocalTime ZoneId]
           [java.time.format DateTimeFormatter]
           [java.util Locale]))


;;; Util functions

(defn from-string
  "### Returns the specified time today in epoch milliseconds
   ### Or milliseconds until time today if supplied with :after true

   Arguments:
   * time    - string required as representation of time \"18:25\" or \"23.20\" (defaults is HH:mm 24 hour notation or hh.mma for US/UK locale setting)
   * :locale - string optional (defaults to nl,NL and 24 hour notation, other options are en,US or en,UK)
   * :after  - boolean optional, default is false

   Examples:
   * (from-string \"11:20\") ;=> 1674382800000
   * (from-string \"12:30\" :after true) ;=> 1629643
   * (from-string \"23:10\") ;=> 1674425400000
   * (from-string \"23:40\" :after true) ;=> 41803046
   * (from-string \"02.30AM\" :locale \"en,US\") ;=> 1674351000000
   * (from-string \"02.30AM\" :locale \"en,US\" :after true) ;=> 562636
   * (from-string \"11.30PM\" :locale \"en,UK\") ;=> 1674426600000
   * (from-string \"11.30PM\" :locale \"en,UK\" :after true) ;=> 41160551"
  [time & {:keys [locale after] :or {locale nil after false}}]
  (let [format (if (some? locale) "hh.mma" "HH:mm")
        locale (if (some? locale) (string/split locale #",") ["nl" "NL"])
        ;time   (re-seq #"[0-9\.\:]{5}|[APM]{2}" time) ; ("10" "20" AM) or ("23" "40")
        today (LocalDate/now)
        zone  (ZoneId/systemDefault)
        formatter (DateTimeFormatter/ofPattern format (new Locale (first locale), (last locale)))
        local-time (LocalTime/parse time formatter)
        date-time (LocalDateTime/of today local-time)
        at-zone (.atZone date-time zone)
        instant (.toInstant at-zone)
        millis-at (.toEpochMilli instant)]
    (if-not after
      millis-at
      (- millis-at (System/currentTimeMillis)))))

(defn from-local-time
  "### Returns the supplied java.date.LocalTime object as epoch milliseconds
   ### Or milliseconds until if supplied with :after true

   Arguments:
   * time   - java.date.LocalTime required
   * :after - boolean optional, default is false"
  [^LocalTime time & {:keys [after] :or {after false}}]
  (->> time
       (LocalDateTime/of (LocalDate/now))
       (#(.toInstant % (.getOffset (.getRules (ZoneId/systemDefault)) %)))
       (.toEpochMilli)
       (#(if-not after % (- % (System/currentTimeMillis)))))
  #_(let [zone (ZoneId/systemDefault)
          date (LocalDate/now)
          date-time (LocalDateTime/of date time)
          offset (.getOffset (.getRules zone) date-time)
          instant (.toInstant date-time offset)
          millis-at (.toEpochMilli instant)]
      (if-not after
        millis-at
        (- millis-at (System/currentTimeMillis)))))

(defn from-local-date-time
  "### Returns the supplied java.date.LocalDateTime object as epoch milliseconds
   ### Or milliseconds until if supplied with :after true

   Arguments:
   * time   - java.date.LocalDateTime required
   * :after - boolean optional, default is false"
  [^LocalDateTime date-time & {:keys [after] :or {after false}}]
  (-> date-time
      (#(.toInstant % (.getOffset (.getRules (ZoneId/systemDefault)) %)))
      (.toEpochMilli)
      (#(if-not after % (- % (System/currentTimeMillis)))))
  #_(let [zone (ZoneId/systemDefault)
          offset (.getOffset (.getRules zone) date-time)
          instant (.toInstant date-time offset)
          millis-at (.toEpochMilli instant)]
      (if-not after
        millis-at
        (- millis-at (System/currentTimeMillis)))))

(defn from-instant
  "### Returns the supplied java.date.Instant object as epoch milliseconds
   ### Or milliseconds until if supplied with :after true

   Arguments:
   * time   - java.date.Instant required
   * :after - boolean optional, default is false"
  [^Instant instant & {:keys [after] :or {after false}}]
  (-> instant
      (.toEpochMilli)
      (#(if-not after % (- % (System/currentTimeMillis))))))

(defn- to-local-date-time
  "Internal test function, parses epoch millis back to LocalDateTime"
  [epochmillis]
  (LocalDateTime/ofInstant
   (Instant/ofEpochMilli
    epochmillis)
   (ZoneId/systemDefault)))
