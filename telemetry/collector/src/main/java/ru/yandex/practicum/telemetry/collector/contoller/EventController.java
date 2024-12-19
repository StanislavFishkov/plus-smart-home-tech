package ru.yandex.practicum.telemetry.collector.contoller;

import com.google.protobuf.Empty;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import ru.yandex.practicum.grpc.telemetry.collector.CollectorControllerGrpc;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.telemetry.collector.service.EventService;

@Slf4j
@GrpcService
@RequiredArgsConstructor
public class EventController extends CollectorControllerGrpc.CollectorControllerImplBase {
    private final EventService eventService;

    /**
     * Метод для обработки событий от датчиков.
     * Вызывается при получении нового события от gRPC-клиента.
     *
     * @param request           Событие от датчика
     * @param responseObserver  Ответ для клиента
     */
    @Override
    public void collectSensorEvent(SensorEventProto request, StreamObserver<Empty> responseObserver) {
        log.info("collectSensorEvent invoked with request: {}", request);
        try {
            eventService.collectSensorEvent(request);
            // после обработки события возвращаем ответ клиенту
            responseObserver.onNext(Empty.getDefaultInstance());
            // и завершаем обработку запроса
            responseObserver.onCompleted();
            log.info("collectSensorEvent request is successfully handled: {}", request);
        } catch (Exception e) {
            // в случае исключения отправляем ошибку клиенту
            responseObserver.onError(new StatusRuntimeException(Status.fromThrowable(e)));
        }
    }

    @Override
    public void collectHubEvent(HubEventProto request, StreamObserver<Empty> responseObserver) {
        log.info("collectHubEvent invoked with request: {}", request);
        try {
            eventService.collectHubEvent(request);
            // после обработки события возвращаем ответ клиенту
            responseObserver.onNext(Empty.getDefaultInstance());
            // и завершаем обработку запроса
            responseObserver.onCompleted();
            log.info("collectHubEvent request is successfully handled: {}", request);
        } catch (Exception e) {
            // в случае исключения отправляем ошибку клиенту
            responseObserver.onError(new StatusRuntimeException(Status.fromThrowable(e)));
        }
    }
}