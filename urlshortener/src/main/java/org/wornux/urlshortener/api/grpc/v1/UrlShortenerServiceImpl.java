package org.wornux.urlshortener.api.grpc.v1;

import com.google.protobuf.Timestamp;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.wornux.urlshortener.dto.UrlCreatedFullDTO;
import org.wornux.urlshortener.dto.UrlStatsDTO;
import org.wornux.urlshortener.model.User;
import org.wornux.urlshortener.service.UrlService;
import org.wornux.urlshortener.service.UserService;
import org.bson.types.ObjectId;

import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
public class UrlShortenerServiceImpl extends UrlShortenerServiceGrpc.UrlShortenerServiceImplBase {

    private final UrlService urlService;
    private final UserService userService;

    @Override
    public void createUrl(CreateUrlRequest request, StreamObserver<CreateUrlResponse> responseObserver) {
        try {
            ObjectId userId = new ObjectId(request.getUserId());
            User user = userService.getUserById(userId).orElseThrow();

            UrlCreatedFullDTO dto = urlService.createFullUrlRecord(new org.wornux.urlshortener.dto.UrlDTO(
                    request.getOriginalUrl(), user));

            Timestamp timestamp = Timestamp.newBuilder()
                    .setSeconds(dto.createdAt().getTime() / 1000)
                    .build();

            CreateUrlResponse response = CreateUrlResponse.newBuilder()
                    .setOriginalUrl(dto.originalUrl())
                    .setShortUrl(dto.shortenedUrl())
                    .setCreationDate(timestamp)
                    .setSiteImage(dto.sitePreviewBase64())
                    .setStats(UrlStats.newBuilder()
                            .setClicks(dto.stats().clickCount())
                            .setUniqueVisitors(dto.stats().uniqueVisitors())
                            .build())
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();

        } catch (Exception e) {
            responseObserver.onError(e);
        }
    }

    @Override
    public void listUserUrls(ListUserUrlsRequest request, StreamObserver<ListUserUrlsResponse> responseObserver) {
        try {
            ObjectId userId = new ObjectId(request.getUserId());
            User user = userService.getUserById(userId).orElseThrow();

            // ✅ Usa el método correcto
            List<UrlCreatedFullDTO> urls = urlService.getFullUrlsByUser(user);



            ListUserUrlsResponse.Builder responseBuilder = ListUserUrlsResponse.newBuilder();

            for (UrlCreatedFullDTO dto : urls) {
                Timestamp created = Timestamp.newBuilder()
                        .setSeconds(dto.createdAt().getTime() / 1000)
                        .build();

                ListUserUrlsResponse.UserUrlData userUrl = ListUserUrlsResponse.UserUrlData.newBuilder()
                        .setOriginalUrl(dto.originalUrl())
                        .setShortUrl(dto.shortenedUrl())
                        .setCreationDate(created)
                        .setSiteImage(dto.sitePreviewBase64())
                        .setStats(UrlStats.newBuilder()
                                .setClicks(dto.stats().clickCount())
                                .setUniqueVisitors(dto.stats().uniqueVisitors())
                                .build())
                        .build();

                responseBuilder.addUserUrls(userUrl);
            }

            responseObserver.onNext(responseBuilder.build());
            responseObserver.onCompleted();

        } catch (Exception e) {
            responseObserver.onError(e);
        }
    }

}