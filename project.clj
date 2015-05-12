(defproject sqs-client "0.0.0"
  :description "tools for interfaceing with the AWS simple queue service"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :repositories [["snapshots" {:url "s3p://ag-maven/snapshots"
                               :username :env
                               :passphrase :env}]
                 ["releases" {:url "s3p://ag-maven/releases"
                              :username :env
                              :passphrase :env}]]
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [cheshire "5.2.0"]
                 [com.amazonaws/aws-java-sdk "1.6.10"]]
  :plugins [[s3-wagon-private "1.1.2"]]
  :main sqs-client.core
  :aot [sqs-client.core])
