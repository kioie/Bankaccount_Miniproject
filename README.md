**BANK ACCOUNT MINI PROJECT**

*Prerequisites*

Makes sure that nothing is running on port *8080*

Ensure that you are running on a linux environment

Ensure that you have a working internet connection to be able to download your dependencies

***To launch the application***

Change directory to the project folder 
Check for the file launch.sh 
On your command line execute launch.sh using the command 

```
#!bash

./launch.sh
```

**API Endpoints**

http://localhost:8080/checkbalance/ [GET]

http://localhost:8080/deposit/ [POST]

http://localhost:8080/withdraw/ [POST]

**API CALLS**

To run access the api via curl

```
#!bash

curl http://localhost:8080/checkbalance/
```


```
#!bash

curl -H "Content-Type: application/json" -X POST -d '{"amount":36000}' http://localhost:8080/deposit/
```


```
#!bash

curl -H "Content-Type: application/json" -X POST -d '{"amount":22000}' http://localhost:8080/withdraw/
```

**TESTING**

Inside the project directory, run 
```
#!bash

./start_tests.sh
```

To access the reports use build/reports
To access the results use build/test-results


***Code Coverage using Jacoco***

To access it, use build/reports/jacoco/test/html/index.html

**Special thanks to**

George Otieno georgeorti@gmail.com (*for the underlying code base*)
