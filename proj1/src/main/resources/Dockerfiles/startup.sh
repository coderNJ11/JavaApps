#!/bin/sh

# Function to start the node application
start_node_app() {
    echo "Starting node application"
    node --max-http-header-size=131872 fromio >> /app/log/formio.log 2>&1 &
}

# Check if formio.log exists, if not create it
if [ ! -f /app/log/formio.log ]; then
    touch /app/log/formio.log
fi

# Start the node application
start_node_app

# Start the log rotation script in the background
/app/logrotate2.sh &