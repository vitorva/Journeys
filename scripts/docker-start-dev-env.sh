#!/bin/bash

INSTALL=false;
UPDATE=false;

while  getopts idus flag
do
    case "${flag}" in
        i) INSTALL=true;;
        u) UPDATE=true;;
        :) exit;;
        \?) exit;;
    esac
done

if [ $INSTALL = true ];
then
    docker-compose -f docker-compose.build.yml build journeys-api
elif [ $UPDATE = true ];
then
    docker exec -it journeys-app  npm update
fi

docker-compose -f docker-compose.local.yml up -d