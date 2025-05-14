#!/bin/bash
set -e

# Tạo các database
psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" <<-EOSQL
    CREATE DATABASE "user-service";
    CREATE DATABASE "borrowing-service";
    CREATE DATABASE "book-service";
    CREATE DATABASE "inventory-service";
EOSQL