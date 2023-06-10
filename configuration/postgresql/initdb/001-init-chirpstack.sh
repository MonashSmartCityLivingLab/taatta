#!/bin/bash
set -e

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" <<-EOSQL
    create role chirpstack with login password '$TAATTA_POSTGRES_PASSWORD';
    create database chirpstack with owner chirpstack;
EOSQL
