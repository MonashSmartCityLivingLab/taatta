#!/bin/bash
set -e

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" <<-EOSQL
  create database rhf1s001 with owner $TAATTA_POSTGRES_USER;
EOSQL