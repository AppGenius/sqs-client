# sqs-client

Clojure client library for Amazon's [Simple Queue Service](http://aws.amazon.com/sqs/).
Depends upon the standard [AWS SDK for Java](http://aws.amazon.com/sdkforjava/),
and provides a Clojure-idiomatic API for the SQS-related functionality therein.

## Usage

project.clj
```
[sqs-client "0.0.2"]
```

compatible with Clojure 1.2.0+.

using SQS
```
(:require [sqs-client :as sqs])

(def client (sqs/create-client aws-id aws-secret-key))
(def q (sqs/create-queue client "foo"))
(sqs/send client q "message body")

(sqs/receive client q)
(sqs/delete client q (first *1))
```

## License

Copyright © 2015 AppGenius

``sqs-client.sqs`` Copyright © 2011-2013 Chas Emerick and contributors.  
Licensed under the EPL. (See the file epl-v10.html)  
Adapted 2015 Ben Slawski & AppGenius
