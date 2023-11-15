# Playground

# Prerequisites
 * Java 17
 * Gradle

# Weather API PoC
Purpose of the application is to demonstrate a simple weather api that allows sensors to add data to the api and saved to a h2 database and for the api to serve data for one or more sensors when queried followed by metrics. 
Please note in order to simplify approach I have made design decision to represent a sensor by a location meaning one sensor represents one location.

For an example of querys I have attached a Postman collection for testing.

# Metrics are in the following format
  * Temperature - Celcius
  * Humidity - Percentage 0-100
  * Wind speed - Miles per hour
  * Precipitation - Milimetres
  * Wind Direction - Relative to 360 degrees

# How to run
 * Clone repository
 * Open in IntelliJ
 * Press play/run in ApiApplication.class
