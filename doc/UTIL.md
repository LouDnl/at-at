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
```

## Usage (from docstrings)
```clj
(overtone.at-at.util/from-string string)
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
```

```clj
(overtone.at-at.util/from-local-time LocalTime)
"### Returns the supplied java.date.LocalTime object as epoch milliseconds
 ### Or milliseconds until if supplied with :after true

 Arguments:
 * time   - java.date.LocalTime required
 * :after - boolean optional, default is false"
```

```clj
(overtone.at-at.util/from-local-date-time LocalDateTime)
"### Returns the supplied java.date.LocalDateTime object as epoch milliseconds
 ### Or milliseconds until if supplied with :after true

 Arguments:
 * time   - java.date.LocalDateTime required
 * :after - boolean optional, default is false"
```

```clj
(overtone.at-at.util/from-instant Instant)
"### Returns the supplied java.date.Instant object as epoch milliseconds
 ### Or milliseconds until if supplied with :after true

 Arguments:
 * time   - java.date.Instant required
 * :after - boolean optional, default is false"
```
