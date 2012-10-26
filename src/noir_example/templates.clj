(ns noir-example.templates
  (:use noir.core
        hiccup.page-helpers))


(defpartial main-layout [& content]
  (html5
    [:head
     [:title "Holds, the power of Lisp"]
     (include-css "/css/reset.css")
     (include-css "/css/noir.css")]
    [:body
     [:div {:id "main-layout"}
      content]]))

