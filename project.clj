(defproject org.clojars.loud/at-at "1.6.0-SNAPSHOT"
  :description "Ahead-of-time function scheduler"
  :url "https://github.com/LouDnl/at-at"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.10.1"]]
  :plugins      [[lein-ancient "1.0.0-RC4-SNAPSHOT"]]
  :profiles {:1.6    {:dependencies [[org.clojure/clojure "1.6.0"]]}
             :1.7    {:dependencies [[org.clojure/clojure "1.7.0"]]}
             :1.8    {:dependencies [[org.clojure/clojure "1.8.0"]]}
             :1.9    {:dependencies [[org.clojure/clojure "1.9.0"]]}
             :1.10   {:dependencies [[org.clojure/clojure "1.10.0"]]}
             :1.10.1 {:dependencies [[org.clojure/clojure "1.10.1"]]}
             :1.10.2 {:dependencies [[org.clojure/clojure "1.10.2"]]}
             :1.10.3 {:dependencies [[org.clojure/clojure "1.10.3"]]}
             :1.11   {:dependencies [[org.clojure/clojure "1.11.0"]]}
             :1.11.1 {:dependencies [[org.clojure/clojure "1.11.1"]]}
             :alpha  {:dependencies [[org.clojure/clojure "1.12.0-alpha1"]]}}
  :aliases  {"all" ["with-profile" "+1.6:+1.7:+1.8:+1.9:+1.10:+1.10.1:+1.10.2:+1.10.3+:1.11:+1.11.1:+alpha"]})
