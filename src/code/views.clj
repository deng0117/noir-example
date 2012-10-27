(ns code.views
  (:use noir.core
        hiccup.page
        
        ; local namespace
        noir-example.core
        noir-example.templates))


(defpage code-page "/code" []
  (main-layout
    (code-block "/views.clj")))


