(ns sqs-client.redis
  (:require [sqs-client.util :as util]
            [cheshire.core :as json]
            [taoensso.carmine :as car]))


;; connection config for redis cache
(def cache-connection
  (let [cache-url (util/getenv "RESPONSE_CACHE_URL")]
    (if (empty? cache-url)
      {:pool {} :spec {:host "127.0.0.1" :port 6379}}
      {:pool {} :spec {:uri cache-url}})))

(defmacro wcar*
  "executes the given body using the preset cache-connection"
  [& body]
  `(car/wcar cache-connection ~@body))


(defn post-response
  "sends the given response to the given task to the cache"
  [task-id response]
  (wcar* (car/set task-id (json/generate-string response))))


(defn consume-response
  "returns the response for the given task from the cache, deleting if found"
  [task-id]
  (when-let [cached-response (wcar* (car/get task-id))]
    (wcar* (car/del task-id))
    (json/parse-string cached-response true)))


(defn peek-response
  "returns the response for the given task from the cache without deleting it"
  [task-id]
  (json/parse-string (wcar* (car/get task-id)) true))
