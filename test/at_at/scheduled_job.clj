(ns at-at.scheduled-job
  (:require [clojure.test :refer [deftest testing is]]
            [overtone.at-at :as at-at]))

(def my-pool (at-at/mk-pool))

(deftest scheduled-at
  (testing "Scheduling job with at"
    (is (= overtone.at_at.ScheduledJob
           (type
            (at-at/at ; schedule something 1 hour from now
             (+ (* 60 60 1000) (at-at/now)) ; now + 60 minutes = 60 * 60 * 1000
             #(println "unit test - job with at") ; the function to schedule
             my-pool ; the pool
             :desc "description - job with at"))))))

(deftest scheduled-after
  (testing "Scheduling job with after"
    (is (= overtone.at_at.ScheduledJob
           (type
            (at-at/after ; schedule something 1 hour from now
             (+ (* 60 60 1000) (at-at/now)) ; now + 60 minutes = 60 * 60 * 1000
             #(println "unit test - job with after") ; the function to schedule
             my-pool ; the pool
             :desc "description - job with after"))))))

(deftest scheduled-at-stopped
  (testing "Scheduling job with at and stopping it"
    (is (= true
           (let [job (at-at/at ; schedule something 1 hour from now
                      (+ (* 60 60 1000) (at-at/now)) ; now + 60 minutes = 60 * 60 * 1000
                      #(println "unit test - job with at") ; the function to schedule
                      my-pool ; the pool
                      :desc "description - job with at")
                 id  (:id job)]
             (at-at/stop id my-pool))))))

(deftest scheduled-after-stopped
  (testing "Scheduling job with after and stopping it"
    (is (= true
           (let [job (at-at/after ; schedule something 1 hour from now
                      (+ (* 60 60 1000) (at-at/now)) ; now + 60 minutes = 60 * 60 * 1000
                      #(println "unit test - job with after") ; the function to schedule
                      my-pool ; the pool
                      :desc "description - job with after")
                 id  (:id job)]
             (at-at/stop id my-pool))))))

(deftest scheduled-at-scheduled?
  (testing "Scheduling job with at and checking if scheduled"
    (is (= true
           (let [job        (at-at/at ; schedule something 1 hour from now
                             (+ (* 60 60 1000) (at-at/now)) ; now + 60 minutes = 60 * 60 * 1000
                             #(println "unit test - job with at") ; the function to schedule
                             my-pool ; the pool
                             :desc "description - job with at")
                 id         (:id job)
                 scheduled? @(:scheduled? (first (keep #(when (= id (:id %)) %) (at-at/scheduled-jobs my-pool))))]
             (at-at/stop id my-pool)
             scheduled?)))))

(deftest scheduled-after-scheduled?
  (testing "Scheduling job with after and checking if scheduled"
    (is (= true
           (let [job        (at-at/after ; schedule something 1 hour from now
                              (+ (* 60 60 1000) (at-at/now)) ; now + 60 minutes = 60 * 60 * 1000
                              #(println "unit test - job with after") ; the function to schedule
                              my-pool ; the pool
                              :desc "description - job with after")
                 id         (:id job)
                 scheduled? @(:scheduled? (first (keep #(when (= id (:id %)) %) (at-at/scheduled-jobs my-pool))))]
             (at-at/stop id my-pool)
             scheduled?)))))

(deftest scheduled-at-with-uid
  (testing "Scheduling job with at and a uid and checking an Exception is thrown if we try to schedule it again"
    (is (thrown? Exception
                 (at-at/at ; schedule something 1 hour from now
                  (+ (* 60 60 1000) (at-at/now)) ; now + 60 minutes = 60 * 60 * 1000
                  #(println "unit test - job with at") ; the function to schedule
                  my-pool ; the pool
                  :desc "description - job with at"
                  :uid "scheduled-at-with-uid")
                 ; Now lets schedule it again with the same uid
                 (at-at/at ; schedule something 1 hour from now
                  (+ (* 60 60 1000) (at-at/now)) ; now + 60 minutes = 60 * 60 * 1000
                  #(println "unit test - job with at") ; the function to schedule
                  my-pool ; the pool
                  :desc "description - job with at"
                  :uid "scheduled-at-with-uid")))))

(deftest scheduled-after-with-uid
  (testing "Scheduling job with after and a uid and checking an Exception is thrown if we try to schedule it again"
    (is (thrown? Exception
                 (at-at/after ; schedule something 1 hour from now
                  (+ (* 60 60 1000) (at-at/now)) ; now + 60 minutes = 60 * 60 * 1000
                  #(println "unit test - job with after") ; the function to schedule
                  my-pool ; the pool
                  :desc "description - job with after"
                  :uid "scheduled-after-with-uid")
                 ; Now lets schedule it again with the same uid
                 (at-at/after ; schedule something 1 hour from now
                  (+ (* 60 60 1000) (at-at/now)) ; now + 60 minutes = 60 * 60 * 1000
                  #(println "unit test - job with after") ; the function to schedule
                  my-pool ; the pool
                  :desc "description - job with after"
                  :uid "scheduled-after-with-uid")))))

(deftest stoppool (is (= (type (at-at/stop-and-reset-pool! my-pool)) overtone.at_at.PoolInfo)))
