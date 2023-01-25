(ns overtone.at-at.util
  {:author "LouD"
   :last-update "22-01-2023"
   :doc "This namespaces contains util functions for parsing time strings, java.time LocalTime LocalDateTime and Instant objects for scheduling with /at and /after"}
  (:require [clojure.string :refer [blank? split]])
  (:import [java.sql Timestamp Time]
           [java.text SimpleDateFormat]
           [java.time Instant LocalDate LocalDateTime LocalTime ZoneId]
           [java.time.format DateTimeFormatter]
           [java.util Locale]))


;;; Util functions

(defn from-string
  "### Returns the specified time today in epoch milliseconds
   ### Or milliseconds until time today if supplied with :after true

   #### Arguments:
   * time    - string required as representation of time \"18:25\" or \"23.20\" (default is HH:mm 24 hour notation)
   * :locale - string optional (defaults to nl_NL and 24 hour notation when not supplied)
   * - note: locale string can be nl_NL (underscore) or nl,NL (comma), default locale uses underscore.
   * :format - string optional, overrides defaults (default is HH:mm 24 hour notation)
   * - note: if locale argument is en_UK or en_US formatting will be hh.mma if format is not supplied as argument
   * :after  - boolean optional, default is false

   ##### Tip: see (get-locale-options) for more locale options
   ##### Tip: see java.time.format.DateTimeFormatter/ofPattern for more formatting options

   #### Examples:
   * `(from-string \"11:20\")` ;=> 1674382800000
   * `(from-string \"12:30\" :after true)` ;=> 1629643
   * `(from-string \"23:10\")` ;=> 1674425400000
   * `(from-string \"23:40\" :after true)` ;=> 41803046

   #### Locale and formatting examples
   * `(from-string \"02.30AM\" :locale \"en,US\")` ;=> 1674351000000
   * `(from-string \"02.30AM\" :locale \"en,UK\" :after true)` ;=> 562636
   * `(from-string \"11:30 PM\" :locale \"en,US\" :format \"hh:mm a\")` ;=> 1674426600000
   * `(from-string \"11:30 PM\" :locale \"en,UK\" :format \"hh:mm a\" :after true)` ;=> 41160551"
  [time & {:keys [locale format after] :or {locale nil format nil after false}}]
  (let [format (if (some? format) format (if (re-find #"en[,|_]U[S|K]" locale) "hh.mma" "HH:mm"))
        locale (if (some? locale) (split locale #",|_") ["nl" "NL"])
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

   Note: This always assumes the supplied time is today for conversion purposes

   Arguments:
   * time   - java.date.LocalTime required
   * :after - boolean optional, default is false"
  [^LocalTime time & {:keys [after] :or {after false}}]
  (->> time
       (LocalDateTime/of (LocalDate/now) ,,,)
       (#(.toInstant % (.getOffset (.getRules (ZoneId/systemDefault)) %)))
       (.toEpochMilli ,,,)
       (#(if-not after % (- % (System/currentTimeMillis))))))

(defn from-local-date-time
  "### Returns the supplied java.date.LocalDateTime object as epoch milliseconds
   ### Or milliseconds until if supplied with :after true

   Arguments:
   * date-time - java.date.LocalDateTime required
   * :after    - boolean optional, default is false"
  [^LocalDateTime date-time & {:keys [after] :or {after false}}]
  (-> date-time
      (#(.toInstant % (.getOffset (.getRules (ZoneId/systemDefault)) %)))
      (.toEpochMilli ,,,)
      (#(if-not after % (- % (System/currentTimeMillis))))))

(defn from-instant
  "### Returns the supplied java.date.Instant object as epoch milliseconds
   ### Or milliseconds until if supplied with :after true

   Arguments:
   * instant - java.date.Instant required
   * :after  - boolean optional, default is false"
  [^Instant instant & {:keys [after] :or {after false}}]
  (-> instant
      (.toEpochMilli ,,,)
      (#(if-not after % (- % (System/currentTimeMillis))))))

(defn from-sql-time
  "### Returns the supplied java.sql.Time object as epoch milliseconds
   ### Or milliseconds until if supplied with :after true

   Arguments:
   * time   - java.sql.Time required
   * :after - boolean optional, default is false"
  [^Time time & {:keys [after] :or {after false}}]
  (-> time
      (.toLocalTime ,,,)
      (LocalTime/from ,,,)
      (from-local-time ,,, :after after)))

(defn from-sql-timestamp
  "### Returns the supplied java.sql.Timestamp object as epoch milliseconds
   ### Or milliseconds until if supplied with :after true

   Arguments:
   * timestamp - java.sql.Timestamp required
   * :after    - boolean optional, default is false"
  [^Timestamp timestamp & {:keys [after] :or {after false}}]
  (-> timestamp
      (.toInstant ,,,)
      (.toEpochMilli ,,,)
      (#(if-not after % (- % (System/currentTimeMillis))))))


(defn get-locale-options
  "### Returns a list of maps with accepted locale options for (from-string) sorted by name

   Example output:
   ({:country \"Netherlands\", :locale \"nl_NL\", :name \"Dutch (Netherlands)\"})

   #### Optional:
   **Pretty print:** `(clojure.pprint/print-table (get-locale-options))` \\
   **Find your country:** `(keep #(when (= (:country %) \"Netherlands\") %) (get-locale-options))`"
  []
  (sort-by :name
           (keep (fn [l]
                   (when-not (blank? (str l))
                     {:name (.getDisplayName l)
                      :locale (str l)
                      :country (.getDisplayCountry l)})) (SimpleDateFormat/getAvailableLocales))))

(defn- to-local-date-time
  "Test function, parses epoch millis back to LocalDateTime"
  [epochmillis]
  (LocalDateTime/ofInstant
   (Instant/ofEpochMilli
    epochmillis)
   (ZoneId/systemDefault)))
