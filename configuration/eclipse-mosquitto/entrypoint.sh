#!/bin/sh

PASSWDFILE=/mosquitto/config/passwd

if [ -z "$TAATTA_MOSQUITTO_USER" ] || [ -z "$TAATTA_MOSQUITTO_PASSWORD" ]; then
  echo "$(date +%s): Credentials not set, skipping"
else
  echo "$(date +%s): Setting credentials"
  mosquitto_passwd -c -b $PASSWDFILE "$TAATTA_MOSQUITTO_USER" "$TAATTA_MOSQUITTO_PASSWORD"
  chown mosquitto:mosquitto $PASSWDFILE
fi

exec "$@"