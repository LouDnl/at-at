(ns at-at.recurring-job
  (:require [clojure.test :refer [deftest testing is]]
            [overtone.at-at :as at-at]))

(def my-pool (at-at/mk-pool))

(deftest every
  (testing "Starting recurring job with every"
    (is (= overtone.at_at.RecurringJob (type (at-at/every 100000 #(println "unit test - every job") my-pool))))))

(deftest interspaced
  (testing "Starting recurring job with interspaced"
    (is (= overtone.at_at.RecurringJob (type (at-at/interspaced 100000 #(println "unit test - interspaced job") my-pool))))))

(deftest every-stop
  (testing "Starting and stopping recurring job with every"
    (is (= true
           (let [job (at-at/every 100000 #(println "unit test - every job start and stop") my-pool)
                 id  (:id job)]
             (at-at/stop id my-pool))))))

(deftest interspaced-stop
  (testing "Starting and stopping recurring job with interspaced"
    (is (= true
           (let [job (at-at/interspaced 100000 #(println "unit test - interspaced job start and stop") my-pool)
                 id  (:id job)]
             (at-at/stop id my-pool))))))

(deftest every-scheduled?
  (testing "Starting, checking if scheduled and stopping job with every"
    (is (= true
           (let [job        (at-at/every 100000 #(println "unit test - every job start, check if scheduled and stop") my-pool)
                 id         (:id job)
                 scheduled? @(:scheduled? (first (keep #(when (= id (:id %)) %) (at-at/scheduled-jobs my-pool))))]
             (at-at/stop id my-pool)
             scheduled?)))))

(deftest interspaced-scheduled?
  (testing "Starting, checking if scheduled and stopping job with interspaced"
    (is (= true
           (let [job        (at-at/interspaced 100000 #(println "unit test - interspaced job start, check if scheduled and stop") my-pool)
                 id         (:id job)
                 scheduled? @(:scheduled? (first (keep #(when (= id (:id %)) %) (at-at/scheduled-jobs my-pool))))]
             (at-at/stop id my-pool)
             scheduled?)))))

(at-at/stop-and-reset-pool! my-pool)
