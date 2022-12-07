#!/bin/bash
docker exec -it journeys-db cypher-shell -u neo4j -p password -f /import/db_drop.cypher
docker exec -it journeys-db cypher-shell -u neo4j -p password -f /import/db_test_data.cypher
