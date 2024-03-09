# /bin/sh

cron

echo "Starting node application"
node --max-http-header-size=131872 fromio > /app/log/formio.log 2>&1

tail -f /dev/null