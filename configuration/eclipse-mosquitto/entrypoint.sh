#!/bin/sh

PASSWDFILE=/etc/mosquitto/passwd

if [ -z "$TAATTA_MOSQUITTO_USERNAME" ] || [ -z "$TAATTA_MOSQUITTO_PASSWORD" ]; then
  echo "Credentials not set, skipping"
else
  echo "Setting credentials"
  mosquitto_passwd -b $PASSWDFILE "$TAATTA_MOSQUITTO_USERNAME" "$TAATTA_MOSQUITTO_PASSWORD"
fi

exec "$@"