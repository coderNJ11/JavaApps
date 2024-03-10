#!/bin/sh

log_file="/app/log/formio.log"
max_size_kb=2
max_rotated_files=10
daily_rotation_check_delay="1d"
hourly_rotation_check_delay="4h"
cpu_utilization_threshold=80

rotate_log() {
    if [ -f "$log_file" ] && [ $(stat -c%s "$log_file") -gt $((max_size_kb * 1024)) ]; then
        for ((i=max_rotated_files; i>1; i--)); do
            [ -f "${log_file}.$((i-1)).gz" ] && mv "${log_file}.$((i-1)).gz" "${log_file}.$i.gz"
        done
        mv "$log_file" "${log_file}.1"
        sleep 10  # Introduce a delay to reduce CPU usage during compression
        sudo -u node nice -n 10 gzip -f "${log_file}.1"
    fi
}

while true; do
    current_hour=$(date +"%H")
    if [ "$current_hour" = "00" ]; then
        sleep $daily_rotation_check_delay
        rotate_log
    else
        # Check CPU utilization
        cpu_utilization=$(top -b -n 1 | awk 'NR>7{s+=$9} END{print s}')
        if [ -z "$cpu_utilization" ] || [ "$cpu_utilization" -lt "$cpu_utilization_threshold" ]; then
            rotate_log
            sleep $hourly_rotation_check_delay
        else
            sleep 1h  # Delay subsequent rotation check if CPU utilization is high
        fi
    fi
done