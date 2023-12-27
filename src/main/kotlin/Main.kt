import service.EventAggregator

/**
 * Main entry point for running the application.
 *
 * This function parses command line arguments and calls the `aggregateEvents` function
 * of the [EventAggregator] class to process user events and generate reports.
 *
 * Command Line Usage:
 * -i input.json -o output.json [--update]
 *
 * Parameters:
 * - -i input.json: Specifies the input file containing user events in JSON format.
 * - -o output.json: Specifies the output file where the aggregated data will be written in JSON format.
 * - [--update]: Optional flag indicating whether to update existing output if it exists.
 *
 * @param args Command line arguments passed to the program.
 */
fun main(args: Array<String>) {
    // Check if the correct number and format of command line arguments are provided
    if (args.size < 4 || args.size > 5 || args[0] != "-i" || args[2] != "-o") {
        println("Usage: -i input.json -o output.json [--update]")
        return
    }

    // Extract input file name, output file name, and update flag from command line arguments
    val inputFileName = args[1]
    val outputFileName = args[3]
    val update = args.size == 5 && args[4] == "--update"

    // Create an instance of EventAggregator to perform event aggregation
    val eventAggregator = EventAggregator()

    // Call the aggregateEvents function with input and output file paths and update flag
    eventAggregator.aggregateEvents(inputFileName, outputFileName, update)
}
