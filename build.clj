(ns build
  (:require [clojure.tools.build.api :as b]))

(comment
  "https://clojure.org/guides/tools_build"
  "https://clojure.github.io/tools.build/clojure.tools.build.api.html"
  "https://kozieiev.com/blog/packaging-clojure-into-jar-uberjar-with-tools-build/")

(set! *warn-on-reflection* true)


;;; Basic settings

(def build-folder "target")
(def jar-content (str build-folder "/" "classes"))  ;; folder where we collect files to pack in a jar
(def basis (b/create-basis {:project "deps.edn"}))  ;; basis structure
(def version "1.6.0-SNAPSHOT")  ;; library version

;; JAR
(def lib-name 'org.clojars.loud/at-at)  ;; library name
(def jar-file-name (format "%s/%s-%s.jar" build-folder (name lib-name) version))  ;; path for result jar file

;; UBERJAR
(def app-name "at-at")
(def uber-file-name (format "%s/%s-%s-standalone.jar" build-folder app-name version))  ;; path for result uber file


;;; Main build functions

(defn clean  ; clj -T:build clean
  [_]
  (println "Cleaning build folder")
  (b/delete {:path build-folder})
  (println (format "Build folder \"%s\" removed" build-folder)))

(defn cleancpcache  ; clj -T:build cleancpcache
  [_]
  (println "Cleaning cpcache cache")
  (b/delete {:path ".cpcache"})
  (println ".cpcache cleaned"))

(defn cleanall  ; clj -T:build cleanall
  [_]
  (clean nil)
  (println "Cleaning calva cache")
  (b/delete {:path ".calva/deps.clj.jar"})
  (b/delete {:path ".calva/output-window/output.calva-repl"})
  (cleancpcache nil)
  (println "Cleaning clj-kondo cache")
  (b/delete {:path ".clj-kondo/.cache"})
  (println "Cleaning lsp cache")
  (b/delete {:path ".lsp/.cache"})
  (println (format "Completed cleanall")))

(defn jar ; clj -T:build jar
  "Build a library jar for Clojars"
  [_]
  (clean nil) ; clean leftovers
  (println "Copying files") ; prepare jar content
  (b/copy-dir    {:src-dirs   ["src"
                               "resources"]
                  :ignores    ["^timedate.*"]
                  :target-dir jar-content})
  (println "Writing POM") ; create pom.xml
  (b/write-pom   {:class-dir  jar-content
                  :lib        lib-name
                  :version    version
                  :basis      basis
                  :src-dirs   ["src"]})
  (println "Creating jar") ; create jar
  (b/jar         {:class-dir  jar-content
                  :jar-file   jar-file-name})
  (println (format "Jar file created: \"%s\"" jar-file-name)))

(defn uberjar ; clj -T:build uberjar
  "Build an uberjar for standalone running"
  [_]
  (clean nil) ; clean leftovers
  (println "Copying files") ; prepare uberjar content
  (b/copy-dir    {:src-dirs   ["src"  ;; needed for repl development e.g. clojure.repl/source
                               "resources"]
                  :ignores    ["^timedate.*"]
                  :target-dir jar-content})
  (println "Writing POM") ; create pom.xml
  (b/write-pom   {:class-dir  jar-content
                  :lib        lib-name
                  :version    version
                  :basis      basis
                  :src-dirs   ["src"]})
  (println "Compiling Clojure") ; compile clojure code
  (b/compile-clj {:basis      basis
                  :src-dirs   ["src"]
                  :class-dir  jar-content})
  (println "Making uberjar") ; create uber file
  (b/uber        {:class-dir  jar-content
                  :uber-file  uber-file-name
                  :basis      basis
                  :main       'overtone.at-at})  ;; here we specify the entry point for uberjar
  (println (format "Uber file created: \"%s\"" uber-file-name)))
