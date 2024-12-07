package ru.yandex.practicum.kafka.telemetry.collector.dto.hub;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class DeviceRemovedEvent extends HubEvent {

    @Override
    public HubEventType getType() {
        return HubEventType.DEVICE_REMOVED_EVENT;
    }
}