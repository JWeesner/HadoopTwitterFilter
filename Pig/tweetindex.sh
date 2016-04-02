#!/bin/sh

cd /home/jwees1/hw3

LOG_DIR=/home/jwees1/hw3/log
LOG_FILE=$LOG_DIR/`date +%Y%m%d_%H%M%S`.log

mkdir -p $LOG_DIR

PIG_SCRIPT=tweetindex.pig
INPUT=/open/tweets/`date -d '1 hour ago' +%Y/%m/%d/%H`
OUTPUT=/analytics/tweetindex/`date -d '1 hour ago' +%Y/%m/%d/%H`

RUN_CMD="/usr/bin/pig -p input=$INPUT -p output=$OUTPUT -f $PIG_SCRIPT"
echo $RUN_CMD
$RUN_CMD &> $LOG_FILE