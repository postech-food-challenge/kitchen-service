#!/bin/sh

# Wait for DynamoDB Local to start up
sleep 5

# Create the table
aws dynamodb create-table \
    --table-name orders \
    --attribute-definitions AttributeName=id,AttributeType=S \
    --key-schema AttributeName=id,KeyType=HASH \
    --provisioned-throughput ReadCapacityUnits=10,WriteCapacityUnits=10 \
    --endpoint-url http://localhost:8000 \
    --region us-east-1