#!/bin/bash
set -e

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" <<-EOSQL
  create role $TAATTA_POSTGRES_USER with login password '$TAATTA_POSTGRES_PASSWORD';
EOSQL