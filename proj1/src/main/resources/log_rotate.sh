#!/bin/sh

echo "Running logrotate..."
logrotate /etc/logrotate.conf

echo "Running node application..."
echo $(node --max-http-header-size=131872 fromio) > /app/log/fromio.log 2>&1
echo "Node application has finished running."