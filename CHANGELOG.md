# at-at Changelog

## 1.6.0-SNAPSHOT unreleased
_Insert date here_
* Implement tools.deps and tools.build
* Add deps.edn
* Add cognitect test-runner
* Update test runs (unfinished)

## 1.5.1
_25th January 2023_

* Use public class for reflection - fixes bb compatibility (thanks @borkdude)
* Use core functions (thanks @borkdude)
* Add from-sql-time, from-sql-timestamp and get-locale-options to util
* Rework from-string to accept any locale and format when supplied
* Update / fix docstrings
* Update [UTIL.md](doc/UTIL.md) accordingly

## 1.5.0
_22nd January 2023_

* Add util namespace, see [UTIL.md](doc/UTIL.md)
  - add functions for parsing java.time objects
  - supports LocalTime, LocalDateTime and Instant
  - add function for parsing time strings

## 1.4.0
_9th January 2023_

* Add optional uid for scheduled jobs - see docstrings for description \
  When a job is being scheduled with the same uid throws an Exception error
* Add uid to print-method for ScheduledJob
* Add unit tests (run with `lein all test`)
* Add license, plugins and profiles
* Add config.edn for clj-kondo
* Some small printing fixes

## 1.3.1
_10th March 2022_

* Added some simple tests
* Updated readme
* Brought in changes from 1.3.0 that handle exceptions better

## 1.2.0
_28th May 2013_

* BREAKING CHANGE - Remove support for specifying stop-delayed? and
  stop-periodic? scheduler strategies.
* Jobs now correctly report as no longer being scheduled when pool is shutdown.

## 1.1.0
_14th Jan 2013_

* Added new fn `interspaced` which will call fun repeatedly with a
  specified interspacing.
* Added missing trailing quotes when printing schedule.
