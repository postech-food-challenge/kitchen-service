#!/bin/bash

queues=(
  start-preparation
  order-ready
)
for i in "${queues[@]}"; do
    echo -e "Creating queue $i"
    awslocal sqs create-queue --queue-name "$i"
done