# Module athom-smart-plug

This is a logging module for the [Athom Smart Plug V2](https://www.athom.tech/blank-1/esphome-au-plug),
using [ESPHome MQTT client API](https://esphome.io/components/mqtt.html).

See [ESPHome documentation](../docs/esphome.md) for more details on how to set up your devices.

## MQTT topics

- `<plug_name>/status`: Status message published when the plugs go online or offline
- `<plug_name>/debug`: Log messages similar to the one seen on the plug's web UI
- `<plug_name>/sensor/<sensor_name>/state`: Values from a sensor. Known sensor names are
    - `athom_smart_plug_v2_voltage`
    - `athom_smart_plug_v2_current`
    - `athom_smart_plug_v2_power`
    - `athom_smart_plug_v2_energy`
    - `athom_smart_plug_v2_total_energy`
    - `athom_smart_plug_v2_total_daily_energy`
    - `athom_smart_plug_v2_uptime_sensor`
    - `ip_address`
    - `mac_address`
    - `connected_ssid`
- `<plug_name>/binary_sensor/<sensor_name>/state`: Values from a binary sensor (i.e. only has ON or OFF value). Known sensor names are
    - `athom_smart_plug_v2_power_button`