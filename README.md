# Http service for hotel search
Test for Agoda SDE.

## Usage
- Language used for coding- Scala 2.11.11
- Play framework
- Build tool: Sbt
- Two api are provided
  - hotelSearch api: http://localhost:9000/hotel/search?apiKey=cool&cityName=Ashburn&sort=DESC
            - parameters: apiKey, cityName, sort (optional)
            - sort can take 2 values: ASC, DESC
  - config api: http://localhost:9001/config?key=cool&value=10
            - To set request limit of apikey. set key to apiKey and value= number of request per 10 seconds
            - parameters: key & value [optional]
            - set the given key to the value 
            - in case value is not present it returns the current value of the key
```bash
$ sbt run
```

Sample respone for hotelSearchApi:
http://localhost:9000/hotel/search?apiKey=cool&cityName=Ashburn&sort=DESC

    [
      {
        "hotelId": 22,
        "room": "Sweet Suite",
        "price": 14000
      },
      {
        "hotelId": 21,
        "room": "Deluxe",
        "price": 7000
      },
      {
        "hotelId": 20,
        "room": "Superior",
        "price": 4444
      },
      {
        "hotelId": 17,
        "room": "Deluxe",
        "price": 2800
      },
      {
        "hotelId": 25,
        "room": "Deluxe",
        "price": 1900
      },
      {
        "hotelId": 12,
        "room": "Deluxe",
        "price": 1800
     },
      {
        "hotelId": 7,
        "room": "Deluxe",
        "price": 1600
      },
      {
        "hotelId": 24,
        "room": "Superior",
        "price": 1400
      },
      {
        "hotelId": 3,
        "room": "Sweet Suite",
        "price": 1300
      },
      {
        "hotelId": 5,
        "room": "Sweet Suite",
        "price": 1200
      },
      {
        "hotelId": 10,
            "room": "Superior",
            "price": 1100
          },
          {
            "hotelId": 19,
            "room": "Superior",
            "price": 1000
      },
      {
        "hotelId": 16,
        "room": "Superior",
        "price": 800
      }
    ]
    
Incase of rate limit execeeded.
```bash
 HttpStatus Code: 429 (Too many request)
 Rate limit crossed
 ```

## Assumption

- Search will be made by city name rather than id as mentioned in the test

## Architecture

```
         
   Client Request
        |
        |
        v
+------------------------+  Check rate limit   +--------------------+        
| HotelSearch Controller |-------------------->| RateLimit Service |
+------------------------+                     +--------------------+     
        |                                               | 
        | After success rate limit                      | get config for api-key
        v                                               v
+---------------------+                       +-----------------------+
| HotelSearch Service |                       |    Config service     |  
+---------------------+                       +-----------------------+
        |
        |
        v
+-----------------------+
| HotelSearch DataLayer |
| (reads from csv)      |
+-----------------------+

     
```

## Components & Data Structures

- Hotel Service
    - Caches the hotel data returned from the hotel data layer
    - It used Concurrent Trie map for storing the info. Key been the city name and value is the list of hotels

- RateLimit Service
    - Stores the request details with respect to apikey
    - for each api key its stores list of request. For storing this it uses concurrent trie hashmap
    - It also stores the suspended state of the apikey.

-   Config service
    - Stores key value pair. It uses concurrent trie hashmap of the same.


