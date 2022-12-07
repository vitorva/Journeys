#!/bin/bash

#STR="../../journey-teams-service-keys.json"
#cat $STR  | docker login -u _json_key --password-stdin https://europe-west3-docker.pkg.dev
docker push europe-west3-docker.pkg.dev/journeys-lredv/journeysimg/journeys-api:dev
docker push europe-west3-docker.pkg.dev/journeys-lredv/journeysimg/journeys-app:dev
docker push europe-west3-docker.pkg.dev/journeys-lredv/journeysimg/journeys-db:dev
