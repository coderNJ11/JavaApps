#!/bin/sh

# Define the log file path
LOG_FILE="/app/log/formio.log"

while true; do
  # Create the log message with a timestamp
  LOG_MESSAGE="$(date): This is a dummy log message."

  # Append the log message to the log file
  echo "$LOG_MESSAGE" >> "$LOG_FILE"

  # Sleep for 1 second before generating the next log message
  sleep 1
done