#!/bin/sh

log_file="/app/log/formio.log"
max_size_kb=2
max_rotated_files=10
rotation_check_delay="4h"
cpu_throttle_delay="1m"

rotate_log() {
    if [ -f "$log_file" ] && [ $(stat -c%s "$log_file") -gt $((max_size_kb * 1024)) ]; then
        for ((i=max_rotated_files; i>1; i--)); do
            [ -f "${log_file}.$((i-1)).gz" ] && mv "${log_file}.$((i-1)).gz" "${log_file}.$i.gz"
        done
        mv "$log_file" "${log_file}.1"
        sleep $compression_delay
        sudo -u node rsync -aW --no-compress "${log_file}.1" "${log_file}.1.gz"
    fi
}

rotate_log

while true; do
    sleep $rotation_check_delay
    rotate_log
    sleep $cpu_throttle_delay
done