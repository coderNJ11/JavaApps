#!/bin/sh

echo "Starting node application"
node --max-http-header-size=131872 fromio > /app/log/formio.log 2>&1 &

# Start the log rotation script in the background
/app/logrotate.sh &

# Keep the container running
tail -f /app/log/formio.log