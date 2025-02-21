(ns volcanoes.core
  (:require [clojure.data.csv :as csv]
            [clojure.java.io :as io]))

(def csv-read (csv/read-csv (io/reader "resources/volcanoes.csv")))

(defn transform-header [header]
  (if (= "Elevation (m)" header)
    :elevation-metters
    (-> header
        (clojure.string/lower-case)
        (clojure.string/replace #" " "-")
        keyword)))

(defn transform-header-row [header-line]
  (map transform-header header-line)) 

(def volcano-records
  (let [csv-line (rest csv-read)]
    (map (fn [line]
           (zipmap (first csv-read) line))
         csv-line)))
