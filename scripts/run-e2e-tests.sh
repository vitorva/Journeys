#!/bin/bash
#cd ..

#echo "================================="
#echo "===== Stops running dev env ====="
#echo "================================="
#./scripts/docker-stop-env.sh

#echo "=================================="
#echo "===== Starts dev environment ====="
#echo "=================================="
#./scripts/docker-start-dev-env.sh

#echo "================================="
#echo "===== Wait 180s to run tests ====="
#echo "================================="
#sleep 180

echo "========================================================"
echo "===== Exporting current DB state to a .cypher file ====="
echo "========================================================"
docker exec -it journeys-db cypher-shell -u neo4j -p password -f /import/db_export.cypher

echo "==========================================="
echo "===== Drop and import test data in DB ====="
echo "==========================================="
docker exec -it journeys-db cypher-shell -u neo4j -p password -f /import/db_drop.cypher
docker exec -it journeys-db cypher-shell -u neo4j -p password -f /import/db_export/test_data.cypher

cd ../frontend/codecept

echo "================================"
echo "===== Run end to end tests ====="
echo "================================"
npx codeceptjs run