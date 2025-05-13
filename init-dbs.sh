#!/bin/bash
set -e
psql -v ON_ERROR_STOP=1 --username "postgre" <<-EOSQL
  CREATE DATABASE "user-service";
  CREATE DATABASE "borrowing-service";
  CREATE DATABASE "book-service";
  CREATE DATABASE "inventory-service";
EOSQL