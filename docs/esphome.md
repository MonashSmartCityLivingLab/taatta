# ESPHome Documentation

## Enabling MQTT

In order for the plugs to send data to the MQTT broker, you will need to enable MQTT on each plug. Add the following to
the plug's yml file:

```yaml
mqtt:
  broker: <ip_address>
  username: !secret mqtt_username
  password: !secret mqtt_password
```

Where `broker_address` is the IP address of your MQTT broker. You'll need to add `mqtt_username` and `mqtt_password` in
the ESPHome secrets configuration.

You can edit the yml file if you add the plug to Home Assistant.

More information can be found in the [ESPHome documentation](https://esphome.io/components/mqtt.html)

### Trips and tricks

If you get 'not enough space' error when updating the yml file, comment out the MQTT part, install the config, uncomment
them back, and then install again. This is more convenient than the 'official' solution which is to flash the plug with
mini firmware and redoing the Wi-Fi setup before installing our configuration.

To save the trouble of debugging and resetting the device, always validate your updated configuration before installing
them.