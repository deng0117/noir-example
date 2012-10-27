(ns noir-example.views
  (:gen-class)
  (:use noir.core
        hiccup.core
        hiccup.element
        hiccup.form
        hiccup.page
        
        ; local namespace
        noir-example.core
        noir-example.templates)
  
  (:require [noir.server :as server]
            [noir.cookies :as cookie]
            [noir.validation :as vali]
            [noir.statuses :as statuses]
            [noir.response :as resp]
            [noir.session :as session]))



(defpage "/foo*" {:as params}
  (let [params-str (str params)
        url (get params :*)]
  (main-layout
    [:p params-str]
    [:p url])))

(pre-route "/admin/*" {}
           (when-not (session/get :admin)
             (resp/redirect "/login")))

(defpage "/admin/" {}
  (main-layout
    [:p "Admin section things"]))

(defpage "/middleware" {m1 :m1 m2 :m2}
  (main-layout
    [:p "You added middleware! " m1 " :: " m2]))

(defpage "/logout" {}
  (session/remove! :admin)
  (resp/redirect "/"))

(defpage login-page "/login" {}
  (main-layout
    (form-to [:post "/login"]
             (text-field "username" "Username")
             [:br]
             (password-field "password" "Password")
             [:br]
             (submit-button "Login"))))

(defpage [:post "/login"] {username :username
                           password :password}
  (session/put! :admin true)
  (main-layout
    [:h2 "you are now logged in!"]
    [:p "your name: " username]
    [:p "your password: " password]))

(defpage "/image" []
  (main-layout
    (image "/img/woot.png" 
           "woot")))

(defpage "/params" {awk :awk} 
  (main-layout
    [:h2 "Params everywhere."]
    [:p "Do you have an awk get-param? " (if awk
                                           (str "Yes you do! " awk)
                                           "It doesn't appear so")]
    (form-to [:post "/params"]
             (text-field "hey"))))

(defpage [:post "/params"] {hey :hey
                            params :params}
  (main-layout
    [:h2 "You posted something:"]
    [:p hey]
    [:p params]))

(defpage "/render" {:as params}
  (render [:post "/params"] {:hey "what's up?"
                             :params params}))

(defpage "/json/:name" {n :name}
  (resp/json
    {:name n}))

(defpage "/cookie" []
  (cookie/put! :noir "stuff")
  (main-layout
    [:p "You created a cookie."]))

(defpage "/cookie-map" []
  (cookie/put! :noir2 {:value "more stuff" :path "/cookie-map" :expires 1})
  (main-layout
    [:p "You created another cookie."]))

(defpage "/exception" []
  (/ 1 0))

(defpage "/multi-cookie" []
  (cookie/put! :a1 "awk")
  (cookie/put! :a2 "awk2")
  (main-layout
    [:p "Multiple cookies on a single page!"]))

(defpage "/cookie-vals" []
  (let [v (cookie/get :a1)]
    (main-layout
      [:p "Here's the value of one of the cookies you set: " v])))

(defpage "/session" []
  (session/put! :awe "sweet")
  (session/put! :userid 1)
  (main-layout
    [:p "session: " @session/*noir-session*]
    [:p "userid: " (session/get :userid)]))

(defpage "/session-continues" []
  (main-layout
    [:p "session: " @session/*noir-session*]
    [:p "userid: " (session/get :userid)]))

(defpage "/valid" []
  (vali/rule (= 1 2)
             [:math "One and two are not equal"])
  (vali/rule (vali/min-length? "chris" 4)
             [:username "A name has to be at least 4 chars!"])
  (main-layout
    [:p "Let's check your math: " (vali/errors? :math)]
    [:p "How about your name? " (or (vali/errors? :username) 
                                    "Well, it appears to be the appropriate length.")]))

(statuses/set-page! 400
                    (main-layout
                      [:p "We couldn't find what you were looking for!"]))

(defn wrap-key [handler k v]
  (fn [request]
    (let [neue (assoc-in request [:params k] v)]
      (handler neue))))

(server/add-middleware wrap-key :m1 "middleware 1 added this")
(server/add-middleware wrap-key :m2 "middleware 2 added this")



