#!/bin/bash
set -e

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" <<-EOSQL
    create role chirpstack_ns with login password '$TAATTA_POSTGRES_PASSWORD';
    create database chirpstack_ns with owner chirpstack_ns;
EOSQL
