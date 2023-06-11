# athom-smart-plug

This is a logging module for the [Athom Smart Plug V2](https://www.athom.tech/blank-1/esphome-au-plug),
using [ESPHome MQTT client API](https://esphome.io/components/mqtt.html).

## Enabling MQTT

In order for the plugs to send data to the MQTT broker, you will need to enable MQTT on each plug. Add the following to
the plug's yml file:

```yaml
mqtt:
  broker: broker_address
```

Where `broker_address` is the IP address of your MQTT broker.

You can edit the yml file if you add the plug to Home Assistant.

More information can be found in the [ESPHome documentation](https://esphome.io/components/mqtt.html)

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