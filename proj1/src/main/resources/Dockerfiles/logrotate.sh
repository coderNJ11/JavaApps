#!/bin/sh

log_file="/app/log/formio.log"
max_size_kb=2
max_rotated_files=10

rotate_log() {
    if [ -f "$log_file" ] && [ $(stat -c%s "$log_file") -gt $((max_size_kb * 1024)) ]; then
        mv "$log_file" "${log_file}.1"
        gzip -f "${log_file}.1"
    fi
}

rotate_log

# Schedule log rotation every 4 hours
while true; do
    sleep 4h
    rotate_log
done