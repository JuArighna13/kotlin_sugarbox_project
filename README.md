# Social Media Event Aggregator

This command-line utility is designed for a backend service in a social media app. Its primary purpose is to aggregate user activity events, producing daily summary reports. These reports encompass various metrics such as the number of posts, likes received, and more, organized on a per-day basis. Notably, the utility has the capability to update these summary reports in real-time as new events are introduced into the dataset.

## Project Structure

* UserEvent.kt: Data class representing a user event.
* DailySummary.kt: Data class representing daily summary information.
* EventAggregator.kt: Service class for aggregating events and generating reports.
* Main.kt: Main entry point for running the application.

### UserEvent

The `UserEvent` data class represents a user event with the following properties:

- **userId**: The unique identifier of the user who performed the event.
- **eventType**: The type of event (e.g., post, likeReceived, comment).
- **timestamp**: The timestamp when the event occurred, represented in milliseconds.

### DailySummary

The `DailySummary` data class encapsulates information about daily event summaries:

- **userId**: The unique identifier of the user for whom the summary is generated.
- **date**: The date for which the summary is created in the format "yyyy-MM-dd".
- **post**: The count of post events for the day (default is 0).
- **likeReceived**: The count of likeReceived events for the day (default is 0).
- **comment**: The count of comment events for the day (default is 0).

**Note**: The `@JsonInclude(JsonInclude.Include.NON_DEFAULT)` annotation is used to exclude default (0) values from the generated JSON.

### EventAggregator

The `EventAggregator` class provides functionality to aggregate user events and generate daily summaries:

- **readExistingOutput(outputFileName: String): List<DailySummary>**: Reads existing summary data from a JSON file.

- **readInputFile(inputFileName: String): List<UserEvent>**: Reads user events from a JSON file.

- **aggregateEvents(inputFileName: String, outputFileName: String, update: Boolean)**: Aggregates user events, updates existing summaries, and writes the results to a JSON file.

### Main

The `Main` class serves as the entry point for running the application. It reads command-line arguments and calls the `aggregateEvents` function.

### Usage

Run the application with the following command:

```bash
.\gradlew build"
```

```bash
.\gradlew run -Pargs="-i input.json -o output.json [--update]"
```

* -i: Input JSON file containing user events.
* -o: Output JSON file for daily summary reports.
* --update: Update existing summary with new events (optional).
