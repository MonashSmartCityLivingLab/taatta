# Collector

MQTT sensor data routing service.

## Routers

The collectors has a configurable list of routers which will send messages to the appropriate logger module.
By default, the collector will read `sensorRouters.json` in the current working directory to load the routers, but this
can be overridden with the `TAATTA_SENSOR_ROUTERS` environment variable.

An example of sensor router configuration can be found
in [`<repo_root>/configuration/collector/sensorRouters.json`](../configuration/collector/sensorRouters.json). For
non-Docker deployment, replace the hostname to `http://localhost:<port_number>`.

## MQTT authentication

If your broker requires authentication, pass `TAATTA_MOSQUITTO_USER` and `TAATTA_MOSQUITTO_PASSWORD` for the broker's
credentials.