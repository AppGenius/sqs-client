(ns sqs-client.core
  (:require [sqs-client.sqs :as sqs]
            [sqs-client.redis :as redis]
            [sqs-client.util :as util]
            [cheshire.core :as json]
            [clj-time.core :as time]
            [clj-time.coerce :as coerce]))



;; Methods for message producers


(defn post-task-request
  "Posts a task with the given group, type, and params to the task queue,
   returning a hash-map containing the id and body-md5 of the posted task."
  [task-group task-type params]
  (sqs/send
    (sqs/create-client)
    (util/getenv "TASK_QUEUE_URL")
    (json/generate-string
      {:task-group task-group :task-type task-type :params params})))


(defn wait-on-task-response
  "Waits for a response to the given task to appear in the queue, blocking the current thread.
   Returns a timeout message if a response is not received by the optional timeout,
   or the system's DEFAULT_RESPONSE_TIMEOUT if none provided."

  ([task-id]
    (let [default-timeout (read-string (util/getenv "DEFAULT_RESPONSE_TIMEOUT" "20000"))]
      (wait-on-task-response task-id default-timeout)))

  ([task-id timeout]
    (loop [sleep-time (read-string (util/getenv "MIN_RESPONSE_TIME" "10"))]
      (if (> sleep-time timeout)
        {:error "RESPONSE TIMEOUT"}
        (do
          (Thread/sleep sleep-time)
          (if-let [response (redis/consume-response task-id)]
            response
            (recur (* 2 sleep-time))))))))


(defn wait-on-task-request
  "Posts a task with the given group, type, and params to the task queue,
   returning the response when available and blocking the current thread until ready."
  [task-group task-type params]
  (if-let [task-id (:id (post-task-request task-group task-type params))]
    (wait-on-task-response task-id)
    {:error "COULD NOT POST TASK TO QUEUE"}))



;; Methods for message consumers


(defn consume-task-request
  "Gets and deletes a request from the task queue.
   Blocks the current thread until a request is ready,
   returning an error message if none is received after the given poll time."

  ([]
    (consume-task-request 0))

  ([poll-time]
    (let [client (sqs/create-client)
          queue-url (util/getenv "TASK_QUEUE_URL")]
      (if-let [task-request (first (sqs/receive client queue-url :limit 1 :wait-time-seconds 20))]
        (do
          (sqs/delete client queue-url task-request)
          task-request)
        {:error "EMPTY TASK QUEUE"}))))


(defn post-task-response
  "Posts the given response to the given task to the response cache."
  [task-id response]
  (redis/post-response task-id response))
