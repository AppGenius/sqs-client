(defproject sqs-client "0.0.0"
  :description "tools for interfaceing with the AWS simple queue service"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[cheshire "5.2.0"]
                 [com.amazonaws/aws-java-sdk "1.6.10"]]
  :main sqs-client.core
  :aot [sqs-client.core])
