package ru.yandex.practicum.telemetry.avro;

import org.apache.avro.io.BinaryDecoder;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.kafka.common.serialization.Deserializer;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import org.apache.kafka.common.errors.SerializationException;

public class AvroDeserializer implements Deserializer<SensorEventAvro> {
    private final DecoderFactory decoderFactory = DecoderFactory.get();
    private final DatumReader<SensorEventAvro> reader = new SpecificDatumReader<>(SensorEventAvro.getClassSchema());

    @Override
    public SensorEventAvro deserialize(String topic, byte[] data) {
        try {
            if (data != null) {
                BinaryDecoder decoder = decoderFactory.binaryDecoder(data, null);
                return this.reader.read(null, decoder);
            }
            return null;
        } catch (Exception e) {
            throw new SerializationException("Ошибка десериализации данных из топика [" + topic + "]", e);
        }
    }
}