# overtone.at-at.util
Uses `java.time` for parsing `LocalTime` `LocalDateTime` and `Instant` to epoch millis. \
Uses `clojure.string/split` for parsing time as string.

## Add to your project
```clj
(require 'overtone.at-at.util)
```

## Util functions
```clj
(overtone.at-at.util/from-string string)
(overtone.at-at.util/from-local-time LocalTime)
(overtone.at-at.util/from-local-date-time LocalDateTime)
(overtone.at-at.util/from-instant Instant)
(overtone.at-at.util/from-sql-time Time)
(overtone.at-at.util/from-sql-timestamp Timestamp)
(overtone.at-at.util/get-locale-options)
```

## Usage (from docstrings)
```clj
(overtone.at-at.util/from-string string)
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
```

```clj
(overtone.at-at.util/from-local-time LocalTime)
  "### Returns the supplied java.date.LocalTime object as epoch milliseconds
   ### Or milliseconds until if supplied with :after true

   Note: This always assumes the supplied time is today for conversion purposes

   Arguments:
   * time   - java.date.LocalTime required
   * :after - boolean optional, default is false"
```

```clj
(overtone.at-at.util/from-local-date-time LocalDateTime)
  "### Returns the supplied java.date.LocalDateTime object as epoch milliseconds
   ### Or milliseconds until if supplied with :after true

   Arguments:
   * date-time - java.date.LocalDateTime required
   * :after    - boolean optional, default is false"
```

```clj
(overtone.at-at.util/from-instant Instant)
  "### Returns the supplied java.date.Instant object as epoch milliseconds
   ### Or milliseconds until if supplied with :after true

   Arguments:
   * instant - java.date.Instant required
   * :after  - boolean optional, default is false"
```

```clj
(overtone.at-at.util/from-sql-time Time)
  "### Returns the supplied java.sql.Time object as epoch milliseconds
   ### Or milliseconds until if supplied with :after true

   Arguments:
   * time   - java.sql.Time required
   * :after - boolean optional, default is false"
```

```clj
(overtone.at-at.util/from-sql-timestamp Timestamp)
  "### Returns the supplied java.sql.Timestamp object as epoch milliseconds
   ### Or milliseconds until if supplied with :after true

   Arguments:
   * timestamp - java.sql.Timestamp required
   * :after    - boolean optional, default is false"
```

```clj
(overtone.at-at.util/get-locale-options)
  "### Returns a list of maps with accepted locale options for (from-string) sorted by name

   Example output:
   ({:country \"Netherlands\", :locale \"nl_NL\", :name \"Dutch (Netherlands)\"})

   #### Optional:
   **Pretty print:** `(clojure.pprint/print-table (get-locale-options))` \\
   **Find your country:** `(keep #(when (= (:country %) \"Netherlands\") %) (get-locale-options))`"
```
