(ns volcanoes.core
  (:require [clojure.data.csv :as csv]
            [clojure.java.io :as io]))
(defn transform-header [header]
  (if (= "Elevation (m)" header)
   :elevation-meters
    (-> header
        clojure.string/lower-case
        (clojure.string/replace #" " "-")
        keyword)))

(defn transform-header-row [header-line]
  (map transform-header header-line))

(defonce csv-lines
  (with-open [csv (io/reader "resources/volcanoes.csv")]
    (doall
      (csv/read-csv csv))))

(defonce state (atom nil))

(defn reset-state []
  (reset! state nil))

(def volcano-records
  (let [csv-lines (rest csv-lines)
        header-line (transform-header-row (first csv-lines))
        volcano-lines (rest csv-lines)]
    (map (fn [volcano-line]
           (zipmap header-line volcano-line))
         volcano-lines)))
(defn parse-numbers [volcano]
  (-> volcano
      (update :elevation-meters #(Integer/parseInt %))
      (update :longitude #(Double/parseDouble %))
      (update :latitude #(Double/parseDouble %))))
(def volcanoes-parsed
  (map parse-numbers volcano-records))

(comment
  (let [volcano (nth volcanoes-parsed 100)]
    (clojure.pprint/pprint volcano))

  (let [volcanoes (first (filter #(= "210010" (:volcano-number %)) volcanoes-parsed))]
    (clojure.pprint/pprint volcanoes)))
(def types (set (map :primary-volcano-type volcano-records)))

(defn p [v & more]
  (apply prn v more)
  v)

(defn hot-or-cold [temp]
  (if (< temp 0)
    :cold
    :hot))

;;main way to execute code in the files

;; 1. Whole file
;; 2. Top-level form (epression)
;; 3. Single Expression
;; 4. REPL Prompt

;; Ergonomics

;; Printing in Clojure
;; There are 5 ways to print
;; 1. println
;; 2. prn
;; 3. pprint : pretty print for a big map
;; 4. print
;; 5. format