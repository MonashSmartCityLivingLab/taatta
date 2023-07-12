# Module athom-presence-sensor

This is a logging module for the [Athom Human Presence Sensor](https://www.athom.tech/blank-1/human-presence-sensor),
using [ESPHome MQTT client API](https://esphome.io/components/mqtt.html).

See [ESPHome documentation](../docs/esphome.md) for more details on how to set up your devices.

## MQTT topics

- `<sensor_name>/status`: Status message published when the plugs go online or offline
- `<sensor_name>/debug`: Log messages similar to the one seen on the plug's web UI
- `<sensor_name>/sensor/<sensor_name>/state`: Values from a sensor. Known sensor names are
    - `uptime_sensor`
    - `wifi_signal_sensor`
    - `light_sensor`
    - `ip_address`
    - `mac_address`
    - `connected_ssid`
- `<plug_name>/binary_sensor/<sensor_name>/state`: Values from a binary sensor (i.e. only has ON or OFF value). Known sensor names are
    - `pir_sensor`
    - `mmwave_sensor`
    - `occupancy`
    - `button`