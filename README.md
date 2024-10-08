                                            ________
                                        _,.-Y  |  |  Y-._
                                    .-~"   ||  |  |  |   "-.
                                    I" ""=="|" !""! "|"[]""|     _____
                                    L__  [] |..------|:   _[----I" .-{"-.
                                   I___|  ..| l______|l_ [__L]_[I_/r(=}=-P
                                  [L______L_[________]______j~  '-=c_]/=-^
                                   \_I_j.--.\==I|I==_/.--L_]
                                     [_((==)[`-----"](==)j
                                        I--I"~~"""~~"I--I
                                        |[]|         |[]|
                                        l__j         l__j
                                        |!!|         |!!|
                                        |..|         |..|
                                        ([])         ([])
                                        ]--[         ]--[
                                        [_L]         [_L]  -Row
                                       /|..|\       /|..|\
                                      `=}--{='     `=}--{='
                                     .-^--r-^-.   .-^--r-^-.
                              ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                                          __               __
                                   ____ _/ /_       ____ _/ /_
                                  / __ `/ __/______/ __ `/ __/
                                 / /_/ / /_ /_____/ /_/ / /_
                                 \__,_/\__/       \__,_/\__/

## Project status
|          | Master         | Develop      |
| -------  | :-----         | :-----       |
|          |                |              |
| Build    | ![build][1]    | ![build][2]  |
| Tests    | ![tests][3]    | ![tests][4]  |
| Commit   | ![commit][5]   | ![commit][6] |
| Tag      | ![tag][7]
| License  | ![license][8]
| Language | ![language][9]
| Issues   | ![issues][10]

[1]: https://github.com/LouDnl/at-at/actions/workflows/leinbuild.yml/badge.svg?branch=master
[2]: https://github.com/LouDnl/at-at/actions/workflows/leinbuild.yml/badge.svg?branch=dev
[3]: https://github.com/LouDnl/at-at/actions/workflows/leintest.yml/badge.svg?branch=master
[4]: https://github.com/LouDnl/at-at/actions/workflows/leintest.yml/badge.svg?branch=dev
[5]: https://shields.io/github/last-commit/LouDnl/at-at/master
[6]: https://shields.io/github/last-commit/LouDnl/at-at/dev
[7]: https://shields.io/github/v/tag/LouDnl/at-at?sort=semver
[8]: https://shields.io/github/license/LouDnl/at-at
[9]: https://shields.io/github/languages/top/LouDnl/at-at
[10]: https://shields.io/github/issues/LouDnl/at-at

### Install

Fetch at-at from github: https://github.com/LouDnl/at-at or pull from clojars: \
[![Clojars Project](https://img.shields.io/clojars/v/org.clojars.loud/at-at.svg)](https://clojars.org/org.clojars.loud/at-at)

### at-at

Simple ahead-of-time function scheduler. Allows you to schedule the execution of an anonymous function for a point in the future.

### Adaption and addition

This is an updated fork of [overtone/at-at](https://github.com/overtone/at-at). All credits go to [overtone](https://github.com/overtone).

Commits were cherry-picked from open pull-requests and forks. \
Credits:
- [adamneilson](https://github.com/adamneilson)
- [alekcz](https://github.com/alekcz)
- [bmabey](https://github.com/bmabey)
- [djui](https://github.com/djui)
- [dmac](https://github.com/dmac)
- [EdwardBetts](https://github.com/EdwardBetts)
- [jwhitbeck](https://github.com/jwhitbeck)
- [karolinepauls](https://github.com/karolinepauls)
- [thomas-shares](https://github.com/thomas-shares)

#### Unit testing

Run `lein all test` to run all unit tests

### Basic Usage

First pull in the lib:

```clj
(use 'overtone.at-at)
```

`at-at` uses `ScheduledThreadPoolExecutor`s behind the scenes which use a thread pool to run the scheduled tasks. You therefore need create a pool before you can get going:

```clj
(def my-pool (mk-pool))
```

It is possible to pass in extra options `:cpu-count`, `:stop-delayed?` and `:stop-periodic?` to further configure your pool. See `mk-pool`'s docstring for further info.

Next, schedule the function of your dreams. Here we schedule the function to execute in 1000 ms from now (i.e. 1 second):

```clj
(at (+ 1000 (now)) #(println "hello from the past!") my-pool)
```

You may also specify a description for the scheduled task with the optional `:desc` key.

Another way of achieving the same result is to use `after` which takes a delaty time in ms from now:

```clj
(after 1000 #(println "hello from the past!") my-pool)
```

If you want a scheduled function to be unique, it is possible to pass a uid string to both scheduled job functions.

Will throw an Exception error when you try to schedule a job with the same uid:

```clj
(after 10000 #(println "hello from the past!") my-pool :uid "my-unique-identifier") ; schedules function
(at (+ 10000 (now)) #(println "hello from the past!") my-pool :uid "my-unique-identifier") ; will throw an Execution error
; Execution error at overtone.at-at/at (at_at.clj:281).
; Error: Unable to schedule job with uid my-unique-identifier, job is already scheduled.0
```

You can also schedule functions to occur periodically. Here we schedule the function to execute every second:

```clj
(every 1000 #(println "I am cool!") my-pool)
```

This returns a scheduled-fn which may easily be stopped `stop`:

```clj
(stop *1)
```

Or more forcefully killed with `kill`.

It's also possible to start a periodic repeating fn with an initial delay:

```clj
(every 1000 #(println "I am cool!") my-pool :initial-delay 2000)
```

Finally, you can also schedule tasks for a fixed delay (vs a rate):

```clj
(interspaced 1000 #(println "I am cool!") my-pool)
```

This means that it will wait 1000 ms after the task is completed before
starting the next one.

### Resetting a pool.

When necessary it's possible to stop and reset a given pool:

```clj
(stop-and-reset-pool! my-pool)
```

You may forcefully reset the pool using the `:kill` strategy:

```clj
(stop-and-reset-pool! my-pool :strategy :kill)
```

### Viewing running scheduled tasks.

`at-at` keeps an eye on all the tasks you've scheduled. You can get a set of the current jobs (both scheduled and recurring) using `scheduled-jobs` and you can pretty-print a list of these job using `show-schedule`. The ids shown in the output of `show-schedule` are also accepted in `kill` and `stop`, provided you also specify the associated pool. See the `kill` and `stop` docstrings for more information.

```clj
(def tp (mk-pool))
(after 10000 #(println "hello") tp :desc "Hello printer")
(every 5000 #(println "I am still alive!") tp :desc "Alive task")
(show-schedule tp)
;; [6][RECUR] created: Thu 12:03:35s, period: 5000ms, desc: "Alive task
;; [5][SCHED] created: Thu 12:03:32s, starts at: Thu 12:03:42s, uid: "G__441", desc: "Hello printer
```

### overtone.at-at.util
See [UTIL.md](doc/UTIL.md) for more information

### History

at-at was extracted from the awesome music making wonder that is Overtone (http://github.com/overtone/overtone)

### Authors

* Sam Aaron
* Jeff Rose
* Michael Neale

(Ascii art borrowed from http://www.sanitarium.net/jokes/getjoke.cgi?132)
