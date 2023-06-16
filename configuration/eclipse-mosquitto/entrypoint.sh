#!/bin/sh

PASSWDFILE=/mosquitto/config/passwd

if [ -z "$TAATTA_MOSQUITTO_USER" ] || [ -z "$TAATTA_MOSQUITTO_PASSWORD" ]; then
  echo "Credentials not set, skipping"
else
  echo "Setting credentials"
  mosquitto_passwd -c -b $PASSWDFILE "$TAATTA_MOSQUITTO_USER" "$TAATTA_MOSQUITTO_PASSWORD"
fi

exec "$@"