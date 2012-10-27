(ns noir-example.test
  (:use noir-example.core
        [noir-example.settings :only (static-path)])
  
  (:import (java.io BufferedReader FileReader)))


(defn print-code [file-name]
  (with-open [rdr (BufferedReader. (FileReader. (str code-path 
                                                     file-name)))]
    (doseq [line (line-seq rdr)]
      (println line))))

(defn print-code-slurp [file-name]
  (slurp (str code-path file-name)))





