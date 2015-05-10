(ns sqs-client.util)


(defn getenv
  "Returns the given environment variable, returning the given default if not set or '' if none given"
  ([var-name]
    (getenv var-name ""))
  ([var-name default-val]
    (or (System/getenv var-name) default-val)))
