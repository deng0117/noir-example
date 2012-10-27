(ns noir-example.templates
  (:use noir.core
        hiccup.page
        [clojure.string :only (join)]
        
        ; local namespace
        noir-example.core
        noir-example.settings)
  
  (:import (java.io BufferedReader FileReader)))


(defpartial main-layout [& content]
  (html5
    [:head
     [:title "Holds, the power of Lisp"]
     (include-css "/css/reset.css")
     (include-css "/css/noir.css")
     (include-css "/css/code.css")]
    [:body
     [:div {:id "main-layout"}
      content]]))


(defn indent-count [line]
  (let [indent (re-find #"[\ ]*" 
                        line)]
    (count indent)))

(defn indent-html [indent-count]
  (let [non-breaking-space "&nbsp;"]
    (cond 
      (= 0 indent-count) ""
      :else (join (repeat indent-count
                          non-breaking-space)))))

(defn code-line-html-combine [content line]
  (let [count (indent-count line)
        indent (indent-html count)]
    (list content
          indent [:span line] [:br])))

(defn code-block [file-name]
  (with-open [rdr (BufferedReader. (FileReader. (str static-path 
                                                     file-name)))]
    [:code
     (reduce code-line-html-combine
             [:span]
             (line-seq rdr))]))







