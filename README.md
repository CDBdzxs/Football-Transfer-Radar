# Football Transfer Radar

A Spring Boot web app that uses Jsoup to crawl live football player market-value data, converts messy HTML rows into Java objects, ranks them with a handwritten insertion sort, and displays the result in a clean HTML/CSS/JS frontend.

## Features

- Java Spring Boot backend
- Jsoup live web crawler
- Pure HTML, CSS, and JavaScript frontend
- Abstract parent class `WebData`
- Inheritance and polymorphism with `FootballTransferItem`, `TransferRumor`, and `HighValueTransferRumor`
- `ArrayList<WebData>` data storage
- Handwritten insertion sort in `WebDataSorter`
- At least 50 live player-value nodes per crawler run
- Railway-ready deployment configuration

## Local Run

Requirements:

- Java 17
- Maven

Run:

```bash
mvn spring-boot:run
```

Open:

```text
http://localhost:8080
```

The frontend calls:

```text
GET /api/transfers
```

## Railway Deployment

1. Push this folder to a GitHub repository.
2. Open Railway and choose **New Project**.
3. Choose **Deploy from GitHub repo**.
4. Select the repository.
5. Railway will use Nixpacks and the included `railway.json`.
6. After deployment, open the generated Railway URL.

No manual environment variables are required. The app reads Railway's `PORT` automatically through:

```properties
server.port=${PORT:8080}
```

## Project Structure

```text
football-transfer-radar/
  pom.xml
  railway.json
  system.properties
  src/main/java/com/example/footballradar/
    FootballRadarApplication.java
    WebData.java
    FootballTransferItem.java
    TransferRumor.java
    HighValueTransferRumor.java
    TransfermarktCrawler.java
    WebDataSorter.java
    WebDataReport.java
    WebDataService.java
    WebDataController.java
  src/main/resources/
    application.properties
    static/
      index.html
      styles.css
      app.js
```
